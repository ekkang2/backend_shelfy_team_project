package com.shelfy.controller;

import com.shelfy.dto.NoteRequestDTO;
import com.shelfy.dto.NoteResponseDTO;
import com.shelfy.dto.ResponseDTO;
import com.shelfy.service.NoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
     날짜 : 2025/02/06
     이름 : 박경림
     내용
      - NoteController 생성 - Create, Read, Update, Delete API 추가
      - 작성, 수정, 삭제시 응답 반환값 ResponseDTO 적용 / 북마크 설정/해제 및 북마크된 메모 목록 조회 API 추가
*/

@RestController
@RequestMapping("/api/note")
public class NoteController {

    private final NoteService noteService;

    // 생성자 주입
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    // dto 분리

    // 노트 글 작성
    // 노트 생성 dto
    @PostMapping
    public ResponseEntity<Integer> createNote(@RequestBody NoteRequestDTO noteRequestDTO) {
        // 토큰을 받아서 디코딩해서 사용자 id로 검증하는데 검증은 서비스 함수에서!!
        int noteId = noteService.createNote(noteRequestDTO);  // 서비스에서 반환된 ID 받기
        return ResponseEntity.ok(noteId);  // 프론트에 ID 반환
    }

    // 노트 글 목록 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<ResponseDTO<List<NoteResponseDTO>>> getAllNotes(@PathVariable int userId) {
        List<NoteResponseDTO> notes = noteService.getAllNotes(userId);
        return ResponseEntity.ok(ResponseDTO.success(notes));  // 성공 응답 DTO 적용
    }

    // 노트 글 상세 조회
    @GetMapping("/{noteId}")
    public NoteResponseDTO getNoteById(@PathVariable int noteId) {
        return noteService.getNoteById(noteId);
    }

    // 노트 글 부분 수정 (PATCH)
    @PatchMapping("/{noteId}")
    public ResponseEntity<ResponseDTO<Integer>> patchNote(@PathVariable int noteId, @RequestBody NoteRequestDTO noteRequestDTO) {
        noteRequestDTO.setNoteId(noteId);  // 수정할 글 ID 설정
        int updatedNoteId = noteService.patchNote(noteRequestDTO);  // 반환된 ID 사용
        return ResponseEntity.ok(ResponseDTO.success(updatedNoteId));
    }

    // 노트 글 삭제 (Delete)
    @DeleteMapping("/{noteId}")
    public ResponseEntity<ResponseDTO<Void>> deleteNote(@PathVariable int noteId) {

        noteService.deleteNote(noteId);
        return ResponseEntity.ok(ResponseDTO.success(null));  // 삭제 성공 시 null 반환
    }

    // 특정 노트의 북마크 추가/해제
    @PatchMapping("/{noteId}/pin")
    public ResponseEntity<ResponseDTO<Void>> updateNotePin(
            @PathVariable int noteId, @RequestParam boolean notePin) {

        noteService.updateNotePin(noteId, notePin);
        return ResponseEntity.ok(ResponseDTO.success(null));  // 성공 시 응답
    }

    // 북마크된 노트 목록 조회
    @GetMapping("/pinned")
    public ResponseEntity<ResponseDTO<List<NoteResponseDTO>>> getPinnedNotes(
            @RequestParam int noteUserId) {

        return ResponseEntity.ok(ResponseDTO.success(noteService.getPinnedNotes(noteUserId)));
    }

}
