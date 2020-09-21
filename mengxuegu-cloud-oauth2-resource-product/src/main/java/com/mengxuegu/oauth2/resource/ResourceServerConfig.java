package com.mengxuegu.oauth2.resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

/**
 * @author wangpengyu
 * @version 1.0-SNAPSHOT
 * @date 2020/9/21 10:26
 * @desc 资源服务器配置
 */
@Configuration
@EnableResourceServer  // 标记为资源服务器，每次收到请求都会去请求头找token验证
@EnableGlobalMethodSecurity(prePostEnabled = true)  // 开启方法级别权限控制
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    /**
     * 资源服务器的ID
     */
    public static final String RESOURCE_ID = "product_server";

    /**
     * 认证服务器验证token URL
     */
    public static final String AUTHORIZATION_SERVER_URL = "localhost:8090/auth/oauth/check_token";

    /**
     * 当前资源服务器配置
     *
     * @param resources
     * @throws Exception
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(RESOURCE_ID)
                .tokenServices(tokenServices());
    }

    /**
     * 配置资源服务器如何验证token有效性
     * 1.DefaultTokenServices
     *  如果资源服务器与认证服务器
     * @return
     */
    @Bean
    public ResourceServerTokenServices tokenServices() {

        // 资源服务器去远程认证服务器验证token是否有效
        RemoteTokenServices services = new RemoteTokenServices();
        // 设置验证token url,默认是拒绝访问的，需要配置登录后可访问
        services.setCheckTokenEndpointUrl(AUTHORIZATION_SERVER_URL);
        // 设置在认证服务器配置的客户端ID
        services.setClientId("mengxuegu-pc");
        // 设置在认证服务器配置的客户端密码
        services.setClientSecret("mengxuegu-secret");

        return services;
    }
}
