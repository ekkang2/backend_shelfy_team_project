package com.shelfy.dto.record;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRecordDTO {
    private int progress;
    private String comment;
    private LocalDate startDate;
    private LocalDate endDate;
    private double rating;
}
