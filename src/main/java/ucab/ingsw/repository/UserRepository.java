package ucab.ingsw.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ucab.ingsw.model.User;

import java.util.List;
import java.util.Optional;

@Repository("UserRepository")
public interface UserRepository extends CrudRepository<User, Long>{

    User findByEmail(String email);
    boolean  existsByEmail(String email);
    User findFirstByEmailIgnoreCaseContaining(String partialEmailAddress);

    List<User> findByFirstNameIgnoreCaseContaining(String partialFirstName);

    Optional<User> findById(Long id);

}
