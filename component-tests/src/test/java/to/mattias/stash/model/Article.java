package to.mattias.stash.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Article {

  private String ean;
  private String description;

}
