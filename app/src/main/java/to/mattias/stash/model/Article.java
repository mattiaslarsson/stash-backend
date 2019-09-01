package to.mattias.stash.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Entity
public class Article {

  @Id
  private String ean;
  private String description;

}
