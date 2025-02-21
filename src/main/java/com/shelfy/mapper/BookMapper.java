package com.shelfy.mapper;

import com.shelfy.dto.BookDTO;
import com.shelfy.dto.request.MyBookDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;
/*
     날짜 : 2025/01/30
     이름 : 강은경
     내용 : BookMapper 생성

*/
@Mapper
public interface BookMapper {

    // 250212 강은경 mybook 테이블 insert
    @Insert("INSERT INTO tb_my_book " +
            "(my_book_image, my_book_title, my_book_desc, my_book_author, my_book_publisher, my_book_isbn, my_book_page, my_book_published_at) " +
            "VALUES (#{myBookImage}, #{myBookTitle}, #{myBookDesc}, #{myBookAuthor}, #{myBookPublisher}, #{myBookIsbn}, #{myBookPage}, #{myBookPublishedAt} )")
    @Options(useGeneratedKeys = true, keyProperty = "myBookId")
    MyBookDTO insertMyBook(MyBookDTO myBookDTO);







}
