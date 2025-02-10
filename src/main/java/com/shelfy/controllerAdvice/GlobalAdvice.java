package com.shelfy.controllerAdvice;

import com.shelfy.dto.ResponseDTO;
import com.shelfy.exception.NoteNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/*
     날짜 : 2025/02/09
     이름 : 박경림
     내용 : 전역 예외 처리를 위한 RestControllerAdvice 추가
*/
// basePackages 는 해당 패키지 내부에서만 예외 감지?, 기본값은 전역
//@RestControllerAdvice(basePackages = "com.shelfy.controller")
@RestControllerAdvice
public class GlobalAdvice {
    // 구체적인 예외 (커스텀 예외 가능)
    @ExceptionHandler(NoteNotFoundException.class)
    public ResponseEntity<ResponseDTO<Void>> handleException(NoteNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ResponseDTO.fail("현재 노트는 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
    }

    // api 경로가 잘못되면 NoResourceFoundException 예외가 실행됨
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ResponseDTO<Void>> handleException(NoResourceFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ResponseDTO.fail("찾을 수 없는 리소스 입니다.", HttpStatus.NOT_FOUND));
    }

    // 나머지 모든 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO<Void>> handleException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseDTO.fail("알 수 없는 서버 에러입니다.", HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
