package com.mengxuegu.oauth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;

/**
 * @author wangpengyu
 * @version 1.0-SNAPSHOT
 * @date 2020/9/19 17:11
 * @desc 认证服务配置
 */
@Configuration
@EnableAuthorizationServer // 开启认证服务共能
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService customUserDetailsService;

    /**
     * 配置允许访问此认证服务器的客户端详细信息
     * 授权模式
     * 方式1：内存方式管理
     * 方式2：数据库管理
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 使用内存方式

        clients.inMemory()
                .withClient("mengxuegu-pc")  // 客户端ID
                // 客户端密码，要加密，不然一直要求登录，获取不到令牌，而且一定不能被泄露
                .secret(passwordEncoder.encode("mengxuegu-secret"))
                // 资源ID,如商品资源
                .resourceIds("product-server")
                // 授权类型，可同时支持多种授权类型
                .authorizedGrantTypes("authorization_code", "password", "implicit", "client_credentials", "refresh_token")
                // 授权范围标识，哪部分资源可访问(all 是标识，不代表所有)
                .scopes("all")
                // false表示需要跳转到用户授权页面手动授权，true不用手动授权，直接响应授权码
                .autoApprove(false)
                // 客户端回调地址
                .redirectUris("http://www.mengxuegu.com/");
    }

    /**
     * 密码模式
     *
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // 密码模式要设置认证管理器
        endpoints.authenticationManager(authenticationManager)
                //刷新令牌时需要
        .userDetailsService(customUserDetailsService);

    }
}