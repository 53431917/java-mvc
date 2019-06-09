package org.hyperledger.fabric.chaincode.annotation;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/**
 * 自定义注解扫描类.扫描自定义注解.
 * @author  
 *
 */
public class FabricScannerRegistrar
  implements ImportBeanDefinitionRegistrar,
             ResourceLoaderAware {

  /**
   * .
   */
  private ResourceLoader resourceLoader;

  /**
   * {@inheritDoc}
   */
  @Override
  public void setResourceLoader(final ResourceLoader resourceLoader) {
      this.resourceLoader = resourceLoader;
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public void registerBeanDefinitions(
      final AnnotationMetadata importingClassMetadata,
      final BeanDefinitionRegistry registry) {
      AnnotationAttributes mapperScanAttrs = AnnotationAttributes
              .fromMap(importingClassMetadata.getAnnotationAttributes(
                  FabricScan.class.getName()));
      if (mapperScanAttrs != null) {
          registerBeanDefinitions(mapperScanAttrs, registry);
      }
  }

  /**
   * .
   * @param annoAttrs
   * @param registry
   */
  void registerBeanDefinitions(
      final AnnotationAttributes annoAttrs,
      final BeanDefinitionRegistry registry) {

     ClassPathBaasScanner scanner = new ClassPathBaasScanner(registry);

      // this check is needed in Spring 3.1
      if (resourceLoader != null) {
          scanner.setResourceLoader(resourceLoader);
      }

      Class<? extends Annotation> annotationClass =
          annoAttrs.getClass("annotationClass");
      if (!Annotation.class.equals(annotationClass)) {
          scanner.setAnnotationClass(annotationClass);
      }

      Class<?> markerInterface = annoAttrs.getClass("markerInterface");
      if (!Class.class.equals(markerInterface)) {
          scanner.setMarkerInterface(markerInterface);
      }

      Class<? extends BeanNameGenerator> generatorClass =
          annoAttrs.getClass("nameGenerator");
    /*  if (!BeanNameGenerator.class.equals(generatorClass)) {
          scanner.setBeanNameGenerator(
              BeanUtils.instantiateClass(generatorClass));
      }*/
      scanner.setBeanNameGenerator(new CustomAnnotationBeanNameGenerator());
/*
      Class<? extends MapperFactoryBean> mapperFactoryBeanClass = annoAttrs.getClass("factoryBean");
      if (!MapperFactoryBean.class.equals(mapperFactoryBeanClass)) {
          scanner.setMapperFactoryBean(BeanUtils.instantiateClass(mapperFactoryBeanClass));
      }
*/

      List<String> basePackages = new ArrayList<>();
      basePackages.addAll(
              Arrays.stream(annoAttrs.getStringArray("value"))
              .filter(StringUtils::hasText)
              .collect(Collectors.toList()));

      basePackages.addAll(
              Arrays.stream(annoAttrs.getStringArray("basePackages"))
              .filter(StringUtils::hasText)
              .collect(Collectors.toList()));

      basePackages.addAll(
              Arrays.stream(annoAttrs.getClassArray("basePackageClasses"))
              .map(ClassUtils::getPackageName)
              .collect(Collectors.toList()));

      scanner.registerFilters();
      scanner.doScan(StringUtils.toStringArray(basePackages));
      
  }

  /**
   * A {@link MapperScannerRegistrar} for {@link MapperScans}.
   * @since 2.0.0
   */
  static class RepeatingRegistrar extends FabricScannerRegistrar {
      /**
       * {@inheritDoc}
       */
      @Override
      public void registerBeanDefinitions(
          final AnnotationMetadata importingClassMetadata,
          final BeanDefinitionRegistry registry) {
          AnnotationAttributes mapperScansAttrs = AnnotationAttributes
                  .fromMap(importingClassMetadata.getAnnotationAttributes(
                      FabricScans.class.getName()));
          if (mapperScansAttrs != null) {
              Arrays.stream(mapperScansAttrs.getAnnotationArray("value"))
              .forEach(mapperScanAttrs ->
                    registerBeanDefinitions(mapperScanAttrs, registry));
          }
      }
  }

}
