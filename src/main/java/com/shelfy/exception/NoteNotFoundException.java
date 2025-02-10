package com.shelfy.exception;

// 특정 노트가 존재하지 않을 때 발생하는 커스텀 예외 클래스
// - 존재하지 않는 노트를 조회할 경우 발생
// - 전역 예외 처리(`GlobalAdvice`)에서 감지하여 적절한 응답 반환
public class NoteNotFoundException extends RuntimeException {
    public NoteNotFoundException() {
    }

    public NoteNotFoundException(String message) {
        super(message);
    }

    public NoteNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoteNotFoundException(Throwable cause) {
        super(cause);
    }
}
