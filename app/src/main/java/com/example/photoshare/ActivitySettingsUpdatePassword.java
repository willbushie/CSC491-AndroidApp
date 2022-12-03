package com.example.photoshare;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class ActivitySettingsUpdatePassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_updatepassword);
    }

    public void change_password(View v) {
        /*
        This method will change a user's password & return them to the settings profile page
         */

        // future - ENSURE NO FIELD IS EMPTY - now error is returned
        EditText et_oldpass = findViewById(R.id.editText_oldpass_field);
        EditText et_newpass = findViewById(R.id.editText_pass1_field);
        EditText et_newpass2 = findViewById(R.id.editText_pass2_field);

        new Thread(new Runnable() {
            @Override
            public void run() {
                // call API to conduct update
                Boolean accepted = APIHandler.update_password(
                        ActivitySettingsUpdatePassword.this,
                        et_oldpass.getText().toString(),
                        et_newpass.getText().toString(),
                        et_newpass2.getText().toString());

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
