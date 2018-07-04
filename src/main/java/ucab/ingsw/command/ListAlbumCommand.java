package ucab.ingsw.command;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@ToString
@Data
public class ListAlbumCommand {


    @NotNull(message = "Por favor, introduzca la contraseña.")
    @NotEmpty(message = "Por favor, introduzca la contraseña.")
    private String password;
}
