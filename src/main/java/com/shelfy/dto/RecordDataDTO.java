package com.shelfy.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/*
    날 짜 : 2025/02/07(금)
    담당자 : 박연화
    내 용 : record 정보를 전달하기 위한 DTO (하나로 통합해서 사용)
*/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // 알 수 없는 JSON 필드 자동 무시
public class RecordDataDTO {


    @JsonProperty("recordId")
    public int recordId;

    @JsonProperty("stateId")
    public int stateId;

    @JsonProperty("startDate")
    public LocalDate startDate;

    @JsonProperty("endDate")
    public LocalDate endDate;

    @JsonProperty("comment")
    public String comment;

    @JsonProperty("progress")
    public int progress;

    @JsonProperty("rating")
    public double rating;

    @JsonProperty("active")
    public int active;
}
