package to.mattias.stash.persistence;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static to.mattias.stash.util.StashItemUtil.createStashItem;

import java.util.List;
import org.junit.Before;
import org.junit.Test;
import to.mattias.stash.exception.BoxNotExistingException;
import to.mattias.stash.exception.EanNotFoundException;
import to.mattias.stash.model.Box;
import to.mattias.stash.model.StashItem;

public class StashRepositoryTest {

  private StashRepository repository;

  @Before
  public void setup() {
    repository = new StashRepository();
  }

  @Test
  public void addItemToNonExistingBox() throws BoxNotExistingException {
    int box = 1;
    StashItem item = createStashItem();

    repository.addItemToBox(box, item);

    List<StashItem> itemsInBox = repository.getItemsInBox(box);

    assertThat(itemsInBox, hasSize(1));
    assertThat(itemsInBox.get(0), is(item));
    assertThat(repository.getAllBoxes(), hasSize(1));
  }

  @Test
  public void addItemToExistingBox() throws BoxNotExistingException {
    int box = 1;
    repository.addItemToBox(box, createStashItem());

    assertThat(repository.getItemsInBox(box), hasSize(1));

    repository.addItemToBox(box, createStashItem());

    assertThat(repository.getItemsInBox(box), hasSize(2));
    assertThat(repository.getAllBoxes(), hasSize(1));
  }

  @Test
  public void findBoxByEan() {
    int box1 = 1;
    int box2 = 2;
    StashItem item1 = createStashItem();
    StashItem item2 = createStashItem();
    String ean = item2.getEan();

    repository.addItemToBox(box1, item1);
    repository.addItemToBox(box2, item2);

    List<Box> boxes = repository.findBoxesByEan(ean);
    assertThat(boxes, hasSize(1));
    assertThat(boxes.get(0).getItems().get(), hasSize(1));
    assertThat(boxes.get(0).getItems().get().get(0), is(item2));
  }

  @Test
  public void deleteItemFromBox() throws BoxNotExistingException, EanNotFoundException {
    int boxNumber = 1;
    StashItem item = createStashItem();
    String ean = item.getEan();

    repository.addItemToBox(boxNumber, item);
    assertThat(repository.getItemsInBox(1), hasSize(1));

    repository.deleteItemFromBox(ean, 1);
    assertThat(repository.getItemsInBox(1), hasSize(0));
  }

}