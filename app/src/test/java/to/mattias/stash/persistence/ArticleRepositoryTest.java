package to.mattias.stash.persistence;

import static org.assertj.core.api.Fail.fail;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static to.mattias.stash.util.StashItemUtil.randomUUID;

import org.junit.Before;
import org.junit.Test;
import to.mattias.stash.exception.ArticleNotFoundException;
import to.mattias.stash.model.Article;

public class ArticleRepositoryTest {

  private ArticleRepository articleRepository;

  @Before
  public void setup() {
    articleRepository = new ArticleRepository();
  }

  @Test
  public void addArticle() {
    String ean = randomUUID();
    String expectedDescription = randomUUID();

    Article article = Article.builder()
        .ean(ean)
        .description(expectedDescription)
        .build();

    articleRepository.setArticleDescription(article);

    String actualDescription = articleRepository.findArticleDescription(ean).orElseThrow(
        ArticleNotFoundException::new);

    assertThat(actualDescription, is(expectedDescription));

  }

  @Test(expected = ArticleNotFoundException.class)
  public void getNonExistingArticle() {
    String ean = randomUUID();

    articleRepository.findArticleDescription(ean).orElseThrow(ArticleNotFoundException::new);

    fail("Should have thrown ArticleNotFoundException");
  }

}