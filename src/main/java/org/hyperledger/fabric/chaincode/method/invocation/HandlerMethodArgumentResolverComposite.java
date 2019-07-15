package org.hyperledger.fabric.chaincode.method.invocation;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.hyperledger.fabric.chaincode.request.IRequest;
import org.springframework.core.MethodParameter;

public class HandlerMethodArgumentResolverComposite implements HandlerMethodArgumentResolver {

  private final List<HandlerMethodArgumentResolver> argumentResolvers = new LinkedList<>();

  private final Map<MethodParameter, HandlerMethodArgumentResolver> argumentResolverCache =
      new ConcurrentHashMap<>(256);


  /**
   * Add the given {@link HandlerMethodArgumentResolver}.
   */
  public HandlerMethodArgumentResolverComposite addResolver(HandlerMethodArgumentResolver argumentResolver) {
    this.argumentResolvers.add(argumentResolver);
    return this;
  }

  /**
   * Add the given {@link HandlerMethodArgumentResolver}s.
   * @since 4.3
   */
  public HandlerMethodArgumentResolverComposite addResolvers( HandlerMethodArgumentResolver... resolvers) {
    if (resolvers != null) {
      for (HandlerMethodArgumentResolver resolver : resolvers) {
        this.argumentResolvers.add(resolver);
      }
    }
    return this;
  }

  /**
   * Add the given {@link HandlerMethodArgumentResolver}s.
   */
  public HandlerMethodArgumentResolverComposite addResolvers(
       List<? extends HandlerMethodArgumentResolver> argumentResolvers) {

    if (argumentResolvers != null) {
      for (HandlerMethodArgumentResolver resolver : argumentResolvers) {
        this.argumentResolvers.add(resolver);
      }
    }
    return this;
  }

  /**
   * Return a read-only list with the contained resolvers, or an empty list.
   */
  public List<HandlerMethodArgumentResolver> getResolvers() {
    return Collections.unmodifiableList(this.argumentResolvers);
  }

  /**
   * Clear the list of configured resolvers.
   */
  public void clear() {
    this.argumentResolvers.clear();
  }


  /**
   * Whether the given {@linkplain MethodParameter method parameter} is supported by any registered
   * {@link HandlerMethodArgumentResolver}.
   */
  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return getArgumentResolver(parameter) != null;
  }

  /**
   * Iterate over registered {@link HandlerMethodArgumentResolver}s and invoke the one that supports it.
   * @throws IllegalStateException if no suitable {@link HandlerMethodArgumentResolver} is found.
   */
  @Override
  public Object resolveArgument(MethodParameter parameter, IRequest request) throws Exception {
    HandlerMethodArgumentResolver resolver = getArgumentResolver(parameter);
    if (resolver == null) {
      throw new IllegalStateException("Unknown parameter type [" + parameter.getParameterType().getName() + "]");
    }
    return resolver.resolveArgument(parameter, request);
  }

  /**
   * Find a registered {@link HandlerMethodArgumentResolver} that supports the given method parameter.
   */
  private HandlerMethodArgumentResolver getArgumentResolver(MethodParameter parameter) {
    HandlerMethodArgumentResolver result = this.argumentResolverCache.get(parameter);
    if (result == null) {
      for (HandlerMethodArgumentResolver resolver : this.argumentResolvers) {
        if (resolver.supportsParameter(parameter)) {
          result = resolver;
          this.argumentResolverCache.put(parameter, result);
          break;
        }
      }
    }
    return result;
  }
}