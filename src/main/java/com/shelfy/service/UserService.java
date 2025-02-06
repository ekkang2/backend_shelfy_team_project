/*
    2025/02/05
    전규찬
    회원 관련 기능을 구현할 Service
 */

package com.shelfy.service;

import com.shelfy.config.JwtProvider;
import com.shelfy.dto.UserDTO;
import com.shelfy.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Log4j2
@RequiredArgsConstructor
@Service
public class UserService {

    // 비밀번호 암호화를 위한 인코더 주입
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final UserMapper userMapper;


    /**
     * 회원가입 메서드
     *
     * @param userDTO : 사용자가 기입한 정보를 userDTO 형태로 수신
     * @return
     */
    public String insertUser(UserDTO userDTO) {
        try {
            // 회원가입 중복 검사
            if (userMapper.selectUserForLogin(userDTO.getUserUid()) != null) {
                return "fail";
            }

            // 받아온 비밀번호 암호화
            String encodedPwd = passwordEncoder.encode(userDTO.getUserPwd());

            UserDTO user = UserDTO.builder()
                    .userUid(userDTO.getUserUid())
                    .userNick(userDTO.getUserNick())
                    .userEmail(userDTO.getUserEmail())
                    .userPwd(encodedPwd)
                    .userProfile(userDTO.getUserProfile())
                    .build();

            userMapper.insert(user);

            return "success";

        } catch (Exception e) {
            log.error(e);
            return "fail";
        }
    }

    /**
     * 로그인 메서드 : 아이디 비밀번호 인증 후 토큰 반환
     */
    public String login(UserDTO userDTO) throws Exception {

        String userUid = userDTO.getUserUid();
        String userPwd = userDTO.getUserPwd();

        // 1. 사용자 조회
        UserDTO user = userMapper.selectUserForLogin(userUid);
        log.info(user);
        if (user == null) {
            throw new Exception("존재하지 않는 사용자입니다.");
        }

        // 2. 비밀번호 검증 : 입력받은 비밀번호와 암호화된 비밀번호 비교
        if (!passwordEncoder.matches(userPwd, user.getUserPwd())) {
            throw new Exception("비밀번호가 올바르지 않습니다.");
        }

        return jwtProvider.generateToken(userUid);
    }

    /**
     * 회원 아이디로 회원 정보 조회
     *
     * @param uid
     * @return
     */
    public UserDTO getUserByUid(String uid) {
        return userMapper.selectUser(uid);
    }


    /**
     * 아이디 중복 검사
     * @param userUid
     * @return
     */
    public boolean isUserUidDuplicated(String userUid){
        String user_uid = userMapper.selectUserUid(userUid);
        return user_uid.equals(userUid);
    }


    public boolean isUserEmailDuplicated(String userEmail){
        String user_email = userMapper.selectUserEmail(userEmail);
        return user_email.equals(userEmail);
    }


    public boolean isUserNickDuplicated(String userNick){
        String user_nick = userMapper.selectUserNick(userNick);
        return user_nick.equals(userNick);
    }



}
