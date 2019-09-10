package to.mattias.stash.model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class StashItem {

  @Id
  @GeneratedValue
  private int id;
  private String ean;
  private String description;
  private Date expiration;
  private int box;

}
