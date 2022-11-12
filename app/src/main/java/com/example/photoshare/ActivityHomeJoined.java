package com.example.photoshare;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.content.ClipboardManager;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityHomeJoined extends AppCompatActivity {

    boolean paused;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_joined);
        paused = false;
        initializeAllButtons();
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
                            Toast.makeText(ActivityHomeJoined.this, "Showing QR Code", Toast.LENGTH_SHORT).show();
                        }
                        else if (item.getItemId() == R.id.menu_item_copyLink) {
                            Toast.makeText(ActivityHomeJoined.this, "Copied Link to Clipboard", Toast.LENGTH_SHORT).show();
                            // this does copy text to the clipboard
                            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("label", "This is a copy test");
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

        This button, since it will begin a new activity, needs to be initialized prior.
        Button functionality (this method) will still work onClick.
         */

        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityHomeJoined.this);
        builder.setMessage("Are you sure you want to leave the group?");
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

}