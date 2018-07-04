package ucab.ingsw.command;

import java.io.Serializable;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.*;

@ToString
@Data
public class AlbumSignUpCommand implements Serializable {



        @NotNull(message = "Por favor, introduzca el nombre del album")
        @NotEmpty(message = "Por favor, introduzca el nombre del album")
        @Size(max = ValidationRules.FIRST_LAST_NAME_MAX_SIZE, message = "El nombre del album no puede contener más de 40 caracteres.")
        @Pattern(regexp = ValidationRules.FIRST_LAST_NAME_REGEX, message = "El nombre del album posee caracteres no válidos.")
        private String nombreAlbum;


    @Size(max = ValidationRules.NAME_MAX_SIZE, message = "La Descripcion no puede contener más de 40 caracteres.")
     private String descripcion;






    public String getNombreAlbum() {
        return nombreAlbum;
    }

    public void setNombreAlbum(String nombreAlbum) {
        this.nombreAlbum = nombreAlbum;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
