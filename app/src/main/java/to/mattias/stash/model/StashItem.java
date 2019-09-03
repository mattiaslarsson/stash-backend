package to.mattias.stash.model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@Entity
public class StashItem {
  @Id
  private int id;
  @ManyToOne
  @JoinColumn(name = "box")
  private int box;
  private String ean;
  private String description;
  private Date expiration;

}
