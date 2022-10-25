package com.example.photoshare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityHomeJoined extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_joined);
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
                startActivity(new Intent(ActivityHomeJoined.this, ActivityTimelineHome.class));
            }
        });

        // Initialize Settings Button
        Button open_settings_button = (Button)findViewById(R.id.button_settings);
        open_settings_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityHomeJoined.this, ActivitySettingsHome.class));
            }
        });
    }

    public void shareShareGroup(View v) {
        /*
        This will begin a new share group.
        This action is conducted when button_start is tapped.
         */
        Button button_start = (Button)findViewById(R.id.button_start);
    }

    public void leaveShareGroup(View v) {
        /*
        This will join a share group.
        This action is conducted when button_join is tapped.
         */
        Button button_join = (Button)findViewById(R.id.button_join);
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
    }

}