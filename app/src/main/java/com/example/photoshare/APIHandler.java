package com.example.photoshare;

import android.app.Activity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class APIHandler {

    // global variables - these are stored on device
    public static String url_base = "http://192.168.1.126:8000/";

    public static Boolean refresh(Activity context) {
        /*
        This method will refresh a user's tokens when needed (if a request is denied - 401 for example).
         */
        String path = "auth/login/refresh/";

        try {
            URL obj = new URL(url_base + path);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Accept", "application/json");

            FileHandler handler = new FileHandler();
            String params = "refresh=" + handler.read(context, "app_data.json", "refresh");
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
                JSONObject json = new JSONObject(response.toString());
                handler.write(context, "app_data.json", json);
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~REFRESH RESPONSE: " + json);
                return true;
            } else {
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~REFRESH RESPONSE CODE: " + responseCode);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Boolean login(Activity context, String username, String password) {
        /*
        This method will login a user.
         */

        String path = "auth/login/";

        try {
            URL obj = new URL(url_base + path);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Accept", "application/json");

            String params = "username=" + username + "&password=" + password;
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
                JSONObject json = new JSONObject(response.toString());
                FileHandler handler = new FileHandler();
                handler.write(context, "app_data.json", json);
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ LOGIN RESPONSE: " + json);

                return true;
            } else {
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~LOGIN RESPONSE CODE: " + responseCode);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Boolean register(Activity context, String firstname, String lastname, String email, String username, String password, String password2) {
        /*
        This method will register a new user.
         */

        String path = "auth/register/";

        try {
            URL obj = new URL(url_base + path);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Accept", "application/json");

            String params = "first_name=" + firstname + "&last_name=" + lastname + "&email=" + email + "&username=" + username + "&password=" + password + "&password2=" + password2;
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

                // obtain data to be written to data files
                login(context, username, password);
                currentUser(context);

                // write data to app_data.json
                JSONObject json = new JSONObject(response.toString());
                //FileHandler handler = new FileHandler();
                //handler.write(context, "app_data.json", json);
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~REGISTER RESPONSE: " + json);
                return true;
            } else {
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~REGISTER RESPONSE CODE: " + responseCode);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Boolean update_profile(Activity context, String username, String email, String fname, String lname) {
        /*
        This method will update a user's profile.
        If there is an error with the input, an HTTP 400 error is thrown.
         */
        String path = "auth/update_profile/";
        FileHandler handler = new FileHandler();

        try {
            String pk = handler.read(context, "user_data.json", "id");
            URL obj = new URL(url_base + path + pk + "/");
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            conn.setRequestMethod("PUT");
            conn.setDoOutput(true);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + handler.read(context, "app_data.json", "access"));

            String params = "username=" + username + "&email=" + email + "&first_name=" + fname + "&last_name=" + lname;
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

                // write data to user_data.json
                JSONObject json  = new JSONObject(response.toString());
                handler.write(context, "user_data.json", json);
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ UPDATE PROFILE RESPONSE: " + json);

                return true;
            } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                if (refresh(context)) {
                    update_profile(context, username, email, fname, lname);
                }
            } else {
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~UPDATE PROFILE RESPONSE CODE: " + responseCode);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Boolean update_password(Activity context) {
        /*
        This method will update a user's password
         */
        String path = "auth/logout/";
        FileHandler handler = new FileHandler();

        try {
            URL obj = new URL(url_base + path);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            conn.setRequestMethod("PUT");
            conn.setDoOutput(true);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + handler.read(context, "app_data.json", "access"));

            String params = "refresh_token=" + handler.read(context, "app_data.json", "refresh");
            OutputStream os = conn.getOutputStream();
            os.write(params.getBytes());
            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_RESET) {
                return true;
            } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                if (refresh(context)) {
                    logout(context);
                    return true;
                }
            } else {
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~LOGOUT RESPONSE CODE: " + responseCode);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Boolean logout(Activity context) {
        /*
        This method will logout a user - and remove on device attributes.
         */
        String path = "auth/logout/";
        FileHandler handler = new FileHandler();

        try {
            URL obj = new URL(url_base + path);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            conn.setRequestMethod("POST");
            //conn.setDoOutput(true);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + handler.read(context, "app_data.json", "access"));

            String params = "refresh_token=" + handler.read(context, "app_data.json", "refresh");
            OutputStream os = conn.getOutputStream();
            os.write(params.getBytes());
            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_RESET) {
                return true;
            } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                if (refresh(context)) {
                    logout(context);
                }
            } else {
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~LOGOUT RESPONSE CODE: " + responseCode);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Boolean groupCreate(Activity context, String group_name) {
        /*
        This method will create a group and place all returned items into the group_data file.
         */

        String path = "groups/create/";
        FileHandler handler = new FileHandler();

        try {
            URL obj = new URL(url_base + path);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + handler.read(context, "app_data.json", "access"));

            // param values
            String refresh_token = handler.read(context, "app_data.json", "refresh");
            String admin_id = handler.read(context, "user_data.json", "id");

            String params = "name=" + group_name + "&admin=" + admin_id;
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" + params);
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
                JSONObject json = new JSONObject(response.toString());
                handler.write(context, "group_data.json", json);
                JSONObject groupMember = new JSONObject("{\"groupMember\":\"true\"}");
                handler.write(context, "group_data.json", groupMember);
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~GROUPCREATE RESPONSE: " + json);

                currentUser(context);

                return true;
            } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                if (refresh(context)) {
                    groupCreate(context, group_name);
                }
            } else {
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~GROUPCREATE RESPONSE CODE: " + responseCode);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Boolean groupJoin(Activity context, String join_url) {
        /*
        This method will join a group only if it is currently active.

        *** THIS METHOD CURRENTLY OBTAINS GROUP INFORMATION - IF SUCCESSFUL, TRUE IS RETURNED ***

         */

        //String path = "groups/join/";
        FileHandler handler = new FileHandler();

        try {
            URL obj = new URL(join_url);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + handler.read(context, "app_data.json", "access"));

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
                JSONObject json = new JSONObject(response.toString());
                handler.write(context, "group_data.json", json);
                JSONObject groupMember = new JSONObject("{\"groupMember\":\"true\"}");
                handler.write(context, "group_data.json", groupMember);
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~JOINGROUP RESPONSE: " + json);
                return true;
            } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                if (refresh(context)) {
                    groupJoin(context, join_url);
                }
            } else if (responseCode == HttpURLConnection.HTTP_BAD_METHOD) {
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~JOINGROUP RESPONSE: " + join_url);
            } else {
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~JOINGROUP RESPONSE CODE: " + responseCode);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Boolean groupLeave(Activity context) {
        /*
        This method will remove a member from a group.

        *** THIS METHOD IS CURRENTLY NOT USED - ACTIONS ARE CONDUCTED IN ACTIVITYHOMEJOINED ***
         */

        String path = "groups/leave/";
        FileHandler handler = new FileHandler();

        try {
            URL obj = new URL(url_base + path);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + handler.read(context, "app_data.json", "access"));

            // param values
            String refresh_token = handler.read(context, "app_data.json", "refresh");
            String admin_id = handler.read(context, "user_data.json", "id");

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
                JSONObject json = new JSONObject(response.toString());
                handler.write(context, "group_data.json", json);
                JSONObject groupMember = new JSONObject("{\"groupMember\":\"true\"}");
                handler.write(context, "group_data.json", groupMember);
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~GROUPLEAVE RESPONSE: " + json);
                return true;
            } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                if (refresh(context)) {
                    groupLeave(context);
                }
            } else {
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~GROUPLEAVE RESPONSE CODE: " + responseCode);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void currentUser(Activity context) {
        /*
        This method will create a group and place all returned items into the group_data file.
         */

        String path = "groups/user/";
        FileHandler handler = new FileHandler();

        try {
            URL obj = new URL(url_base + path);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            conn.setRequestMethod("GET");
            //conn.setDoOutput(true);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + handler.read(context, "app_data.json", "access"));

            //String params = "refresh_token=" + handler.read(context, handler.user_data, "refresh");
            //OutputStream os = conn.getOutputStream();
            //os.write(params.getBytes());
            //os.flush();
            //os.close();

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
                JSONObject json = new JSONObject(response.toString());
                handler.write(context, "user_data.json", json);
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~CURRENTUSER RESPONSE: " + json);
            } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                if (refresh(context)) {
                    currentUser(context);
                }
            } else {
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~CURRENTUSER RESPONSE CODE: " + responseCode);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {}
}
