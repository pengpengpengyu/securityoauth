package com.mengxuegu.oauth.sso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author wangpengyu
 * @version 1.0-SNAPSHOT
 * @date 2020/10/10 9:51
 */
@SpringBootApplication
@EnableEurekaClient
public class Clent1Application {

    public static void main(String[] args) {
        SpringApplication.run(Clent1Application.class, args);
    }
}
