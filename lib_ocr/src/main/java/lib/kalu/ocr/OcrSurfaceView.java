package lib.kalu.ocr;

import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

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
//            List<Camera.Size> supportedPictureSizes = myParam.getSupportedPictureSizes();
//            pictureSize = findSizeFromList(supportedPictureSizes, previewSize);
//            if (pictureSize == null) {
//                pictureSize = getPictureMaxSize(supportedPictureSizes, previewSize);
//            }
//            L.e(TAG, "照片尺寸w===" + pictureSize.width + ",h==="
//                    + pictureSize.height);
//            // 设置照片分辨率，注意要在摄像头支持的范围内选择
//            myParam.setPictureSize(pictureSize.width, pictureSize.height);
//            // 设置预浏尺寸，注意要在摄像头支持的范围内选择
//            myParam.setPreviewSize(previewSize.width, previewSize.height);
            parameters.setJpegQuality(70);
            mCamera.setParameters(parameters);

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

    private final HandlerThread mThread = new HandlerThread("OcrThread");

    {
        mThread.start();
    }

    private final Handler mHandler = new Handler(mThread.getLooper()) {
        @Override
        public void handleMessage(Message msg) {
            byte[] data = (byte[]) msg.obj;

            final byte[] obtain = EXOCREngine.obtain();
            final int width = getWidth();
            final int height = getHeight();
            final int code = EXOCREngine.decode(data, width, height, width, 1, obtain, obtain.length);

            if (code > 0) {

                final byte[] finalObtain = obtain;
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        EXOCRModel idcard = EXOCRModel.decode(finalObtain, code);
                        //int[] rects = new int[32];
                        //Bitmap imcard = EXOCREngine.decode(finalObtain, width, height, obtain, obtain.length, rects);
                        //idcard.SetBitmap(getContext().getApplicationContext(), imcard);

                        if (null != listener) {
                            listener.onSucc(idcard);
                        }

                    }
                }).start();

                mHandler.removeCallbacksAndMessages(null);
                Log.e("kalu11", "handleMessage ==> size = " + data.length + ", code = " + code);
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
