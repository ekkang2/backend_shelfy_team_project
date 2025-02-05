package com.shelfy.mapper;

import com.shelfy.dto.UserDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface UserMapper {

    // 로그인 시 일치 여부를 위한 조회
    @Select("SELECT user_id, user_uid, user_pwd FROM tb_user WHERE user_uid = #{userUid}")
    UserDTO selectUserForLogin(String userUid);

    // 로그인 시 응답 Body에 보낼 회원 정보를 추출하기 위해 조회
    @Select("SELECT user_id, user_uid, user_nick, user_email, user_profile, user_genre FROM tb_user WHERE user_uid = #{userUid}")
    UserDTO selectUser(String userUid);

    @Insert("insert into tb_user (user_uid, user_nick, user_email, user_pwd) values (#{userUid}, #{userNick}, #{userEmail}, #{userPwd})")
    @Options(useGeneratedKeys = true, keyProperty = "userId", keyColumn = "user_id")
    void insert(UserDTO user);

}
