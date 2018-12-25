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

extern "C"
JNIEXPORT jint JNICALL
Java_exocr_exocrengine_EXOCREngine_nativeGetVersion(JNIEnv *env, jclass type,
                                                    jbyteArray exversion_) {
    jbyte *exversion = env->GetByteArrayElements(exversion_, NULL);

    // TODO

    env->ReleaseByteArrayElements(exversion_, exversion, 0);
}

extern "C"
JNIEXPORT jint JNICALL
Java_exocr_exocrengine_EXOCREngine_nativeInit(JNIEnv *env, jclass type, jbyteArray dbpath_) {
    jbyte *dbpath = env->GetByteArrayElements(dbpath_, NULL);

    // TODO

    env->ReleaseByteArrayElements(dbpath_, dbpath, 0);
}

extern "C"
JNIEXPORT jint JNICALL
Java_exocr_exocrengine_EXOCREngine_nativeDone(JNIEnv *env, jclass type) {

    // TODO

}

extern "C"
JNIEXPORT jint JNICALL
Java_exocr_exocrengine_EXOCREngine_nativeCheckSignature(JNIEnv *env, jclass type, jobject context) {

    // TODO

}

extern "C"
JNIEXPORT jint JNICALL
Java_exocr_exocrengine_EXOCREngine_nativeRecoIDCardBitmap(JNIEnv *env, jclass type, jobject bitmap,
                                                          jbyteArray bresult_, jint maxsize) {
    jbyte *bresult = env->GetByteArrayElements(bresult_, NULL);

    // TODO

    env->ReleaseByteArrayElements(bresult_, bresult, 0);
}

extern "C"
JNIEXPORT jobject JNICALL
Java_exocr_exocrengine_EXOCREngine_nativeRecoIDCardStillImageV2(JNIEnv *env, jclass type,
                                                                jobject bitmap, jint tryhard,
                                                                jint bwantimg, jbyteArray bresult_,
                                                                jint maxsize, jintArray rects_,
                                                                jintArray rets_) {
    jbyte *bresult = env->GetByteArrayElements(bresult_, NULL);
    jint *rects = env->GetIntArrayElements(rects_, NULL);
    jint *rets = env->GetIntArrayElements(rets_, NULL);

    // TODO

    env->ReleaseByteArrayElements(bresult_, bresult, 0);
    env->ReleaseIntArrayElements(rects_, rects, 0);
    env->ReleaseIntArrayElements(rets_, rets, 0);
}

extern "C"
JNIEXPORT jint JNICALL
Java_exocr_exocrengine_EXOCREngine_nativeRecoIDCardRawdat(JNIEnv *env, jclass type,
                                                          jbyteArray imgdata_, jint width,
                                                          jint height, jint pitch, jint imgfmt,
                                                          jbyteArray bresult_, jint maxsize) {
    jbyte *imgdata = env->GetByteArrayElements(imgdata_, NULL);
    jbyte *bresult = env->GetByteArrayElements(bresult_, NULL);

    // TODO

    env->ReleaseByteArrayElements(imgdata_, imgdata, 0);
    env->ReleaseByteArrayElements(bresult_, bresult, 0);
}

extern "C"
JNIEXPORT jobject JNICALL
Java_exocr_exocrengine_EXOCREngine_nativeGetIDCardStdImg(JNIEnv *env, jclass type, jbyteArray NV21_,
                                                         jint width, jint height,
                                                         jbyteArray bresult_, jint maxsize,
                                                         jintArray rects_) {
    jbyte *NV21 = env->GetByteArrayElements(NV21_, NULL);
    jbyte *bresult = env->GetByteArrayElements(bresult_, NULL);
    jint *rects = env->GetIntArrayElements(rects_, NULL);

    // TODO

    env->ReleaseByteArrayElements(NV21_, NV21, 0);
    env->ReleaseByteArrayElements(bresult_, bresult, 0);
    env->ReleaseIntArrayElements(rects_, rects, 0);
}

extern "C"
JNIEXPORT jint JNICALL
Java_exocr_exocrengine_EXOCREngine_nativeRecoVECardBitmap(JNIEnv *env, jclass type, jobject bitmap,
                                                          jbyteArray bresult_, jint maxsize) {
    jbyte *bresult = env->GetByteArrayElements(bresult_, NULL);

    // TODO

    env->ReleaseByteArrayElements(bresult_, bresult, 0);
}

extern "C"
JNIEXPORT jobject JNICALL
Java_exocr_exocrengine_EXOCREngine_nativeRecoVECardStillImage(JNIEnv *env, jclass type,
                                                              jobject bitmap, jint tryhard,
                                                              jint bwantimg, jbyteArray bresult_,
                                                              jint maxsize, jintArray rets_) {
    jbyte *bresult = env->GetByteArrayElements(bresult_, NULL);
    jint *rets = env->GetIntArrayElements(rets_, NULL);

    // TODO

    env->ReleaseByteArrayElements(bresult_, bresult, 0);
    env->ReleaseIntArrayElements(rets_, rets, 0);
}

extern "C"
JNIEXPORT jobject JNICALL
Java_exocr_exocrengine_EXOCREngine_nativeRecoVECardStillImageV2(JNIEnv *env, jclass type,
                                                                jobject bitmap, jint tryhard,
                                                                jint bwantimg, jbyteArray bresult_,
                                                                jint maxsize, jintArray rects_,
                                                                jintArray rets_) {
    jbyte *bresult = env->GetByteArrayElements(bresult_, NULL);
    jint *rects = env->GetIntArrayElements(rects_, NULL);
    jint *rets = env->GetIntArrayElements(rets_, NULL);

    // TODO

    env->ReleaseByteArrayElements(bresult_, bresult, 0);
    env->ReleaseIntArrayElements(rects_, rects, 0);
    env->ReleaseIntArrayElements(rets_, rets, 0);
}

extern "C"
JNIEXPORT jint JNICALL
Java_exocr_exocrengine_EXOCREngine_nativeRecoVECardRawdat(JNIEnv *env, jclass type,
                                                          jbyteArray imgdata_, jint width,
                                                          jint height, jint pitch, jint imgfmt,
                                                          jbyteArray bresult_, jint maxsize) {
    jbyte *imgdata = env->GetByteArrayElements(imgdata_, NULL);
    jbyte *bresult = env->GetByteArrayElements(bresult_, NULL);

    // TODO

    env->ReleaseByteArrayElements(imgdata_, imgdata, 0);
    env->ReleaseByteArrayElements(bresult_, bresult, 0);
}

extern "C"
JNIEXPORT jobject JNICALL
Java_exocr_exocrengine_EXOCREngine_nativeGetVECardStdImg(JNIEnv *env, jclass type, jbyteArray NV21_,
                                                         jint width, jint height,
                                                         jbyteArray bresult_, jint maxsize,
                                                         jintArray rects_) {
    jbyte *NV21 = env->GetByteArrayElements(NV21_, NULL);
    jbyte *bresult = env->GetByteArrayElements(bresult_, NULL);
    jint *rects = env->GetIntArrayElements(rects_, NULL);

    // TODO

    env->ReleaseByteArrayElements(NV21_, NV21, 0);
    env->ReleaseByteArrayElements(bresult_, bresult, 0);
    env->ReleaseIntArrayElements(rects_, rects, 0);
}

extern "C"
JNIEXPORT jobject JNICALL
Java_exocr_exocrengine_EXOCREngine_nativeRecoVE2CardNV21(JNIEnv *env, jclass type,
                                                         jbyteArray imgnv21_, jint width,
                                                         jint height, jint bwantimg,
                                                         jbyteArray bresult_, jint maxsize,
                                                         jintArray rects_, jintArray rets_) {
    jbyte *imgnv21 = env->GetByteArrayElements(imgnv21_, NULL);
    jbyte *bresult = env->GetByteArrayElements(bresult_, NULL);
    jint *rects = env->GetIntArrayElements(rects_, NULL);
    jint *rets = env->GetIntArrayElements(rets_, NULL);

    // TODO

    env->ReleaseByteArrayElements(imgnv21_, imgnv21, 0);
    env->ReleaseByteArrayElements(bresult_, bresult, 0);
    env->ReleaseIntArrayElements(rects_, rects, 0);
    env->ReleaseIntArrayElements(rets_, rets, 0);
}

extern "C"
JNIEXPORT jobject JNICALL
Java_exocr_exocrengine_EXOCREngine_nativeRecoVE2CardStillImage(JNIEnv *env, jclass type,
                                                               jobject bitmap, jint tryhard,
                                                               jint bwantimg, jbyteArray bresult_,
                                                               jint maxsize, jintArray rects_,
                                                               jintArray rets_) {
    jbyte *bresult = env->GetByteArrayElements(bresult_, NULL);
    jint *rects = env->GetIntArrayElements(rects_, NULL);
    jint *rets = env->GetIntArrayElements(rets_, NULL);

    // TODO

    env->ReleaseByteArrayElements(bresult_, bresult, 0);
    env->ReleaseIntArrayElements(rects_, rects, 0);
    env->ReleaseIntArrayElements(rets_, rets, 0);
}

extern "C"
JNIEXPORT jint JNICALL
Java_exocr_exocrengine_EXOCREngine_nativeRecoScanLineRawdata(JNIEnv *env, jclass type,
                                                             jbyteArray imgdata_, jint width,
                                                             jint height, jint imgfmt, jint lft,
                                                             jint rgt, jint top, jint btm,
                                                             jint nRecoType, jbyteArray bresult_,
                                                             jint maxsize) {
    jbyte *imgdata = env->GetByteArrayElements(imgdata_, NULL);
    jbyte *bresult = env->GetByteArrayElements(bresult_, NULL);

    // TODO

    env->ReleaseByteArrayElements(imgdata_, imgdata, 0);
    env->ReleaseByteArrayElements(bresult_, bresult, 0);
}

extern "C"
JNIEXPORT jobject JNICALL
Java_exocr_exocrengine_EXOCREngine_nativeRecoDRCardNV21(JNIEnv *env, jclass type,
                                                        jbyteArray imgnv21_, jint width,
                                                        jint height, jint bwantimg,
                                                        jbyteArray bresult_, jint maxsize,
                                                        jintArray rects_, jintArray rets_) {
    jbyte *imgnv21 = env->GetByteArrayElements(imgnv21_, NULL);
    jbyte *bresult = env->GetByteArrayElements(bresult_, NULL);
    jint *rects = env->GetIntArrayElements(rects_, NULL);
    jint *rets = env->GetIntArrayElements(rets_, NULL);

    // TODO

    env->ReleaseByteArrayElements(imgnv21_, imgnv21, 0);
    env->ReleaseByteArrayElements(bresult_, bresult, 0);
    env->ReleaseIntArrayElements(rects_, rects, 0);
    env->ReleaseIntArrayElements(rets_, rets, 0);
}

extern "C"
JNIEXPORT jobject JNICALL
Java_exocr_exocrengine_EXOCREngine_nativeRecoDRCardStillImage(JNIEnv *env, jclass type,
                                                              jobject bitmap, jint tryhard,
                                                              jint bwantimg, jbyteArray bresult_,
                                                              jint maxsize, jintArray rects_,
                                                              jintArray rets_) {
    jbyte *bresult = env->GetByteArrayElements(bresult_, NULL);
    jint *rects = env->GetIntArrayElements(rects_, NULL);
    jint *rets = env->GetIntArrayElements(rets_, NULL);

    // TODO

    env->ReleaseByteArrayElements(bresult_, bresult, 0);
    env->ReleaseIntArrayElements(rects_, rects, 0);
    env->ReleaseIntArrayElements(rets_, rets, 0);
}