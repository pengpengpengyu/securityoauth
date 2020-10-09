package com.mengxuegu.oauth2.resource.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

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
    public static final String RESOURCE_ID = "product-server";

    /**
     * 认证服务器验证token URL
     */
    public static final String AUTHORIZATION_SERVER_URL = "http://localhost:8090/auth/oauth/check_token";

    @Autowired
    private TokenStore tokenStore;


    /**
     * 当前资源服务器配置
     *
     * @param resources
     * @throws Exception
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(RESOURCE_ID)  // 配置当前资源服务器的ID,会在认证服务器验证(客户端表的 resources配置了就可以访问这个服务)
        .tokenStore(tokenStore);
        //.tokenServices(tokenServices());  // 实现令牌服务，ResuourceServiceTokenServices实例
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.sessionManagement()
                // springSecurity不会再创建也不会再使用HttpSession
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 所有请求对应访问的用户都要有all的范围权限
                .antMatchers("/**").access("#oauth2.hasScope('all')");

    }

    /**
     * 配置资源服务器如何验证token有效性
     * 1.DefaultTokenServices
     *  如果认证服务器和资源服务器同一服务时,则直接采用此默认服务验证即可
     *  2. RemoteTokenServices (当前采用这个)
     *  当认证服务器和资源服务器不是同一服务时, 要使用此服务去远程认证服务器验证
     * @return
     */
    /*@Bean
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
    }*/
}
