package to.mattias.stash.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import to.mattias.stash.exception.BoxNotExistingException;
import to.mattias.stash.exception.EanNotFoundException;
import to.mattias.stash.model.Box;
import to.mattias.stash.model.ExpiringItem;
import to.mattias.stash.model.StashItem;

@Component
@Transactional
public class StashService {

  @Autowired
  private StashRepository repository;

  public void addItemToBox(int boxNumber, StashItem item) {
    Box actualBox = repository.findById(boxNumber)
        .orElse(Box.builder().boxNumber(boxNumber).build());

    List<StashItem> itemsInBox = actualBox.getItems().orElse(new ArrayList<>());
    itemsInBox.add(item);
    actualBox.setItems(itemsInBox);
    repository.save(actualBox);
  }

  public List<StashItem> getItemsInBox(int boxNumber) throws BoxNotExistingException {
    Box box = repository.findById(boxNumber).orElseThrow(BoxNotExistingException::new);

    return box.getItems().orElse(new ArrayList<>());
  }

  public List<Box> getAllBoxes() {
    return repository.findAll();
  }

  public List<Box> findBoxesByEan(String ean) {
    List<Box> matchingBoxes = new ArrayList<>();
    getAllBoxes().stream()
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
    getAllBoxes().stream()
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
    getAllBoxes().stream()
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
    Box box = repository.findById(boxNumber).orElseThrow(BoxNotExistingException::new);

    StashItem itemToRemove = box.getItems().get().stream()
        .filter(item -> item.getEan().equals(ean))
        .findFirst()
        .orElseThrow(EanNotFoundException::new);

    box.getItems().get().remove(itemToRemove);
  }

  public void deleteBox(int boxNumber) {
    repository.deleteById(boxNumber);
  }
}
