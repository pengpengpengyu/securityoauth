package com.mengxuegu.oauth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * @author wangpengyu
 * @version 1.0-SNAPSHOT
 * @date 2020/9/20 9:42
 */
@Configuration
public class TokenConfig {

    /**
     * Redis管理令牌
     * 1.启动rdis服务器
     * 2.引入redis相关依赖
     * 3.添加redis依赖后，容器中就会有RedisConnectionFactory实例
     */
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    /**
     * redis管理令牌
     *
     * @return
     */
    @Bean
    public TokenStore tokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }


}
