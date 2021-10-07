package com.example.annotation_processor.config;

/**
 * create by wangzhen 2021/10/7
 * 配置信息（编译选项）
 *
 */
public interface ProcessorConfig  {
    String ROUTER_PACKAGE_NAME = "com.example.annotation.ARouter"; //注解的包名


    String MODULE_NAME = "moduleName";
    String APT_PACKAGE_NAME = "packageNameForAPT";
    String ACTIVITY_PACKAGE_NAME = "android.app.Activity";

    String ROUTER_API_PACKAGE_NAME = "com.example.router_api";

    String ROUTER_API_GROUP = ROUTER_API_PACKAGE_NAME + ".ARouterGroup";

    String ROUTER_API_PATH = ROUTER_API_PACKAGE_NAME + ".ARouterPath";

    //方法名称
    String PATH_METHOD_NAME = "getPathMap";

    String GROUP_METHOD_NAME = "getGroupMap";

    //变量名称
    String PATH_VAR = "pathMap";

    String GROUP_VAR = "groupMap";

    //文件名
    String PATH_FILE_NAME = "ARouter$$Path$$";

    String GROUP_FILE_NAME = "ARouter$$Group$$";

    String PARAMETER_PACKAGE = "com.example.annotation.Parameter";

    String ROUTER_API_PARAMETER_NAME = ROUTER_API_PACKAGE_NAME + ".ParameterLoad";

    String PARAMETER_METHOD_NAME = "getParameter";

    String PARAMETER_TARGET_NAME = "targetParameter";

    String STRING_PACKAGE_NAME = "java.lang.String";

    // OrderMainActivity$$Parameter
    String PARAMETER_FILE_NAME = "$$Parameter";

}
