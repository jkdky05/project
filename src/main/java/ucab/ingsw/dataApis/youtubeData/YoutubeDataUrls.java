package ucab.ingsw.dataApis.youtubeData;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class YoutubeDataUrls implements Serializable {
    private Id id;
    private Snippet snippet;
}
