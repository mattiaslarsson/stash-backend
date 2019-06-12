package to.mattias.stash.expiration;

import static java.time.temporal.ChronoUnit.DAYS;

import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import to.mattias.stash.exception.NoExpiringItemsException;
import to.mattias.stash.exception.NotificationTargetNotSetException;
import to.mattias.stash.model.ExpiringItem;
import to.mattias.stash.notification.Notifier;
import to.mattias.stash.persistence.StashRepository;

@Component
public class ExpiryScheduler {

  private static final Logger LOGGER = LoggerFactory.getLogger(ExpiryScheduler.class);
  private final StashRepository repository;
  private final Notifier notifier;
  private int daysAhead;

  @Autowired
  public ExpiryScheduler(StashRepository repository,
      @Value("${stash.expiryscheduler.daysahead}") int daysAhead, Notifier notifier) {
    this.repository = repository;
    this.daysAhead = daysAhead;
    this.notifier = notifier;
  }

  @Scheduled(fixedRateString = "${stash.expiryscheduler.rate}")
  public void checkItemsForExpiration() {
    LOGGER.info("Checking for expirations");
    LOGGER.info("Days ahead: {}", daysAhead);
    Instant expiryTime = Instant.now().plus(daysAhead, DAYS);
    Date expiryDate = Date.from(expiryTime);
    try {
      List<ExpiringItem> items = repository.getExpiries(expiryDate)
          .orElseThrow(NoExpiringItemsException::new);

      notifier.sendNotification(items);

      items.forEach(
          item -> LOGGER.warn("Box number: {} contains: {} that expires in: {}", item.getBox(),
              item.getItem().getDescription(), item.getItem().getExpiration()));
    } catch (NoExpiringItemsException e) {
    } catch (NotificationTargetNotSetException e) {
      LOGGER.warn("Couldn't send push notification due to no target");
    } catch (URISyntaxException e) {
      LOGGER.error("Malformed URL");
    }
  }
}
