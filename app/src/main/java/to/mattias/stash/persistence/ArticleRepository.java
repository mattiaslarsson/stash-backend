package to.mattias.stash.persistence;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Component;
import to.mattias.stash.model.Article;

@Component
public class ArticleRepository {
  private Map<String, String> articles;

  public ArticleRepository() {
    articles = new HashMap<>();
  }

  public Optional<String> findArticleDescription(String ean) {
    return Optional.ofNullable(articles.get(ean));
  }

  public void setArticleDescription(Article article) {
    articles.put(article.getEan(), article.getDescription());
  }

}
