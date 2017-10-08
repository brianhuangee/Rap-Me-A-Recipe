import mccormick.Pull;
import mccormick.Recipe;
import musixmatch.MusixMatchApi;
import org.json.JSONObject;
import spotify.Song;
import utils.APIKeys;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class RapMeARecipe {

    private static final ThreadLocal<MusixMatchApi> matchApi = ThreadLocal.withInitial(() -> new MusixMatchApi(
            APIKeys.keys.get("musixmatch"),
            APIKeys.keys.get("youtube")));

    public static String handleRequest(Map<String, Object> recipeTitle) {
        String json = "{";
        try {
            String requestJson = recipeTitle.get("request").toString();
            int indx = requestJson.indexOf("value=") + 6;
            String recipe = Recipe.getRecipeDetails(requestJson.substring(indx, requestJson.indexOf(",", indx)));
            Scanner s = new Scanner(Pull.getElementData("description", recipe));

            Song currentSong;
            while (s.hasNext()) {
                String word = s.next();
                HashMap<String, String> timestamp = matchApi.get().getTimestamp(word, 0, 0);
                if (timestamp == null) {
                    currentSong = new Song(word, 0, 0);
                    json += " { id: " + word + ", start: 0, end: 0}";
                } else {
                    currentSong = new Song(spotify.Pull.getTrack(word),
                            (long) (Double.parseDouble(timestamp.get("start")) * 1000),
                            (long) (Double.parseDouble(timestamp.get("end")) * 1000));
                    json += " id: " + spotify.Pull.getTrack(word) +
                            ", start: " + (long) (Double.parseDouble(timestamp.get("start")) * 1000) +
                            ", end: " + (long) (Double.parseDouble(timestamp.get("end")) * 1000) + " }";
                }
                json += " { id: " + currentSong.getID() + ", start: " + currentSong.getTime() + ", end: " + currentSong.getLength() + "}, ";
            }
        } catch (Exception e) {
        }
        json = json.substring(0, json.length() - 2) + " }";
        System.out.println(json);
        return json;
    }
    public static void main(String args[]) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("title", "lasagna");
        System.out.println(handleRequest(map));
    }
}
