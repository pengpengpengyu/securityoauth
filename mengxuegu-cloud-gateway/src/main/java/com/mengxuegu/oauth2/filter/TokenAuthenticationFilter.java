package com.mengxuegu.oauth2.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Base64Utils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wangpengyu
 * @version 1.0-SNAPSHOT
 * @date 2020/10/12 10:42
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        // 1. 获取从网关转发请求中的明文token
        String token = httpServletRequest.getHeader("auth-token");

        if (StringUtils.isNotBlank(token)) {
            // Base64解码
            String authenTokenJson = new String(Base64Utils.decodeFromString(token));

            // 转化成json对象
            JSONObject jsonObject = JSON.parseObject(authenTokenJson);

            // 用户权限
            String authorities = ArrayUtils.toString(jsonObject.getJSONArray("authorities").toArray());

            // 构建Authentication对象，SprignSecurity就会用于权限判断
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    jsonObject.get("principal"), null, AuthorityUtils.commaSeparatedStringToAuthorityList(authorities)
            );

            // 设置请求详情
            usernamePasswordAuthenticationToken.setDetails(jsonObject.get("details"));

            // 传递给上下文，这样服务可以获取对应数据
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
