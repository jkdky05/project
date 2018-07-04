package ucab.ingsw.dataApis.instagramData;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Images implements Serializable {
    private Resolution standard_resolution;
}
