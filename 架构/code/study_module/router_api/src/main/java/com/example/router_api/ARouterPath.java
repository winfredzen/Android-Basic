package com.example.router_api;

import com.example.annotation.bean.RouterBean;
import java.util.Map;

public interface ARouterPath {
    //path 和 RouterBean 之间的映射
    Map<String, RouterBean> getPathMap();
}