package com.shelfy.mapper;

import com.shelfy.dto.UserDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("SELECT user_id, user_uid, user_pwd FROM tb_user WHERE user_uid = #{userUid}")
    UserDTO findByUserUid(String userUid);

    @Select("select user_id, user_uid, user_email, user_nick, user_profile, user_genre from tb_user where user_uid = #{userUid} and user_pwd=#{userPwd}")
    UserDTO findByUsernameAndPassword(String userUid, String userPwd);

    @Insert("insert into tb_user (user_uid, user_nick, user_email, user_pwd) values (#{userUid}, #{userNick}, #{userEmail}, #{userPwd})")
    @Options(useGeneratedKeys = true, keyProperty = "userId", keyColumn = "user_id")
    void insert(UserDTO user);

}
