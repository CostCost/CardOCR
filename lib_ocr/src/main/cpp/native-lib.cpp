#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_lib_kalu_ocr_OcrJni_stringFromJNI(JNIEnv *env, jclass type) {

    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT jobject JNICALL
Java_exocr_exocrengine_EXOCREngine_nativeRecoIDCardStillImage(JNIEnv *env, jclass type,
                                                              jobject bitmap, jint tryhard,
                                                              jint bwantimg, jbyteArray bresult_,
                                                              jint maxsize, jintArray rets_) {
    jbyte *bresult = env->GetByteArrayElements(bresult_, NULL);
    jint *rets = env->GetIntArrayElements(rets_, NULL);

    // TODO

    env->ReleaseByteArrayElements(bresult_, bresult, 0);
    env->ReleaseIntArrayElements(rets_, rets, 0);
}