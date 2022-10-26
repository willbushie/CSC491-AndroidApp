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
         */
        Button button_start = (Button)findViewById(R.id.button_start);
    }

    public void joinShareGroup(View v) {
        /*
        This will join a share group.
        This action is conducted when button_join is tapped.
         */
        Button button_join = (Button)findViewById(R.id.button_join);
    }

}