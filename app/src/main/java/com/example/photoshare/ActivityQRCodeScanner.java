package com.example.photoshare;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

import java.lang.reflect.Array;


public class ActivityQRCodeScanner extends AppCompatActivity {

    private CodeScanner mCodeScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_scanner);
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        setupPermission();
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // this obtains the qr code once its read - check for correctness & move to join page
                        Toast.makeText(ActivityQRCodeScanner.this, result.getText(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ActivityQRCodeScanner.this, ActivityHomeJoined.class));
                        finish();
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    public void setupPermission() {
        int permission = ContextCompat.checkSelfPermission(ActivityQRCodeScanner.this, Manifest.permission.CAMERA);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest();
        }
    }

    public void makeRequest() {
        ActivityCompat.requestPermissions(ActivityQRCodeScanner.this, new String[]{Manifest.permission.CAMERA}, 1888);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] results) {
        super.onRequestPermissionsResult(requestCode, permissions, results);
        if (requestCode == 1888) {
            if (results.length == 0 || results[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(ActivityQRCodeScanner.this, "Camera access is required to use this feature.", Toast.LENGTH_SHORT).show();
            }
            else {
                // successful
            }
        }
    }

}
