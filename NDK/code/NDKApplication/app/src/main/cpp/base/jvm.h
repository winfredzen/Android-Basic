
#ifndef MY_APPLICATION_JVM_H
#define MY_APPLICATION_JVM_H
#include <jni.h>
#include <base.h>

#ifdef __cplusplus
extern "C" {
#endif
void setJvm(JavaVM *jvm);


JavaVM *getJvm();
#ifdef __cplusplus
}
#endif


#endif //MY_APPLICATION_JVM_H
