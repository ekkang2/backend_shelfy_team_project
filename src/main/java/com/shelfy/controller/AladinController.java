package com.shelfy.controller;

import com.shelfy.dto.BookDTO;
import com.shelfy.dto.BookResponseDTO;
import com.shelfy.service.AladinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
     날짜 : 2025/01/30
     이름 : 강은경
     내용 : AladinController 생성

*/

@RestController
@RequestMapping("/api/aladin")
@RequiredArgsConstructor
@Log4j2
public class AladinController {

    private final AladinService aladinService;

    /**
     *  검색 api로 도서 조회
     * @param query
     * @return List<BookDTO>
     */

    @GetMapping("/search")
    public List<BookDTO> searchBooks(@RequestParam String query) {

        log.info("query : " + query);

        List<BookDTO> bookDTOS = aladinService.searchByAladin(query);
        log.info("알라딘 검색 api로 반환되는 bookDTOS : " + bookDTOS);

        return bookDTOS;
    }

    // 책 상세 조회
    @GetMapping("/detail/{bookIsbn}")
    public BookDTO selectBookDetail(@PathVariable String bookIsbn) {

        log.info("알라딘 상세조회 api요청 보낸 isbn : " + bookIsbn);
        BookDTO bookDTO = aladinService.selectBookDetailByAladin(bookIsbn);
        log.info("알라딘 상세조회 반환되는 bookDTO : " + bookDTO);

        return null;
    }




    // 📌 2차 검색 (ISBN으로 페이지 수 가져오기) - 책 검색 후 상세페이지
//    @GetMapping("/detail")
//    public BookDTO getBookDetail(@RequestParam String bookIsbn) {
//        log.info(bookIsbn);
//        return aladinService.getBookDetail(bookIsbn);
//    }
}
