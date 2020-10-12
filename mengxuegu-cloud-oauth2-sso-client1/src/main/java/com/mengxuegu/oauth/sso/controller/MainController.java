package com.mengxuegu.oauth.sso.controller;

import com.mengxuegu.base.result.MengxueguResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author wangpengyu
 * @version 1.0-SNAPSHOT
 * @date 2020/10/10 9:54
 */
@Controller
@Slf4j
public class MainController {

    @Autowired
    private OAuth2RestTemplate restTemplate;

    @GetMapping(value = "/")
    public String index() {
        return "index";
    }

    @GetMapping(value = "/member")
    public String member() {
        MengxueguResult result = restTemplate.getForObject("http://localhost:7001/product/list", MengxueguResult.class);

        log.info("获取商品信息：{}", result);
        return "member";
    }


}
