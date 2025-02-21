package com.shelfy.controller;

import com.shelfy.dto.BookDTO;
import com.shelfy.dto.ResponseDTO;
import com.shelfy.dto.request.MyBookDTO;
import com.shelfy.service.BookService;
import com.shelfy.service.MyBookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
     날짜 : 2025/02/04
     이름 : 강은경
     내용 : MyBookController 생성

*/

@RestController
@RequestMapping("/api/my/book")
@RequiredArgsConstructor
@Log4j2
public class MyBookController {

    private final BookService bookService;
    private final MyBookService myBookService;


    /**
     * 내가 등록한 책 저장
     * @param myBookDTO
     * @return
     */
    @PostMapping("/save")
    public ResponseDTO<MyBookDTO> saveBook(@RequestBody MyBookDTO myBookDTO) {

        log.info("책 저장할 myBookDTO : " + myBookDTO);

        try {
            MyBookDTO myBook = myBookService.insertMyBook(myBookDTO);
            log.info("내가 저장한 책 반환 myBook : " + myBook);
            return ResponseDTO.success(myBook);
        }catch (Exception e) {
            log.error(e);
            return ResponseDTO.fail("책 저장 중 서버 오류 발생");
        }
    }


}
