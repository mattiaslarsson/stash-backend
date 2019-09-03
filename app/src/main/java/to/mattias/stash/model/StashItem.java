package to.mattias.stash.model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@Entity
@NoArgsConstructor
public class StashItem {
  @Id
  @GeneratedValue
  private int id;
  @ManyToOne
  @JoinColumn(name = "box")
  private Box box;
  private String ean;
  private String description;
  private Date expiration;

}
