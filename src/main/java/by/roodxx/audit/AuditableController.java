package by.roodxx.audit;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuditableController {

  MethodDescriptor create() default @MethodDescriptor(methodName = "create", methodLabel = "Создать");
  MethodDescriptor update() default @MethodDescriptor(methodName = "update", methodLabel = "Обновить");;
  MethodDescriptor delete() default @MethodDescriptor(methodName = "delete", methodLabel = "Удалить");;
}
