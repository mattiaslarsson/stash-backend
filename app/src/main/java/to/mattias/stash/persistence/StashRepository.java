package to.mattias.stash.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import to.mattias.stash.model.Box;

public interface StashRepository extends JpaRepository<Box, Integer> {

}
