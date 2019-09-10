package to.mattias.stash.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import to.mattias.stash.model.NotificationTarget;
import to.mattias.stash.persistence.NotificationTargetRepository;

@RestController()
public class NotificationController {

  private final NotificationTargetRepository repository;
  private final Logger LOGGER = LoggerFactory.getLogger(NotificationController.class);

  public NotificationController(NotificationTargetRepository repository) {
    this.repository = repository;
  }

  @GetMapping(path = "/notificationtarget", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<List<NotificationTarget>> getAllNotificationTargets() {
    return ResponseEntity.ok(repository.findAll());
  }

  @PostMapping("/notificationtarget/{target}")
  public ResponseEntity<Void> registerNotificationTarget(
      @PathVariable("target") final String target) {
    LOGGER.info("Registering target: {}", target);
    NotificationTarget notificationTarget = new NotificationTarget(target);
    repository.save(notificationTarget);

    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/notificationtarget/{target}")
  public ResponseEntity<Void> deleteNotificationTarget(@PathVariable("target") String target) {

    repository.deleteById(target);

    return ResponseEntity.noContent().build();
  }

}
