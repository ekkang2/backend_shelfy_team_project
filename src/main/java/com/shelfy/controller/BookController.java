package com.shelfy.controller;

import com.shelfy.document.BookDocument;
import com.shelfy.dto.BookDTO;
import com.shelfy.dto.ResponseDTO;
import com.shelfy.service.AladinService;
import com.shelfy.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.util.List;

/*
     날짜 : 2025/02/04
     이름 : 강은경
     내용 : BookController 생성

*/

@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
@Log4j2
public class BookController {

    private final BookService bookService;


    /**
     * 도서 검색
     * @param query
     * @return List<BookDTO>
     */
    @GetMapping("/search")
    public ResponseDTO<List<BookDTO>> searchBooks(@RequestParam String query) {
        log.info("책 검색 query : " + query);

        try {
            List<BookDTO> bookDTOList = bookService.searchBooks(query);
            log.info("책 검색 반환 bookDTOList : " + bookDTOList);
            return ResponseDTO.success(bookDTOList);
        }catch (Exception e) {
            log.error(e);
            return ResponseDTO.fail("책 검색 중 서버 오류 발생");
        }

    }


    /**
     * 도서 상세보기 조회
     * @param bookIsbn
     * @return
     */
    @GetMapping("/detail/{bookIsbn}")
    public ResponseDTO<BookDTO> detailBook(@PathVariable String bookIsbn) {

        log.info("상세보기 할 책의 isbn : " + bookIsbn);

        try {
            BookDTO bookDTO = bookService.selectBookPageAndUpdate(bookIsbn);
            log.info("책 상세보기 반환 bookDTO : " + bookDTO);
            return ResponseDTO.success(bookDTO);

        }catch (Exception e) {
            log.error(e);
            return ResponseDTO.fail("상세보기 조회 중 서버 오류 발생");
        }

    }

}
