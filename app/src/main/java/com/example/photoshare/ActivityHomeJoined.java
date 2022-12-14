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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


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
                            /*/ This alert dialog will show the qr code (dynamically changing)
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
                            FileHandler handler = new FileHandler();
                            String group_id = "";
                            group_id = handler.read(ActivityHomeJoined.this, "group_data.json", "id");
                            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ GROUP ID: " + group_id);
                            String base_url = APIHandler.url_base + "groups/" + group_id + "/";
                            ClipData clip = ClipData.newPlainText("Share Group URL", base_url);
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

        FileHandler handler = new FileHandler();
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityHomeJoined.this);
        builder.setMessage("Are you sure you want to leave the group? You will have to rejoin.");
        builder.setTitle("Are you sure?");

        // if confirmed
        builder.setPositiveButton("Yes, Leave", (DialogInterface.OnClickListener) (dialog, which) -> {
            JSONObject groupMember = null;
            try {
                groupMember = new JSONObject("{\"groupMember\":\"false\"}");
                handler.write(ActivityHomeJoined.this, "group_data.json", groupMember);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
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

    public void editShareGroup(View v) throws Exception {
        /*
        This will propose an edit to the members of the share group.
        This action is conducted when button_edit is tapped.
         */
        Button button_edit = (Button)findViewById(R.id.button_edit);

        // this section crashes the activity
        /*String url = "http://192.168.1.136:80/groups/?format=json";
        URL obj = new URL(url);
        StringBuffer response = new StringBuffer();

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        /*int responseCode = con.getResponseCode();
        if (responseCode != 200) {
            response.replace(0,response.length(),"Response Code: " + responseCode);
        }
        else {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        }

        //System.out.println(response);
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityHomeJoined.this);
        builder.setMessage(response);
        builder.setTitle("Response");

        // if canceled
        builder.setNegativeButton("Close", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });

        // Create  & show Alert dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();*/

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

}