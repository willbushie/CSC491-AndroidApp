package com.example.photoshare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ActivitySettingsProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_profile);
    }



    public void updateEmail(View v) {
        /*
        Update a user's email.
         */
        Button button_update_email = (Button)findViewById(R.id.button_update_email);
    }

    public void updatePassword(View v) {
        /*
        Update a user's password.
         */
        Button button_update_password = (Button)findViewById(R.id.button_update_password);
    }

    // need to add logout functionality

}