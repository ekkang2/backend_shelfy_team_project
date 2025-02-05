package com.shelfy.controller;

import com.shelfy.dto.ResponseDTO;
import com.shelfy.dto.UserDTO;
import com.shelfy.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
            userService.insertUser(userDTO);
            return ResponseDTO.success(userDTO);
        }catch (Exception e) {
            log.error(e);
            return ResponseDTO.fail("회원가입 중 서버 오류 발생");
        }
    }

    @PostMapping("/login")
    public ResponseDTO<UserDTO> login(@RequestBody UserDTO userDTO) {
        log.info(userDTO.toString());
        return ResponseDTO.success(userDTO);
    }
    

}
