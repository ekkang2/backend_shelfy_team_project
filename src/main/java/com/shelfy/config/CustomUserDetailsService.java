/*
    2025/02/04
    전규찬
    Spring Security 가 인증 시 DB에서 사용자 정보를 호출하도록 설정하는 부분
 */
package com.shelfy.config;

import com.shelfy.dto.UserDTO;
import com.shelfy.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO userDTO = userMapper.findByUserUid(username);
        if (userDTO == null) {
            throw new UsernameNotFoundException("해당 유저를 찾을 수 없습니다.");
        }
        return new org.springframework.security.core.userdetails.User(
                userDTO.getUserUid(),
                userDTO.getUserPwd(),
                new ArrayList<>()
        );
    }
}
