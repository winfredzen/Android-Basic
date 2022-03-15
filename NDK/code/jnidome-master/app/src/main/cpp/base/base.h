//
// Created by jia76 on 2020/11/9.
//
#include <android/log.h>
#include <jni.h>
#ifndef MY_APPLICATION_BASE_H
#define MY_APPLICATION_BASE_H
#define LOG_TAG "NativeMethod"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,LOG_TAG,__VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
#endif //ANDROIDCPPSOLIB_LOGUTIL_H