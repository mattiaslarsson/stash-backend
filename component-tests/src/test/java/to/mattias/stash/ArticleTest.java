package to.mattias.stash;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.restassured.response.Response;
import org.junit.Test;
import to.mattias.stash.model.Article;

public class ArticleTest extends TestBase {

  @Test
  public void addAndGetArticle() {
    String ean = "1234";
    String description = "snus";

    String article = "{\"ean\":\"" + ean + "\","
        + "\"description\":\"" + description + "\"}";

    addArticle(article);

    Response response = get("/article/" + ean);
    assertThat(response.statusCode(), is(OK.value()));

    Article articleInDb = response.getBody().as(Article.class);
    assertThat(articleInDb.getEan(), is(ean));
    assertThat(articleInDb.getDescription(), is(description));
  }

  @Test
  public void addAndGetArticle_oneOfTwo() {
    String ean1 = "1234";
    String ean2 = "12345";
    String description1 = "snus";
    String description2 = "kaffe";

    String article1 = "{\"ean\":\"" + ean1 + "\","
        + "\"description\":\"" + description1 + "\"}";
    String article2 = "{\"ean\":\"" + ean2 + "\","
        + "\"description\":\"" + description2 + "\"}";

    addArticle(article1);
    addArticle(article2);

    Response response = get("/article/" + ean1);
    assertThat(response.statusCode(), is(OK.value()));

    Article actualArticle = response.getBody().as(Article.class);
    assertThat(actualArticle.getEan(), is(ean1));
    assertThat(actualArticle.getDescription(), is(description1));
  }

  private void addArticle(String article) {
    given()
        .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
        .body(article)
        .post("/article")
        .then()
        .assertThat().statusCode(is(NO_CONTENT.value()));
  }
}
