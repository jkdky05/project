package ucab.ingsw.dataApis.spotifyData;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpotifyItem implements Serializable {
    private SpotifyAlbum album;
    private List<SpotifyArtist> artists;
    private SpotifyUrls external_urls;
    private String name;
}
