package com.shelfy.service;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shelfy.config.AladinProperties;
import com.shelfy.dto.BookDTO;
import com.shelfy.dto.BookResponseDTO;
import com.shelfy.mapper.BookMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
/*
     날짜 : 2025/01/30
     이름 : 강은경
     내용 : AladinService 생성

*/
@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class AladinService {

    private final RestTemplate restTemplate;
    private final AladinProperties aladinProperties;
    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 변환기

    /**
     * 도서 검색 API
     * @param query 검색어
     * @return 검색된 도서 목록 (BookDTO 리스트)
     */
    public List<BookDTO> searchByAladin(String query) {

        // 검색 요청 url
        // 문자열 포맷팅 > "%s"는 문자열값을 삽입할 자리 표시자
        String searchUrl = String.format(
                "%s/ItemSearch.aspx?TTBKey=%s&Query=%s&QueryType=Keyword&SearchTarget=Book&Output=js",
                aladinProperties.getBaseUrl(), aladinProperties.getApiKey(), query
        );
        log.info("검색 url : " + searchUrl);
        // https://www.aladin.co.kr/ttb/api/ItemSearch.aspx?TTBKey=ttbrkddmsrud271916001&Query=한강&QueryType=Title&MaxResults=10&Start=1&SearchTarget=Book&Output=js

        try {
            // API 응답을 String으로 받음
            String response = restTemplate.getForObject(searchUrl, String.class);
            log.info("response : " + response);

            // 응답이 null인 경우 처리
            if (response == null) {
                log.error("API 응답이 null입니다.");
                return null;
            }

            // 응답 후처리 및 JSON 파싱
            // 마지막 `;` 때문에 json 파싱 오류 발생 > 제거
            response = response.replaceAll(";$", "").replace("'", "\"");
            BookResponseDTO bookResponse = objectMapper.readValue(response, BookResponseDTO.class);
            log.info("bookResponse : " + bookResponse);

            return bookResponse.getItem(); // 검색된 도서 목록 반환

        } catch (JsonMappingException e) {
            log.error("JSON 매핑 오류: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("알 수 없는 오류: " + e.getMessage(), e);
        }
        return null; // 오류 발생 시 null 반환
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
