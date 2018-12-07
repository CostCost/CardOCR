package exocr.exocrengine;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Keep;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import lib.kalu.ocr.R;

/**
 * description: jni
 * create by kalu on 2018/11/20 10:24
 */
public final class EXOCREngine {

    private EXOCREngine() {
    }

    static {
        System.loadLibrary("exocrenginec");
    }

    private static final byte[] bResultBuf = new byte[4096];

    public static final byte[] obtain() {
        return bResultBuf;
    }

    public static final void clearDict() {

        int code = EXOCREngine.nativeDone();
        Log.e("kalu", "clearDict ==> code = " + code);
    }

    private static final boolean checkSign(final Context context) {
        int code = EXOCREngine.nativeCheckSignature(context);
        Log.e("kalu", "checkSign ==> code = " + code);
        return code == 1;
    }

    private static final boolean checkDict(final String path) {

        final byte[] bytes = path.getBytes();
        final int code = EXOCREngine.nativeInit(bytes);
        Log.e("kalu", "checkDict ==> code = " + code);
        return code >= 0;
    }

    private static final boolean checkFile(final Context context, final String pathname) {

        final File file = new File(pathname);
        if (!file.exists() || file.isDirectory()) {

            file.delete();

            try {
                file.createNewFile();

                int byteread;
                final byte[] buffer = new byte[1024];
                final InputStream is = context.getResources().openRawResource(R.raw.zocr0);
                final OutputStream fs = new FileOutputStream(file);// to为要写入sdcard中的文件名称

                while ((byteread = is.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteread);
                }
                is.close();
                fs.close();
                return true;
            } catch (Exception e) {
                Log.e("kalu", "checkFile ==> message = " + e.getMessage(), e);
                return false;
            }
        } else {
            return true;
        }
    }

    public static final boolean InitDict(final Context activity) {

        final String name = "/zocr0.lib";
        final String path = activity.getCacheDir().getAbsolutePath();
        final String pathname = path + name;

        // step1: 检测字典是否存在
        boolean okFile = checkFile(activity, pathname);
        if (!okFile) {

            clearDict();
            return false;
        }

        // step2: 检测字典是否正确
        boolean okDict = checkDict(path);
        if (!okDict) {

            clearDict();

            return false;
        }

        return true;
    }

    public static int decode(byte[] imgdata, int width, int height, int pitch, int imgfmt, byte[] bresult, int maxsize) {
        return nativeRecoIDCardRawdat(imgdata, width, height, pitch, imgfmt, bresult, maxsize);
    }

    public static Bitmap decode(byte[] NV21, int width, int height, byte[] bresult, int maxsize, int[] rects) {
        return nativeGetIDCardStdImg(NV21, width, height, bresult, maxsize, rects);
    }

    /**********************************************************************************************/

    @Keep
    private static native int nativeGetVersion(byte[] exversion);

    @Keep
    private static native int nativeInit(byte[] dbpath);

    @Keep
    private static native int nativeDone();

    @Keep
    private static native int nativeCheckSignature(Context context);

    @Keep
    private static native int nativeRecoIDCardBitmap(Bitmap bitmap, byte[] bresult, int maxsize);

    @Keep
    private static native Bitmap nativeRecoIDCardStillImage(Bitmap bitmap, int tryhard, int bwantimg, byte[] bresult, int maxsize, int[] rets);

    @Keep
    private static native Bitmap nativeRecoIDCardStillImageV2(Bitmap bitmap, int tryhard, int bwantimg, byte[] bresult, int maxsize, int[] rects, int[] rets);

    @Keep
    private static native int nativeRecoIDCardRawdat(byte[] imgdata, int width, int height, int pitch, int imgfmt, byte[] bresult, int maxsize);

    @Keep
    private static native Bitmap nativeGetIDCardStdImg(byte[] NV21, int width, int height, byte[] bresult, int maxsize, int[] rects);

    @Keep
    private static native int nativeRecoVECardBitmap(Bitmap bitmap, byte[] bresult, int maxsize);

    @Keep
    private static native Bitmap nativeRecoVECardStillImage(Bitmap bitmap, int tryhard, int bwantimg, byte[] bresult, int maxsize, int[] rets);

    @Keep
    private static native Bitmap nativeRecoVECardStillImageV2(Bitmap bitmap, int tryhard, int bwantimg, byte[] bresult, int maxsize, int[] rects, int[] rets);

    @Keep
    private static native int nativeRecoVECardRawdat(byte[] imgdata, int width, int height, int pitch, int imgfmt, byte[] bresult, int maxsize);

    @Keep
    private static native Bitmap nativeGetVECardStdImg(byte[] NV21, int width, int height, byte[] bresult, int maxsize, int[] rects);

    @Keep
    private static native Bitmap nativeRecoVE2CardNV21(byte[] imgnv21, int width, int height, int bwantimg, byte[] bresult, int maxsize, int[] rects, int[] rets);

    @Keep
    private static native Bitmap nativeRecoVE2CardStillImage(Bitmap bitmap, int tryhard, int bwantimg, byte[] bresult, int maxsize, int[] rects, int[] rets);

    @Keep
    private static native int nativeRecoScanLineRawdata(byte[] imgdata, int width, int height, int imgfmt, int lft, int rgt, int top, int btm, int nRecoType, byte[] bresult, int maxsize);

    @Keep
    private static native Bitmap nativeRecoDRCardNV21(byte[] imgnv21, int width, int height, int bwantimg, byte[] bresult, int maxsize, int[] rects, int[] rets);

    @Keep
    private static native Bitmap nativeRecoDRCardStillImage(Bitmap bitmap, int tryhard, int bwantimg, byte[] bresult, int maxsize, int[] rects, int[] rets);
}