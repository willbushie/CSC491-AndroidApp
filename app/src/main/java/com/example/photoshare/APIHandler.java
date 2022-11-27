package com.example.photoshare;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class APIHandler {
    /*
    This class will handle all API connection & communication.
     */

    // global variables - these are stored on device
    public static String url_base = "http://192.168.1.123:80/";
    public static String access_token;
    public static String refresh_token;

    public static Boolean login(String username, String password) throws Exception {
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

            System.out.println("RESPONSE: " + response.toString());
            return true;
        } else {
            System.out.println("ERROR - RESPONSE CODE: " + responseCode);
            return false;
        }
    }

    public static Boolean register(String firstname, String lastname, String email, String username, String password, String password2) throws Exception {
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

            System.out.println("RESPONSE: " + response.toString());
            return true;
        } else {
            System.out.println("ERROR - RESPONSE CODE: " + responseCode);
            return false;
        }
    }


    public static void main(String[] args) {}
}
