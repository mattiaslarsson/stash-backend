package to.mattias.stash.notification;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import to.mattias.stash.model.ExpiringItem;
import to.mattias.stash.notification.model.ExpiringItems;
import to.mattias.stash.notification.model.Notification;
import to.mattias.stash.notification.model.NotificationRequest;
import to.mattias.stash.persistence.NotificationTargetRepository;

@Component
public class Notifier {

  private static final Logger LOGGER = LoggerFactory.getLogger(Notifier.class);
  private final RestTemplate restTemplate;
  private final NotificationTargetRepository repository;
  @Value("${stash.notification.url}")
  private String URL;
  @Value("${stash.notification.apikey}")
  private String API_KEY;

  public Notifier(RestTemplate restTemplate, NotificationTargetRepository repository) {
    this.restTemplate = restTemplate;
    this.repository = repository;
  }

  public void sendNotification(List<ExpiringItem> items) throws URISyntaxException {
    String target = repository.getNotificationTarget();

    LOGGER.info("Notifying {}", target);
    HttpHeaders headers = new HttpHeaders();
    headers.add("content-type", APPLICATION_JSON_VALUE);
    headers.add("Authorization", "key=" + API_KEY);

    NotificationRequest requestBody = NotificationRequest.builder()
        .registration_ids(Arrays.asList(target))
        .data(ExpiringItems.builder()
            .items(items)
            .build())
        .notification(Notification.builder()
            .body("Expiry")
            .build())
        .build();

    HttpEntity<NotificationRequest> request = new HttpEntity<>(requestBody, headers);

    LOGGER.info("" + request.getBody());
    LOGGER.info("" + request.getHeaders());

    restTemplate.exchange(new URI(URL), POST, request,
        Void.class);

  }
}
