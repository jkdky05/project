package ucab.ingsw.dataApis.youtubeData;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Snippet implements Serializable {
    private String  publishedAt;
    private String  channelId;
    private String  title;
    private String  description;
    private String  channelTitle;
}
