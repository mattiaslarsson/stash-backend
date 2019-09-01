package to.mattias.stash.persistence;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Component;
import to.mattias.stash.model.Article;

@Component
public class ArticleRepository_old {
  private Map<String, Article> articles;

  public ArticleRepository_old() {
    articles = new HashMap<>();
  }

  public Optional<Article> findArticleByEan(String ean) {
    return Optional.ofNullable(articles.get(ean));
  }

  public void setArticle(Article article) {
    articles.put(article.getEan(), article);
  }

}
