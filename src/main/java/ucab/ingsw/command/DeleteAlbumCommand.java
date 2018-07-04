package ucab.ingsw.command;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.io.Serializable;

@ToString
@Data
public class DeleteAlbumCommand implements Serializable{

    @NotNull(message = "Se requiere id del album.")
    @NotEmpty(message = "Se requiere id del album.")
    private String albumId;

    @NotNull(message = "Se requiere contraseña.")
    @NotEmpty(message = "Se requiere contraseña.")
    private String password;

}
