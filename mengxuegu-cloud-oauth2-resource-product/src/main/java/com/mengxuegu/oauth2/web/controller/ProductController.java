package com.mengxuegu.oauth2.web.controller;

import com.mengxuegu.base.result.MengxueguResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangpengyu
 * @version 1.0-SNAPSHOT
 * @date 2020/9/21 10:13
 */
@RestController
@RequestMapping(value = "/product")
public class ProductController {

    /**
     * 获取商品列表
     *
     * @return
     */
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('product:list')")
    public Object list() {
        List<String> list = new ArrayList<>();
        list.add("眼镜");
        list.add("格子衬衣");
        list.add("双肩包");
        return MengxueguResult.ok(list);
    }
}
