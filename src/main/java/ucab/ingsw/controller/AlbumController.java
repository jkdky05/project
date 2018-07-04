package ucab.ingsw.controller;

import ucab.ingsw.command.AlbumSignUpCommand;
import ucab.ingsw.service.AlbumService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ucab.ingsw.command.*;

import javax.validation.Valid;

@Slf4j

@CrossOrigin
@RestController
@RequestMapping(value = "/album", produces = "application/json")

public class AlbumController {

    @Autowired
    private AlbumService albumService;



    @RequestMapping(value = "/register/{id}", consumes = "application/json", method = RequestMethod.POST)
    public ResponseEntity register(@Valid @RequestBody AlbumSignUpCommand command, @PathVariable("id") String id) {
        return albumService.registerAlbum(command,id);
    }

    @RequestMapping(value = "/delete/{id}", consumes = "application/json", method = RequestMethod.POST)
    public ResponseEntity delete(@Valid @RequestBody DeleteAlbumCommand command, @PathVariable("id") String id) {
        return albumService.deleteAlbum(command,id);
    }

    @RequestMapping(value = "/list/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> list(@PathVariable("id") String id) {
        return albumService.AlbumList(id);
    }
}
