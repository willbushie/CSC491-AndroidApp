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

                // BEFORE LAUNCHING ACTIVITY, ENSURE THE USER CAN BE CREATED FIRST

                startActivity(new Intent(ActivitySignup.this, ActivityHomeUnjoined.class));
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

    public boolean signup(View v) {
        /*
        This method is run when the signup button is pressed.
        The user needs to be created & logged into the home page
         */

        // get the entered username
        EditText nameInput = findViewById(R.id.editText_username_input);
        String username = nameInput.getText().toString();
        //Toast.makeText(ActivityLogin.this, username, Toast.LENGTH_SHORT).show();

        // get the entered email
        EditText emailInput = findViewById(R.id.editText_signup_email_input);
        String email = emailInput.getText().toString();
        //Toast.makeText(ActivityLogin.this, email, Toast.LENGTH_SHORT).show();

        // get the entered password 1
        EditText pass1Input = findViewById(R.id.editText_signup_password_input_1);
        String pass1 = emailInput.getText().toString();
        //Toast.makeText(ActivityLogin.this, pass1, Toast.LENGTH_SHORT).show();

        // get the entered password 2
        EditText pass2Input = findViewById(R.id.editText_signup_password_input_2);
        String pass2 = emailInput.getText().toString();
        //Toast.makeText(ActivityLogin.this, pass2, Toast.LENGTH_SHORT).show();

        // check for matching passwords, else notify user
        // signup user

        return false;
    }

}
