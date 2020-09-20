package com.mengxuegu.oauth2.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;

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
     * JDBC管理令牌
     * 1.创建相关数据表
     * 2.添加jdbc相关依赖
     * 3.配置数据源信息
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return new DruidDataSource();
    }

    /**
     * 令牌管理方式
     *
     * @return
     */
    @Bean
    public TokenStore tokenStore() {
        // Redis管理令牌
        // return new RedisTokenStore(redisConnectionFactory);

        // JDBC管理令牌
        return new JdbcTokenStore(dataSource());
    }


}
