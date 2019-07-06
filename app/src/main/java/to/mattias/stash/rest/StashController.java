package to.mattias.stash.rest;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import to.mattias.stash.exception.BoxNotExistingException;
import to.mattias.stash.exception.EanNotFoundException;
import to.mattias.stash.model.Box;
import to.mattias.stash.model.StashItem;
import to.mattias.stash.persistence.StashRepository;

@RestController
@RequestMapping("/stash")
public class StashController {

  private final StashRepository stashRepository;

  @Autowired
  public StashController(StashRepository stashRepository) {
    this.stashRepository = stashRepository;
  }

  @GetMapping
  public ResponseEntity<List<Box>> getAllBoxes() {

    return ResponseEntity.ok(stashRepository.getAllBoxes());
  }

  @GetMapping("/{boxNumber}")
  public ResponseEntity<List<StashItem>> getItemsInBox(
      @PathVariable("boxNumber") final int boxNumber) {

    try {
      return ResponseEntity.ok(stashRepository.getItemsInBox(boxNumber));
    } catch (BoxNotExistingException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/box/{boxNumber}")
  public ResponseEntity<Box> getBox(@PathVariable("boxNumber") final int boxNumber) {
    try {
      Box box = Box.builder()
          .items(stashRepository.getItemsInBox(boxNumber))
          .boxNumber(boxNumber)
          .build();
      return ResponseEntity.ok(box);
    } catch (BoxNotExistingException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping("/{boxNumber}")
  public ResponseEntity postItemToStash(@PathVariable("boxNumber") final int box,
      @RequestBody final StashItem item) {

    stashRepository.addItemToBox(box, item);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{boxNumber}")
  public ResponseEntity deleteBox(@PathVariable("boxNumber") final int box) {
    stashRepository.deleteBox(box);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{boxNumber}/ean/{ean}")
  public ResponseEntity deleteItemFromBox(@PathVariable("boxNumber") final int box,
      @PathVariable("ean") final String ean) {
    try {
      stashRepository.deleteItemFromBox(ean, box);
    } catch (EanNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body("The box does not contain such an item");
    } catch (BoxNotExistingException e) {
      return ResponseEntity.status(NOT_FOUND).body("The box does not exist");
    }

    return ResponseEntity.noContent().build();

  }

  @GetMapping("/ean/{ean}")
  public ResponseEntity<List<Box>> findBoxesByEan(@PathVariable("ean") final String ean) {

    return ResponseEntity.ok(stashRepository.findBoxesByEan(ean));
  }

  @GetMapping("/description/{description}")
  public ResponseEntity<List<Box>> findBoxesByDescription(
      @PathVariable("description") final String description) {

    return ResponseEntity.ok(stashRepository.findBoxesByDescription(description));
  }

}
