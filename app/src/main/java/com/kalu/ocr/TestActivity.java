package com.kalu.ocr;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import exocr.exocrengine.EXOCRModel;
import lib.kalu.ocr.CaptureView;
import lib.kalu.ocr.OcrSurfaceView;

public class TestActivity extends Activity {

    public static final String FRONT = "front";
    public static final String RESULT = "result";
    public static final int RESULT_CODE = 1234;

    @Override
    public void finish() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final View root = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_test, null);
        final boolean isFront = getIntent().getBooleanExtra(FRONT, true);
        final CaptureView capture = root.findViewById(R.id.captuer_scan);
        capture.setFront(isFront);
        setContentView(root);

        final OcrSurfaceView surface = root.findViewById(R.id.surface);
        surface.setOnOcrChangeListener(exocrModel -> {

            runOnUiThread(() -> {

                Intent intent = new Intent();
                intent.putExtra(RESULT, exocrModel);
                setResult(RESULT_CODE, intent);
                onBackPressed();
            });
        });
    }
}
