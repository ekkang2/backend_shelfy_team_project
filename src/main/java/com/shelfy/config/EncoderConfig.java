/*
    2025/02/05
    전규찬
    비밀번호 암호화/복호화 기능에 필요한 BCryptPasswordEncoder를 전역적으로 사용하기 위해 @Bean 등록
 */

package com.shelfy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class EncoderConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
