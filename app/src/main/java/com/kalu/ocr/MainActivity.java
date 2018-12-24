package com.kalu.ocr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public final class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.idcard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), IdcardActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.bankcard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BankActivity.class);
                startActivity(intent);
            }
        });
    }
}
