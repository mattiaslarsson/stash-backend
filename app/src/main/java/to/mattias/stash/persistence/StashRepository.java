package to.mattias.stash.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import to.mattias.stash.exception.BoxNotExistingException;
import to.mattias.stash.exception.EanNotFoundException;
import to.mattias.stash.exception.NoExpiringItemsException;
import to.mattias.stash.model.Box;
import to.mattias.stash.model.ExpiringItem;
import to.mattias.stash.model.StashItem;

@Component
public class StashRepository {

  private Map<Integer, Box> boxes;

  public StashRepository() {
    boxes = new HashMap<>();
  }

  public void addItemToBox(int boxNumber, StashItem item) {
    Box actualBox = boxes.get(boxNumber);
    if (actualBox == null) {
      actualBox = Box.builder().boxNumber(boxNumber).build();
    }

    List<StashItem> itemsInBox = actualBox.getItems().orElse(new ArrayList<>());
    itemsInBox.add(item);
    actualBox.setItems(itemsInBox);
    boxes.put(boxNumber, actualBox);
  }

  public List<StashItem> getItemsInBox(int boxNumber) throws BoxNotExistingException {
    Box box = boxes.get(boxNumber);
    if (box == null) {
      throw new BoxNotExistingException();
    }

    return box.getItems().orElse(new ArrayList<>());
  }

  public List<Box> getAllBoxes() {
    return boxes.values().stream().collect(Collectors.toList());
  }

  public List<Box> findBoxesByEan(String ean) {
    List<Box> matchingBoxes = new ArrayList<>();
    boxes.values().stream()
        .forEach(box ->
            box.getItems().get().forEach(item -> {
              if (item.getEan().equals(ean)) {
                matchingBoxes.add(box);
              }
            })
        );

    return matchingBoxes;
  }

  public List<Box> findBoxesByDescription(String description) {

    List<Box> matchingBoxes = new ArrayList<>();
    boxes.values().stream()
        .forEach(box ->
            box.getItems().get().forEach(item -> {
              if (item.getDescription().contains(description)) {
                matchingBoxes.add(box);
              }
            })
        );

    return matchingBoxes;
  }

  public Optional<List<ExpiringItem>> getExpiries(Date expiryDate) {
    List<ExpiringItem> expiringItems = new ArrayList<>();
    boxes.values().stream()
        .forEach(box ->
            box.getItems().get().forEach(item -> {
              if (item.getExpiration().before(expiryDate)) {
                expiringItems.add(ExpiringItem.builder()
                    .box(box.getBoxNumber())
                    .item(item)
                    .build());
              }
            })
        );
    if (expiringItems.size() > 0) {
      return Optional.of(expiringItems);
    }
    return Optional.empty();
  }

  public void deleteItemFromBox(String ean, int boxNumber)
      throws EanNotFoundException, BoxNotExistingException {
    Box box = boxes.get(boxNumber);
    if (box == null) {
      throw new BoxNotExistingException("The box does not exist");
    }
    StashItem itemToRemove = box.getItems().get().stream()
        .filter(item -> item.getEan().equals(ean))
        .findFirst()
        .orElseThrow(EanNotFoundException::new);

    box.getItems().get().remove(itemToRemove);
  }

  public void deleteBox(int boxNumber) {
    boxes.remove(boxNumber);
  }
}
