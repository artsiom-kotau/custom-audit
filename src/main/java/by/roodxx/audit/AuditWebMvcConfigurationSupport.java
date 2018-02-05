package by.roodxx.audit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcRegistrationsAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration
public class AuditWebMvcConfigurationSupport {

  private static final Logger logger = LoggerFactory.getLogger(AuditWebMvcConfigurationSupport.class);

  private Map<Method, String> methodActionName = new HashMap<>();

  private Map<Class, Collection<MethodDescriptor>> beanMethodDescriptors = new HashMap<>();

  @Bean
  public WebMvcRegistrationsAdapter webMvcRegistrationsHandlerMapping() {
    return new WebMvcRegistrationsAdapter() {
      @Override
      public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        return new RequestMappingHandlerMapping() {
          private final static String API_BASE_PATH = "api";

          @Override
          protected void registerHandlerMethod(Object handler, Method method, RequestMappingInfo mapping) {
            AuditableMethod auditableMethod = AnnotationUtils.findAnnotation(method, AuditableMethod.class);
            String methodLabel = null;
            if (auditableMethod != null) {
              methodLabel = auditableMethod.name();
              methodActionName.put(method, auditableMethod.name());
            }
            Class<?> beanType = method.getDeclaringClass();
            AuditableController auditableController = AnnotationUtils.findAnnotation(beanType, AuditableController.class);
            if (StringUtils.isEmpty(methodLabel) && auditableController != null) {
              Collection<MethodDescriptor> methodDescriptors = beanMethodDescriptors.get(beanType);
              if (methodDescriptors == null) {
                methodDescriptors = Arrays.asList(auditableController.create(), auditableController.update(), auditableController.delete());
                beanMethodDescriptors.put(beanType, methodDescriptors);
              }

              for(MethodDescriptor methodDescriptor : methodDescriptors) {
                if (Objects.equals(method.getName(), methodDescriptor.methodName())) {
                  methodActionName.put(method, methodDescriptor.methodLabel());
                }
              }

            }
            super.registerHandlerMethod(handler, method, mapping);
          }

          @Override
          protected HandlerMethod getHandlerInternal(HttpServletRequest request) throws Exception {
            HandlerMethod handlerMethod = super.getHandlerInternal(request);
            if (handlerMethod != null) {
              String actionName = methodActionName.get(handlerMethod.getMethod());
              if (actionName != null) {
                logger.info(actionName);
              }
            }
            return handlerMethod;
          }
        };
      }


    };
  }

}
