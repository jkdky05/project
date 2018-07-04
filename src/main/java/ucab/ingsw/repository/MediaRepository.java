package ucab.ingsw.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ucab.ingsw.model.Media;

@Repository("DataRepository")

public interface MediaRepository extends CrudRepository<Media, Long>{

}
