package ucab.ingsw.response;

import lombok.Data;
import lombok.ToString;
import ucab.ingsw.model.Media;


import java.util.List;

@Data
@ToString
public class AlbumResponse {
    private String id;
    private String name;
    private String description;
    private List<String> media;
}
