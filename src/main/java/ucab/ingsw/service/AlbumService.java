package ucab.ingsw.service;

import ucab.ingsw.command.AlbumSignUpCommand;
import ucab.ingsw.model.Album;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ucab.ingsw.model.User;
import ucab.ingsw.repository.AlbumRepository;
import ucab.ingsw.response.NotifyResponse;
import ucab.ingsw.repository.UserRepository;
import ucab.ingsw.response.AlbumResponse;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import ucab.ingsw.command.DeleteAlbumCommand;
import ucab.ingsw.repository.MediaRepository;

@Slf4j

@Service("AlbumService")


public class AlbumService {

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


    public NotifyResponse buildNotifyResponse(String message) {
        NotifyResponse respuesta = new NotifyResponse();
        respuesta.setMessage(message);
        respuesta.setTimestamp(LocalDateTime.now());
        return respuesta;
    }

    public ResponseEntity<Object> registerAlbum(AlbumSignUpCommand command, String id) { //SE ENCARGA DE REGISTRAR TODOS LOS Albunes
        if (!userRepository.existsById(Long.parseLong(id))) {
            log.info("ID NO RECONOCIDA");

            return ResponseEntity.badRequest().body(buildNotifyResponse("ID NO RECONOCIDA"));
        } else  {

            User u = userService.searchUserById(id);
            Album album = new Album();

            album.setId(System.currentTimeMillis());
            album.setIdentificador(Long.parseLong(id));
            album.setNombreAlbum(command.getNombreAlbum());
            album.setDescripcion(command.getDescripcion());
            u.getAlbums().add(album.getId());
            album.setMedia(null);
            albumRepository.save(album);
            userRepository.save(u);

            log.info("AGREGADO ALBUM CON ID={}", album.getId());

            return ResponseEntity.ok().body(buildNotifyResponse("EL ALBUM HA SIDO AGREGADO"));

        }
    }

    public ResponseEntity<Object> AlbumList(String id){
        User user = userService.searchUserById(id);
        if (user==null) {
            log.info("NO EXISTE EL USUARIO CON ID={}", id);
            return ResponseEntity.badRequest().body("NO EXISTE EL USUARIO");
        }
        else {
            List<AlbumResponse> albumList = createAlbumList(user);
            if(albumList.isEmpty()){
                log.info("LA LISTA DEL USUARIO SE ENCUENTRA VACÍA");
                return ResponseEntity.ok().body("LA LISTA SE ENCUENTRA VACÍA");
            }
            else {
                log.info("LISTA HALLADA");
                return ResponseEntity.ok(albumList);
            }
        }
    }


    public List<AlbumResponse> createAlbumList(User user){
        List<AlbumResponse> albumList = new ArrayList<>();
        List<Long> albumIdList = user.getAlbums();
        albumRepository.findAll().forEach(it->{
            if(albumIdList.stream().anyMatch(item -> item == it.getId())){
                AlbumResponse albumResponse = new AlbumResponse();
                albumResponse.setId(String.valueOf(it.getId()));
                albumResponse.setName(it.getNombreAlbum());
                albumResponse.setDescription(it.getDescripcion());
                List<String> media = new ArrayList<>();
                it.getMedia().forEach( j->{
                           media.add(j.toString());
                        }
                );
                albumResponse.setMedia(media);
                albumList.add(albumResponse);
            }
        });
        return albumList;
    }


    public ResponseEntity<Object> deleteAlbum(DeleteAlbumCommand command, String id){
        User user = userService.searchUserById(id);
        Album album2 = searchAlbumById(command.getAlbumId());
        if(user==null || album2==null || !user.getAlbums().contains(album2) ){
            return ResponseEntity.badRequest().body(buildNotifyResponse("CREDENCIALES INVÁLIDAS"));
        }
        else{
            Album album = albumRepository.findById(Long.parseLong(command.getAlbumId())).get();
            album.getMedia().forEach(it->{
                mediaRepository.deleteById(it);
            });
            boolean success = user.getAlbums().remove(Long.parseLong(command.getAlbumId()));
            if(success){
                log.info("ALBUM ={} ELIMINADO", command.getAlbumId());

                userRepository.save(user);
                albumRepository.deleteById(Long.parseLong(command.getAlbumId()));
                return ResponseEntity.ok().body("PROCESO COMPLETADO CON ÉXITO");
            }
            else{
                log.error("ALBUM NO PUDO SER ELIMINADO");
                return ResponseEntity.badRequest().body(buildNotifyResponse("ALBUM NO PUDO SER ELIMINADO"));
            }
        }
    }

    public Album searchAlbumById(String id) {
        try {
            if(albumRepository.findById(Long.parseLong(id)).isPresent()){
                return albumRepository.findById(Long.parseLong(id)).get();
            }
            else
                return null;
        } catch (NumberFormatException e) {
            return null;
        }
    }


}
