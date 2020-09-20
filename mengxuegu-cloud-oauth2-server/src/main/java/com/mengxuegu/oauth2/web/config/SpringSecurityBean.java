package com.mengxuegu.oauth2.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author wangpengyu
 * @version 1.0-SNAPSHOT
 * @date 2020/9/19 17:13
 * @desc 统一管理配置类bean
 */
@Configuration
public class SpringSecurityBean {

    /**
     * 加密方式
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
