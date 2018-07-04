package ucab.ingsw.response;

import lombok.Data;
import lombok.ToString;


@Data
@ToString
public class UserProfileRelatedResponse {
    private Long id;
    private String firstName;
    private String lastName;     //OBJETO QUE SE ENVÍA COMO RESPUESTA DE LOS PROCESOS RELACIONADOS CON
    private String email;        //EL PERFIL DEL USUARIO
    private String password;
    private String dateOfBirth;
    private String instagramToken;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
