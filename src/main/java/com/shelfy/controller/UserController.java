package com.shelfy.controller;

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
    public ResponseEntity<?> join(@RequestBody UserDTO userDTO) {
        log.info(userDTO.toString());
        try {
            userService.insertUser(userDTO);
            userDTO.setSuccess(Boolean.TRUE);
            return ResponseEntity.ok().body(userDTO);
        }catch (Exception e) {
            log.error(e);
            userDTO.setSuccess(Boolean.FALSE);
            return ResponseEntity.badRequest().body(userDTO);
        }
    }

    @GetMapping("/")
    public String home() {
        return "Hello World";
    }


}
