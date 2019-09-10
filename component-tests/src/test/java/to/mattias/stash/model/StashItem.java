package to.mattias.stash.model;

import java.util.Date;
import lombok.Data;


@Data
public class StashItem {

  private int id;
  private String ean;
  private String description;
  private Date expiration;
  private int box;

}
