package mccormick;

import utils.APIKeys;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Recipe {

    public String getRecipeDetails() throws Exception {
        String url = "https://www.mccormick.com/search?t=lasagna";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");

        con.setRequestProperty("content-type", "application/json");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return getRecipe(response.toString());
    }

    private String getRecipe(String recipeid) throws Exception {
        String url = "https://gdt-api.mccormick.com/recipes/" + recipeid;

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");

        con.setRequestProperty("content-type","application/json");
        con.setRequestProperty("x-api-key", APIKeys.keys.get("mccormick"));

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }
}
