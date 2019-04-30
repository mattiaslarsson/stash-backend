package to.mattias.stash.expiration;

import static java.time.temporal.ChronoUnit.DAYS;

import java.time.Instant;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import to.mattias.stash.exception.NoExpiringItemsException;
import to.mattias.stash.persistence.StashRepository;

@Component
public class ExpiryScheduler {

  private static final Logger LOGGER = LoggerFactory.getLogger(ExpiryScheduler.class);
  private final StashRepository repository;
  private int daysAhead;

  @Autowired
  public ExpiryScheduler(StashRepository repository,
      @Value("${stash.expiryscheduler.daysahead}") int daysAhead) {
    this.repository = repository;
    this.daysAhead = daysAhead;
  }

  @Scheduled(fixedRateString = "${stash.expiryscheduler.rate}")
  public void checkItemsForExpiration() {
    LOGGER.info("Checking for expirations");
    LOGGER.info("Days ahead: {}", daysAhead);
    Instant expiryTime = Instant.now().plus(daysAhead, DAYS);
    Date expiryDate = Date.from(expiryTime);
    try {
      repository.getExpiries(expiryDate)
          .orElseThrow(NoExpiringItemsException::new).forEach(
          item -> LOGGER.warn("Box number: {} contains: {} that expires in: {}", item.getBox(),
              item.getItem().getDescription(), item.getItem().getExpiration()));
    } catch (NoExpiringItemsException e) {
      return;
    }
  }
}
