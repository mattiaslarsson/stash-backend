package to.mattias.stash.util;

import java.util.Date;
import java.util.UUID;
import to.mattias.stash.model.StashItem;

public final class StashItemUtil {

  private StashItemUtil() {} // Hiding implicit default constructor

  public static StashItem createStashItem() {
    return createStashItem(randomUUID(), randomUUID(), new Date());
  }

  public static StashItem createStashItem(String ean, String description, Date expiryDate) {
    return StashItem.builder()
        .ean(ean)
        .description(description)
        .expiration(expiryDate)
        .build();
  }

  public static String randomUUID() {
    return UUID.randomUUID().toString();
  }

}
