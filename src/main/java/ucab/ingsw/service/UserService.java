package ucab.ingsw.service;

import org.bson.internal.Base64;
import ucab.ingsw.command.FriendCommand;
import ucab.ingsw.command.UserSignUpCommand;
import ucab.ingsw.command.UserLoginCommand;
import ucab.ingsw.command.UserDeleteCommand;
import ucab.ingsw.command.UserChangingAttributesCommand;
import ucab.ingsw.model.*;
import ucab.ingsw.response.UserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ucab.ingsw.response.NotifyResponse;
import ucab.ingsw.repository.*;
import java.util.ArrayList;

import java.time.LocalDateTime;
import java.util.Random;

import java.util.List;

@Slf4j

@Service("UserService")
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private AlbumService albumService;

    @Autowired
    private MediaService mediaService;



    public String getRandomImage() {
         Random random = new Random();
         int image = random.nextInt(99999999);
         String imgUrl = "https://www.gravatar.com/avatar/"+String.valueOf(image)+"?s=450&d=identicon&r=PG";
    return imgUrl;
}



    //-----------------------------------------------------------------------------------------------------------
    //SERVICIO PARA ACCEDER A LA RED SOCIAL
    public ResponseEntity<Object> login(UserLoginCommand command) {
        User u = userRepository.findFirstByEmailIgnoreCaseContaining(command.getEmail());
        if (u == null) {
             log.info("NO SE PUEDE HALLAR AL USUARIO CON LA DIRECCIÓN DE CORREO");

            return ResponseEntity.badRequest().body(buildNotifyResponse("DIRECCIÓN DE CORREO NO VÁLIDA"));
        } else {
            if (u.getPassword().equals(command.getPassword())) {
                  log.info("LOGIN EXITOSO");

                UserResponse respuesta = new UserResponse();
                respuesta.setFirstName(u.getFirstName());
                respuesta.setLastName(u.getLastName());
                respuesta.setEmail(u.getEmail());
                respuesta.setId(String.valueOf(u.getId()));
                respuesta.setInstagramToken(u.getInstagramToken());
                respuesta.setDateOfBirth(u.getDateOfBirth());

                return ResponseEntity.ok(respuesta);
            } else {
                log.info("CONTRASEÑA NO ES CORRECTA");

                return ResponseEntity.badRequest().body(buildNotifyResponse("CONTRASEÑA NO VÁLIDA"));
            }
        }

    }


//-----------------------------------------------------------------------------------------------------------
    //SERVICIO PARA REGISTRAR USUARIO

    public ResponseEntity<Object> register(UserSignUpCommand command) {

        if (userRepository.existsByEmail(command.getEmail())) {
            log.info("LA DIRECCIÓN YA ESTÁ SIENDO USADA POR OTRO USUARIO", command.getEmail());

            return ResponseEntity.badRequest().body(buildNotifyResponse("EL USUARIO YA SE ENCUENTRA REGISTRADO EN EL SISTEMA"));
        } else {
            if (!command.getPassword().equals(command.getConfirmationPassword())) {
                    log.info("LAS CONTRASEÑAS NO SON IGUALES");
                return ResponseEntity.badRequest().body(buildNotifyResponse("LAS CONTRASEÑAS NO SON IGUALES"));
            } else {
                User user = new User();


                user.setId(System.currentTimeMillis());
                user.setFirstName(command.getFirstName());
                user.setLastName(command.getLastName());
                user.setEmail(command.getEmail());
                user.setPassword(command.getPassword());
                if (verifyUserBirthDate(command.getDateOfBirth())) user.setDateOfBirth(command.getDateOfBirth());
                else return ResponseEntity.badRequest().body(buildNotifyResponse("LA FECHA DE NACIMIENTO NO ES VÁLIDA"));
                user.setProfilePicture(getRandomImage());
                user.setInstagramToken(command.getTokenInstagram());
                user.setAlbums(null);
                user.setFriends(null);
                userRepository.save(user);

                  log.info("REGISTRADO USUARIO CON ID={}", user.getId());

                return ResponseEntity.ok().body(buildNotifyResponse("USUARIO REGISTRADO"));
            }
        }
    }

    //-----------------------------------------------------------------------------------------------------------

    //SERVICIO PARA ACTUALIZAR USUARIO

    public ResponseEntity<Object> update(UserChangingAttributesCommand command, String id) {
        if (!userRepository.existsById(Long.parseLong(id))) {
            log.info("NO SE PUDO HALLAR AL USUARIO CON ID={}", id);
            return ResponseEntity.badRequest().body(buildNotifyResponse("EL ID ES INVÁLIDO"));
        } else {
            User user = searchUserById(id);
            user.setFirstName(command.getFirstName());
            user.setLastName(command.getLastName());
            user.setEmail(command.getEmail());
            user.setPassword(command.getPassword());
            user.setDateOfBirth(command.getDateOfBirth());
            user.setProfilePicture(getRandomImage());
            user.setInstagramToken(command.getTokenInstagram());

            userRepository.save(user);
            log.info("ACTUALIZADO USUARIO CON ID={}", user.getId());
            return ResponseEntity.ok().body(buildNotifyResponse("LA OPERACIÓN FUE EXITOSA"));
        }
    }




    //-----------------------------------------------------------------------------------------------------------

    public List<User> searchByName(String name){
        List<User> u = userRepository.findByFirstNameIgnoreCaseContaining(name);
        if(u==null){
            log.info("NO SE HALLARON USUARIOS CON EL NOMBRE : ",name);
        }else
        log.info("NÚMERO DE USUARIOS HALLADOS={}", u.size(), name);
        return u;
    }

    //-----------------------------------------------------------------------------------------------------------
    //SERVICIO PARA ELIMINAR USUARIO
    public ResponseEntity<Object> delete(UserDeleteCommand command, String id) {
        if (userRepository.existsById(Long.parseLong(id))) {
           User u = searchUserById(id);
           if (command.getPassword().equals(u.getPassword())){
               u.getAlbums().forEach(i -> {
                   Album album = albumService.searchAlbumById(String.valueOf(i));
                    album.getMedia().forEach(j -> {
                       mediaRepository.deleteById(j);
                    });
                   albumRepository.deleteById(i);
               });
               u.getFriends().forEach(h -> {
                   User friend = searchUserById(String.valueOf(h));
                   friend.getFriends().remove(u.getId());
                   userRepository.save(friend);
               });
            userRepository.deleteById(Long.parseLong(id));
            return  ResponseEntity.ok().body(buildNotifyResponse("LA OPERACIÓN HA SIDO EXITOSA"));}
            else{
               return  ResponseEntity.ok().body(buildNotifyResponse("LAS CONTRASEÑAS NO SON IGUALES"));
        }

    } else {
            log.info("NO SE PUDO ENCONTRAR AL USUARIO");

            return ResponseEntity.badRequest().body(buildNotifyResponse("USUARIO NO ENCONTRADO"));
            }
        }

    //-----------------------------------------------------------------------------------------------------------

    private boolean verifyUserBirthDate (String string){
        try {
            String[] birthDate = string.split("/");
            int day = Integer.parseInt(birthDate[0]);
            int mon = Integer.parseInt(birthDate[1]);
            int year = Integer.parseInt(birthDate[2]);
            if (!((day > 0 && day < 32)&& (mon > 0 && mon < 13) && (year > 0) )) return false;
            LocalDateTime date = LocalDateTime.now();
            if (date.getYear()-10 > year) return true;
            else if (date.getYear()-10 == year) {
                if (date.getDayOfMonth() > day) return true;
                else if (date.getDayOfMonth() == day) return false;
                else return false;
            } else return false;
        }catch(NumberFormatException e){
            return false;
        }
    }

    //-----------------------------------------------------------------------------------------------------------

    private NotifyResponse buildNotifyResponse(String message) { //MUESTRA UN MENSAJE DE NOTIFICACIÓN
        NotifyResponse respuesta = new NotifyResponse();
        respuesta.setMessage(message);
        respuesta.setTimestamp(LocalDateTime.now());
        return respuesta;
    }
    //-----------------------------------------------------------------------------------------------------------
    //SERVICIO PARA BUSCAR USUARIO POR ID

    public User searchUserById(String id) {
        try {
            if(userRepository.findById(Long.parseLong(id)).isPresent()){
                return userRepository.findById(Long.parseLong(id)).get();
            }
            else
                return null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
    //-----------------------------------------------------------------------------------------------------------
    public Long searchUserById2(String id) {
        try {
            if(userRepository.findById(Long.parseLong(id)).isPresent()){
                return userRepository.findById(Long.parseLong(id)).get().getId();
            }
            else
                return null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    //-----------------------------------------------------------------------------------------------------------
    //SERVICIO PARA AGREGAR AMIGO
    public ResponseEntity<Object> addFriend(FriendCommand command) {
        if (!userRepository.existsById(Long.parseLong(command.getId())) || !userRepository.existsById(Long.parseLong(command.getIdFriend()))) {
            log.info("NO SE HA PODIDO HALLA USUARIO");
            return ResponseEntity.badRequest().body(buildNotifyResponse("ID NO VÁLIDO"));
        } else {
            User user1= searchUserById(command.getIdFriend());
           User user0 = searchUserById(command.getId());
           if (user0.getFriends().contains(user1.getId())) return  ResponseEntity.badRequest().body(buildNotifyResponse("LOS USUARIOS YA SON AMIGOS"));
            if (user0.equals(user1)) return ResponseEntity.badRequest().body(buildNotifyResponse("EL USUARIO NO PUEDE SER AMIGO DE SÍ MISMO"));
           if (user0.getPassword().equals(command.getContraseña())){
            User user = searchUserById(command.getId());
            User friend = searchUserById(command.getIdFriend());
            user.getFriends().add(Long.parseLong(command.getIdFriend()));
            friend.getFriends().add(Long.parseLong(command.getId()));
         userRepository.save(user);
         userRepository.save(friend);
            log.info("ACTUALIZADO USUARIO CON ID={}", user.getId());
            return ResponseEntity.ok().body(buildNotifyResponse("LA OPERACIÓN HA SIDO EXITOSA"));}
            else {
               return ResponseEntity.badRequest().body(buildNotifyResponse("LA CONTRASEÑA NO PERTENECE AL USUARIO"));
        }

        }
    }

    public ResponseEntity<Object> getFriendsList(String id){
        User user = searchUserById(id);
        if (!(userRepository.existsById(Long.parseLong(id)))) {
            log.info("NO SE HA PODIDO HALLAR AL USUARIO CON EL ID:", id);
            return ResponseEntity.badRequest().body(buildNotifyResponse("ID NO VÁLIDO."));
        }
        else{
            List<UserResponse> friendList = createFriendList(user);
            if(friendList.isEmpty()){
                log.info("LA LISTA DE AMIGOS DEL USUARIO SE ENCUENTRA VACÍA.");
                return ResponseEntity.ok().body(buildNotifyResponse("LA LISTA NO POSEE AMIGOS."));
            }
            else{
                log.info("RETORNANDO LA LISTA DE AMIGOS PARA EL USUARIO CON ID={}", id);
                return ResponseEntity.ok(friendList);
            }
        }
    }

    public List<UserResponse> createFriendList(User user) {
        List<UserResponse> friendList = new ArrayList<>();
        List<Long> friendIdList = user.getFriends();
        userRepository.findAll().forEach(it->{
            if(friendIdList.stream().anyMatch(item -> item == it.getId())){
                UserResponse normalResponse = new UserResponse();
                normalResponse.setId(String.valueOf(it.getId()));
                normalResponse.setFirstName(it.getFirstName());
                normalResponse.setLastName(it.getLastName());
                normalResponse.setEmail(it.getEmail());
                normalResponse.setPassword(it.getPassword());
                normalResponse.setDateOfBirth(it.getDateOfBirth());
                normalResponse.setInstagramToken(it.getInstagramToken());
                normalResponse.setProfilePicture(it.getProfilePicture());
                List<String> albumes = new ArrayList<>();
                it.getAlbums().forEach( j->{
                            albumes.add(j.toString());
                        }
                );
                List<String> friends = new ArrayList<>();
                it.getFriends().forEach( j->{
                            friends.add(j.toString());
                        }
                );
                normalResponse.setFriends(friends);
                normalResponse.setAlbums(albumes);
                friendList.add(normalResponse);
            }
        });
        return friendList;
    }

    public ResponseEntity<Object> deleteFriend(FriendCommand command){
        User user = searchUserById(command.getId());
        
        if(!(command.getContrasena().equalsIgnoreCase(user.getPassword()))){
            return ResponseEntity.badRequest().body(buildNotifyResponse("contrasena del usuario no es la correcta"));
        }
        if(user==null) {
            return ResponseEntity.badRequest().body(buildNotifyResponse("USUARIO NO EXISTE"));
        }
        else{
            List<Long> friends = user.getFriends();
            Long friendId = Long.parseLong(command.getIdFriend());
            boolean success = friends.remove(friendId);
            if (success) {
                log.info("AMIGO REMOVIDO");
                user.setFriends(friends);
                userRepository.save(user);
                return ResponseEntity.ok().body("AMIGO REMOVIDO");
            }
            else {
                log.error("ERROR");
                return ResponseEntity.badRequest().body(buildNotifyResponse("AMIGO NO PUDO SER REMOVIDO"));
            }
        }
    }
}



