package utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public class APIKeys {

    public static HashMap<String, String> keys = loadKeys();

    private static HashMap<String, String> loadKeys() {
        HashMap<String, String> keys = new HashMap<>();

        Path path = Paths.get("keys");
        try {
            List<String> lines = Files.readAllLines(path);

            for (String line : lines) {
                String id = line.substring(0, line.indexOf('/') + 1);
                String key = line.substring(line.indexOf('/') + 1);
                keys.put(id, key);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return keys;
    }
}
