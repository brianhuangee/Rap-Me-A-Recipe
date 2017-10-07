package spotify;

import com.wrapper.spotify.Api;
import com.wrapper.spotify.methods.TrackSearchRequest;
import com.wrapper.spotify.models.*;

import java.util.List;

public class Pull {

    public static void pull() {
        Api api = Auth.getApi();

        TrackSearchRequest request = api.searchTracks("Starboy").build();

        try {
            List<Track> tracks = request.get().getItems();
            for (Track track : tracks) {
                System.out.println(track.getName() + " : " + track.getDuration());
            }

        } catch (Exception e) {
            System.out.println("Could not get albums.");
        }
    }
}
