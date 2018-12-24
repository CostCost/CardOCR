package lib.kalu.ocr;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
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

    private int result = -1;
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
        getHolder().setKeepScreenOn(true);
        getHolder().setFormat(PixelFormat.TRANSPARENT);
        getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        final int height = MeasureSpec.getSize(heightMeasureSpec);
        final int width = 640 * height / 480;
        setMeasuredDimension(width, height);

//        final int canvasHeight = MeasureSpec.getSize(heightMeasureSpec);
//        final int canvasWidth = MeasureSpec.getSize(widthMeasureSpec);
//
//        if (canvasWidth < canvasHeight) {
//            final int layerWidth = (int) (canvasWidth * 0.85f);
//            final int layerHeight = (int) (layerWidth / 1.6f);
//            setMeasuredDimension(layerWidth, layerHeight);
//        } else {
//            final int layerHeight = (int) (canvasHeight * 0.85f);
//            final int layerWidth = (int) (layerHeight * 1.6f);
//            setMeasuredDimension(layerWidth, layerHeight);
//        }
    }

    /**********************************************************************************************/

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        final PackageManager pm = getContext().getPackageManager();
        final boolean hasACamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
        if (!hasACamera) {
            Log.e("kalu", "没有发现相机");
            return;
        }

        try {
            mCamera = Camera.open();

            Camera.Parameters parameters = mCamera.getParameters();//得到摄像头的参数
            parameters.setJpegQuality(100);//设置照片的质量
            parameters.setPreviewSize(640, 480);//设置预览尺寸
            parameters.setPictureSize(640, 480);//设置照片尺寸
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);// 连续对焦模式
            //Camera.Parameters.FOCUS_MODE_AUTO; //自动聚焦模式
            //Camera.Parameters.FOCUS_MODE_INFINITY;//无穷远
            //Camera.Parameters.FOCUS_MODE_MACRO;//微距
            //Camera.Parameters.FOCUS_MODE_FIXED;//固定焦距
            mCamera.setParameters(parameters);

            if (result != -1) {
                mCamera.setDisplayOrientation(result);
            }

            mCamera.setPreviewDisplay(getHolder());//通过SurfaceView显示取景画面
            mCamera.startPreview();//开始预览

            mCamera.setPreviewCallback(new Camera.PreviewCallback() {
                @Override
                public void onPreviewFrame(byte[] data, Camera camera) {
                    //Log.e("kalu", "onPreviewFrame");

                    boolean alive = mThread.isAlive();
                    if (!alive) return;
                    //Log.e("kalu11", "setPreviewCallback ==> ");

                    final Message obtain = Message.obtain();
                    obtain.obj = data;
                    obtain.arg1 = 640;
                    obtain.arg2 = 480;
                    mHandler.removeCallbacksAndMessages(null);
                    mHandler.sendMessage(obtain);
                }
            });

        } catch (Exception e) {
            Log.e("kalu", e.getMessage(), e);
            if (mCamera != null) {
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }
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
            final int width = msg.arg1;
            final int height = msg.arg2;
            final byte[] bytes = EXOCREngine.obtainByte();
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

    public final void calcuResult(final Activity activity) {

        Camera.CameraInfo info = new Camera.CameraInfo();
        //获取摄像头信息
        Camera.getCameraInfo(0, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        //获取摄像头当前的角度
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            // 前置摄像头
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else {
            // 后置摄像头
            result = (info.orientation - degrees + 360) % 360;
        }
    }

    /**********************************************************************************/

    private OnOcrChangeListener listener;

    public interface OnOcrChangeListener {
        void onSucc(final EXOCRModel exocrModel);
    }

    public void setOnOcrChangeListener(OnOcrChangeListener listener) {
        this.listener = listener;
    }
}
