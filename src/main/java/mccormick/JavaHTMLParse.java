package mccormick;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class JavaHTMLParse {

    public static void main(String[] args) throws Exception {

        JavaHTMLParse http = new JavaHTMLParse();

        System.out.println("Testing 1 - Send Http GET request");
        String html = http.sendGet();
        html = html.substring(html.indexOf("slider js-item-container item-container searched-recipe-container"));
        //System.out.println(html);
        //System.out.println(html.indexOf("<div class=\"item-wrapper js-item-wrapper item-square-category\" data-itemid=")+76);
        html = html.substring(html.indexOf("itemid=")+8);
        //System.out.println(html);
        html = html.substring(0,html.indexOf("\""));

        //html = html.replace("-","");

        System.out.println(html);

    }

    // HTTP GET request
    public String sendGet() throws Exception {

        String url = "https://www.mccormick.com/search?t=lasagna";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("content-type", "application/json");
        con.setRequestProperty("x-api-key", "");

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        int i = 0;

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result

        return response.toString();

    }
}
