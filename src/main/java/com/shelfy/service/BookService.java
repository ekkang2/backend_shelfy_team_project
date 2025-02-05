package com.shelfy.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DuplicateKeyException;
import com.shelfy.config.AladinProperties;
import com.shelfy.document.BookDocument;
import com.shelfy.dto.BookDTO;
import com.shelfy.dto.BookResponseDTO;
import com.shelfy.mapper.BookMapper;
import com.shelfy.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.awt.print.Book;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/*
     날짜 : 2025/02/04
     이름 : 강은경
     내용 : BookService 생성

*/
@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class BookService {


    private final AladinService aladinService;
    private final BookRepository bookRepository;

    /**
     * 책 검색 메서드
     * @param query
     * @return List<BookDTO>
     */
    // 1. db에서 검색
    // 2. 검색 결과가 없으면 알라딘 api 호출
    // 3. isbn 검증 후 중복 확인
    // 4. 새로운 책만 저장 후 반환
    public List<BookDTO> searchBooks(String query) {
        // 1. 먼저 내 DB에서 책 검색
        List<BookDocument> foundBooks = bookRepository.searchBooksByQuery(query);
        log.info("foundBooks : " + foundBooks);

        // DB에서 검색 결과가 있으면 DTO 변환 후 결과 반환
        if (!foundBooks.isEmpty()) {
            return foundBooks.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        }

        // 2. 검색 결과 없으면 알라딘 API 요청
        List<BookDTO> bookDTOList = aladinService.searchByAladin(query);
        log.info("알라딘 검색 응답받은 bookDTOList : " + bookDTOList);

        // API에서도 책을 찾지 못하면 빈 리스트 반환
        if (bookDTOList == null || bookDTOList.isEmpty()) {
            return Collections.emptyList();
        }

        // 3. ISBN이 없는 책 필터링
        List<BookDTO> validBooks = bookDTOList.stream()
                .filter(dto -> dto.getBookIsbn() != null && !dto.getBookIsbn().trim().isEmpty())
                .toList();

        // ISBN이 있는 책이 하나도 없으면 빈 리스트 반환
        if (validBooks.isEmpty()) {
            return Collections.emptyList();
        }

        // 4. DB에 존재하는 ISBN 조회하여 중복 제거
        // 검색한 책 리스트의 isbn 문자열을 가져옴
        List<String> isbnList = validBooks.stream()
                .map(BookDTO::getBookIsbn)
                .toList();
        log.info("검색한 책 리스트의 isbn : " + isbnList);

        // 검색한 책의 isbn와 기존에 있는 isbn이랑 중복되는 책 조회
        List<BookDocument> existingBooks = bookRepository.findByBookIsbnIn(isbnList);
        log.info("중복되는 책 : " + existingBooks);

        // 조회한 결과를 Set으로 변환하여 중복을 제거
        Set<String> existingIsbnSet = existingBooks.stream()
                .map(BookDocument::getBookIsbn)
                .collect(Collectors.toSet());

        // 5. 기존에 없는 책만 저장 대상으로 선정
        List<BookDocument> newBooks = validBooks.stream()
                .map(BookDTO::toDocument)
                .filter(book -> !existingIsbnSet.contains(book.getBookIsbn()))
                .toList();

        log.info("저장할 책 목록 : " + newBooks);

        // 저장할 책이 없으면 기존 validBooks 반환
        if (newBooks.isEmpty()) {
            return validBooks;
        }

        try {
            // 6. 새로운 책만 저장
            List<BookDocument> savedDocuments = bookRepository.saveAll(newBooks);
            log.info("저장된 도서 수 : " + savedDocuments.size());
            log.info("저장된 책들 : " + savedDocuments);

            // 7. 저장된 bookId를 BookDTO에 설정
            for (int i = 0; i < savedDocuments.size(); i++) {
                validBooks.get(i).setBookId(savedDocuments.get(i).getBookId());
            }

            return validBooks;

        } catch (Exception e) {
            log.error("저장 중 오류 발생: " + e.getMessage(), e);
            return Collections.emptyList();
        }
    }




    // bookDocument > bookDTO로 변환
    private BookDTO convertToDTO(BookDocument bookDocument) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setBookId(bookDocument.getBookId());
        bookDTO.setBookImage(bookDocument.getBookImage());
        bookDTO.setBookTitle(bookDocument.getBookTitle());
        bookDTO.setBookDesc(bookDocument.getBookDesc());
        bookDTO.setBookAuthor(bookDocument.getBookAuthor());
        bookDTO.setBookPublisher(bookDocument.getBookPublisher());
        bookDTO.setBookIsbn(bookDocument.getBookIsbn());
        bookDTO.setBookPage(bookDocument.getBookPage());
        bookDTO.setBookPublishedAt(bookDocument.getBookPublishedAt());
        return bookDTO;
    }


    // 1차 검색 > db 저장
//    public List<BookDTO> searchBooks(String query) {
//        String searchUrl = String.format(
//                "%s/ItemSearch.aspx?TTBKey=%s&Query=%s&QueryType=Title&MaxResults=10&Start=1&SearchTarget=Book&Output=js",
//                aladinProperties.getBaseUrl(), aladinProperties.getApiKey(), query
//        );
//
//        try {
//            // API 응답을 String으로 받음
//            String response = restTemplate.getForObject(searchUrl, String.class);
//
//            // 마지막 `;` 제거
//            if (response != null) {
//                response = response.replaceAll(";$", ""); // 마지막 `;` 삭제
//            }
//
//            // JSON을 BookResponseDTO 객체로 변환
//            BookResponseDTO bookResponse = objectMapper.readValue(response, BookResponseDTO.class);
//            List<BookDTO> books = bookResponse.getItem();
//
//            // DB 저장
//            for (BookDTO book : books) {
//                book.setBookPage(0); // 기본값 설정
//
//                // `LocalDate.parse()`를 사용하지 않고 그대로 저장
//                if (book.getBookPublishedAt() == null || book.getBookPublishedAt().isEmpty()) {
//                    book.setBookPublishedAt("정보 없음"); // 기본값 설정 가능
//                }
//
//                // 중복 확인 후 저장
//                if (bookMapper.checkBookExists(book.getBookIsbn()) == 0) {
//                    bookMapper.insertBook(book);
//                }
//            }
//            return books;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }



    // 2차 검색: 상세 조회 (ISBN으로 페이지 수 가져오기)
//    public BookDTO getBookDetail(String bookIsbn) {
//        String detailUrl = String.format(
//                "%s/ItemLookUp.aspx?TTBKey=%s&ItemIdType=ISBN13&ItemId=%s&Output=js&OptResult=itemPage",
//                aladinProperties.getBaseUrl(), aladinProperties.getApiKey(), bookIsbn
//        );
//
//        log.info("요청 URL: " + detailUrl);
//
//        try {
//            // API 응답을 `String`으로 받아 확인
//            String jsonResponse = restTemplate.getForObject(detailUrl, String.class);
//            log.info("Aladin API 원본 응답: " + jsonResponse);
//
//            // 응답이 없는 경우
//            if (jsonResponse == null || jsonResponse.isEmpty()) {
//                log.error("API 응답이 비어 있음! 요청 URL: " + detailUrl);
//                return null;
//            }
//
//            // API 응답의 마지막 `;` 제거
//            jsonResponse = jsonResponse.replaceAll(";$", "");
//
//            // JSON을 DTO로 변환
//            ObjectMapper objectMapper = new ObjectMapper();
//            BookResponseDTO response = objectMapper.readValue(jsonResponse, BookResponseDTO.class);
//            log.info("변환된 DTO: " + response);
//
//            // `item`이 없을 경우 처리
//            if (response.getItem() == null || response.getItem().isEmpty()) {
//                log.error("`item` 값이 없음! ISBN: " + bookIsbn);
//                return null;
//            }
//
//            // `item` 리스트에서 첫 번째 책 정보 가져오기
//            BookDTO book = response.getItem().get(0);
//            int itemPage = book.getBookPage();
//
//            log.info("추출된 페이지 수: " + itemPage);
//
//            // 데이터베이스 업데이트 (페이지 수 추가)
//            book.setBookIsbn(bookIsbn);
//            book.setBookPage(itemPage);
//            bookMapper.updateBookPages(book);
//
//            return book;
//
//        } catch (Exception e) {
//            log.error("예외 발생: " + e.getMessage(), e);
//        }
//        return null;
//    }




}
