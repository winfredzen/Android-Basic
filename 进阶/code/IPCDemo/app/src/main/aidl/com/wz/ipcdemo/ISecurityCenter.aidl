package com.wz.ipcdemo;

interface ISecurityCenter {
    String encrypt(String content);
    String decrypt(String password);
}