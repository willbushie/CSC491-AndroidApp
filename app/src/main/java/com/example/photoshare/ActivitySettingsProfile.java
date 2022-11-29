package com.example.photoshare;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

public class ActivitySettingsProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_profile);
        initializeAllButtons();
    }

    public void initializeAllButtons() {
        /*
        Initialize all buttons so they are all prepared once the activity is loaded.
        This is only for buttons that require OnClickListeners.
         */

        // Initialze Logout Button
        Button logout_button = (Button) findViewById(R.id.button_logout);
        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySettingsProfile.this);
                builder.setMessage("Are you sure you want to logout?");
                builder.setTitle("Are you sure?");

                // if confirmed
                builder.setPositiveButton("Yes, Logout", (DialogInterface.OnClickListener) (dialog, which) -> {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (APIHandler.logout(ActivitySettingsProfile.this)) {
                                    FileHandler handler = new FileHandler();
                                    JSONObject logged_in = new JSONObject("{\"logged_in\":\"false\"}");
                                    handler.write(ActivitySettingsProfile.this, "app_data.json", logged_in);
                                    startActivity(new Intent(ActivitySettingsProfile.this, ActivityLogin.class));
                                    finish();
                                }
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                });
                // if canceled
                builder.setNegativeButton("Cancel", (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.cancel();
                });

                // Create  & show Alert dialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
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

}