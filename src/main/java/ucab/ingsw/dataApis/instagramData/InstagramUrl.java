package ucab.ingsw.dataApis.instagramData;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.ToString;
import lombok.Data;
import ucab.ingsw.dataApis.instagramData.InstagramDataUrls;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class InstagramUrl implements Serializable {
    private List<InstagramDataUrls> data;
}
