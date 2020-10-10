package com.mengxuegu.oauth.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author wangpengyu
 * @version 1.0-SNAPSHOT
 * @date 2020/10/10 9:54
 */
@Controller
public class MainController {

    @GetMapping(value = "/")
    public String index() {
        return "index";
    }

    @GetMapping(value = "/member")
    public String member() {
        return "member";
    }


}
