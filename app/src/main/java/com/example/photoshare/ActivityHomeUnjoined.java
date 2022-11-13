package com.example.photoshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.content.ClipboardManager;

import android.os.Bundle;
import android.widget.PopupMenu;
import android.widget.Toast;


public class ActivityHomeUnjoined extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_unjoined);
        initializeAllButtons();
    }

    public void initializeAllButtons() {
        /*
        Initialize all buttons so they are all prepared once the activity is loaded.
        This is only for buttons that require OnClickListeners.
         */

        // Initialze Start Button
        Button start_share_button = (Button) findViewById(R.id.button_start);
        start_share_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityHomeUnjoined.this, ActivityHomeJoined.class));
            }
        });

        // Initialize Join Button
        Button join_group_button = (Button) findViewById(R.id.button_join);
        join_group_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // popup menu to show joining options
                PopupMenu joinOptions = new PopupMenu(ActivityHomeUnjoined.this, join_group_button);
                joinOptions.getMenuInflater().inflate(R.menu.menu_join, joinOptions.getMenu());
                joinOptions.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.menu_item_scanQRcode) {
                            // open camera for qr scanning
                            Toast.makeText(ActivityHomeUnjoined.this, "Joining Group", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ActivityHomeUnjoined.this, ActivityHomeJoined.class));
                        }
                        else if (item.getItemId() == R.id.menu_item_enterLink) {
                            // this copies text to the clipboard
                            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = clipboard.getPrimaryClip();
                            if (clip != null && clip.getItemCount() > 0) {
                                ClipData.Item clipItem = clip.getItemAt(0);
                                String data = clipItem.getText().toString();
                                //Toast.makeText(ActivityHomeUnjoined.this, "CLIP: " + data, Toast.LENGTH_SHORT).show();


                                // If the data does not match a known base URL, open a text input for the user
                                if (!data.startsWith("https://www.photoshare.com/api/group/")) {
                                    Toast.makeText(ActivityHomeUnjoined.this, "Opening User Input", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    // otherwise join the group
                                    Toast.makeText(ActivityHomeUnjoined.this, "Joining Group", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        else if (item.getItemId() == R.id.menu_item_cancel) {
                            Toast.makeText(ActivityHomeUnjoined.this, "Canceled", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });
                joinOptions.show();
            }
        });

        // Initialize Settings Button
        Button open_settings_button = (Button)findViewById(R.id.button_settings);
        open_settings_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityHomeUnjoined.this, ActivitySettingsHome.class));
            }
        });
    }

    public void startShareGroup(View v) {
        /*
        This will begin a new share group.
        This action is conducted when button_start is tapped.

        This button, since it will begin a new activity, needs to be initialized prior.
        Button functionality (this method) will still work onClick.
         */

        // Notify the API that a new group needs to be created.
        // Receive the new group information

    }

    public void joinShareGroup(View v) {
        /*
        This will join a share group.
        This action is conducted when button_join is tapped.

        This button, since it will begin a new activity, needs to be initialized prior.
        Button functionality (this method) will still work onClick.
         */

        // Obtain a URL, or use the camera to scan a QR Code
        // Notify the API of the share group which needs to be joined
    }

    public void openSettings(View v) {
        // this method does nothing, but is called by the settings onClick method.
    }

}