package com.shelfy.service;

import com.shelfy.dto.NoteRequestDTO;
import com.shelfy.dto.NoteResponseDTO;
import com.shelfy.exception.NoteNotFoundException;
import com.shelfy.mapper.NoteMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/*
     날짜 : 2025/02/06
     이름 : 박경림
     내용
        - NoteService 생성 -  Create, Read, Update, Delete 추가
        - 작성, 수정시 글 id 반환 값 추가 / notePin 설정/해제 및 북마크된 메모 목록 조회 기능 추가
*/

@Service
public class NoteService {

    private final NoteMapper noteMapper;

    // 생성자 NoteMapper 주입 받아 DB 작업 가능하게 설정
    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    // 노트 글 작성
    public int createNote(NoteRequestDTO noteRequestDTO) {
        noteMapper.insertNote(noteRequestDTO);
        return noteRequestDTO.getNoteId();
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
    public int patchNote(NoteRequestDTO noteRequestDTO) {
        noteMapper.patchNote(noteRequestDTO);
        return noteRequestDTO.getNoteId();
    }

    // 노트 글 삭제 (Delete)
    public void deleteNote(int noteId) {
        NoteResponseDTO foundNote = noteMapper.getNoteById(noteId);

        if (foundNote == null)
        {
            throw new NoteNotFoundException();
        }
        noteMapper.deleteNote(noteId);
    }

    // 특정 노트 글 북마크 상태 변경 (true: 북마크 추가, false: 해제)
    public void updateNotePin(int noteId, boolean notePin) {
        noteMapper.updateNotePin(noteId, notePin);
    }

    // 북마크된 노트 목록 조회 (사용자가 북마크한 노트 리스트)
    public List<NoteResponseDTO> getPinnedNotes(int noteUserId) {
        return noteMapper.getPinnedNotes(noteUserId);
    }
}

