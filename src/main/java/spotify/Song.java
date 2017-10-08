package spotify;

import com.wrapper.spotify.models.Track;

public class Song {

    private Track track;
    private String word;
    private long time;
    private long length;

    public Song(Track track, long time, long length) {
        this.track = track;
        this.time = time;
        this.length = length - time;
    }

    public Song(String word, long time, long length) {
        this.word = word;
        this.time = time;
        this.length = length - time;
    }

    @Override
    public String toString() {
        if (track != null) {
            return track.getId() + " : " + time + " - " + length;
        } else {
            return word + " : " + time + " - " + length;
        }
    }

    public String getID() {
        if (track != null) {
            return track.getId();
        } else {
            return word;
        }
    }

    public long getTime() {
        return time;
    }

    public long getLength() {
        return length;
    }
}
