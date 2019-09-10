package to.mattias.stash;

import static io.restassured.RestAssured.get;
import static org.hamcrest.CoreMatchers.is;

import org.junit.Test;

public class PingTest extends TestBase {

  @Test
  public void pingTest() {
    get("/ping").then().assertThat().body(is("pong"));
  }

}
