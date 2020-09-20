package com.mengxuegu.oauth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.sql.DataSource;

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

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private DataSource dataSource;

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

        /*clients.inMemory()
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
                .redirectUris("https://www.baidu.com/");*/

        // 数据库管理
        clients.withClientDetails(jdbcClientDetailsService());
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
                .userDetailsService(customUserDetailsService)
                // 令牌管理策略配置
                .tokenStore(tokenStore)
                // 授权码管理策略配置
                .authorizationCodeServices(jdbcAuthorizationCodeServices());

    }

    /**
     * 令牌端点安全配置
     *
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
                // 所有人可以访问/oauth/token_key
        security.tokenKeyAccess("permitAll()")
                // 认证后可以访问/oauth/check_token，默认拒绝访问
                .checkTokenAccess("isAuthenticated()");
    }

    /**
     * 添加JDBC授权码管理策略
     *
     * @return
     */
    @Bean
    public AuthorizationCodeServices jdbcAuthorizationCodeServices() {
        // JDBC方式保存授权码到oauth_code表中
        // 先保存授权码，获取令牌token后删除授权码，其实放到数据库意义不大
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    /**
     * 添加客户端管理策略为JDBC
     *
     * @return
     */
    @Bean
    public ClientDetailsService jdbcClientDetailsService() {
        // 使用JDBC 方式管理客户端信息
        return new JdbcClientDetailsService(dataSource);
    }
}
