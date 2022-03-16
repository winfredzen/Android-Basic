package com.imooc.router.demo.sample;

import java.util.HashMap;
import java.util.Map;

public class RouterMapping_2 {

    public static Map<String, String> get() {

        Map<String, String> mapping = new HashMap<>();

        mapping.put("router://page-kotlin", "com.imooc.router.demo.KtMainActivity");
        mapping.put("router://page-home", "com.imooc.router.demo.MainActivity");
        return mapping;
    }
}
