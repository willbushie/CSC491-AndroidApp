package com.example.photoshare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeAllButtons();
    }

    @Override
    public void onBackPressed() {
        /*
        This overrides original onBackPressed method.
        Ensures the user cannot return to the settings menu & must login.

        ~~~ TEMPORARY SOLUTION ~~~

         */
    }


    public void initializeAllButtons() {
        /*
        Initialize all buttons so they are all prepared once the activity is loaded.
        This is only for buttons that require OnClickListeners.
         */

        // Initialize Login Button
        Button login_button = (Button) findViewById(R.id.button_login);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // obtain the user inputs - check to make sure they are not blank
                EditText username = findViewById(R.id.editText_login_username_input);
                String username_str = username.getText().toString();
                EditText password = findViewById(R.id.editText_login_password_input);
                String password_str = password.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // attempt to login
                        try {
                            Boolean login_value = APIHandler.login(ActivityLogin.this, username_str, password_str);
                            if (login_value) {
                                startActivity(new Intent(ActivityLogin.this, ActivityHomeUnjoined.class));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        // Initialze Signup Button
        Button signup_button = (Button) findViewById(R.id.button_login_signup);
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityLogin.this, ActivitySignup.class));
            }
        });
    }
}
