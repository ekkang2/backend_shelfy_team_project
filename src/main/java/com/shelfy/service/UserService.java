package com.shelfy.service;

import com.shelfy.dto.UserDTO;
import com.shelfy.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserMapper userMapper;

    public void insertUser(UserDTO userDTO) {
        if (userMapper.findByUserUid(userDTO.getUserUid()) != null) {
            throw new RuntimeException("이미 존재하는 사용자입니다.");
        }

        UserDTO user = UserDTO.builder()
                .userUid(userDTO.getUserUid())
                .userNick(userDTO.getUserNick())
                .userEmail(userDTO.getUserEmail())
                .userPwd(userDTO.getUserPwd())
                .build();

        userMapper.insert(user);
    }

}
