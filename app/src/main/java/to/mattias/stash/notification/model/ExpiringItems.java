package to.mattias.stash.notification.model;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import to.mattias.stash.model.ExpiringItem;

@Data
@Builder
public class ExpiringItems {

  private List<ExpiringItem> items;

}
