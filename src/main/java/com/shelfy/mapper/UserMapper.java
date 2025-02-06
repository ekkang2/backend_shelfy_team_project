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

    // 회원가입
    @Insert("insert into tb_user (user_uid, user_nick, user_email, user_pwd, user_profile) values (#{userUid}, #{userNick}, #{userEmail}, #{userPwd}, #{userProfile})")
    @Options(useGeneratedKeys = true, keyProperty = "userId", keyColumn = "user_id")
    void insert(UserDTO user);

    // 회원가입 시 아이디 중복검사 쿼리
    @Select("select user_uid from tb_user where user_uid = #{userUid}")
    String selectUserUid(String userUid);

    // 회원가입 시 이메일 중복검사 쿼리
    @Select("select user_email from tb_user where user_email = #{userEmail}")
    String selectUserEmail(String userEmail);

    // 회원가입 시 닉네임 중복검사 쿼리
    @Select("select user_nick from tb_user where user_nick = #{userNick}")
    String selectUserNick(String userNick);

}
