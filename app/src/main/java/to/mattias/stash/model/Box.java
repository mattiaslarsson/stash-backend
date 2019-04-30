package to.mattias.stash.model;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Box {

  private int boxNumber;
  private List<StashItem> items;

  public Optional<List<StashItem>> getItems() {
    return Optional.ofNullable(items);
  }
}
