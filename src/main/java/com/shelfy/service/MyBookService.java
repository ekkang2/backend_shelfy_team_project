package com.shelfy.service;

import com.shelfy.document.BookDocument;
import com.shelfy.dto.BookDTO;
import com.shelfy.dto.request.MyBookDTO;
import com.shelfy.mapper.BookMapper;
import com.shelfy.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/*
     날짜 : 2025/02/12
     이름 : 강은경
     내용 : MyBookService 생성

*/
@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class MyBookService {


    private final AladinService aladinService;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    // 나의 책 저장
    public MyBookDTO insertMyBook(MyBookDTO myBookDTO) {

        MyBookDTO myBook = bookMapper.insertMyBook(myBookDTO);
        return myBook;
    }




}
