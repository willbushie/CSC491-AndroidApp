package com.example.photoshare;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;

import android.os.Bundle;


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
                startActivity(new Intent(ActivityHomeUnjoined.this, ActivityHomeJoined.class));
            }
        });

        // Initialize Timeline Button
        Button open_all_timeline_view = (Button)findViewById(R.id.button_timeline);
        open_all_timeline_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityHomeUnjoined.this, ActivityTimelineHome.class));
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