import mccormick.Pull;
import mccormick.Recipe;
import musixmatch.MusixMatchApi;
import spotify.Song;
import utils.APIKeys;

import java.util.HashMap;
import java.util.Scanner;

public class RapMeARecipe {

    private static MusixMatchApi matchApi = new MusixMatchApi(
            APIKeys.keys.get("musixmatch"),
            APIKeys.keys.get("youtube"));

    public static void main(String[] args) {
        String json = "{";
        try {
            String recipe = Recipe.getRecipeDetails("pie");
            Scanner s = new Scanner(Pull.getElementData("description", recipe));

            Song currentSong;
            while (s.hasNext()) {
                String word = s.next();
                HashMap<String, String> timestamp = matchApi.getTimestamp(word, 0, 0);
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
            e.printStackTrace();
        }

        json = json.substring(0, json.length() - 2) + " }";
        System.out.println(json);
    }
}
