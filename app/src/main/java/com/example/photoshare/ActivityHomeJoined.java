package com.example.photoshare;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.content.ClipboardManager;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;

public class ActivityHomeJoined extends AppCompatActivity {

    FileHandler handler = new FileHandler();

    @RequiresApi(api = Build.VERSION_CODES.R) // needed to run checkerBee()
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_joined);
        initializeAllButtons();
        test_detect();
        //checkerBee(false);
        //Set volumes = MediaStore.getExternalVolumeNames(ActivityHomeJoined.this);
        //System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        //System.out.println(volumes);
    }

    @Override
    public void onBackPressed() {
        /*
        This overrides original onBackPressed method.
        Ensures the user confirms before leaving a group.
         */
        leaveShareGroup(null);
    }

    public void initializeAllButtons() {
        /*
        Initialize all buttons so they are all prepared once the activity is loaded.
        This is only for buttons that require OnClickListeners.
         */

        // Initialize Settings Button
        Button open_settings_button = (Button)findViewById(R.id.button_settings);
        open_settings_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityHomeJoined.this, ActivitySettingsHome.class));
            }
        });

        /*/ Initialze Edit button as test Notification
        Button button_edit = (Button)findViewById(R.id.button_edit);
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.O) {
            NotificationChannel channel= new NotificationChannel("My Notification","My Notification",NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager =getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        button_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message="Test Notification Message";
                NotificationCompat.Builder builder = new NotificationCompat.Builder(ActivityHomeJoined.this,"My Notification");
                builder.setContentTitle("NotificationTitle");
                builder.setContentText(message);
                builder.setSmallIcon(R.drawable.ic_baseline_email_24);
                builder.setAutoCancel(true);
                NotificationManagerCompat managerCompat=NotificationManagerCompat.from(ActivityHomeJoined.this);
                managerCompat.notify(1,builder.build());
            }
        });*/

        // Initialize Share Button
        Button share_group_button = (Button) findViewById(R.id.button_share);
        share_group_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // popup menu to show joining options
                PopupMenu shareOptions = new PopupMenu(ActivityHomeJoined.this, share_group_button);
                shareOptions.getMenuInflater().inflate(R.menu.menu_sharegroup, shareOptions.getMenu());
                shareOptions.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.menu_item_showQRCode) {
                            //Toast.makeText(ActivityHomeJoined.this, "Showing QR Code", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ActivityHomeJoined.this, ActivityShareableQRCode.class));
                            /*/ This alert dialog will show the qr code (dynamically changing)
                            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityHomeJoined.this);
                            LayoutInflater factory = LayoutInflater.from(ActivityHomeJoined.this);
                            final View view = factory.inflate(R.layout.activity_shareable_qrcode, null);

                            builder.setView(view);
                            builder.setTitle("Scan QR Code to Join");
                            builder.setNeutralButton("Close", (DialogInterface.OnClickListener) (dialog, which) -> {
                                dialog.cancel();
                            });
                            AlertDialog alert = builder.create();
                            alert.show();*/
                        }
                        else if (item.getItemId() == R.id.menu_item_copyLink) {
                            Toast.makeText(ActivityHomeJoined.this, "Copied Link to Clipboard", Toast.LENGTH_SHORT).show();
                            // this does copy text to the clipboard
                            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                            String group_id = "";
                            try {
                                group_id = handler.read(ActivityHomeJoined.this, "group_data.json", "id");
                                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ GROUP ID: " + group_id);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            String base_url = APIHandler.url_base + "groups/" + group_id + "/";
                            ClipData clip = ClipData.newPlainText("Share Group URL", base_url);
                            clipboard.setPrimaryClip(clip);
                        }
                        else if (item.getItemId() == R.id.menu_item_cancel) {
                            Toast.makeText(ActivityHomeJoined.this, "Canceled", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });
                shareOptions.show();
            }
        });

    }

    public void shareShareGroup(View v) {
        /*
        This will begin a new share group.
        This action is conducted when button_start is tapped.
         */
        Button button_share = (Button)findViewById(R.id.button_share);

    }

    public void leaveShareGroup(View v) {
        /*
        This will join a share group.
        This action is conducted when button_join is tapped.
         */

        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityHomeJoined.this);
        builder.setMessage("Are you sure you want to leave the group? You will have to rejoin.");
        builder.setTitle("Are you sure?");

        // if confirmed
        builder.setPositiveButton("Yes, Leave", (DialogInterface.OnClickListener) (dialog, which) -> {
            JSONObject groupMember = null;
            try {
                groupMember = new JSONObject("{\"groupMember\":\"false\"}");
                handler.write(ActivityHomeJoined.this, "group_data.json", groupMember);
            }
            catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            finish();
        });
        // if canceled
        builder.setNegativeButton("Cancel", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });

        // Create  & show Alert dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void editShareGroup(View v) throws Exception {
        /*
        This will propose an edit to the members of the share group.
        This action is conducted when button_edit is tapped.
         */

        // this section crashes the activity
        /*String url = "http://192.168.1.136:80/groups/?format=json";
        URL obj = new URL(url);
        StringBuffer response = new StringBuffer();

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        /*int responseCode = con.getResponseCode();
        if (responseCode != 200) {
            response.replace(0,response.length(),"Response Code: " + responseCode);
        }
        else {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        }

        //System.out.println(response);
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityHomeJoined.this);
        builder.setMessage(response);
        builder.setTitle("Response");

        // if canceled
        builder.setNegativeButton("Close", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });

        // Create  & show Alert dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();*/

    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void pauseShareGroup(View v) {
        /*
        This will pause a members sharing to the share group. (this will be a toggle)
        This action is conducted when button_pause is tapped.
         */
        Button button_pause = (Button)findViewById(R.id.button_pause);
        try {
            String paused = handler.read(ActivityHomeJoined.this, "check_data.json", "paused");
            if (paused.equals("false")) {
                // call checkerBee() to handle the change checking
                //checkerBee(true);
                JSONObject writer = new JSONObject("{\"paused\":\"true\"}");
                handler.write(ActivityHomeJoined.this, "check_data.json", writer);
                // update activity look
                button_pause.setText(R.string.button_text_resume);
                button_pause.setBackgroundColor(getColor(R.color.easy_red));
                button_pause.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_baseline_play_arrow_24, 0, 0);
            }
            else if (paused.equals("true")) {
                // call checkerBee() to handle the change checking
                //checkerBee(false);
                JSONObject writer = new JSONObject("{\"paused\":\"false\"}");
                handler.write(ActivityHomeJoined.this, "check_data.json", writer);
                // update activity look
                button_pause.setText(R.string.button_text_pause);
                button_pause.setBackgroundColor(getColor(R.color.easy_green));
                button_pause.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_baseline_pause_24, 0, 0);
            }
        }
        catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R) // needed for media store items
    public void checkerBee(Boolean stop) {
        /*
        This method resets .getVersion & .getGeneration values stored in check_data.json
        This method also starts a worker thread that checks every interval for updates to the MediaStore.
        If updates are detected, other methods are notified to take the appropriate action,
        and .getVersion & .getGeneration are reset

        Volumes that can be checked: {0000-0000, external_primary} (obtained via MediaStore.getExternalVolumeNames(ActivityHomeJoined.this))

        Helpful Resources?
        https://developer.android.com/reference/android/app/job/JobInfo.Builder.html#addTriggerContentUri(android.app.job.JobInfo.TriggerContentUri)
        https://stackoverflow.com/questions/40025585/android-get-recently-added-media-in-android-device-similarly-to-file-manager
         */

        // initialize method variables
        String version;
        long generation;

        try {
            if (stop) {


                // PERFORM A CHECK TO MAKE SURE NO CHANGES HAVE BEEN MADE


                String json = "{\"paused\":\"true\"}";
                JSONObject writer = new JSONObject(json);
                handler.write(ActivityHomeJoined.this, "check_data.json", writer);
            }
            else {
                // set values to handler
                version = MediaStore.getVersion(ActivityHomeJoined.this);
                generation = MediaStore.getGeneration(ActivityHomeJoined.this,"0000-0000");
                // write handler
                String json = "{\"version\":" + version + ",\"generation\":" + generation + ",\"paused\":\"false\"}";
                JSONObject writer = new JSONObject(json);
                handler.write(ActivityHomeJoined.this, "check_data.json", writer);



                /*/ WORKER BEE THREAD LOGIC - needs to be a background task, cannot run on main UI thread
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                    }
                }).start();*/




            }
        }
        catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void reset() {
        /*
        This method will reset the version & generation values held in check_data.json
         */

        // set values to handler
        String version = MediaStore.getVersion(ActivityHomeJoined.this);
        long generation = MediaStore.getGeneration(ActivityHomeJoined.this,"0000-0000");
        // write handler
        try {
            String json = "{\"version\":" + version + ",\"generation\":" + generation + ",\"paused\":\"false\"}";
            JSONObject writer = new JSONObject(json);
            handler.write(ActivityHomeJoined.this, "check_data.json", writer);
        }
        catch (JSONException | IOException e) {
            e.printStackTrace();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public Boolean check() {
        /*
        This method conducts a basic comparison between the version and generation values.
        If these values differ, a false is given, otherwise a true is given.
         */
        String new_version = MediaStore.getVersion(ActivityHomeJoined.this);
        String old_version;
        long new_generation = MediaStore.getGeneration(ActivityHomeJoined.this,"0000-0000");
        String old_generation;
        //System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ after: " + new_version + " " + new_generation);
        try {
            old_version = handler.read(ActivityHomeJoined.this, "check_data.json", "version");
            old_generation = handler.read(ActivityHomeJoined.this, "check_data.json", "generation");
            String new_generation_str = Long.toString(new_generation);
            if (old_version.equals(new_version)) {
                    return old_generation.equals(new_generation_str);
            }
            else {
                return false;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void test_detect() {
        /*
        This method is for testing detection of new images.
         */

        // everything is detected - onChange called:
        // 3 for added photo
        // 1 for both modified and deletion
        getContentResolver().registerContentObserver(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, true,
                new ContentObserver(new Handler()) {
                    @RequiresApi(api = Build.VERSION_CODES.R)
                    @Override
                    public void onChange(boolean selfChange) {
                        super.onChange(selfChange);
                        long timestamp = readLastDateFromMediaStore(ActivityHomeJoined.this, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        // comapare with your stored last value and do what you need to do
                        //System.out.println(check());
                        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Most recent timestamp: " + timestamp);


                    }
                });
    }

    private long readLastDateFromMediaStore(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        MediaStore.Images.Media media = null;
        long dateAdded = 1;
        // this is not getting called
        if (cursor.moveToNext()) {
            //Long dateAdded = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_ADDED));
            dateAdded = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_ADDED));
        }
        cursor.close();
        return dateAdded;
    }


}
