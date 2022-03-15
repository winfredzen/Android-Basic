//
// Created by jia76 on 2020/11/9.
//

#include <jni.h>
#include <base.h>
#include <cstdio>
#include <jvm.h>
#include <pthread.h>
#include <unistd.h>


struct ThreadRunArgs{
    int id;
    int result;
};

void *printThreadHello(void *){
    LOGE("say %s","hello ");
//    显示退出函数
    pthread_exit(0);
    return nullptr;
}


void *printThreadArgs(void *arg){
    ThreadRunArgs *args= static_cast<ThreadRunArgs *>(arg);
    LOGE("Thread id is %d",args->id);
    LOGE("Thread id result %d",args->result);
    return nullptr;
}


extern "C"
JNIEXPORT jint JNICALL
Java_com_xingkong_myapplication_jni_JNIThread_createNativeThread(JNIEnv *env, jobject thiz) {
    pthread_t  handles;
   int result= pthread_create(&handles, nullptr,printThreadHello, nullptr);
    if(result==JNI_OK){
         LOGD("create thread success");
    } else{
         LOGD("create thread failed");
    }

    return result;
}


extern "C"
JNIEXPORT jint JNICALL
Java_com_xingkong_myapplication_jni_JNIThread_createNativeThreadWithArgs(JNIEnv *env,jobject thiz) {
    pthread_t  handles;
    ThreadRunArgs *args=new ThreadRunArgs;
    args->id=2;
    args->result=100;
    int result= pthread_create(&handles, nullptr,printThreadArgs, args);
    if(result==JNI_OK){
        LOGD("create thread success");
    } else{
        LOGD("create thread failed");
    }
    return result;
}

void *printThreadJoin(void *arg){
    ThreadRunArgs *args= static_cast<ThreadRunArgs *>(arg);
    struct timeval begin;
    gettimeofday(&begin, nullptr);
    LOGD("start time is %ld",begin.tv_sec);
    sleep(3);
    struct timeval end;
    gettimeofday(&end, nullptr);
    LOGD("end time is %ld",end.tv_sec);

    LOGD("Time used is %ld",end.tv_sec- begin.tv_sec);

    return reinterpret_cast<void *>(args->result);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_xingkong_myapplication_jni_JNIThread_joinNativeThread(JNIEnv *env, jobject thiz) {
 //拿到异步线程的结果
    pthread_t  handles;
    ThreadRunArgs *args=new ThreadRunArgs;
    args->id=2;
    args->result=100;
    int result= pthread_create(&handles, nullptr,printThreadJoin, args);

    void *ret= nullptr;

    pthread_join(handles,&ret);

    LOGD("result is %d", ret);
}