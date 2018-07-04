package ucab.ingsw.command;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@ToString
@Data
public class DeleteMediaCommand implements Serializable {

    @NotNull(message = "Por favor, introduzca el id de media.")
    @NotEmpty(message = "Por favor, introduzca el id de media.")
    private String mediaId;

    @NotNull(message = "Por favor, introduzca la contraseña.")
    @NotEmpty(message = "Por favor, introduzca la contraseña.")
    private String password;
}
