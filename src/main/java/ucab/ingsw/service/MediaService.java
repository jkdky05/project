package ucab.ingsw.service;




import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ucab.ingsw.command.DeleteMediaCommand;
import ucab.ingsw.model.Album;
import ucab.ingsw.model.User;
import ucab.ingsw.repository.UserRepository;
import ucab.ingsw.response.AlbumResponse;
import ucab.ingsw.response.NotifyResponse;
import ucab.ingsw.response.MediaResponse;
import ucab.ingsw.model.Media;
import ucab.ingsw.command.MediaSignUpCommand;
import ucab.ingsw.repository.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;




@Slf4j

@Service("MediaService")


public class MediaService {


        @Autowired
        private AlbumRepository albumRepository;

        @Autowired
        private MediaRepository mediaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


    @Autowired
    private AlbumService albumService;


        public ResponseEntity<Object> registerMedia(MediaSignUpCommand command, String id) {

            if (!albumRepository.existsById(Long.parseLong(id))) {
                log.info("EL ALBUM NO EXISTE ");

                return ResponseEntity.badRequest().body(buildNotifyResponse("EL ALBUM NO EXISTE"));
            } else {
                Album album = albumService.searchAlbumById(id);
                command.getUrl().forEach( j->{
                          Media media = new Media();
                          media.setId(System.currentTimeMillis());
                          media.setIdentificador(Long.parseLong(id));
                          media.setUrl(j);
                          mediaRepository.save(media);
                          album.getMedia().add(media.getId());
                        }
                );
                albumRepository.save(album);
                log.info("AGREGADA LISTA DE MEDIAS");
                return ResponseEntity.ok().body(buildNotifyResponse("LISTA DE MEDIAS AGREGADA"));
            }

        }



        private NotifyResponse buildNotifyResponse(String message) { //MUESTRA UN MENSAJE DE NOTIFICACIÓN
            NotifyResponse respuesta = new NotifyResponse();
            respuesta.setMessage(message);
            respuesta.setTimestamp(LocalDateTime.now());
            return respuesta;
        }

    public ResponseEntity<Object> MediaList(String id){
        Album album = albumService.searchAlbumById(id);
        if (album==null) {
            log.info("NO EXISTE EL ALBUM CON ID={}", id);
            return ResponseEntity.badRequest().body("NO EXISTE EL ALBUM");
        }
        else {
            List<MediaResponse> albumList = createMediaList(album);
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


    public List<MediaResponse> createMediaList(Album album){
        List<MediaResponse> mediaList = new ArrayList<>();
        List<Long> mediaIdList = album.getMedia();
        mediaRepository.findAll().forEach(it->{
            if(mediaIdList.stream().anyMatch(item -> item == it.getId())){
                MediaResponse mediaResponse = new MediaResponse();
                mediaResponse.setId(String.valueOf(it.getId()));
               mediaResponse.setUrl(it.getUrl());
               mediaList.add(mediaResponse);
            }
        });
        return mediaList;
    }


    public ResponseEntity<Object> deleteMedia(DeleteMediaCommand command, String id){
        Album album = albumService.searchAlbumById(id);
        Media media2 = searchMediaById(command.getMediaId());
        if(album==null || media2==null || !album.getMedia().contains(media2) ){
            return ResponseEntity.badRequest().body(buildNotifyResponse("CREDENCIALES INVÁLIDAS"));
        }
        else{
            boolean success = album.getMedia().remove(Long.parseLong(command.getMediaId()));
            if(success){
                log.info("MEDIA ={} ELIMINADA", command.getMediaId());
                albumRepository.save(album);
                mediaRepository.deleteById(Long.parseLong(command.getMediaId()));
                return ResponseEntity.ok().body("PROCESO COMPLETADO CON ÉXITO");
            }
            else{
                log.error("ALBUM NO PUDO SER ELIMINADO");
                return ResponseEntity.badRequest().body(buildNotifyResponse("ALBUM NO PUDO SER ELIMINADO"));
            }
        }
    }

    public Media searchMediaById(String id) {
        try {
            if(mediaRepository.findById(Long.parseLong(id)).isPresent()){
                return mediaRepository.findById(Long.parseLong(id)).get();
            }
            else
                return null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    }


