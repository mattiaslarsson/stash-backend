package to.mattias.stash.persistence;

import org.springframework.stereotype.Component;

@Component
public class NotificationTargetRepository {

  private static String notificationTarget;

  public String getNotificationTarget() {
    return notificationTarget;
  }

  public void setNotificationTarget(String target) {
    notificationTarget = target;
  }

}
