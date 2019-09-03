package to.mattias.stash.model;

import java.util.List;
import java.util.Optional;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Box {

  @Id
  private int boxNumber;
  @OneToMany(mappedBy = "box")
  private List<StashItem> items;
}
