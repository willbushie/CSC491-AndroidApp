package com.example.photoshare;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.zxing.WriterException;
import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class ActivityShareableQRCode extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shareable_qrcode);
        generateQRCode();
    }

    public void generateQRCode() {
        /*
        This method will set the QR code in this view to the share URL
         */

        // variable initialization
        ImageView qrCodeIV = findViewById(R.id.idIVQrcode);
        Bitmap bitmap;
        QRGEncoder qrgEncoder;

        // below line is for getting the windowmanager service.
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);

        // initializing a variable for default display.
        Display display = manager.getDefaultDisplay();

        // creating a variable for point which is to be displayed in QR Code.
        Point point = new Point();
        display.getSize(point);

        // getting width and height of a point
        int width = point.x;
        int height = point.y;

        // generating dimension from width and height.
        int dimen = width < height ? width : height;
        dimen = dimen * 3 / 4;

        // setting this dimensions inside our qr code encoder to generate our qr code.
        //qrgEncoder = new QRGEncoder(dataEdt.getText().toString(), null, QRGContents.Type.TEXT, dimen);
        qrgEncoder = new QRGEncoder("https://photoshare.com/api/group/join/788", null, QRGContents.Type.TEXT, dimen);
        try {
            // getting our qrcode in the form of bitmap.
            bitmap = qrgEncoder.encodeAsBitmap();
            // the bitmap is set inside our image view using .setimagebitmap method.
            qrCodeIV.setImageBitmap(bitmap);
        } catch (WriterException e) {
            // this method is called for exception handling.
            Log.e("Tag", e.toString());
        }
    }
}
