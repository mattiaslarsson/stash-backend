package to.mattias.stash.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
  public ResponseEntity findArticleByEan(@PathVariable("ean") final String ean) {
    return ResponseEntity.ok(articleRepository.findById(ean).orElseThrow(
        ArticleNotFoundException::new));
  }

  @PostMapping(consumes = APPLICATION_JSON_VALUE)
  @Transactional
  public ResponseEntity setArticleDescription(@RequestBody final Article article) {
    articleRepository.save(article);
//    articleRepository.setArticle(article);

    return ResponseEntity.noContent().build();
  }
}
