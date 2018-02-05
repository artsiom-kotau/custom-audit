package by.roodxx.audit;

import java.io.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("book")
@AuditableController(delete = @MethodDescriptor(methodName = "delete", methodLabel = "Уничтожить"))
public class BookController implements CrudController<Book> {

  private static final Logger logger = LoggerFactory.getLogger(BookController.class);

  private long id = 0;

  @Override
  public Book create(@RequestBody Book body) {
    body.setId(id++);
    logger.info("Create: " + body);
    return body;
  }

  @Override
  public Book update(@PathVariable Serializable id, @RequestBody Book body) {
    logger.info("Update: " + body);
    return body;
  }

  @Override
  public void delete(@PathVariable Serializable id) {
    logger.info("Delete: " + id);
  }

  @PostMapping("{id}/publish")
  @AuditableMethod(name = "Опубликовать")
  public void publish(@PathVariable Serializable id) {
    logger.info("Publish: " + id);
  }
}
