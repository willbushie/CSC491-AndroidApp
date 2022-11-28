package com.example.photoshare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class ActivitySignup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initializeAllButtons();
    }

    public void initializeAllButtons() {
        /*
        Initialize all buttons so they are all prepared once the activity is loaded.
        This is only for buttons that require OnClickListeners.
         */

        // Initialze Signup Button
        Button signup_button = (Button) findViewById(R.id.button_signup);
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText username = findViewById(R.id.editText_username_input);
                String username_str = username.getText().toString();
                EditText email = findViewById(R.id.editText_signup_email_input);
                String email_str = email.getText().toString();
                EditText firstname = findViewById(R.id.editText_firstname_input);
                String firstname_str = firstname.getText().toString();
                EditText lastname = findViewById(R.id.editText_lastname_input);
                String lastname_str = lastname.getText().toString();
                EditText password = findViewById(R.id.editText_signup_password_input_1);
                String password_str = password.getText().toString();
                EditText password1 = findViewById(R.id.editText_signup_password_input_2);
                String password1_str = password1.getText().toString();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // attempt to signup
                        try {
                            Boolean signup = APIHandler.register(ActivitySignup.this, firstname_str,lastname_str,email_str,username_str,password_str,password1_str);
                            if (signup) {
                                Boolean login = APIHandler.login(ActivitySignup.this,username_str,password_str);
                                startActivity(new Intent(ActivitySignup.this, ActivityHomeUnjoined.class));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });

        // Initialize Cancel Signup Button - might not be necessary to have this onclick override
        Button cancel_button = (Button) findViewById(R.id.button_cancel_singup);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //startActivity(new Intent(ActivitySignup.this, ActivityLogin.class));
            }
        });
    }

}
