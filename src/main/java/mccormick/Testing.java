package mccormick;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Testing {

    public static void main(String[] args) throws Exception {

        Testing http = new Testing();

        System.out.println("Testing 1 - Send Http GET request");
        http.sendGet();

    }

    // HTTP GET request
    private void sendGet() throws Exception {

        String url = "https://gdt-api.mccormick.com/recipes?page=1&size=3";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("content-type","application/json");
        con.setRequestProperty("x-api-key", "0RiIi36IwU96NTUNhszOS6QzLOaNrKdX67UeEpml");

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());

    }


    /*https://gdt-api.mccormick.com/recipes?page=0&size=2&content-type=application/json&x-api-key=0RiIi36IwU96NTUNhszOS6QzLOaNrKdX67UeEpml*/

}
