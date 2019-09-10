package to.mattias.stash.rest;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import to.mattias.stash.model.StashItem;
import to.mattias.stash.persistence.StashService;

@RestController
@RequestMapping("/stash")
public class StashController {

  private final StashService stashService;
  private static Logger log = LoggerFactory.getLogger(StashController.class);

  @Autowired
  public StashController(StashService stashService) {
    this.stashService = stashService;
  }

  @GetMapping
  public ResponseEntity<Map<Integer, List<StashItem>>> getAllBoxes() {

    log.info("Getting all boxes");

    return ResponseEntity.ok(stashService.getAllBoxes());
  }

  @GetMapping("/{boxNumber}")
  public ResponseEntity<List<StashItem>> getItemsInBox(
      @PathVariable("boxNumber") final int boxNumber) {

    log.info("Getting box #{}", boxNumber);

    return ResponseEntity.ok(stashService.getItemsInBox(boxNumber));
  }

  @PostMapping("/{boxNumber}")
  public ResponseEntity postItemToStash(@PathVariable("boxNumber") final int box,
      @RequestBody final StashItem item) {

    log.info("Adding item: {} to box#{}", item, box);

    stashService.addItemToBox(box, item);

    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{boxNumber}")
  public ResponseEntity deleteBox(@PathVariable("boxNumber") final int box) {
    stashService.deleteBox(box);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/item")
  public ResponseEntity deleteItemFromBox(@RequestBody StashItem item) {
    stashService.deleteItemFromBox(item);

    return ResponseEntity.noContent().build();

  }

  @GetMapping("/ean/{ean}")
  public ResponseEntity<List<Integer>> findBoxesByEan(@PathVariable("ean") final String ean) {

    return ResponseEntity.ok(stashService.findBoxesByEan(ean));
  }

  @GetMapping("/description/{description}")
  public ResponseEntity<List<Integer>> findBoxesByDescription(
      @PathVariable("description") final String description) {

    return ResponseEntity.ok(stashService.findBoxesByDescription(description));
  }

}
