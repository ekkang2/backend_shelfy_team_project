package com.shelfy.controller;

import com.shelfy.dto.NoteRequestDTO;
import com.shelfy.dto.NoteResponseDTO;
import com.shelfy.service.NoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
     날짜 : 2025/02/04
     이름 : 박경림
     내용 : NoteController 생성 - Create, Read, Update, Delete API 추가
*/

@RestController
@RequestMapping("/api/note")
public class NoteController {

    private final NoteService noteService;

    // 생성자 주입
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    // 노트 글 작성
    @PostMapping
    public ResponseEntity<Map<String, Object>> createNote(@RequestBody NoteRequestDTO noteRequestDTO) {
        noteService.createNote(noteRequestDTO);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Note saved successfully!");
        return ResponseEntity.ok(response);
    }

    // 노트 글 목록 조회
    @GetMapping("/user/{userId}")
    public List<NoteResponseDTO> getAllNotes(@PathVariable int userId) {
        return noteService.getAllNotes(userId);
    }

    // 노트 글 상세 조회
    @GetMapping("/{noteId}")
    public NoteResponseDTO getNoteById(@PathVariable int noteId) {
        return noteService.getNoteById(noteId);
    }

    // 노트 글 부분 수정 (PATCH)
    @PatchMapping("/{noteId}")
    public void patchNote(@PathVariable int noteId, @RequestBody NoteRequestDTO noteRequestDTO) {
        noteRequestDTO.setNoteId(noteId);  // noteId를 DTO에 세팅
        noteService.patchNote(noteRequestDTO);
    }

    // 노트 글 삭제 (Delete)
    @DeleteMapping("/{noteId}")
    public void deleteNote(@PathVariable int noteId) {
        noteService.deleteNote(noteId);
    }

}
