package ucab.ingsw.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ToString
public class VideoResponse {
    private String  videoUrl;
    private String  publishedAt;
    private String  channelUrl;
    private String  title;
    private String  description;
    private String  channelTitle;

}
