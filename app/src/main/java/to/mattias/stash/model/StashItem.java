package to.mattias.stash.model;

import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class StashItem {

  private String ean;
  private String description;
  private Date expiration;

}
