#include <string>
#include <jni.h>


#ifndef ANDROIDLAB_JNI_ANDROID_SAMPLE_CPP
#define ANDROIDLAB_JNI_ANDROID_SAMPLE_CPP


#ifdef __cplusplus
extern "C" {
#endif


JNIEXPORT jstring JNICALL Java_cn_blinkdagger_androidLab_utils_JniUtil_stringFromJNI
        (JNIEnv *env, jclass type){

    std::string hello ="HELLO JNI INVOKE";
    return  env->NewStringUTF(hello.c_str());
}


JNIEXPORT jint JNICALL Java_cn_blinkdagger_androidLab_utils_JniUtil_addFromJNI
        (JNIEnv *env, jclass type, jint a, jint b){
    int result = a + b;
    return result;
}


#ifdef __cplusplus
}
#endif
#endif