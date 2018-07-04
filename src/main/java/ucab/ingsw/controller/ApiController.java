package ucab.ingsw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import ucab.ingsw.service.ApisService;

import java.util.ArrayList;
import java.util.List;


@CrossOrigin
@RestController
@RequestMapping(value = "/search", produces = "application/json")
public class ApiController {


    @Autowired
    private ApisService apisService;



    @RequestMapping(value = "/apis", method = RequestMethod.GET)
    public ResponseEntity search( @RequestParam("query") String query, @RequestParam("id") String id) {
                query = query.replace(" ", "");
                return apisService.searchApiContent(query, id);
    }
}
