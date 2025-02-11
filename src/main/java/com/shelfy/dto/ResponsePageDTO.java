package com.shelfy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponsePageDTO<T> {
    public boolean isFirst;
    public boolean isLast;
    public int pageNumber;
    public int size;
    public int totalPages;
    public List<T> contents;
}
