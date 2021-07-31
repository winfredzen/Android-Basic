package com.example.expandablelistviewdemo.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baidu on 2018/6/10.
 */

public class ChapterLab {

    public static List<Chapter> generateDatas() {

        List<Chapter> chapters = new ArrayList<>();


        Chapter root1 = new Chapter(1, "Android");
        Chapter root2 = new Chapter(2, "IOS");
        Chapter root3 = new Chapter(3, "Unity 3D");
        Chapter root4 = new Chapter(4, "Cocos2d-x");

        root1.addChild(1, "PullToRefresh");
        root1.addChild(2, "Android 8.0通知栏解决方案");
        root1.addChild(4, "Android 与WebView的js交互");
        root1.addChild(8, "Android UiAutomator 2.0 入门实战");
        root1.addChild(10, "移动端音频视频入门");

        root2.addChild(11, "iOS开发之LeanCloud");
        root2.addChild(12, "iOS开发之传感器");
        root2.addChild(13, "iOS开发之网络协议");
        root2.addChild(14, "iOS之分享集成");
        root2.addChild(15, "iOS之FTP上传");


        root3.addChild(16, "Unity 3D 翻牌游戏开发");
        root3.addChild(17, "Unity 3D基础之变体Transform");
        root3.addChild(20, "带你开发类似Pokemon Go的AR游戏");
        root3.addChild(21, "Unity 3D游戏开发之脚本系统");
        root3.addChild(22, "Unity 3D地形编辑器");


        root4.addChild(25, "Cocos2d-x游戏之七夕女神抓捕计划");
        root4.addChild(26, "Cocos2d-x游戏开发初体验-C++篇");
        root4.addChild(27, "Cocos2d-x全民俄罗斯");
        root4.addChild(28, "Cocos2d-x坦克大战");
        root4.addChild(30, "新春特辑-Cocos抢红包");


        chapters.add(root1);
        chapters.add(root2);
        chapters.add(root3);
        chapters.add(root4);

        return chapters;
    }

}
