package com.shelfy.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/*
    2025/02/12
    강은경
    MyBookDTO 추가 - 내가 등록한 책
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyBookDTO {

    public int myBookId;
    public String myBookImage;
    public String myBookTitle;
    //public String myBookDesc;
    public String myBookAuthor;
    public String myBookPublisher;
    public String myBookIsbn;
    public String myBookPage;
    //public String myBookPublishedAt;

}
