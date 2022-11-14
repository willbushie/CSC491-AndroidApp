package com.example.photoshare;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.content.ClipboardManager;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;


public class ActivityHomeJoined extends AppCompatActivity {

    boolean paused;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_joined);
        paused = false;
        initializeAllButtons();
    }

    @Override
    public void onBackPressed() {
        /*
        This overrides original onBackPressed method.
        Ensures the user confirms before leaving a group.
         */
        leaveShareGroup(null);
    }

    public void initializeAllButtons() {
        /*
        Initialize all buttons so they are all prepared once the activity is loaded.
        This is only for buttons that require OnClickListeners.
         */

        // Initialize Settings Button
        Button open_settings_button = (Button)findViewById(R.id.button_settings);
        open_settings_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityHomeJoined.this, ActivitySettingsHome.class));
            }
        });

        // Initialize Share Button
        Button share_group_button = (Button) findViewById(R.id.button_share);
        share_group_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // popup menu to show joining options
                PopupMenu shareOptions = new PopupMenu(ActivityHomeJoined.this, share_group_button);
                shareOptions.getMenuInflater().inflate(R.menu.menu_sharegroup, shareOptions.getMenu());
                shareOptions.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.menu_item_showQRCode) {
                            //Toast.makeText(ActivityHomeJoined.this, "Showing QR Code", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ActivityHomeJoined.this, ActivityShareableQRCode.class));
                            /*/ This alert dialog will show the qr code (dynamically changing) - DOES NOT CURRENTLY WORK
                            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityHomeJoined.this);
                            LayoutInflater factory = LayoutInflater.from(ActivityHomeJoined.this);
                            final View view = factory.inflate(R.layout.activity_shareable_qrcode, null);

                            builder.setView(view);
                            builder.setTitle("Scan QR Code to Join");
                            builder.setNeutralButton("Close", (DialogInterface.OnClickListener) (dialog, which) -> {
                                dialog.cancel();
                            });
                            AlertDialog alert = builder.create();
                            alert.show();*/
                        }
                        else if (item.getItemId() == R.id.menu_item_copyLink) {
                            Toast.makeText(ActivityHomeJoined.this, "Copied Link to Clipboard", Toast.LENGTH_SHORT).show();
                            // this does copy text to the clipboard
                            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("Share Group URL", "https://www.photoshare.com/api/group/788/join");
                            clipboard.setPrimaryClip(clip);
                        }
                        else if (item.getItemId() == R.id.menu_item_cancel) {
                            Toast.makeText(ActivityHomeJoined.this, "Canceled", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });
                shareOptions.show();
            }
        });

    }

    public void shareShareGroup(View v) {
        /*
        This will begin a new share group.
        This action is conducted when button_start is tapped.
         */
        Button button_share = (Button)findViewById(R.id.button_share);

    }

    public void leaveShareGroup(View v) {
        /*
        This will join a share group.
        This action is conducted when button_join is tapped.
         */

        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityHomeJoined.this);
        builder.setMessage("Are you sure you want to leave the group? You will have to rejoin.");
        builder.setTitle("Are you sure?");

        // if confirmed
        builder.setPositiveButton("Yes, Leave", (DialogInterface.OnClickListener) (dialog, which) -> {
            finish();
        });
        // if canceled
        builder.setNegativeButton("Cancel", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });

        // Create  & show Alert dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void editShareGroup(View v) {
        /*
        This will propose an edit to the members of the share group.
        This action is conducted when button_edit is tapped.
         */
        Button button_edit = (Button)findViewById(R.id.button_edit);

    }

    public void pauseShareGroup(View v) {
        /*
        This will pause a members sharing to the share group. (this will be a toggle)
        This action is conducted when button_pause is tapped.
         */
        Button button_pause = (Button)findViewById(R.id.button_pause);
        if (!paused) {
            paused = true;
            button_pause.setText(R.string.button_text_resume);
            button_pause.setBackgroundColor(getColor(R.color.easy_red));
            button_pause.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_baseline_play_arrow_24, 0, 0);
        }
        else if (paused) {
            paused = false;
            button_pause.setText(R.string.button_text_pause);
            button_pause.setBackgroundColor(getColor(R.color.easy_green));
            button_pause.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_baseline_pause_24, 0, 0);
        }
    }

    public void generateQRCode() {
        /*
        This method will set the QR code in this view to the share URL.
        This method does not work inside of this java file - for now a new activity is used for scanning qr codes
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