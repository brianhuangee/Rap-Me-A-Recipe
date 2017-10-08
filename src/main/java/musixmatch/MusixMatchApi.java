package musixmatch;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

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
    private HashMap<String, String> searchLyricWord(String word, int page) throws UnsupportedEncodingException {
        final String request = SEARCH_METHOD + API_KEY_PARAM + MUSIX_API_KEY + "&q_lyrics=" + URLEncoder.encode(word, "UTF-8") +
                "&f_music_genre_id=18&s_track_rating=desc&page=" + page + "&page_size=1";
        final String response = requestApi(MUSIX_BASE, request);
        final JSONObject responseObj = new JSONObject(response);
        final JSONObject songObj = (JSONObject)
                responseObj.getJSONObject("message").getJSONObject("body").getJSONArray("track_list").get(0);
        final JSONObject trackObj = songObj.getJSONObject("track");
        HashMap<String, String> ret = new HashMap<>();
        ret.put("title", trackObj.getString("track_name"));
        ret.put("artist", trackObj.getString("artist_name"));
        return ret;

    }

    public HashMap<String, String> getTimestamp(String word, int page, int timeout) throws IOException, SAXException, ParserConfigurationException {
        HashMap<String, String> ret = new HashMap<>();
        HashMap<String, String> songNameAndArtist = null;
        StringBuffer buffer = new StringBuffer();
        while (buffer.toString().isEmpty()) {
            timeout++;
            if (timeout == 15) {
                return null;
            }
            try {
                songNameAndArtist = searchLyricWord(word, page);
                if (songNameAndArtist.get("title").equals("rockstar") || songNameAndArtist.get("title").equals("Bodak Yellow")) {
                    System.out.println("post");
                    page++;
                    continue;
                }
            } catch (JSONException e) {
                return null;
            }

            final String artist = songNameAndArtist.get("artist");
            final String title = songNameAndArtist.get("title");
            final String response = requestApi(YOUTUBE_BASE,
                    "&type=video&videoCaption=closedCaption&key=" + YOUTUBE_API_KEY + "&q=" + URLEncoder.encode(title + " " + artist, "UTF-8") + "&maxResults=1");
            final JSONObject responseObj = new JSONObject(response);
            String youtubeId;
            try {
            if (!((JSONObject) responseObj.getJSONArray("items").get(0)).getJSONObject("snippet").getString("title").toLowerCase().contains(title.toLowerCase())) {
                page++;
                continue;
            }
                youtubeId =
                        ((JSONObject) responseObj.getJSONArray("items").get(0)).getJSONObject("id").getString("videoId");
            } catch (JSONException e) {
                return null;
            }
            try {
                URL url = new URL("http://video.google.com/timedtext?lang=en&v=" + youtubeId);
                System.out.println(youtubeId);
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
                String str;
                while ((str = in.readLine()) != null) {
                    buffer.append(str);
                }
                in.close();
            } catch (IOException ignored) {

            }
            page++;
        }
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(buffer.toString()));
        Document document = builder.parse(is);
        NodeList list = document.getElementsByTagName("text");
        for (int i = 0; i < list.getLength(); ++i) {
            Node item = list.item(i);
            if (item.getTextContent().toLowerCase().contains(word.toLowerCase())) {
                float start = Float.parseFloat(item.getAttributes().getNamedItem("start").getTextContent());
                String[] line = item.getTextContent().toLowerCase().split(" ");
                int syllableCount = 0;
                int syllablesIn = 0;
                EnglishSyllableCounter counter = new EnglishSyllableCounter();
                for (String words : line) {
                    if (words.toLowerCase().equals(word.toLowerCase()) && syllablesIn == 0){
                        syllablesIn = syllableCount;
                    }
                   syllableCount += counter.countSyllables(words);
                }
                float dur = Float.parseFloat(item.getAttributes().getNamedItem("dur").getTextContent());
                start += (syllablesIn/syllableCount) * dur;
                float end = start + ((syllablesIn + counter.countSyllables(word))/(float) syllableCount) * dur;
                if ((start == end && end == 0)) {
                    continue;
                }
                if (end - start > 1) {
                    end = start + 1;
                }
                if (songNameAndArtist.get("title").equals("No Limit")) {{
                    start -= 14.681;
                    end -= 14.681;
                    System.out.println("G-ezy");
                }}
                ret.put("start", String.valueOf(start));
                ret.put("end", String.valueOf(end));
                ret.put("artist", songNameAndArtist.get("artist"));
                ret.put("title", songNameAndArtist.get("title"));
                return ret;
            }
        }
        return getTimestamp(word, page + 1, timeout);
    }
}
