package com.shelfy.mapper;

import com.shelfy.dto.BookDTO;
import com.shelfy.dto.RecordStateDTO;
import com.shelfy.dto.request.CreateRecordReqDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/*
     날짜 : 2025/02/04
     이름 : 박연화
     내용 : RecordMapper 생성

*/
@Mapper
public interface RecordMapper {

    // tb_r_state ---------------------------------------------------------------------------------
    // 250204 박연화 bookId, stateType이 동일한 기록이 존재하는지 조회
    @Select("SELECT COUNT(*) FROM tb_r_state WHERE r_state_book_id = #{bookId} AND r_state_type = #{stateType} ")
    int selectByBookIdAndStateType(CreateRecordReqDTO dto);

    // 250206 박연화 bookId 동일한 기록이 존재하는지 조회
    @Select("SELECT * FROM tb_r_state WHERE r_state_book_id = #{bookId} ")
    RecordStateDTO selectByBookId(CreateRecordReqDTO dto);

    // 250204 박연화 record state 테이블 insert
    @Insert("INSERT INTO tb_r_state (r_state_book_id, r_state_user_id ,r_state_type) " +
            "VALUES (#{bookId}, #{userId}, #{stateType}) AND r_state_user_id = #{userId ")
    @Options(useGeneratedKeys = true, keyProperty = "stateId")
    void insertRecordState(CreateRecordReqDTO dto);

    // 250206 박연화 record state 테이블 동일한 state id 를 가진 데이터 update
    @Update("UPDATE tb_r_state " +
            "SET r_state_type = #{bookId}, r_state_updated_at = now() " +
            "WHERE r_state_id = #{stateId} AND r_state_user_id = #{userId ")
    @Options(keyProperty = "stateId")
    void updateRecordState(RecordStateDTO dto);


    // tb_r_done ---------------------------------------------------------------------------------
    // 250204 박연화 Done 테이블 insert
    @Insert("INSERT INTO tb_r_done " +
            "(r_done_state_id, r_done_start_date, r_done_end_date, r_done_star, r_done_review) " +
            "VALUES (#{stateId}, #{startDate}, #{endDate}, #{rating}, #{comment})")
    int insertDone(CreateRecordReqDTO dto);


    // tb_r_doing ---------------------------------------------------------------------------------
    // 250204 박연화 Doing 테이블 insert
    @Insert("INSERT INTO tb_r_doing " +
            "(r_doing_state_id, r_doing_start_date, r_doing_progress) " +
            "VALUES (#{stateId}, #{startDate}, #{progress})")
    int insertDoing(CreateRecordReqDTO dto);


    // tb_r_wish ---------------------------------------------------------------------------------
    // 250204 박연화 Wish 테이블 insert
    @Insert("INSERT INTO tb_r_wish (r_wish_state_id, r_wish_heart, r_wish_comment) " +
            "VALUES (#{stateId}, #{rating}, #{comment})")
    int insertWish(CreateRecordReqDTO dto);


    // tb_r_stop ---------------------------------------------------------------------------------
    // 250204 박연화 Stop 테이블 insert
    @Insert("INSERT INTO tb_r_stop (r_stop_state_id, r_stop_start_date, r_stop_end_date, r_stop_end_page, r_stop_star, r_stop_review) " +
            "VALUES (#{stateId}, #{startDate}, #{endDate}, #{progress}, #{rating}, #{comment})")
    int insertStop(CreateRecordReqDTO dto);


}
