package to.mattias.stash.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import to.mattias.stash.persistence.NotificationTargetRepository;

@RestController
public class NotificationController {

  private final NotificationTargetRepository repository;
  private final Logger LOGGER = LoggerFactory.getLogger(NotificationController.class);

  public NotificationController(NotificationTargetRepository repository) {
    this.repository = repository;
  }

  @PostMapping("/notificationtarget/{target}")
  public ResponseEntity registerNotificationTarget(@PathVariable("target") final String target) {
    LOGGER.info("Registering target: {}", target);
    repository.setNotificationTarget(target);

    return ResponseEntity.noContent().build();
  }

}
