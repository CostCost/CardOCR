package lib.kalu.ocr;

import android.support.annotation.Keep;

/**
 * description: test
 * create by kalu on 2018/12/8 11:40
 */
public final class OcrJni {

    static {
        System.load("ocrjni");
    }

    @Keep
    private static native final String stringFromJNI();

}
