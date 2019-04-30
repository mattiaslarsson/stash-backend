package to.mattias.stash.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import to.mattias.stash.exception.ArticleNotFoundException;
import to.mattias.stash.model.Article;
import to.mattias.stash.persistence.ArticleRepository;

@RestController
@RequestMapping("/article")
public class ArticlesController {

  private final ArticleRepository articleRepository;

  @Autowired
  public ArticlesController(ArticleRepository articleRepository) {
    this.articleRepository = articleRepository;
  }

  @GetMapping(path = "/{ean}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<String> findArticleByEan(@PathVariable("ean") final String ean) {
    String description = articleRepository.findArticleDescription(ean).orElseThrow(
        ArticleNotFoundException::new);

    return ResponseEntity.ok(description);
  }

  @PostMapping(consumes = APPLICATION_JSON_VALUE)
  public ResponseEntity setArticleDescription(@RequestBody final Article article) {

    articleRepository.setArticleDescription(article);

    return ResponseEntity.noContent().build();
  }
}
