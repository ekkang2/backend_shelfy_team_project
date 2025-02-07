package com.shelfy.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/*
    날 짜 : 2025/02/06(목)
    담당자 : 박연화
    내 용 : record state 정보를 전달하기 위한 DTO
*/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // 알 수 없는 JSON 필드 자동 무시
public class RecordStateDTO {

    @JsonProperty("stateId")
    public int rStateId;

    @JsonProperty("bookId")
    public String rStateBookId;

    @JsonProperty("userId")
    public int rStateUserId;

    @JsonProperty("stateType")
    public int rStateType;

    @JsonProperty("createdAt")
    public LocalDateTime rStateCreatedAt;

    @JsonProperty("updatedAt")
    public LocalDateTime rStateUpdatedAt;
}
