package org.hyperledger.fabric.chaincode.annotation;


import java.beans.Introspector;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/**
 * 自定义注解注入容器时的名称,名称根据注解的value值定，若空则用名变量.
 */
public class CustomAnnotationBeanNameGenerator implements BeanNameGenerator {
    /**
     * .
     */
  private static final String COMPONENT_ANNOTATION_CLASSNAME =
          "org.springframework.stereotype.Component";
  /**
   * .
   */
  private static final String BAASCONTROLER_ANNOTATION_CLASSNAME =
          "org.hyperledger.fabric.chaincode.annotation.FabricController";
  /**
   * .
   */
  private static final String BAASSERVICE_ANNOTATION_CLASSNAME =
          "org.hyperledger.fabric.chaincode.annotation.FabricService";

  /*(*
   * (non-Javadoc)
   * @see org.springframework.beans.factory.support.
   * BeanNameGenerator#generateBeanName(
   * org.springframework.beans.factory.config.BeanDefinition,
   *  org.springframework.beans.factory.support.BeanDefinitionRegistry)
   */
  @Override
  public String generateBeanName(final BeanDefinition definition,
     final BeanDefinitionRegistry registry) {
    // TODO Auto-generated method stub
    if (definition instanceof AnnotatedBeanDefinition) {
      String beanName = determineBeanNameFromAnnotation(
                        (AnnotatedBeanDefinition) definition);
      if (StringUtils.hasText(beanName)) {
        // Explicit bean name found.
        return beanName;
      }
    }
    // Fallback: generate a unique default bean name.
    return buildDefaultBeanName(definition, registry);
  }

  /**
   * .
   * @param annotatedDef
   * @return
   */
  protected String determineBeanNameFromAnnotation(
      AnnotatedBeanDefinition annotatedDef) {
    AnnotationMetadata amd = annotatedDef.getMetadata();
    Set<String> types = amd.getAnnotationTypes();
    String beanName = null;
    for (String type : types) {
      AnnotationAttributes attributes =
        AnnotationAttributes.fromMap(amd.getAnnotationAttributes(type, false));
    /*  AnnotationConfigUtils.attributesFor(
          amd, type);*/
      if (attributes != null
          && isStereotypeWithNameValue(type, amd.getMetaAnnotationTypes(type),
              attributes)) {
        Object value = attributes.get("value");
        if (value instanceof String) {
          String strVal = (String) value;
          if (StringUtils.hasLength(strVal)) {
            if (beanName != null && !strVal.equals(beanName)) {
              throw new IllegalStateException(
                  "Stereotype annotations suggest inconsistent "
                      + "component names: '" + beanName + "' versus '" + strVal
                      + "'");
            }
            beanName = strVal;
          }
        }
      }
    }
    return beanName;
  }

  /**
   * .
   * @param annotationType
   * @param metaAnnotationTypes
   * @param attributes
   * @return
   */
  protected boolean isStereotypeWithNameValue(String annotationType,
      final Set<String> metaAnnotationTypes,
      @Nullable final Map<String, Object> attributes) {

  boolean isStereotype = annotationType.equals(
          COMPONENT_ANNOTATION_CLASSNAME)
          ||  metaAnnotationTypes.contains(COMPONENT_ANNOTATION_CLASSNAME)
          ||  annotationType.equals("javax.annotation.ManagedBean")
          ||  annotationType.equals("javax.inject.Named");
   // boolean isStereotype = true;
  isStereotype = annotationType.equals(BAASCONTROLER_ANNOTATION_CLASSNAME)
                || annotationType.equals(BAASSERVICE_ANNOTATION_CLASSNAME);


    return (isStereotype && attributes != null
            && attributes.containsKey("value"));
  }

  /**
   * .
   * @param definition
   * @param registry
   * @return
   */
  protected String buildDefaultBeanName(
          final BeanDefinition definition,
          final BeanDefinitionRegistry registry) {
    return buildDefaultBeanName(definition);
  }

  /**
   * .
   * @param definition
   * @return
   */
  protected String buildDefaultBeanName(BeanDefinition definition) {
    String beanClassName = definition.getBeanClassName();
    Assert.state(beanClassName != null, "No bean class name set");
    String shortClassName = ClassUtils.getShortName(beanClassName);
    return Introspector.decapitalize(shortClassName);
  }
}
