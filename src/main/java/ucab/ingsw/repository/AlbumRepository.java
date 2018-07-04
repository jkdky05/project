package ucab.ingsw.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ucab.ingsw.model.Album;

import java.util.Optional;

@Repository("AlbumRepository")

public interface AlbumRepository extends CrudRepository<Album, Long>{

    Optional<Album> findById(Long id);
}
