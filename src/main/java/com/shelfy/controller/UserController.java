package com.shelfy.controller;

import com.shelfy.dto.ResponseDTO;
import com.shelfy.dto.UserDTO;
import com.shelfy.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseDTO<UserDTO> join(@RequestBody UserDTO userDTO) {
        log.info(userDTO.toString());
        try {
            String status = userService.insertUser(userDTO);
            return ResponseDTO.success(null);
        }catch (Exception e) {
            log.error(e);
            return ResponseDTO.fail("회원가입 중 서버 오류 발생");
        }
    }

    @PostMapping("/login")
    public ResponseDTO<UserDTO> login(@RequestBody UserDTO userDTO, HttpServletResponse response) {
        log.info(userDTO.toString());
        try {

            // jwt 토큰 생성
            String token = userService.login(userDTO);

            // 응답 헤더에 JWT 토큰 추가
            response.setHeader("Authorization", "Bearer " + token);

            // 응답 body 에 회원 정보 주입하기 위해 회원 조회
            UserDTO user = userService.getUserByUid(userDTO.getUserUid());
            log.info(user.toString());

            // 응답 body 에 담아 보내줄 회원 정보 설계
            UserDTO responseUser = UserDTO.builder()
                    .userUid(user.getUserUid())
                    .userNick(user.getUserNick())
                    .userEmail(user.getUserEmail())
                    .userProfile(user.getUserProfile())
                    .userGenre(user.getUserGenre())
                    .build();

            // 클라이언트에 토큰을 반환
            return ResponseDTO.success(responseUser);

        }catch (Exception e) {
            log.error(e);
            return ResponseDTO.fail("로그인 실패");
        }
    }
    

}
