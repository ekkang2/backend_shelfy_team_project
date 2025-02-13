package com.shelfy.dto.record;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/*
    2025/02/04
    박연화
    CreateRecordReqDTO - 독서기록 추가 (done/doing/wish/stop 통합)
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // 알 수 없는 JSON 필드 자동 무시
public class RecordRespDTO {

    @JsonProperty("stateId")
    public int stateId;
    @JsonProperty("bookId")
    public String bookId;
    @JsonProperty("userId")
    public int userId;
    @JsonProperty("stateType")
    public int stateType;

    // cate 통합
    public int recordId;
    public LocalDate startDate;
    public LocalDate endDate;
    public String comment;
    public int progress;
    public double rating;
    public int active;


    public String bookImage;
    public String bookTitle;
    public String bookAuthor;
    public String bookPublisher;
    public int bookPage;

}
