package to.mattias.stash.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import to.mattias.stash.model.Article;

public interface ArticleRepository extends JpaRepository<Article, String> {

}
