package com.shelfy.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.shelfy.document.BookDocument;
import lombok.Data;

/*
    날 짜 : 2025/02/04(화)
    담당자 : 강은경
    내 용 : Book 정보를 전달하기 위한 DTO
*/
@Data
@JsonIgnoreProperties(ignoreUnknown = true) // 알 수 없는 JSON 필드 자동 무시
public class BookDTO {

    private String bookId;  // MongoDB의 기본 키 (_id)

    // private int bookId;  // 책 ID

    @JsonAlias("cover") // json -> 객체(역직렬화)할 때 "cover" 값을 bookImage에 매핑
    @JsonProperty("bookImage") // 객체 -> json(직렬화)할 때 bookImage 필드를 "bookImage"로 변환
    private String bookImage;  // 책 표지 이미지

    @JsonAlias("title")
    @JsonProperty("bookTitle")
    private String bookTitle;  // 책 제목

    @JsonAlias("description")
    @JsonProperty("bookDesc")
    private String bookDesc;  // 책 설명

    @JsonAlias("author")
    @JsonProperty("bookAuthor")
    private String bookAuthor;  // 저자

    @JsonAlias("publisher")
    @JsonProperty("bookPublisher")
    private String bookPublisher;  // 출판사

    @JsonAlias("isbn13")
    @JsonProperty("bookIsbn")
    private String bookIsbn;  // ISBN

    @JsonAlias("itemPage")
    @JsonProperty("bookPage")
    private int bookPage;  // 페이지 수

    @JsonAlias("pubDate")
    @JsonProperty("bookPublishedAt")
    private String bookPublishedAt;  // 출판일

    // bookinfo 내부의 itemPage 값을 자동 매핑
    @JsonProperty("bookinfo")
    private void unpackNestedBookInfo(BookInfo bookinfo) {
        if (bookinfo != null) {
            this.bookPage = bookinfo.getItemPage(); // itemPage 값을 bookPage에 저장
        }
    }

    // 알라딘 상세보기 api 요청시 필요
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BookInfo {
        private int itemPage; // bookinfo 내부의 페이지 수
    }

    // BookDTO를 BookDocument로 변환하는 메서드 추가
    public BookDocument toDocument() {
        BookDocument bookDocument = new BookDocument();
        bookDocument.setBookImage(this.bookImage);
        bookDocument.setBookTitle(this.bookTitle);
        bookDocument.setBookDesc(this.bookDesc);
        bookDocument.setBookAuthor(this.bookAuthor);
        bookDocument.setBookPublisher(this.bookPublisher);
        bookDocument.setBookIsbn(this.bookIsbn);
        bookDocument.setBookPage(this.bookPage);
        bookDocument.setBookPublishedAt(this.bookPublishedAt);
        return bookDocument;
    }
}