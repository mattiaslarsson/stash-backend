package to.mattias.stash.expiration.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "stash.expiryscheduler")
public class SchedulerConfig {

  private long rate;

}
