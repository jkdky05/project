package ucab.ingsw.dataApis.instagramData;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;
import ucab.ingsw.dataApis.instagramData.Images;

import java.io.Serializable;

@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class InstagramDataUrls implements Serializable {
    private Images images;
}
