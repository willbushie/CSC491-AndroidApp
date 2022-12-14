package com.example.photoshare;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.InputType;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.content.ClipboardManager;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;


public class ActivityHomeUnjoined extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_unjoined);
        try {
            if (this.joined()) {
                startActivity(new Intent(ActivityHomeUnjoined.this, ActivityHomeJoined.class));
            }
            else {
                initializeAllButtons();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        /*
        This overrides original onBackPressed method.
        Ensures the user won't be sent to login screen & stays on home screen

        ~~~ TEMPORARY SOLUTION ~~~
            Needs to be more intuitive than this
         */
    }

    public Boolean joined() throws IOException {
        /*
        This method will bypass the login screen, upon confirming the user is logged in.
         */
        FileHandler handler = new FileHandler();
        String logged_in = handler.read(ActivityHomeUnjoined.this, "group_data.json", "groupMember");
        return logged_in.equals("true");
    }

    public void initializeAllButtons() {
        /*
        Initialize all buttons so they are all prepared once the activity is loaded.
        This is only for buttons that require OnClickListeners.
         */

        // Initialze Start Button
        Button start_share_button = (Button) findViewById(R.id.button_start);
        start_share_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // AlertDialog for user to enter group name
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityHomeUnjoined.this);
                builder.setTitle("Enter Group Name");
                EditText nameInput = new EditText(ActivityHomeUnjoined.this);
                nameInput.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(nameInput);

                // submit name for group creation
                builder.setPositiveButton("Create", (DialogInterface.OnClickListener) (dialog, which) -> {
                    String inputData = nameInput.getText().toString();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // create group
                            try {
                                if (APIHandler.groupCreate(ActivityHomeUnjoined.this, inputData)) {
                                    startActivity(new Intent(ActivityHomeUnjoined.this, ActivityHomeJoined.class));
                                }
                                else {
                                    // let user know there was an error creating the group
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                    Toast.makeText(ActivityHomeUnjoined.this, "Creating Group", Toast.LENGTH_SHORT).show();
                });
                // cancel input
                builder.setNegativeButton("Cancel", (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.cancel();
                });

                // Create  & show Alert dialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                //startActivity(new Intent(ActivityHomeUnjoined.this, ActivityHomeJoined.class));
            }
        });

        // Initialize Join Button
        Button join_group_button = (Button) findViewById(R.id.button_join);
        join_group_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // popup menu to show joining options
                PopupMenu joinOptions = new PopupMenu(ActivityHomeUnjoined.this, join_group_button);
                joinOptions.getMenuInflater().inflate(R.menu.menu_join, joinOptions.getMenu());
                joinOptions.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.menu_item_scanQRcode) {
                            // open camera for qr scanning
                            startActivity(new Intent(ActivityHomeUnjoined.this, ActivityQRCodeScanner.class));
                            //Toast.makeText(ActivityHomeUnjoined.this, "Joining Group", Toast.LENGTH_SHORT).show();
                            //startActivity(new Intent(ActivityHomeUnjoined.this, ActivityHomeJoined.class));
                        }
                        else if (item.getItemId() == R.id.menu_item_enterLink) {
                            // this copies from the clipboard
                            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = clipboard.getPrimaryClip();
                            if (clip != null && clip.getItemCount() > 0) {
                                ClipData.Item clipItem = clip.getItemAt(0);
                                String data = clipItem.getText().toString();
                                //Toast.makeText(ActivityHomeUnjoined.this, "CLIP: " + data, Toast.LENGTH_SHORT).show();

                                // If the data does not match a known base URL, open a text input for the user
                                String base_url = APIHandler.url_base + "groups/";
                                if (!data.startsWith(base_url)) {
                                    Toast.makeText(ActivityHomeUnjoined.this, "Opening User Input", Toast.LENGTH_SHORT).show();

                                    // AlertDialog for user to enter URL - input that will pass "test"
                                    // Once actually implemented, the API needs to be notified to 1) test the URL and 2) join the group
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityHomeUnjoined.this);
                                    builder.setTitle("Enter Share Group URL");
                                    EditText urlInput = new EditText(ActivityHomeUnjoined.this);
                                    urlInput.setInputType(InputType.TYPE_CLASS_TEXT);
                                    builder.setView(urlInput);

                                    // submit URL for testing
                                    builder.setPositiveButton("Join", (DialogInterface.OnClickListener) (dialog, which) -> {
                                        String inputData = urlInput.getText().toString();

                                        // attempt to join the group
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    if (APIHandler.groupJoin(ActivityHomeUnjoined.this, inputData)) {
                                                        startActivity(new Intent(ActivityHomeUnjoined.this, ActivityHomeJoined.class));
                                                    }
                                                    else {
                                                        // notify the user of bad group, etc.
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        }).start();

                                        // NEEDS TO BE UPDATED
                                        /*if (inputData.equals("test")) {
                                            Toast.makeText(ActivityHomeUnjoined.this, "Joining Group", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(ActivityHomeUnjoined.this, ActivityHomeJoined.class));
                                        }
                                        else {
                                            Toast.makeText(ActivityHomeUnjoined.this, "Not a Valid Group", Toast.LENGTH_SHORT).show();
                                        }*/

                                    });
                                    // cancel input
                                    builder.setNegativeButton("Cancel", (DialogInterface.OnClickListener) (dialog, which) -> {
                                        dialog.cancel();
                                    });

                                    // Create  & show Alert dialog
                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();
                                }
                                else {


                                    // attempt to join the group
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                if (APIHandler.groupJoin(ActivityHomeUnjoined.this, data)) {
                                                    startActivity(new Intent(ActivityHomeUnjoined.this, ActivityHomeJoined.class));
                                                }
                                                else {
                                                    // notify the user of bad group, etc.
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    }).start();

                                    //Toast.makeText(ActivityHomeUnjoined.this, "Joining Group", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        else if (item.getItemId() == R.id.menu_item_cancel) {
                            Toast.makeText(ActivityHomeUnjoined.this, "Canceled", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });
                joinOptions.show();
            }
        });

        // Initialize Settings Button
        Button open_settings_button = (Button)findViewById(R.id.button_settings);
        open_settings_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityHomeUnjoined.this, ActivitySettingsHome.class));
            }
        });
    }

    public void startShareGroup(View v) {
        /*
        This will begin a new share group.
        This action is conducted when button_start is tapped.

        This button, since it will begin a new activity, needs to be initialized prior.
        Button functionality (this method) will still work onClick.
         */

        // Notify the API that a new group needs to be created.
        // Receive the new group information

    }

    public void joinShareGroup(View v) {
        /*
        This will join a share group.
        This action is conducted when button_join is tapped.

        This button, since it will begin a new activity, needs to be initialized prior.
        Button functionality (this method) will still work onClick.
         */

        // Obtain a URL, or use the camera to scan a QR Code
        // Notify the API of the share group which needs to be joined
    }

    public void openSettings(View v) {
        // this method does nothing, but is called by the settings onClick method.
    }

}