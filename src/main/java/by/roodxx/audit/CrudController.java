package by.roodxx.audit;

import java.io.Serializable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface CrudController<T> {

  @PostMapping
  T create(T body);

  @PutMapping("{id}")
  T update(@PathVariable Serializable id, @RequestBody T body);

  @DeleteMapping("{id}")
  void delete(Serializable id);


}
