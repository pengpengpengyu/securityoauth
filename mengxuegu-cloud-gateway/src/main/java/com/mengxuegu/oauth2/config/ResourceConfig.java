package com.mengxuegu.oauth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * @author wangpengyu
 * @version 1.0-SNAPSHOT
 * @date 2020/10/12 9:42
 */
@Configuration
public class ResourceConfig {

    // 配置当前资源服务器的ID
    public static final String RESOURCE_ID = "product-server";

    @Autowired
    private TokenStore tokenStore;

    // 认证资源服务器拦截
    @Configuration
    @EnableResourceServer
    public class AuthResourceConfig extends ResourceServerConfigurerAdapter {

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
            resources.resourceId(RESOURCE_ID).tokenStore(tokenStore);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            // 认证资源服务器相关资源全部放行
            http.authorizeRequests()
                    .anyRequest().permitAll();
        }
    }

    // 商品资源服务器拦截
    @Configuration
    @EnableResourceServer
    public class ProductResourceConfig extends ResourceServerConfigurerAdapter {

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
            resources.resourceId(RESOURCE_ID).tokenStore(tokenStore);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            // 客户端要有PRODUCT_API范围才可以访问
            http.authorizeRequests()
                    .antMatchers("/product/**")
                    .access("#oauth2.hasScope('PRODUCT_API')");
        }
    }


}
