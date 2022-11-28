package com.example.photoshare;

import android.app.Activity;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class APIHandler {

    // global variables - these are stored on device
    public static String url_base = "http://192.168.1.123:80/";

    public static Boolean login(Activity context, String username, String password) throws Exception {
        /*
        This method will login a user.
         */

        String path = "auth/login/";

        URL obj = new URL(url_base + path);
        HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Accept", "application/json");

        String params = "username=" + username + "&" + "password=" + password;
        OutputStream os = conn.getOutputStream();
        os.write(params.getBytes());
        os.flush();
        os.close();

        int responseCode = conn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // write data to app_data.json
            JSONObject json  = new JSONObject(response.toString());
            FileHandler handler = new FileHandler();
            handler.write(context, json);
            System.out.println("RESPONSE: " + json);
            return true;
        } else {
            System.out.println("ERROR - RESPONSE CODE: " + responseCode);
            return false;
        }
    }

    public static Boolean register(Activity context, String firstname, String lastname, String email, String username, String password, String password2) throws Exception {
        /*
        This method will register a new user.
         */

        String path = "auth/register/";

        URL obj = new URL(url_base + path);
        HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Accept", "application/json");

        String params = "first_name=" + firstname + "&" + "last_name=" + lastname + "&" + "email=" + email + "&" + "username=" + username + "&" + "password=" + password + "&" + "password2=" + password2;
        OutputStream os = conn.getOutputStream();
        os.write(params.getBytes());
        os.flush();
        os.close();

        int responseCode = conn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_CREATED) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // write data to app_data.json
            JSONObject json  = new JSONObject(response.toString());
            FileHandler handler = new FileHandler();
            handler.write(context, json);
            System.out.println("RESPONSE: " + json);
            return true;
        } else {
            System.out.println("ERROR - RESPONSE CODE: " + responseCode);
            return false;
        }
    }

    public static Boolean logout(Activity context) throws Exception {
        /*
        This method will logout a user - and remove on device attributes.
         */
        String path = "auth/logout/";
        FileHandler handler = new FileHandler();

        URL obj = new URL(url_base + path);
        HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + handler.read(context, "access"));

        String params = "refresh_token=" + handler.read(context, "refresh");
        OutputStream os = conn.getOutputStream();
        os.write(params.getBytes());
        os.flush();
        os.close();

        int responseCode = conn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_RESET) {
            return true;
        }
        else {
            System.out.println("ERROR - RESPONSE CODE: " + responseCode);
            return false;
        }
    }


    public static void main(String[] args) {}
}
