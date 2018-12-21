package lib.kalu.ocr;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.util.List;

import exocr.exocrengine.EXOCREngine;
import exocr.exocrengine.EXOCRModel;

/**
 * description: 后置摄像头, ocr
 * create by kalu on 2018/12/7 21:45
 */
public class OcrSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private Camera mCamera;

    /**********************************************************************************************/

    public OcrSurfaceView(Context context) {
        this(context, null, 0);
    }

    public OcrSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OcrSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        boolean b = EXOCREngine.InitDict(getContext().getApplicationContext());
        if (!b) {
            Toast.makeText(getContext().getApplicationContext(), "初始化失败", Toast.LENGTH_SHORT).show();
            return;
        }

        getHolder().addCallback(this);
        getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    /**********************************************************************************************/

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        try {
            mCamera = Camera.open();
            mCamera.setPreviewDisplay(getHolder());

            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
//            List<String> flashModes = myParam.getSupportedFlashModes();
//            String flashMode = myParam.getFlashMode();
//            // Check if camera flash exists
//            if (flashModes == null) {
//                return;
//            }
//            if (!Camera.Parameters.FLASH_MODE_OFF.equals(flashMode)) {
//                // Turn off the flash
//                if (flashModes.contains(Camera.Parameters.FLASH_MODE_OFF)) {
//                    myParam.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
//                } else {
//                }
//            }

//            float percent = calcPreviewPercent();
//            List<Camera.Size> supportedPreviewSizes = myParam.getSupportedPreviewSizes();
//            previewSize = getPreviewMaxSize(supportedPreviewSizes, percent);
//            L.e(TAG, "预览尺寸w===" + previewSize.width + ",h==="
//                    + previewSize.height);
            // 获取摄像头支持的各种分辨率
            //List<Camera.Size> list = parameters.getSupportedPictureSizes();
            int width = 640;
            int height = 480;
//            for (Camera.Size size : list) {
//                Log.e("kalu", "list ==> width = " + size.width + ", height = " + size.height);
//                if (width == -1) {
//                    width = size.width;
//                    height = size.height;
//                } else if (size.width < width) {
//                    width = size.width;
//                    height = size.height;
//                }
//            }
            Log.e("kalu", "result ==> width = " + width + ", height = " + height);
//            parameters.setPictureSize(width, height);
            // parameters.setPreviewSize(640, 480);
            //parameters.setJpegQuality(50);
            mCamera.setParameters(parameters);

            Camera.Size previewSize = parameters.getPreviewSize();
            Log.e("kalu", "result ==> width = " + previewSize.width + ", height = " + previewSize.height);

            //mCamera.setPreviewCallback(this);
            mCamera.startPreview();
            mCamera.cancelAutoFocus();
            requestLayout();
            mCamera.setPreviewCallback(new Camera.PreviewCallback() {
                @Override
                public void onPreviewFrame(byte[] data, Camera camera) {
                    //Log.e("kalu", "onPreviewFrame");

                    boolean alive = mThread.isAlive();
                    if (!alive) return;
                    //Log.e("kalu11", "setPreviewCallback ==> ");

                    final Message obtain = Message.obtain();
                    obtain.obj = data;
                    mHandler.removeCallbacksAndMessages(null);
                    mHandler.sendMessage(obtain);
                }
            });

        } catch (Exception e) {
            Log.e("kalu", e.getMessage(), e);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }

        EXOCREngine.clearDict();
    }

    /**********************************************************************************************/

    @SuppressLint("HandlerLeak")
    private final Handler mMain = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {

            if (null == listener || null == msg.obj)
                return;
            listener.onSucc((EXOCRModel) msg.obj);
        }
    };

    private final HandlerThread mThread = new HandlerThread("OcrThread");

    {
        mThread.start();
    }

    private final Handler mHandler = new Handler(mThread.getLooper()) {
        @Override
        public void handleMessage(Message msg) {

            final byte[] data = (byte[]) msg.obj;
            final byte[] bytes = EXOCREngine.obtainByte();
            final int width = getWidth();
            final int height = getHeight();
            final int code = EXOCREngine.decode(data, width, height, width, 1, bytes, bytes.length);

            if (code > 0) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            final int[] rects = EXOCREngine.obtainRect();
                            final Bitmap imcard = EXOCREngine.decode(data, width, height, bytes, bytes.length, rects);
                            final EXOCRModel idcard = EXOCRModel.decode(bytes, code);
                            idcard.SetBitmap(getContext().getApplicationContext(), imcard);

                            final Message obtain = Message.obtain();
                            obtain.obj = idcard;
                            mMain.removeCallbacksAndMessages(null);
                            mMain.sendMessage(obtain);

                        } catch (Exception e) {
                            Log.e("kalu", e.getMessage(), e);
                        }
                    }
                }).start();

                mHandler.removeCallbacksAndMessages(null);
                //Log.e("kalu11", "handleMessage ==> size = " + data.length + ", code = " + code);
                mThread.quit();
            }
        }
    };

    /**********************************************************************************/

    private OnOcrChangeListener listener;

    public interface OnOcrChangeListener {
        void onSucc(final EXOCRModel exocrModel);
    }

    public void setOnOcrChangeListener(OnOcrChangeListener listener) {
        this.listener = listener;
    }
}
