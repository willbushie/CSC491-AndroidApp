package com.example.photoshare;
import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;


public class FileHandler {

    // class variables
    private final String filename = "app_data.json";

    public void write(Context context, JSONObject json) throws IOException {
        /*
        This method will write a single key/value pair.
        If it does not exist, it is created.
        If it exists, it is modified.
        Note: context in this sense is <activity.this> .
         */

        // read the files contents
        String contents = this.read_all(context);

        JSONObject prev_contents = null;
        try {
            prev_contents = new JSONObject(contents);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //System.out.println("JSON FILE CONTENTS_1: " + prev_contents.toString());

        if (contents.equals("")) {
            // overwrite the whole file for all it matters
            FileOutputStream fos = null;

            try {
                fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
                fos.write(json.toString().getBytes());
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if (fos != null) {
                    fos.close();
                }
            }
        }
        else {
            JSONObject all_data = new JSONObject();
            try {
                all_data = this.combine(prev_contents,json);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

            FileOutputStream fos = null;

            try {
                fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
                fos.write(all_data.toString().getBytes());
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    fos.close();
                }
            }
        }
        //String contents2 = this.read_all(context);
        //System.out.println("JSON FILE CONTENTS_2: " + contents2);
    }

    public String read(Context context, String key) throws IOException {
        /*
        This method will read a single key/value pair.
         */
        //
        FileInputStream fis = null;
        StringBuilder sb = new StringBuilder();
        // read all data from the file
        try {
            fis = context.openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String data;
            while ((data = br.readLine()) != null) {
                sb.append(data);
            }
            // create JSONObject to allow parsing
            JSONObject all_data = new JSONObject(sb.toString());
            String value = all_data.getString(key);
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Read Result: " + value);
            return value;
        }
        catch (FileNotFoundException | JSONException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public String read_all(Context context) {
        String contents = "";
        InputStream inputStream = null;
        try {
            inputStream = context.openFileInput(filename);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                contents = stringBuilder.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return contents;
    }

    public JSONObject combine(JSONObject obj1, JSONObject obj2) throws JSONException {
        /*
        This method combines two JSONObjects and returns a single JSONObject
        (the combination of the two passed objections).
        If there are repeat keys, obj1 values will be overwritten with obj2 values.
         */

        // combined JSONObject that will be returned after merging
        JSONObject all_data = new JSONObject();

        // walk through the keys in obj1 and place inside of all_data
        JSONArray obj1_array = obj1.names();
        for (int i = 0; i < Objects.requireNonNull(obj1_array).length(); i++) {
            // obtain both key and value pair
            String curr_key = obj1_array.getString(i);
            String curr_value = obj1.getString(curr_key);
            // place key value pair into all_data
            all_data.put(curr_key,curr_value);
        }
        // walk though the keys in obj2 and place inside of all_data (replace when necessary)
        JSONArray obj2_array = obj2.names();
        for (int j = 0; j < Objects.requireNonNull(obj2_array).length() ; j++) {
            String curr_key = obj2_array.getString(j);
            String curr_value = obj2.getString(curr_key);
            all_data.put(curr_key, curr_value);
        }

        //System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~OBJ1: " + obj1.toString());
        //System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~OBJ2: " + obj2.toString());
        //System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~all_data: " + all_data.toString());

        // return combined JSONObject
        return all_data;
    }

    public static void main(String[] args) {}
}
