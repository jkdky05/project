package ucab.ingsw.command;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ToString
@Data
public class FriendCommand {

    @NotNull(message = "Por favor, introduzca el id de su usuario.")
    @NotEmpty(message = "Por favor, introduzca el id de su usuario.")
    private String id;

    @NotNull(message = "Por favor, introduzca el id del futuro amigo.")
    @NotEmpty(message = "Por favor, introduzca el id del futuro amigo.")
    private String idFriend;

    @NotNull(message = "Por favor, introduzca la contraseña del usuario.")
    @NotEmpty(message = "Por favor, introduzca la contraseña del usuario.")
    private String contraseña;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdFriend() {
        return idFriend;
    }

    public void setIdFriend(String idFriend) {
        this.idFriend = idFriend;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }
}
