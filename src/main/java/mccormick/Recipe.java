package mccormick;


import utils.APIKeys;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Recipe {

    public static String getRecipeDetails(String recipeTitle) throws Exception {
        System.out.println(recipeTitle);
        String url = "https://www.mccormick.com/search?t=" + recipeTitle;
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

        String html = response.toString();
        html = html.substring(html.indexOf("slider js-item-container item-container searched-recipe-container"));
        html = html.substring(html.indexOf("itemid=")+8);
        html = html.substring(0,html.indexOf("\""));

        return getRecipe(html);
    }

    private static String getRecipe(String recipeid) throws Exception {
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

        return response.toString().substring(response.toString().indexOf("\":{\"") + 2, response.toString().length() - 1);
    }
}
