package ucab.ingsw.dataApis.youtubeData;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;
import ucab.ingsw.dataApis.youtubeData.YoutubeDataUrls;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class YoutubeUrl implements Serializable {
    private List<YoutubeDataUrls> items;
}
