package com.example.photoshare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

                // BEFORE LAUNCHING THE ACTIVITY, ENSURE USER CAN LOGIN FIRST

                startActivity(new Intent(ActivityLogin.this, ActivityHomeUnjoined.class));
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

    public boolean login(View v) {
        /*
        This method is run when the login button is pressed.
        The user needs to be authenticated.

        ~~~ MORE SECURE MEASURES NEED TO BE TAKEN HERE ~~~

         */

        // obtain the user input & check with API for confirmation
        // if confirmed, login, else notify user of problem

        // get the entered email
        EditText emailInput = findViewById(R.id.editText_login_email_input);
        String email = emailInput.getText().toString();
        //Toast.makeText(ActivityLogin.this, email, Toast.LENGTH_SHORT).show();
        // get the entered password
        EditText passInput = findViewById(R.id.editText_login_password_input);
        String password = passInput.getText().toString();
        //Toast.makeText(ActivityLogin.this, password, Toast.LENGTH_SHORT).show();

        return false;

    }

}
