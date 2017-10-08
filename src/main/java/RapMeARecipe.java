import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.*;
import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;
import com.amazon.speech.speechlet.servlet.SpeechletServlet;
import com.amazon.speech.ui.OutputSpeech;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.wrapper.spotify.models.Track;
import mccormick.Pull;
import mccormick.Recipe;
import musixmatch.MusixMatchApi;
import org.mortbay.util.ajax.JSON;
import org.xml.sax.SAXException;
import spotify.Song;
import utils.APIKeys;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

public class RapMeARecipe implements Speechlet {

    private static final String DOMAIN = "";

    private static final MusixMatchApi matchApi = new MusixMatchApi(
            APIKeys.keys.get("musixmatch"),
            APIKeys.keys.get("youtube"));

    private static final Track ANOTHER_ONE = new Track();
    static {
        ANOTHER_ONE.setId("45XhKYRRkyeqoW3teSOkCM");
    }

    private static final Song DJ_KHALED = new Song(ANOTHER_ONE, 1000, 1000);


    public static SpeechletResponse handleRequest(String recipeTitle) throws ParserConfigurationException, SAXException, IOException {
        String json = "{";
        try {
            String recipe = Recipe.getRecipeDetails(recipeTitle);
            Scanner s = new Scanner(Pull.getElementData("description", recipe));

            Song currentSong;
            while (s.hasNext()) {
                String word = s.next();
                HashMap<String, String> timestamp = matchApi.getTimestamp(word, 0, 0);
                if (timestamp == null) {
                    currentSong = DJ_KHALED;
                } else {
                    currentSong = new Song(spotify.Pull.getTrack(timestamp.get("title")),
                            (long) (Double.parseDouble(timestamp.get("start")) * 1000),
                            (long) (Double.parseDouble(timestamp.get("end")) * 1000));
                }
                json += " { \"id\": \"" + currentSong.getID() + "\", \"start\": \"" + currentSong.getTime() + "\", \"end\": \"" + currentSong.getLength() + "\"}, ";
            }
        } catch (Exception e) {
        }
        PlainTextOutputSpeech resp = new PlainTextOutputSpeech();
        resp.setText("Playing");
        if (json.length() < 2) {
            return SpeechletResponse.newTellResponse(resp);
        }
        json = json.substring(0, json.length() - 2) + " }";
        URL url = new URL(DOMAIN + "/input/#" + URLEncoder.encode(json, "UTF-8"));
        return SpeechletResponse.newTellResponse(resp);
    }
    public static void main(String args[]) throws IOException, SAXException, ParserConfigurationException {
        System.out.println(spotify.Pull.getTrack("Wild Thoughts").getId());
    }

    @Override
    public void onSessionStarted(SessionStartedRequest sessionStartedRequest, Session session) throws SpeechletException {
        return;
    }

    @Override
    public SpeechletResponse onLaunch(LaunchRequest launchRequest, Session session) throws SpeechletException {
        return null;
    }

    @Override
    public SpeechletResponse onIntent(IntentRequest intentRequest, Session session) throws SpeechletException {
        Intent intent = intentRequest.getIntent();
        String intentName = (intent != null) ? intent.getName() : null;
        if ("rap".equals(intentName)) {
            try {
                return handleRequest(intent.getSlot("Recipe").getValue());
            } catch (ParserConfigurationException | SAXException | IOException e) {
                throw new SpeechletException(e);
            }
        }
        return null;
    }

    @Override
    public void onSessionEnded(SessionEndedRequest sessionEndedRequest, Session session) throws SpeechletException {
        return;
    }
}
