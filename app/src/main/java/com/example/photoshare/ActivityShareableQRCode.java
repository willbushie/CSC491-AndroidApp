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

import java.io.IOException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class ActivityShareableQRCode extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shareable_qrcode);
        try {
            generateQRCode();
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ ATTEMPTED TO OPEN QR CODE");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generateQRCode() throws IOException {
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

        // Obtain join_url to generate QR code
        FileHandler handler = new FileHandler();
        String group_id = handler.read(ActivityShareableQRCode.this, "group_data.json", "id");
        String join_url = APIHandler.url_base + "/groups/join/" + group_id + "/";
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ SHARE URL: " + join_url);

        // setting this dimensions inside our qr code encoder to generate our qr code.
        //qrgEncoder = new QRGEncoder(dataEdt.getText().toString(), null, QRGContents.Type.TEXT, dimen);
        qrgEncoder = new QRGEncoder(join_url, null, QRGContents.Type.TEXT, dimen);
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
