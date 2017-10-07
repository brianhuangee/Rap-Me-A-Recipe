package musixmatch;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

public class MusixMatchApi {
    private final String MUSIX_BASE = "http://api.musixmatch.com/ws/1.1/";
    private final String YOUTUBE_BASE = "https://www.googleapis.com/youtube/v3/search?part=snippet";
    private final String API_KEY_PARAM = "apikey=";
    private final String SEARCH_METHOD = "track.search?";
    private final String MUSIX_API_KEY;
    private final String YOUTUBE_API_KEY;

    public MusixMatchApi(String musixApiKey, String youtubeApiKey) {
        this.MUSIX_API_KEY = musixApiKey;
        this.YOUTUBE_API_KEY = youtubeApiKey;
    }

    private String requestApi(String BASE_URL, String request) {
        StringBuffer buffer = new StringBuffer();
        try {
            String apiUrl = BASE_URL + request;
            URL url = new URL(apiUrl);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            String str;
            while ((str = in.readLine()) != null) {
                buffer.append(str);
            }
            in.close();
        } catch (IOException ignored) {}
        return buffer.toString();
    }
    private String searchLyricWord(String word) {
        final String request = SEARCH_METHOD + API_KEY_PARAM + MUSIX_API_KEY + "&q_lyrics=" + word +
                "&f_music_genre_id=18&s_track_rating=desc&page=0&page_size=0";
        final String response = requestApi(MUSIX_BASE, request);
        final JSONObject responseObj = new JSONObject(response);
        final JSONObject songObj = (JSONObject)
                responseObj.getJSONObject("message").getJSONObject("body").getJSONArray("track_list").get(0);
        final JSONObject trackObj = songObj.getJSONObject("track");
        return trackObj.getString("track_name") + trackObj.getString("artist_name");

    }

    public String getTimestamp(String word) {
        final String songNameAndArtist = searchLyricWord(word);
        final String response = requestApi(YOUTUBE_BASE,
                "&key=" + YOUTUBE_API_KEY + "&q=" + songNameAndArtist + "&maxResults=1");
        final JSONObject responseObj = new JSONObject(response);
        final String youtubeId =
                ((JSONObject) responseObj.getJSONArray("items").get(0)).getJSONObject("id").getString("videoId");
        StringBuffer buffer = new StringBuffer();
        try {
            URL url = new URL("http://video.google.com/timedtext?lang=en&v=" + youtubeId);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            String str;
            while ((str = in.readLine()) != null) {
                buffer.append(str);
            }
            in.close();
        } catch (IOException ignored) {
        }
        System.out.println(buffer.toString());
        return "";
    }
}
