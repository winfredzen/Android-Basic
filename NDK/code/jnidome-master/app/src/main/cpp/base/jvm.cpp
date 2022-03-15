//
// Created by jia76 on 2020/11/10.
//

#include "jvm.h"
static  JavaVM *gVm= nullptr;
#ifdef __cplusplus
extern "C"{
#endif

    void setJvm(JavaVM *jvm){
        gVm=jvm;
    }

    JavaVM * getJvm(){
        return gVm;
    }
#ifdef __cplusplus
};
#endif