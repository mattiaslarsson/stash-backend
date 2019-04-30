package to.mattias.stash.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Article {

  private String ean;
  private String description;

}
