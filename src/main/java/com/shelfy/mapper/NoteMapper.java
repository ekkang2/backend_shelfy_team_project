package com.shelfy.mapper;

import com.shelfy.dto.NoteRequestDTO;
import com.shelfy.dto.NoteResponseDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/*
     날짜 : 2025/02/06
     이름 : 박경림
     내용 :
        - NoteMapper 생성 - Create, Read, Update, Delete 추가
        - notePin(북마크) 기능, 쿼리 추가
*/

@Mapper
public interface NoteMapper {

    // 노트 글 작성 (Create)
    @Insert("INSERT INTO tb_note (note_user_id, note_title, note_contents, note_image, note_r_state_id, note_pin, note_category) " +
            "VALUES (#{noteUserId}, #{noteTitle}, #{noteContents}, #{noteImage}, #{noteRStateId}, #{notePin}, #{noteCategory})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")  // 자동 생성된 ID를 noteId에 주입
    void insertNote(NoteRequestDTO noteRequestDTO);  // 노트 글 데이터 DB에 저장

    // 모든 노트 글 목록 조회 (Read - List)
    @Select("SELECT note_id AS noteId, note_title AS noteTitle, note_contents AS noteContents, " +
            "note_image AS noteImage, note_pin AS notePin, note_category AS noteCategory, " +
            "note_created_at AS noteCreatedAt, note_updated_at AS noteUpdatedAt " +
            "FROM tb_note " +
            "WHERE note_user_id = #{noteUserId} " +  // 로그인한 유저의 노트 글만 조회
            "ORDER BY note_created_at DESC")
    List<NoteResponseDTO> getAllNotes(int noteUserId);

    // 특정 노트 글 상세 조회 (Read - Detail)
    @Select("SELECT note_id AS noteId, note_title AS noteTitle, note_contents AS noteContents, " +
            "note_image AS noteImage, note_pin AS notePin, note_category AS noteCategory, " +
            "note_created_at AS noteCreatedAt, note_updated_at AS noteUpdatedAt " +
            "FROM tb_note " +
            "WHERE note_id = #{noteId}")
    NoteResponseDTO getNoteById(int noteId);


    // 노트 글 부분 수정 (PATCH - 보낸 필드만 업데이트)
    //`<script>` MyBatis에서 동적 SQL을 사용할 수 있도록 지원 `<set>`자동으로 `,`(콤마) 정리해줌
    // `<if test='변수 != null'>` 값이 있을 때만 `UPDATE` 실행
    @Update("<script>" +
            "UPDATE tb_note " +
            "<set>" +
            "   <if test='noteTitle != null'>note_title = #{noteTitle},</if> " +
            "   <if test='noteContents != null'>note_contents = #{noteContents},</if> " +
            "   <if test='noteImage != null'>note_image = #{noteImage},</if> " +
            "   <if test='noteRStateId != null'>note_r_state_id = #{noteRStateId},</if> " +
            "   <if test='noteCategory != null'>note_category = #{noteCategory},</if> " +
            "   note_updated_at = CURRENT_TIMESTAMP " +  // 항상 업데이트 시간은 수정
            "</set> " +
            "WHERE note_id = #{noteId}" +
            "</script>")
    void patchNote(NoteRequestDTO noteRequestDTO);

    // 노트 글 삭제
    @Delete("DELETE FROM tb_note WHERE note_id = #{noteId}")
    void deleteNote(int noteId);    // 노트 글 ID로 삭제

    // 특정 노트 글 북마크 추가/해제
    @Update("UPDATE tb_note SET note_pin = #{notePin} WHERE note_id = #{noteId}")
    void updateNotePin(@Param("noteId") int noteId, @Param("notePin") boolean notePin);

    // 북마크된 노트 목록 조회 (notePin이 true인 경우만)
    @Select("SELECT * FROM tb_note WHERE note_user_id = #{noteUserId} AND note_pin = true")
    List<NoteResponseDTO> getPinnedNotes(@Param("noteUserId") int noteUserId);
}
