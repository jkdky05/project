package ucab.ingsw.command;

import java.io.Serializable;
import lombok.Data;
import lombok.ToString;
import java.util.*;

import javax.validation.constraints.*;

@ToString
@Data

public class MediaSignUpCommand implements Serializable {



    @NotNull(message = "POR FAVOR, INTRODUZCA LA LISTA DE URLS")
    @NotEmpty(message = "POR FAVOR, INTRODUZCA LA LISTA DE URLS")
    private List<String> url;


}
