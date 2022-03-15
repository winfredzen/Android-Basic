//
// Created by jia76 on 2020/11/9.
//

#include <jni.h>
#include <base.h>
#include <cstdio>
#include <jvm.h>
#include <pthread.h>
#include <unistd.h>

pthread_mutex_t mutex;
pthread_cond_t cond;

pthread_t waitHandle;
pthread_t notifyHandle;

int flag=0;

void  *waitThread(void *){
    LOGD("wait thread lock");
    pthread_mutex_lock(&mutex);
    while (flag==0){
        LOGI("waiting");
        pthread_cond_wait(&cond,&mutex);
    }
    LOGI("wait thread unlock");
    pthread_mutex_unlock(&mutex);
    pthread_exit(0);
}

void  *notifyThread(void *){
    LOGD("notify thread lock");
    pthread_mutex_lock(&mutex);
    flag=1;
    pthread_mutex_unlock(&mutex);
    pthread_cond_signal(&cond);
    LOGD("signal");
    LOGD("notify thread unlock");
    pthread_exit(0);

}

extern "C"
JNIEXPORT void JNICALL
Java_com_xingkong_myapplication_jni_JNIWaitNotify_waitNativeThread(JNIEnv *env, jobject thiz) {
    pthread_mutex_init(&mutex, nullptr);
    pthread_cond_init(&cond, nullptr);
    pthread_create(&waitHandle, nullptr,waitThread, nullptr);

}



extern "C"
JNIEXPORT void JNICALL
Java_com_xingkong_myapplication_jni_JNIWaitNotify_notifyNativeThread(JNIEnv *env, jobject thiz) {

    pthread_create(&notifyHandle, nullptr,notifyThread, nullptr);
}