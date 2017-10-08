import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;

import java.util.HashSet;
import java.util.Set;

public class StreamHandler extends SpeechletRequestStreamHandler{
    private static final Set<String> supportedApplicationIds = new HashSet<String>();
    static {
        /*
         * This Id can be found on https://developer.amazon.com/edw/home.html#/
         * "Edit" the relevant
         * Alexa Skill and put the relevant Application Ids in this Set.
         */
        supportedApplicationIds.add("amzn1.ask.skill.e56a6e70-736a-4e77-a2f9-b3e5a7aa8d6b");
    }

    public StreamHandler() {
        super(new RapMeARecipe(), supportedApplicationIds);
    }
}
