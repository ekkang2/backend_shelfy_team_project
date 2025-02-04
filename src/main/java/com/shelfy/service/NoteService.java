package com.shelfy.service;

import com.shelfy.dto.NoteRequestDTO;
import com.shelfy.dto.NoteResponseDTO;
import com.shelfy.mapper.NoteMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/*
     날짜 : 2025/02/04
     이름 : 박경림
     내용 : NoteService 생성 -  Create, Read, Update, Delete 추가
*/

@Service
public class NoteService {

    private final NoteMapper noteMapper;

    // 생성자 NoteMapper 주입 받아 DB 작업 가능하게 설정
    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    // 노트 글 작성 (반환 값 없어서 void)
    public void createNote(NoteRequestDTO noteRequestDTO) {
        noteMapper.insertNote(noteRequestDTO);
    }

    // 모든 노트 글 목록 조회
    public List<NoteResponseDTO> getAllNotes(int noteUserId) {
        return noteMapper.getAllNotes(noteUserId);
    }

    // 특정 노트 글 상세 조회
    public NoteResponseDTO getNoteById(int noteId) {
        return noteMapper.getNoteById(noteId);
    }

    // 노트 글 부분 수정 (PATCH)
    public void patchNote(NoteRequestDTO noteRequestDTO) {
        noteMapper.patchNote(noteRequestDTO);
    }


    // 노트 글 삭제 (Delete)
    public void deleteNote(int noteId) {
        noteMapper.deleteNote(noteId);
    }
}