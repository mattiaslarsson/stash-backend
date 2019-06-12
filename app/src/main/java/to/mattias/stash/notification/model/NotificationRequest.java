package to.mattias.stash.notification.model;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import to.mattias.stash.model.ExpiringItem;

@Data
@Builder
public class NotificationRequest {

  private ExpiringItems data;
  private List<String> registration_ids;
  private Notification notification;

}
