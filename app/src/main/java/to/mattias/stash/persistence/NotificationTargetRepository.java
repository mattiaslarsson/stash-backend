package to.mattias.stash.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import to.mattias.stash.model.NotificationTarget;

public interface NotificationTargetRepository extends JpaRepository<NotificationTarget, String> {

}
