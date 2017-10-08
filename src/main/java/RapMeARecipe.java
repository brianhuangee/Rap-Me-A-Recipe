import musixmatch.MusixMatchApi;
import org.xml.sax.SAXException;
import utils.APIKeys;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class RapMeARecipe {

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        Scanner scanner = new Scanner(System.in);
        String line;
        while (!(line = scanner.nextLine()).isEmpty()) {
            MusixMatchApi matchApi = new MusixMatchApi(APIKeys.keys.get("musixmatch"), APIKeys.keys.get("youtube"));
            HashMap obj = matchApi.getTimestamp(line, 0, 0);
            if (obj == null) {
                System.out.println("Not found");
            } else {
                System.out.println(obj.get("title"));
                System.out.println(obj.get("artist"));
                System.out.println(obj.get("start"));
                System.out.println(obj.get("end"));
            }
        }
    }
}
