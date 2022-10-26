package com.example.photoshare;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class APIHandler {


    public static void main(String[] args) {
        testAPIConnection();
    }

    public static void testAPIConnection() {
       /*
        Testing the API with the below method.
        This method works correctly and obtains the API information.
        Youtube Tutorial: https://www.youtube.com/watch?v=zZoboXqsCNw (somewhat helpful)
         */
        try {
            URL url = new URL("http://127.0.0.1:8000/api/users/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            StringBuilder information = new StringBuilder();
            Scanner scanner = new Scanner(url.openStream());
            while (scanner.hasNext()) {
                information.append(scanner.nextLine());
            }
            scanner.close();
            System.out.println(information);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        /*
        Output from this request (2022/10/25 @ 19:40):
        [{"uid": "3640ad63-56a7-4239-b498-4b623fb5898e", "email": "william.bushie@cuw.edu",
        "password": "test", "username": "empty_name", "account_creation": "2022-10-03T04:01:02Z",
        "last_seen": "2022-10-03T14:18:40.211446Z", "last_known_ip": "172.0.0.69", "active": true},
        {"uid": "ca2717bf-80a5-4b80-b02a-4021e3dde19c", "email": "william.bushie@aurorawdc.com",
        "password": "haha", "username": "empty_name", "account_creation": "2022-10-03T14:23:43.396216Z",
        "last_seen": "2022-10-04T15:37:36.922589Z", "last_known_ip": "172.0.0.56", "active": true},
        {"uid": "a8bf74d2-1215-4d8c-b53f-ff0af8274037", "email": "william.bushie@gmail.edu",
        "password": "test", "username": "empty_name", "account_creation": "2022-10-04T15:25:05.371233Z",
        "last_seen": "2022-10-04T15:36:55.353348Z", "last_known_ip": "172.0.0.66", "active": true},
        {"uid": "66fc1e43-c533-4eb3-ba2b-e1b63956610e", "email": "william.bushie@cuw.edu",
        "password": "haha", "username": "willbushie", "account_creation": "2022-10-10T14:43:03.585903Z",
        "last_seen": "2022-10-10T14:48:28.020810Z", "last_known_ip": "172.0.1.2", "active": true}]
         */
    }
}
