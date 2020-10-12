package com.mengxuegu.oauth2.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author wangpengyu
 * @version 1.0-SNAPSHOT
 * @date 2020/10/12 10:04
 * @desc 请求资源前, 先通过此过滤器进行用户令牌解析与校验、转发
 */
@Component
@Slf4j
public class AuthenticationFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {

        // 获取Security上下文中的认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // JWT令牌有效会解析用户信息封装到OAuth2Authentication对象中
        if (!(authentication instanceof OAuth2Authentication)) {
            return null;
        }

        // 只是用户名，用户表中手机号，邮箱都没有
        Object principal = authentication.getPrincipal();
        // 该用户拥有的权限
        Collection<? extends GrantedAuthority> grantedAuthorities = authentication.getAuthorities();
        Set<String> authoritySet = AuthorityUtils.authorityListToSet(grantedAuthorities);

        // 请求详情
        Object details = authentication.getDetails();

        // 封装传输数据
        Map<String, Object> map = new HashMap<>();
        map.put("pricipal", principal);
        map.put("details", details);
        map.put("authorities", authoritySet);

        // 获取请求中的上下文
        try {
            RequestContext requestContext = RequestContext.getCurrentContext();
            String base64 = Base64Utils.encodeToString(JSON.toJSONString(map).getBytes("UTF-8"));
            requestContext.addZuulRequestHeader("auth-token", base64);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }
}
