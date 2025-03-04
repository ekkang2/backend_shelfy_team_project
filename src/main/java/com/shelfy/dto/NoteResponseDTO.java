package com.shelfy.dto;

import java.time.LocalDateTime;
/*
     날짜 : 2025/02/04
     이름 : 박경림
     내용 : NoteRequestDTO - 노트 응답 데이터 전송 객체 추가
*/
public class NoteResponseDTO {
    private int noteId;
    private String noteTitle;
    private String noteContents;
    private String noteImage;
    private boolean notePin;
    private String noteCategory;
        private Integer noteRStateId;
    private LocalDateTime noteCreatedAt;
    private LocalDateTime noteUpdatedAt;

    // Getters & Setters
    public Integer getNoteRStateId() {
        return noteRStateId;
    }

    public void setNoteRStateId(Integer noteRStateId) {
        this.noteRStateId = noteRStateId;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteContents() {
        return noteContents;
    }

    public void setNoteContents(String noteContents) {
        this.noteContents = noteContents;
    }

    public String getNoteImage() {
        return noteImage;
    }

    public void setNoteImage(String noteImage) {
        this.noteImage = noteImage;
    }

    public boolean isNotePin() {
        return notePin;
    }

    public void setNotePin(boolean notePin) {
        this.notePin = notePin;
    }

    public String getNoteCategory() {
        return noteCategory;
    }

    public void setNoteCategory(String noteCategory) {
        this.noteCategory = noteCategory;
    }

    public LocalDateTime getNoteCreatedAt() {
        return noteCreatedAt;
    }

    public void setNoteCreatedAt(LocalDateTime noteCreatedAt) {
        this.noteCreatedAt = noteCreatedAt;
    }

    public LocalDateTime getNoteUpdatedAt() {
        return noteUpdatedAt;
    }

    public void setNoteUpdatedAt(LocalDateTime noteUpdatedAt) {
        this.noteUpdatedAt = noteUpdatedAt;
    }
}