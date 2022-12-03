package com.example.photoshare;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ActivitySettingsUpdateProfile extends AppCompatActivity {

    private final FileHandler handler = new FileHandler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_updateprofile);
        // Obtain original field values
        String username = handler.read(ActivitySettingsUpdateProfile.this, "user_data.json", "username");
        String email = handler.read(ActivitySettingsUpdateProfile.this, "user_data.json", "email");
        String fname = handler.read(ActivitySettingsUpdateProfile.this, "user_data.json", "first_name");
        String lname = handler.read(ActivitySettingsUpdateProfile.this, "user_data.json", "last_name");
        // modify the text of all edit text views to be current values
        EditText et_username = findViewById(R.id.editText_username_field);
        et_username.setText(username);
        EditText et_email = findViewById(R.id.editText_email_field);
        et_email.setText(email);
        EditText et_fname = findViewById(R.id.editText_firstname_field);
        et_fname.setText(fname);
        EditText et_lname = findViewById(R.id.editText_lastname_field);
        et_lname.setText(lname);
    }

    public void save_changes(View v) {
        /*
        This method will execute save changes. If the changes are accepted, the user is returned to the settings profile page
         */

        // future - ENSURE NO FIELD IS EMPTY - now error is returned
        EditText et_username = findViewById(R.id.editText_username_field);
        EditText et_email = findViewById(R.id.editText_email_field);
        EditText et_fname = findViewById(R.id.editText_firstname_field);
        EditText et_lname = findViewById(R.id.editText_lastname_field);

        new Thread(new Runnable() {
            @Override
            public void run() {
                // call API to conduct changes
                Boolean accepted = APIHandler.update_profile(
                        ActivitySettingsUpdateProfile.this,
                        et_username.getText().toString(),
                        et_email.getText().toString(),
                        et_fname.getText().toString(),
                        et_lname.getText().toString());

                // conduct the appropriate response (notify user/go to settings profile screen)
                if (accepted) {
                    finish();
                }
                // future - notify user that there was an error
            }
        }).start();
        // this is shown every time
        //Toast.makeText(ActivitySettingsUpdateProfile.this, "There was an error", Toast.LENGTH_SHORT).show();
    }

}
