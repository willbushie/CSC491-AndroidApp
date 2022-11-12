package com.example.photoshare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ActivitySettingsHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_home);
        initializeAllButtons();
    }

    public void initializeAllButtons() {
        /*
        Initialize activity changing buttons so they are all prepared.
         */

        // Initialize Profile Button
        Button open_profile_settings_button = (Button)findViewById(R.id.button_profile);
        open_profile_settings_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivitySettingsHome.this, ActivitySettingsProfile.class);
                startActivity(intent);
            }
        });

        // Initialize Notifications Button
        Button open_notifications_settings_button = (Button)findViewById(R.id.button_notifications);
        open_notifications_settings_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivitySettingsHome.this, ActivitySettingsNotifications.class);
                startActivity(intent);
            }
        });

        // Initialize Preferences Button
        Button open_prefernces_settings_button = (Button)findViewById(R.id.button_preferences);
        open_prefernces_settings_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivitySettingsHome.this, ActivitySettingsPreferences.class);
                startActivity(intent);
            }
        });
    }

    public void close_settings(View v) {
        /*
        This method will close the settings home page.
         */
        finish();
    }

}