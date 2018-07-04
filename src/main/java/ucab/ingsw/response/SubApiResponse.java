package ucab.ingsw.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ToString
public class SubApiResponse {
    private InstagramResponse instagramResponse;
    private YoutubeResponse youtubeResponse;
    private SpotifyResponse spotifyResponse;
}
