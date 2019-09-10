package to.mattias.stash;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import org.junit.Test;

public class NotificationTargetTest extends TestBase {

  @Test
  public void addNotificationTarget() {

    String notificationTarget = "abc123";

    String request = "{\"notificationTarget\":\"" + notificationTarget + "\"}";

    postTarget(notificationTarget);
  }

  private void postTarget(String target) {
    given()
        .post("/notificationtarget/" + target)
        .then()
        .assertThat().statusCode(is(NO_CONTENT.value()));
  }

}
