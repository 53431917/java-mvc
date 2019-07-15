package org.hyperledger.fabric.chaincode.annotation;


import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;


/**
 * 自定义扫描类。扫描自定义注解.
 *
 */
public class ClassPathBaasScanner extends ClassPathBeanDefinitionScanner {
  private static Logger LOGGER = LoggerFactory.getLogger(ClassPathBaasScanner.class);

  private boolean addToConfig = true;

  private Class<? extends Annotation> annotationClass;

  private Class<?> markerInterface;

  public ClassPathBaasScanner(BeanDefinitionRegistry registry) {
    super(registry, false);
  }

  public void setAddToConfig(boolean addToConfig) {
    this.addToConfig = addToConfig;
  }

  public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
    this.annotationClass = annotationClass;
  }

  public void setMarkerInterface(Class<?> markerInterface) {
    this.markerInterface = markerInterface;
  }

  /**
   * Configures parent scanner to search for the right interfaces. It can search
   * for all interfaces or just for those that extends a markerInterface or/and
   * those annotated with the annotationClass
   */
  public void registerFilters() {
    boolean acceptAllInterfaces = true;

    // if specified, use the given annotation and / or marker interface
    if (this.annotationClass != null) {
      addIncludeFilter(new AnnotationTypeFilter(this.annotationClass));
      acceptAllInterfaces = false;
    }

    // override AssignableTypeFilter to ignore matches on the actual marker
    // interface
    if (this.markerInterface != null) {
      addIncludeFilter(new AssignableTypeFilter(this.markerInterface) {
        @Override
        protected boolean matchClassName(String className) {
          return false;
        }
      });
      acceptAllInterfaces = false;
    }

    if (acceptAllInterfaces) {
      // default include filter that accepts all classes
      addIncludeFilter((metadataReader, metadataReaderFactory) -> true);
    }

    // exclude package-info.java
    addExcludeFilter((metadataReader, metadataReaderFactory) -> {
      String className = metadataReader.getClassMetadata().getClassName();
      return className.endsWith("package-info");
    });
  }

  /**
   * Calls the parent search that will search and register all the candidates.
   * Then the registered objects are post processed to set them as
   * MapperFactoryBeans
   */
  @Override
  public Set<BeanDefinitionHolder> doScan(String... basePackages) {
    Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);

    if (beanDefinitions.isEmpty()) {
      if (LOGGER.isInfoEnabled()) {
          LOGGER.info( "No Baas mapper was found in '"
          + Arrays.toString(basePackages)
          + "' package. Please check your configuration.");
      }
    }

    return beanDefinitions;
  }





  private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
    GenericBeanDefinition definition;
    for (BeanDefinitionHolder holder : beanDefinitions) {
      definition = (GenericBeanDefinition) holder.getBeanDefinition();
      String beanClassName = definition.getBeanClassName();
      if (LOGGER.isInfoEnabled()) {
          LOGGER.info("Creating MapperFactoryBean with name '"
          + holder.getBeanName() + "' and '" + beanClassName
          + "' mapperInterface");
      }
      // definition.getConstructorArgumentValues().addGenericArgumentValue(beanClassName);
      // // issue #59

      // definition.getPropertyValues().add("addToConfig", this.addToConfig);
      //给每个类加上一个spring bean的别名。别名取值是注解的value的值.
/*      AnnotatedBeanDefinition annotatedBeanDefinition = (AnnotatedBeanDefinition) definition;
      AnnotationMetadata annotationMetadata = annotatedBeanDefinition.getMetadata();
      Set<String> set = annotationMetadata.getAnnotationTypes();
      set.forEach(s -> {
          String t = annotationMetadata.getClassName();
          Map<String, Object> map = annotationMetadata.getAnnotationAttributes(s, true);
          String value = (String ) map.get("value");
          if (!"".equals(value) && value != null ){
            String name = holder.getBeanName();
            super.getRegistry().registerAlias(name,value);
          }
      });*/

      boolean explicitFactoryUsed = false;

      if (!explicitFactoryUsed) {
        if (LOGGER.isInfoEnabled()) {
           LOGGER.info( "Enabling autowire by type for MapperFactoryBean with name '"
                + holder.getBeanName() + "'.");
        }
        definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
      }
    }
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
    AnnotationMetadata annotationMetadata = beanDefinition.getMetadata();
    Set<String> set = annotationMetadata.getAnnotationTypes();
    // add
    List<String> list = annotationMetadata.getAnnotationTypes().stream()
        .filter(s -> s.lastIndexOf("Fabric") >= 0).collect(Collectors.toList());
    // return beanDefinition.getMetadata().isInterface() &&
    // beanDefinition.getMetadata().isIndependent();


    return !list.isEmpty() && beanDefinition.getMetadata().isIndependent();
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  protected boolean checkCandidate(String beanName,
      BeanDefinition beanDefinition) {
    if (super.checkCandidate(beanName, beanDefinition)) {
      return true;
    } else {
      if (LOGGER.isInfoEnabled()) {
          LOGGER.info("Skipping MapperFactoryBean with name '" + beanName
          + "' and '" + beanDefinition.getBeanClassName() + "' mapperInterface"
          + ". Bean already defined with the same name!");
      }
      return false;
    }
  }


}
