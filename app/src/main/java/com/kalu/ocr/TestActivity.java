package com.kalu.ocr;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

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
        // step1
        final CaptureView capture = root.findViewById(R.id.captuer_scan);
        capture.setFront(isFront);
        // step2
        final OcrSurfaceView surface = root.findViewById(R.id.surface);
        setContentView(root);

        final TextView text = findViewById(R.id.text);
        surface.setOnOcrChangeListener(exocrModel -> {
                if (exocrModel.isOk(isFront)) {
                    Intent intent = new Intent();
                    intent.putExtra(RESULT, exocrModel);
                    setResult(RESULT_CODE, intent);
                    onBackPressed();
                } else {
                    text.setText("请确认正反面是否正确, 退出重试");
                }
        });
    }
}
