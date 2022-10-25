package com.example.photoshare;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import android.os.Bundle;

public class activity_home_unjoined extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_unjoined);
    }

    public void openSettings(View v) {
        /*
        Open the settings page.
         */
        Button open_settings_button = (Button)findViewById(R.id.button_settings);
        open_settings_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_home_unjoined.this, activity_settings_home.class);
                startActivity(intent);
            }
        });
    }
}