package ucab.ingsw.response;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class InstagramResponse {
    private List<String> urls;
}
