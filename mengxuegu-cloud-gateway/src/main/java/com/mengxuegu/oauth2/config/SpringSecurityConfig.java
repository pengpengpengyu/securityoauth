package com.mengxuegu.oauth2.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author wangpengyu
 * @version 1.0-SNAPSHOT
 * @date 2020/10/12 10:00
 */
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 这里放行所有请求，让资源配置类处理请求
        http.authorizeRequests()
                .anyRequest().permitAll();
    }
}
