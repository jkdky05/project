package ucab.ingsw.dataApis.spotifyData;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpotifyTracks implements Serializable {
    private List<SpotifyItem> items;
}
