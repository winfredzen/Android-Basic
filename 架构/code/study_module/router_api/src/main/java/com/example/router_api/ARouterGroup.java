package com.example.router_api;

import java.util.Map;

/**
 * create by wangzhen 2021/10/7
 */
public interface ARouterGroup {
    /**
     * path -> RouterBean  group -> ModuleName
     * @return
     */
    Map<String, Class<? extends ARouterPath>> getGroupMap();
}
