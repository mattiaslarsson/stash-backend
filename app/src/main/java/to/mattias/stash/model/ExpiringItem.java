package to.mattias.stash.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExpiringItem {

  private int box;
  private StashItem item;

}
