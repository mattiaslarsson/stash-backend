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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import to.mattias.stash.exception.NotificationTargetNotSetException;
import to.mattias.stash.model.ExpiringItem;
import to.mattias.stash.notification.model.ExpiringItems;
import to.mattias.stash.notification.model.Notification;
import to.mattias.stash.notification.model.NotificationRequest;
import to.mattias.stash.persistence.NotificationTargetRepository;

@Component
public class Notifier {

  private static final Logger LOGGER = LoggerFactory.getLogger(Notifier.class);
  private static String URL = "https://fcm.googleapis.com/fcm/send";
  private static String API_KEY = "AAAA1AyPMw4:APA91bHBU7H7tnYFkHSXT4akP8n-uCJRSYW8h_SkakpfEl3WlFYDvC6jdqQXl5oj2530vymO_PyQ9t0z0tZv-et4OJarhPRiSpSjpn0gRBA8almUE-JKR8yU64EHukKcm9ge3fOjYFXDPccn7Yuhe3qOs7k7slb4tg";
  private final RestTemplate restTemplate;
  private final NotificationTargetRepository repository;


  public Notifier(RestTemplate restTemplate, NotificationTargetRepository repository) {
    this.restTemplate = restTemplate;
    this.repository = repository;
  }

  public void sendNotification(List<ExpiringItem> items)
      throws NotificationTargetNotSetException, URISyntaxException {
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

  private JSONObject createJson(NotificationRequest request) {
    return new JSONObject(request);
  }


}
