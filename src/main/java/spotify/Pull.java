package spotify;

import com.wrapper.spotify.Api;
import com.wrapper.spotify.exceptions.WebApiException;
import com.wrapper.spotify.methods.PlaylistRequest;
import com.wrapper.spotify.methods.TrackSearchRequest;
import com.wrapper.spotify.methods.UserPlaylistsRequest;
import com.wrapper.spotify.models.*;

import java.io.IOException;
import java.util.List;

public class Pull {

    public static final Api API = Auth.getApi();

    public static Track getTrack(String title) {
        TrackSearchRequest request = API.searchTracks(title).build();

        try {
           return request.get().getItems().get(0);
        } catch (Exception e) {
            return null;
        }
    }
}
