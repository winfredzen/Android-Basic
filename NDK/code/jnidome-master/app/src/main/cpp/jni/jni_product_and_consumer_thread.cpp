//
// Created by jia76 on 2020/11/9.
//

#include <jni.h>
#include <base.h>
#include <cstdio>
#include <jvm.h>
#include <pthread.h>
#include <unistd.h>
#include <queue>


using namespace std;
std::queue<int> data;
pthread_mutex_t dataMutex;
pthread_cond_t dataCond;

pthread_t  ProductHandle;
pthread_t  ConsumerHandle;

void  *productThread(void *){
    while (data.size()<10){
        pthread_mutex_lock(&dataMutex);
        LOGD("生产物资");
        data.push(1);
        if(data.size() > 0){
            LOGD("等待消费");
            pthread_cond_signal(&dataCond);
        }
        pthread_mutex_unlock(&dataMutex);
        sleep(1);
    }

    pthread_exit(0);
}

void  *consumerThread(void *){

    while (1){
        pthread_mutex_lock(&dataMutex);
        if(data.size()>0){
            LOGI("消费物品");
            data.pop();
        } else{
            LOGI("等待生产");
            pthread_cond_wait(&dataCond,&dataMutex);
        }
        pthread_mutex_unlock(&dataMutex);
        sleep(1);
    }
    pthread_exit(0);

}



extern "C"
JNIEXPORT void JNICALL
Java_com_xingkong_myapplication_jni_JNIProductAndConsumerThread_startProductAndConsumerThread(
        JNIEnv *env, jobject thiz) {
    pthread_mutex_init(&dataMutex, nullptr);
    pthread_cond_init(&dataCond, nullptr);
    pthread_t productHandle;
    pthread_t consumerHandle;
    pthread_create(&ProductHandle, nullptr,productThread, nullptr);
    pthread_create(&ConsumerHandle, nullptr,consumerThread, nullptr);
}


extern "C"
JNIEXPORT void JNICALL
Java_com_xingkong_myapplication_jni_JNIProductAndConsumerThread_endProductAndConsumerThread(
        JNIEnv *env, jobject thiz) {



}

