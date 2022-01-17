package com.getui.demo.net.response;

/**
 * Time：2020-03-10 on 11:57.
 * Decription:.
 * Author:jimlee.
 */
public class NotificationResp {
    public String result;//ok 鉴权成功，见详情
    public String taskid;//		任务编号
    public String desc; //String	错误信息描述

    /*推送结果
    successed_offline 离线下发
    successed_online 在线下发
    successed_ignore 非活跃用户不下发*/
    public String status;
}
