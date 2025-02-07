package com.shelfy.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

/*
     날짜 : 2025/01/30
     이름 : 강은경
     내용 : 알라딘 API 반환을 위한 BookResponseDTO 생성

*/
@Data
@JsonIgnoreProperties(ignoreUnknown = true) // JSON에서 알 수 없는 필드는 무시
public class BookResponseDTO {
    @JsonProperty("item")  // JSON의 "item"과 매핑
    private List<BookDTO> item; // `item` 배열을 매핑
}
