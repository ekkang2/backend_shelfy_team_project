package com.shelfy.repository;
import com.shelfy.document.BookDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.print.Book;
import java.util.List;

/*
    날 짜 : 2025/02/03(목)
    담당자 : 강은경
    내 용 : Book 를 위한 Repository 생성
*/

@Repository
public interface BookRepository extends MongoRepository<BookDocument, String> {


    // 제목, 저자 또는 출판사로 책 검색 (LIKE와 유사)
    @Query("{ '$or': [ " +
            "{ 'bookTitle': { '$regex': ?0, '$options': 'i' } }, " +
            "{ 'bookAuthor': { '$regex': ?0, '$options': 'i' } }, " +
            "{ 'bookPublisher': { '$regex': ?0, '$options': 'i' } } " +
            "] }")
    List<BookDocument> searchBooksByQuery(String query);


    // ISBN 목록을 받아 해당하는 책을 조회
    List<BookDocument> findByBookIsbnIn(List<String> bookIsbns);


}