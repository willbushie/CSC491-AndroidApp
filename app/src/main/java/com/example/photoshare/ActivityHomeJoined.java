package com.example.photoshare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

        // Initialze Leave Button
        // MODIFY THIS TO BRING UP A MENU FIRST TO CONFIRM, THEN CONDUCT THE ACTION
        Button button_leave = (Button)findViewById(R.id.button_leave);
        button_leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityHomeJoined.this, ActivityHomeUnjoined.class));
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
        Button button_share = (Button)findViewById(R.id.button_share);

    }

    public void leaveShareGroup(View v) {
        /*
        This will join a share group.
        This action is conducted when button_join is tapped.

        This button, since it will begin a new activity, needs to be initialized prior.
        Button functionality (this method) will still work onClick.
         */

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