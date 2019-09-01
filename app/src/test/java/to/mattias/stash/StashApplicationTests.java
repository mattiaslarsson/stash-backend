package to.mattias.stash;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
public class StashApplicationTests {

  @BeforeClass
  public static void setup() {
    System.setProperty("SPRING_DATASOURCE_URL", "jdbc:localhost");
    System.setProperty("SPRING_DATASOURCE_USERNAME", "mattias");
    System.setProperty("SPRING_DATASOURCE_PASSWORD", "larsson");
  }

  @Test
  public void contextLoads() {
  }

}
