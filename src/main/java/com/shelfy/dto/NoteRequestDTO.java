package com.shelfy.dto;
/*
     날짜 : 2025/02/04
     이름 : 박경림
     내용 : NoteRequestDTO - 노트 요청 데이터 전송 객체 추가
*/
public class NoteRequestDTO {
    private int noteId;              // 메모 ID (수정 시 필요)
    private int noteUserId;
    private String noteTitle;
    private String noteContents;
    private String noteImage;
    private Integer noteRStateId;  // 서재에 있는 책 ID (null 가능)
    private boolean notePin;
    private String noteCategory;

    // Getters & Setters
    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public int getNoteUserId() {
        return noteUserId;
    }

    public void setNoteUserId(int noteUserId) {
        this.noteUserId = noteUserId;
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

    public Integer getNoteRStateId() {
        return noteRStateId;
    }

    public void setNoteRStateId(Integer noteRStateId) {
        this.noteRStateId = noteRStateId;
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
}