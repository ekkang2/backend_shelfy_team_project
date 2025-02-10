package com.shelfy.mapper;

import com.shelfy.dto.RecordDataDTO;
import com.shelfy.dto.RecordStateDTO;
import com.shelfy.dto.request.RecordDTO;
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
    int selectStateByBookIdAndStateType(RecordDTO dto);

    // 250206 박연화 userId, bookId 동일한 state 기록이 존재하는지 조회
    @Select("SELECT r_state_id, r_state_book_id, r_state_user_id, r_state_type, r_state_created_at, r_state_updated_at " +
            "FROM tb_r_state " +
            "WHERE r_state_book_id = #{bookId} AND r_state_user_id = #{userId}")
    RecordStateDTO selectStateByBookIdAndUserId(RecordDTO dto);

    // 250204 박연화 record state 테이블 insert
    @Insert("INSERT INTO tb_r_state (r_state_book_id, r_state_user_id ,r_state_type) " +
            "VALUES (#{bookId}, #{userId}, #{stateType}) ")
    @Options(useGeneratedKeys = true, keyProperty = "stateId")
    void insertRecordState(RecordDTO dto);

    // 250206 박연화 record state 테이블 동일한 state id 를 가진 데이터 update
    @Update("UPDATE tb_r_state " +
            "SET r_state_type = #{rStateType}, r_state_updated_at = now() " +
            "WHERE r_state_id = #{rStateId} AND r_state_user_id = #{rStateUserId} ")
    @Options(keyProperty = "stateId")
    void updateRecordStateType(RecordStateDTO dto);


    // tb_r_done ---------------------------------------------------------------------------------
    // 250207 박연화 stateId 가 동일한 record 조회
    @Select("SELECT r_done_id, r_done_state_id, r_done_start_date, r_done_end_date, r_done_rating, r_done_comment, r_done_active " +
            "FROM tb_r_done " +
            "WHERE r_done_state_id = #{stateId} ")
    @Results({
            @Result(column = "r_done_id", property = "recordId"),
            @Result(column = "r_done_state_id", property = "stateId"),
            @Result(column = "r_done_start_date", property = "startDate"),
            @Result(column = "r_done_end_date", property = "endDate"),
            @Result(column = "r_done_rating", property = "rating"),
            @Result(column = "r_done_comment", property = "comment"),
            @Result(column = "r_done_active", property = "active")
    })
    RecordDataDTO selectDoneByStateId(RecordDTO dto);


    // 250204 박연화 Done 테이블 insert
    @Insert("INSERT INTO tb_r_done " +
            "(r_done_state_id, r_done_start_date, r_done_end_date, r_done_rating, r_done_comment, r_done_active ) " +
            "VALUES (#{stateId}, #{startDate}, #{endDate}, #{rating}, #{comment}, #{active} )")
    @Options(useGeneratedKeys = true, keyProperty = "recordId")
    int insertDone(RecordDTO dto);

    // 250210 박연화 done select all
    @Select("SELECT r_done_id, r_done_state_id, r_done_start_date, r_done_end_date, r_done_rating, r_done_comment, r_done_active " +
            "FROM tb_r_done " +
            "WHERE r_done_user_id = #{userId} ")
    @Results({
            @Result(column = "r_done_id", property = "recordId"),
            @Result(column = "r_done_state_id", property = "stateId"),
            @Result(column = "r_done_start_date", property = "startDate"),
            @Result(column = "r_done_end_date", property = "endDate"),
            @Result(column = "r_done_rating", property = "rating"),
            @Result(column = "r_done_comment", property = "comment"),
            @Result(column = "r_done_active", property = "active")
    })
    List<RecordDTO> selectDoneByUserId(int userId);

    // tb_r_doing ---------------------------------------------------------------------------------
    // 250207 박연화 stateId 가 동일한 record 조회
    @Select("SELECT r_doing_id, r_doing_state_id, r_doing_start_date, r_doing_progress, r_doing_active " +
            "FROM tb_r_doing " +
            "WHERE r_doing_state_id = #{stateId} ")
    @Results({
            @Result(column = "r_doing_id", property = "recordId"),
            @Result(column = "r_doing_state_id", property = "stateId"),
            @Result(column = "r_doing_start_date", property = "startDate"),
            @Result(column = "r_doing_progress", property = "progress"),
            @Result(column = "r_doing_active", property = "active")
    })
    RecordDataDTO selectDoingByStateId(RecordDTO dto);


    // 250204 박연화 Doing 테이블 insert
    @Insert("INSERT INTO tb_r_doing " +
            "(r_doing_state_id, r_doing_start_date, r_doing_progress, r_doing_active ) " +
            "VALUES (#{stateId}, #{startDate}, #{progress}, #{active} )")
    @Options(useGeneratedKeys = true, keyProperty = "recordId")
    int insertDoing(RecordDTO dto);


    // tb_r_wish ---------------------------------------------------------------------------------
    // 250207 박연화 stateId 가 동일한 record 조회
    @Select("SELECT r_wish_id, r_wish_state_id, r_wish_rating, r_wish_comment, r_wish_active " +
            "FROM tb_r_wish " +
            "WHERE r_wish_state_id = #{stateId} ")
    @Results({
            @Result(column = "r_wish_id", property = "recordId"),
            @Result(column = "r_wish_state_id", property = "stateId"),
            @Result(column = "r_wish_rating", property = "rating"),
            @Result(column = "r_wish_comment", property = "comment"),
            @Result(column = "r_wish_active", property = "active")
    })
    RecordDataDTO selectWishByStateId(RecordDTO dto);


    // 250204 박연화 Wish 테이블 insert
    @Insert("INSERT INTO tb_r_wish (r_wish_state_id, r_wish_rating, r_wish_comment, r_wish_active ) " +
            "VALUES (#{stateId}, #{rating}, #{comment}, #{active} )")
    @Options(useGeneratedKeys = true, keyProperty = "recordId")
    int insertWish(RecordDTO dto);


    // tb_r_stop ---------------------------------------------------------------------------------
    // 250207 박연화 stateId 가 동일한 record 조회
    @Select("SELECT r_stop_id, r_stop_state_id, r_stop_start_date, r_stop_end_date, r_stop_progress, r_stop_rating, r_stop_comment, r_stop_active " +
            "FROM tb_r_stop " +
            "WHERE r_stop_state_id = #{stateId} ")
    @Results({
            @Result(column = "r_stop_id", property = "recordId"),
            @Result(column = "r_stop_state_id", property = "stateId"),
            @Result(column = "r_stop_start_date", property = "startDate"),
            @Result(column = "r_stop_end_date", property = "endDate"),
            @Result(column = "r_stop_progress", property = "progress"),
            @Result(column = "r_stop_rating", property = "rating"),
            @Result(column = "r_stop_comment", property = "comment"),
            @Result(column = "r_stop_active", property = "active")
    })
    RecordDataDTO selectStopByStateId(RecordDTO dto);


    // 250204 박연화 Stop 테이블 insert
    @Insert("INSERT INTO tb_r_stop (r_stop_state_id, r_stop_start_date, r_stop_end_date, r_stop_progress, r_stop_rating, r_stop_comment, r_stop_active ) " +
            "VALUES (#{stateId}, #{startDate}, #{endDate}, #{progress}, #{rating}, #{comment}, #{active} )")
    @Options(useGeneratedKeys = true, keyProperty = "recordId")
    int insertStop(RecordDTO dto);


}
