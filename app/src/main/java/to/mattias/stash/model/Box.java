package to.mattias.stash.model;

import java.util.List;
import java.util.Optional;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Entity
public class Box {

  @Id
  private int boxNumber;
  private List<StashItem> items;

  public Optional<List<StashItem>> getItems() {
    return Optional.ofNullable(items);
  }
}
