package ucab.ingsw.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ToString
public class SongResponse {
    private String name;
    private List<String> artists;
    private String album;
    private String albumImageUrl;
    private String url;

}
