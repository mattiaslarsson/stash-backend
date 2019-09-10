package to.mattias.stash;

import static io.restassured.RestAssured.delete;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.BeforeClass;
import to.mattias.stash.model.Article;
import to.mattias.stash.model.NotificationTarget;

public abstract class TestBase {

  private static final String HOST = "127.0.0.1";
  private static final int PORT = 8080;
  private static final ObjectMapper objectMapper = new ObjectMapper();

  @BeforeClass
  public static void init() {
    RestAssured.baseURI = "http://" + HOST;
    RestAssured.port = PORT;
  }

  @Before
  public void resetDb() throws IOException {
    Response responseBoxes = get("/stash");
    Map<Integer, Object> boxes = responseBoxes.getBody().as(Map.class);
    boxes.entrySet().forEach(entry -> {
      delete("/stash/" + entry.getKey());
    });

    String response = given()
        .header(ACCEPT, APPLICATION_JSON_VALUE)
        .get("/article")
        .getBody().print();

    List<Map<String, Object>> articles = objectMapper.readValue(response, List.class);

    for (Map<String, Object> a : articles) {
      given()
          .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
          .body(objectMapper.writeValueAsString(
              Article.builder()
                .ean((String) a.get("ean"))
                .description((String) a.get("description"))
              .build())
          )
          .delete("/article");
    }

    Response responseNotificationTarget = get("/notificationtarget");
    List<Map<String, Object>> targets = responseNotificationTarget.getBody().as(List.class);
    for (Map<String, Object> n : targets) {
      delete("/notificationtarget/" + n.get("notificationTarget"));
    }
  }

}
