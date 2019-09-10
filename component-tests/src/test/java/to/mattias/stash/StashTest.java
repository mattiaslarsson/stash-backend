package to.mattias.stash;

import static io.restassured.RestAssured.delete;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import to.mattias.stash.model.StashItem;

public class StashTest extends TestBase {

  @Test
  public void postItem_boxDontExist() {
    String item = "{\"ean\":\"1234\",\"description\":\"snus\","
        + "\"expiration\":\"2019-06-24\"}";
    int boxNumber = 1;

    addItem(item, boxNumber);

    Response allBoxesResponse = getAllBoxes();

    assertThat(allBoxesResponse.statusCode(), is(OK.value()));
    Map<Integer, Object> boxes = allBoxesResponse.getBody().as(Map.class);

    assertThat(boxes.entrySet(), hasSize(1));
    assertThat(allBoxesResponse.jsonPath().get("1"), hasSize(1));
  }

  @Test
  public void postItemToExistingBox() {
    String item1 = "{\"ean\":\"1234\",\"description\":\"snus\","
        + "\"expiration\":\"2019-06-24\"}";
    String item2 = "{\"ean\":\"12345\",\"description\":\"sylt\","
        + "\"expiration\":\"2019-06-24\"}";

    int boxNumber = 1;

    addItem(item1, boxNumber);
    addItem(item2, boxNumber);

    Response allBoxesResponse = getAllBoxes();

    assertThat(allBoxesResponse.statusCode(), is(OK.value()));
    Map<Integer, Object> boxes = allBoxesResponse.getBody().as(Map.class);

    assertThat(boxes.entrySet(), hasSize(1));
    assertThat(allBoxesResponse.jsonPath().get("1"), hasSize(2));
  }

  @Test
  public void postItemToNewBox() {
    String item1 = "{\"ean\":\"1234\",\"description\":\"snus\","
        + "\"expiration\":\"2019-06-24\"}";
    String item2 = "{\"ean\":\"1234\",\"description\":\"snus\","
        + "\"expiration\":\"2019-06-24\"}";

    int box1 = 1;
    int box2 = 2;

    addItem(item1, box1);
    addItem(item2, box2);

    Response allBoxesResponse = getAllBoxes();

    assertThat(allBoxesResponse.statusCode(), is(OK.value()));
    Map<Integer, Object> boxes = allBoxesResponse.getBody().as(Map.class);

    assertThat(boxes.entrySet(), hasSize(2));
    assertThat(allBoxesResponse.jsonPath().get("1"), hasSize(1));
    assertThat(allBoxesResponse.jsonPath().get("2"), hasSize(1));

  }

  @Test
  public void deleteBox() {
    String item = "{\"ean\":\"1234\",\"description\":\"snus\","
        + "\"expiration\":\"2019-06-24\"}";
    int box = 1;

    addItem(item, box);

    delete("/stash/" + box)
        .then()
        .assertThat().statusCode(is(NO_CONTENT.value()));

    Response response = getAllBoxes();
    Map<Integer, Object> boxes = response.getBody().as(Map.class);
    assertThat(boxes.entrySet(), hasSize(0));
  }

  @Test
  public void deleteOneOfTwoBoxes() {
    String item1 = "{\"ean\":\"1234\",\"description\":\"snus\","
        + "\"expiration\":\"2019-06-24\"}";
    String item2 = "{\"ean\":\"1234\",\"description\":\"kaffe\","
        + "\"expiration\":\"2019-06-24\"}";

    int box1 = 1;
    int box2 = 2;

    addItem(item1, box1);
    addItem(item2, box2);

    delete("/stash/" + box1)
        .then()
        .assertThat().statusCode(is(NO_CONTENT.value()));

    Response response = getAllBoxes();
    Map<Integer, Object> boxes = response.getBody().as(Map.class);
    assertThat(boxes.entrySet(), hasSize(1));
    assertThat(response.jsonPath().get("2"), hasSize(1));
  }

  @Test
  public void deleteItemInBox() {
    String item1 = "{\"ean\":\"1234\",\"description\":\"snus\","
        + "\"expiration\":\"2019-06-24\"}";
    String item2 = "{\"ean\":\"1234\",\"description\":\"kaffe\","
        + "\"expiration\":\"2019-06-24\"}";

    int box = 1;

    addItem(item1, box);
    addItem(item2, box);

    Response response = getAllBoxes();
    assertThat(response.statusCode(), is(OK.value()));
    Map<String, Object> boxes = response.getBody().as(Map.class);
    assertThat(boxes.entrySet(), hasSize(1));

    response.body().prettyPrint();

    List<StashItem> itemsInBox = (ArrayList) boxes.get("1");
    assertThat(itemsInBox, hasSize(2));

    given()
        .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
        .body(itemsInBox.get(0))
        .delete("/stash/item")
        .then()
        .assertThat().statusCode(is(NO_CONTENT.value()));

    Response responseAfterDelete = getAllBoxes();
    Map<String, Object> boxesAfterDelete = responseAfterDelete.getBody().as(Map.class);
    List<Map<String, Object>> itemsInBoxAfterDelete = (ArrayList) boxesAfterDelete.get("1");

    assertThat(itemsInBoxAfterDelete, hasSize(1));
    Map<String, Object> item = itemsInBoxAfterDelete.get(0);
    assertThat(item.get("description"), is("kaffe"));
  }

  @Test
  public void findBoxByEan() {
    String ean1 = "1234";
    String ean2 = "12345";
    String description1 = "kaffe";
    String description2 = "snus";

    String item1 = "{\"ean\":\"" + ean1 + "\","
        + "\"description\":\"" + description1 + "\","
        + "\"expiration\":\"2019-06-24\"}";
    String item2 = "{\"ean\":\"" + ean2 + "\","
        + "\"description\":\"" + description2 + "\","
        + "\"expiration\":\"2019-06-24\"}";

    int box1 = 1;
    int box2 = 2;

    addItem(item1, box1);
    addItem(item2, box2);

    Response response = get("/stash/ean/" + ean1);

    assertThat(response.statusCode(), is(OK.value()));

    List<Integer> boxes = response.getBody().as(List.class);
    assertThat(boxes, hasSize(1));
    assertThat(boxes.get(0), is(box1));
  }

  @Test
  public void findBoxByDescription_oneBox() {
    String ean1 = "1234";
    String ean2 = "12345";
    String description1 = "kaffe";
    String description2 = "snus";

    String item1 = "{\"ean\":\"" + ean1 + "\","
        + "\"description\":\"" + description1 + "\","
        + "\"expiration\":\"2019-06-24\"}";
    String item2 = "{\"ean\":\"" + ean2 + "\","
        + "\"description\":\"" + description2 + "\","
        + "\"expiration\":\"2019-06-24\"}";

    int box1 = 1;
    int box2 = 2;

    addItem(item1, box1);
    addItem(item2, box2);

    Response response = get("/stash/description/" + description1);

    assertThat(response.statusCode(), is(OK.value()));

    List<Integer> boxes = response.getBody().as(List.class);
    assertThat(boxes, hasSize(1));
    assertThat(boxes.get(0), is(box1));
  }

  @Test
  public void findBoxByDescription_twoBoxes() {
    String ean = "1234";
    String description = "snus";

    String item = "{\"ean\":\"" + ean + "\","
        + "\"description\":\"" + description + "\","
        + "\"expiration\":\"2019-06-24\"}";

    int box1 = 1;
    int box2 = 2;

    addItem(item, box1);
    addItem(item, box2);

    Response response = get("/stash/description/" + description);

    assertThat(response.statusCode(), is(OK.value()));

    List<Integer> boxes = response.getBody().as(List.class);
    assertThat(boxes, hasSize(2));
    assertThat(boxes.get(0), is(box1));
    assertThat(boxes.get(1), is(box2));
  }


  private void addItem(String itemAsJson, int box) {
    given()
        .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
        .body(itemAsJson)
        .post("/stash/" + box)
        .then()
        .assertThat().statusCode(is(OK.value()));
  }

  private Response getAllBoxes() {
    return get("/stash");
  }

}
