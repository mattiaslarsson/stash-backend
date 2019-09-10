package to.mattias.stash.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import to.mattias.stash.model.StashItem;

public interface StashItemRepository extends JpaRepository<StashItem, Integer> {

  List<StashItem> findAllByBox(int box);
  void deleteAllByBox(int box);
}
