package to.mattias.stash.persistence;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import to.mattias.stash.model.ExpiringItem;
import to.mattias.stash.model.StashItem;

@Component
@Transactional
public class StashService {

  @Autowired
  private StashItemRepository stashItemRepository;

  public void addItemToBox(int boxNumber, StashItem item) {
    item.setBox(boxNumber);
    stashItemRepository.save(item);
  }

  public List<StashItem> getItemsInBox(int boxNumber) {
    return stashItemRepository.findAllByBox(boxNumber);
  }

  public Map<Integer, List<StashItem>> getAllBoxes() {

    Map<Integer, List<StashItem>> boxes =
        stashItemRepository.findAll().stream().collect(groupingBy(StashItem::getBox));

    return boxes;
  }

  public List<Integer> findBoxesByEan(String ean) {
    return stashItemRepository.findAll().stream()
        .filter(item -> ean.equals(item.getEan()))
        .map(StashItem::getBox).distinct()
        .collect(toList());
  }

  public List<Integer> findBoxesByDescription(String description) {
    return stashItemRepository.findAll().stream()
        .filter(item -> description.equals(item.getDescription()))
        .map(StashItem::getBox).distinct()
        .collect(toList());
  }

  public Optional<List<ExpiringItem>> getExpiries(Date expiryDate) {
    List<ExpiringItem> expiries = stashItemRepository.findAll().stream()
        .filter(item -> item.getExpiration().before(expiryDate))
        .map(this::toExpiringItem)
        .collect(toList());

    if (expiries.size() > 0) {
      return Optional.of(expiries);
    }
    return Optional.empty();
  }

  private ExpiringItem toExpiringItem(StashItem item) {
    return ExpiringItem.builder()
        .box(item.getBox())
        .item(item)
        .build();
  }

  public void deleteItemFromBox(StashItem item) {
    stashItemRepository.delete(item);
  }

  public void deleteBox(int boxNumber) {
    stashItemRepository.deleteAllByBox(boxNumber);
  }
}
