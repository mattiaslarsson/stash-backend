package to.mattias.stash.rest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static to.mattias.stash.util.StashItemUtil.createStashItem;
import static to.mattias.stash.util.StashItemUtil.randomUUID;

import java.util.Date;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import to.mattias.stash.model.Box;
import to.mattias.stash.model.StashItem;
import to.mattias.stash.persistence.ArticleRepository;
import to.mattias.stash.persistence.StashRepository;

public class StashControllerTest {

  private StashController controller;
  private StashRepository stashRepository;

  @Before
  public void setup() {
    stashRepository = new StashRepository();
    controller = new StashController(stashRepository);
  }

  @Test
  public void postNewItem() {
    int boxNumber = 1;
    StashItem item = createStashItem();
    ResponseEntity response = controller.postItemToStash(boxNumber, item);

    assertThat(response.getStatusCode(), is(OK));
  }

  @Test
  public void getAllBoxes() {
    int boxNumber = 1;
    StashItem item = createStashItem();
    controller.postItemToStash(boxNumber, item);

    ResponseEntity response = controller.getAllBoxes();

    assertThat(response.getStatusCode(), is(OK));

    List<Box> boxes = (List<Box>) response.getBody();
    assertThat(boxes, hasSize(1));
    List<StashItem> itemsInBox = boxes.get(0).getItems().orElseThrow(NullPointerException::new);
    assertThat(itemsInBox, hasSize(1));
    assertThat(itemsInBox.get(0).getEan(), is(item.getEan()));
  }

  @Test
  public void getItemsInSpecificBox() {
    int boxNumber = 1;
    controller.postItemToStash(boxNumber, createStashItem());
    boxNumber = 2;
    StashItem item = createStashItem(randomUUID(), "snus", new Date());
    controller.postItemToStash(boxNumber, item);

    assertThat(controller.getAllBoxes().getBody(), hasSize(2));

    ResponseEntity response = controller.getItemsInBox(boxNumber);
    assertThat(response.getStatusCode(), is(OK));
    List<StashItem> items = (List<StashItem>) response.getBody();
    assertThat(items, hasSize(1));
  }

  @Test
  public void getItemsInNonExistingBox() {
    int boxNumber = 1;
    ResponseEntity response = controller.getItemsInBox(boxNumber);
    assertThat(response.getStatusCode(), is(NOT_FOUND));
  }

}