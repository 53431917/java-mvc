package org.hyperledger.fabric.chaincode.annotation;

import java.lang.annotation.*;

import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.Import;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(FabricScannerRegistrar.class)
@Repeatable(FabricScans.class)
@Inherited
public @interface FabricScan {

  String[] value() default {};

  /**
   * Base packages to scan for MyBatis interfaces. Note that only interfaces
   * with at least one method will be registered; concrete classes will be
   * ignored.
   *
   * @return base package names for scanning mapper interface
   */
  String[] basePackages() default {};

  /**
   * Type-safe alternative to {@link #basePackages()} for specifying the packages
   * to scan for annotated components. The package of each class specified will be scanned.
   * <p>Consider creating a special no-op marker class or interface in each package
   * that serves no purpose other than being referenced by this attribute.
   *
   * @return classes that indicate base package for scanning mapper interface
   */
  Class<?>[] basePackageClasses() default {};

  /**
   * The {@link BeanNameGenerator} class to be used for naming detected components
   * within the Spring container.
   *
   * @return the class of {@link BeanNameGenerator}
   */
  Class<? extends BeanNameGenerator> nameGenerator() default BeanNameGenerator.class;

  /**
   * This property specifies the annotation that the scanner will search for.
   * <p>
   * The scanner will register all interfaces in the base package that also have
   * the specified annotation.
   * <p>
   * Note this can be combined with markerInterface.
   *
   * @return the annotation that the scanner will search for
   */
  Class<? extends Annotation> annotationClass() default Annotation.class;

  /**
   * This property specifies the parent that the scanner will search for.
   * <p>
   * The scanner will register all interfaces in the base package that also have
   * the specified interface class as a parent.
   * <p>
   * Note this can be combined with annotationClass.
   *
   * @return the parent that the scanner will search for
   */
  Class<?> markerInterface() default Class.class;
}
