package org.hyperledger.fabric.chaincode.util;

import io.swagger.annotations.ApiResponse;
import io.swagger.converter.ModelConverters;
import io.swagger.jaxrs.config.DefaultReaderConfig;
import io.swagger.jaxrs.config.ReaderConfig;
import io.swagger.jaxrs.ext.SwaggerExtension;
import io.swagger.jaxrs.ext.SwaggerExtensions;
import io.swagger.jaxrs.utils.ReaderUtils;
import io.swagger.models.Model;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Response;
import io.swagger.models.Swagger;
import io.swagger.models.Tag;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.parameters.QueryParameter;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.MapProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;
import io.swagger.models.properties.StringProperty;
import io.swagger.util.ReflectionUtils;

import java.beans.Introspector;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

import org.hyperledger.fabric.chaincode.annotation.FabricParam;
import org.hyperledger.fabric.chaincode.annotation.FabricMapping;
import org.hyperledger.fabric.chaincode.controller.DefaultController;
import org.hyperledger.fabric.chaincode.controller.QueryController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ClassUtils;

public class CustomReader {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(CustomReader.class);
    private static final String SUCCESSFUL_OPERATION = "successful operation";
    private static final String PATH_DELIMITER = "/";

    private final ReaderConfig config;
    private Swagger swagger;

    public CustomReader(Swagger swagger) {
        this(swagger, null);
    }

    public CustomReader(Swagger swagger, ReaderConfig config) {
        this.swagger = (swagger == null) ? new Swagger() : swagger;
        this.config = new DefaultReaderConfig(config);
    }

    public Swagger getSwagger() {
        return swagger;
    }

    public Swagger read(Class<?> cls, String controllerName) {
        Map<String, Tag> tags = new HashMap<String, Tag>();

        String[] consumes = new String[0];
        String[] produces = new String[1];



        // api readable only if @Api present; cannot be hidden because checked
        // in classReadable.

        produces[0] = "application/json";

        // globalSchemes.addAll(parseSchemes(api.protocols()));

        // merge consumes, produces
        if (consumes.length == 0 && cls.getAnnotation(Consumes.class) != null) {
            consumes = ReaderUtils.splitContentValues(cls.getAnnotation(
                    Consumes.class).value());
        }
        if (produces.length == 0 && cls.getAnnotation(Produces.class) != null) {
            produces = ReaderUtils.splitContentValues(cls.getAnnotation(
                    Produces.class).value());
        }
        // look for method-level annotated properties

        // handle sub-resources by looking at return type

        final List<Parameter> globalParameters = new ArrayList<Parameter>();

        // look for constructor-level annotated properties
        globalParameters.addAll(ReaderUtils.collectConstructorParameters(cls,
                swagger));

        // look for field-level annotated properties
        globalParameters.addAll(ReaderUtils
                .collectFieldParameters(cls, swagger));

        if (cls.getName().equals(QueryController.class.getName())) {
            swagger.tag(new Tag().name("queries"));
        } else {
            swagger.tag(new Tag().name(controllerName));
        }
        

        Method methods[] = cls.getMethods();
        for (Method method : methods) {
            FabricMapping baasTrans = ReflectionUtils.getAnnotation(method,
                    FabricMapping.class);
            if (baasTrans == null) {
                continue;
            }

            String operationPath = null;
            if (!"".equals(baasTrans.value())) {
                operationPath = PATH_DELIMITER + controllerName + PATH_DELIMITER + baasTrans.value();
            } else {
                operationPath = PATH_DELIMITER + controllerName + PATH_DELIMITER + method.getName();
            }

            Operation operation = parseMethod(cls, method, controllerName,
                    Collections.<Parameter> emptyList(),
                    Collections.<ApiResponse> emptyList());

            // operation.consumes("application/x-www-form-urlencoded");
            // operation.response(key, response)
          /*  Response response = new Response().description("").headers(null);

            operation.defaultResponse(response);*/

            String[] apiConsumes = consumes;
            // can't continue without a valid http method
            String httpMethod = "post";
            
            if (method.getName().startsWith("delete")) {
                httpMethod = "delete";
            }  else if (method.getName().startsWith("update")) {
                httpMethod = "put";
            } else if (method.getName().startsWith("get")
                     ||  method.getName().startsWith("query")) {
                httpMethod = "get";
            } else {
                httpMethod = "post";
            }
            
            if (baasTrans != null && !"".equals(baasTrans.method())) {
                httpMethod = baasTrans.method();
            } 
        

            if (httpMethod != null) {

                if (operation.getConsumes() == null) {
                    for (String mediaType : apiConsumes) {
                        operation.consumes(mediaType);
                    }
                }

                if (operation.getTags() == null) {
                    for (String tagString : tags.keySet()) {
                        operation.tag(tagString);
                    }
                }

                Path path = swagger.getPath(operationPath);
                if (path == null) {
                    path = new Path();
                    swagger.path(operationPath, path);
                }
                path.set(httpMethod, operation);

            }
        }
        return swagger;
    }

    public Operation parseMethod(Class<?> cls, Method method,
            String controllerName, List<Parameter> globalParameters,
            List<ApiResponse> classApiResponses) {
        Operation operation = new Operation();

        operation.summary(method.getName());
        operation.description(method.getName());
        operation.operationId(controllerName + "." + method.getName());
        operation.produces("application/json");
        
        if (cls.getName().equals(QueryController.class.getName())) {
            operation.tag("queries");
        } else {
            operation.tag(controllerName);
        }
        

   

        String responseContainer = null;

        Type responseType = null;
        Map<String, Property> defaultResponseHeaders = new HashMap<String, Property>();

       // defaultResponseHeaders = parseResponseHeaders(apiOperation
         //       .responseHeaders());

        responseType = method.getGenericReturnType();

        final Property property = ModelConverters.getInstance().readAsProperty(
                responseType);

        if (property != null) {
            final Property responseProperty = ContainerWrapper.wrapContainer(
                    responseContainer, property);
            final int responseCode = 200;

            operation.response(
                    responseCode,
                    new Response().description(SUCCESSFUL_OPERATION)
                            .schema(responseProperty)
                            .headers(defaultResponseHeaders));//....................
            appendModels(responseType);
        }

    
        /*
        Type[] genericParameterTypes = method.getGenericParameterTypes();
        Annotation[][] paramAnnotations = ReflectionUtils
                .getParameterAnnotations(method);
       for (int i = 0; i < genericParameterTypes.length; i++) {
            List<Parameter> parameters = getParameters(genericParameterTypes[i],
                    Arrays.asList(paramAnnotations[i]), controllerName);
             
            for (Parameter parameter : parameters) {
                operation.parameter(parameter);
            }
        }
    */
        
        List<Parameter> parameters = getParameter(method, controllerName);
        for (Parameter parameter : parameters) {
            parameter.setRequired(true);
            operation.parameter(parameter);
        }
        
                
        
        if (operation.getResponses() == null) {
            Response response = new Response()
                    .description(SUCCESSFUL_OPERATION);
            operation.defaultResponse(response);
        }

        processOperationDecorator(operation, method);

        return operation;
    }

   




    private List<Parameter> getParameter(Method method, String controllerName) {
        if (method.getParameterCount() == 0) {
            return  Collections.emptyList();
        }
        
        List<Annotation> annotations = new ArrayList<>();
        java.lang.reflect.Parameter[] parameters = method.getParameters();
        Map<String, Class<?>> propertyMap = new HashMap<>();
        for ( java.lang.reflect.Parameter p : parameters) {
            FabricParam baasParam = p.getAnnotation(FabricParam.class);
            String beanClassName = p.getType().getName();
            String name =  Introspector.decapitalize(ClassUtils.getShortName(beanClassName));
            if (baasParam != null) {
                name = baasParam.value();
                annotations.add(baasParam);
            }
           Type t = p.getParameterizedType();
           
            Class<?> clazz = p.getType();
         
            Object o = SpringContextUtil.getBean(controllerName);
            if (o.getClass().equals(DefaultController.class)
                    && p.getType().getName().equals(Object.class.getName())) {
                
                clazz = ((DefaultController) o).getEntityClass();
            }
            propertyMap.put(name, clazz);
        }
        ParamEntity paramEntity = new ParamEntity();
        Object target = ReflectUtil.getTarget(paramEntity, propertyMap);

        
        List<Parameter> list =  this.getParameters(target.getClass(), annotations, controllerName);
        return list;
    }

    private void processOperationDecorator(Operation operation, Method method) {
        final Iterator<SwaggerExtension> chain = SwaggerExtensions.chain();
        if (chain.hasNext()) {
            SwaggerExtension extension = chain.next();
            LOGGER.debug("trying to decorate operation: " + extension);
            extension.decorateOperation(operation, method, chain);
        }
    }

  

    private void appendModels(Type type) {
        final Map<String, Model> models = ModelConverters.getInstance()
                .readAll(type);
        for (Map.Entry<String, Model> entry : models.entrySet()) {
            swagger.model(entry.getKey(), entry.getValue());
        }
    }







    public ReaderConfig getConfig() {
        return config;
    }

    enum ContainerWrapper {
        LIST("list") {
            @Override
            protected Property doWrap(Property property) {
                return new ArrayProperty(property);
            }
        },
        ARRAY("array") {
            @Override
            protected Property doWrap(Property property) {
                return new ArrayProperty(property);
            }
        },
        MAP("map") {
            @Override
            protected Property doWrap(Property property) {
                return new MapProperty(property);
            }
        },
        SET("set") {
            @Override
            protected Property doWrap(Property property) {
                ArrayProperty arrayProperty = new ArrayProperty(property);
                arrayProperty.setUniqueItems(true);
                return arrayProperty;
            }
        };

        private final String container;

        ContainerWrapper(String container) {
            this.container = container;
        }

        public static Property wrapContainer(String container,
                Property property, ContainerWrapper... allowed) {
            final Set<ContainerWrapper> tmp = (allowed.length > 0) ? EnumSet
                    .copyOf(Arrays.asList(allowed)) : EnumSet
                    .allOf(ContainerWrapper.class);
            for (ContainerWrapper wrapper : tmp) {
                final Property prop = wrapper.wrap(container, property);
                if (prop != null) {
                    return prop;
                }
            }
            return property;
        }

        public Property wrap(String container, Property property) {
            if (this.container.equalsIgnoreCase(container)) {
                return doWrap(property);
            }
            return null;
        }

        protected abstract Property doWrap(Property property);
    }

    private Property createProperty(Type type) {
        return enforcePrimitive(
                ModelConverters.getInstance().readAsProperty(type), 0);
    }

    private Property enforcePrimitive(Property in, int level) {
        if (in instanceof RefProperty) {
            return new StringProperty();
        }
        if (in instanceof ArrayProperty) {
            if (level == 0) {
                final ArrayProperty array = (ArrayProperty) in;
                array.setItems(enforcePrimitive(array.getItems(), level + 1));
            } else {
                return new StringProperty();
            }
        }
        return in;
    }

    private List<Parameter> getParameters(Type type,
            List<Annotation> annotations ,String beanName) {
        final Iterator<SwaggerExtension> chain = SwaggerExtensions.chain();
        if (!chain.hasNext()) {
            return Collections.emptyList();
        }
        

        
        LOGGER.debug("getParameters for " + type);
        Set<Type> typesToSkip = new HashSet<Type>();
        final SwaggerExtension extension = chain.next();
        LOGGER.debug("trying extension " + extension);

        final Parameter parameter = this.extractParameters(annotations, type, typesToSkip, chain, beanName);
        if (parameter !=null) {
            final List<Parameter> processed = new ArrayList<Parameter>();
                if (ParameterProcessor.applyAnnotations(swagger,
                        type, annotations, beanName) != null) {
                    processed.add(parameter);
                }
            return processed;
        } else {
            LOGGER.debug("no parameter found, looking at body params");
            final List<Parameter> body = new ArrayList<Parameter>();
            if (!typesToSkip.contains(type)) {
                Parameter param = ParameterProcessor.applyAnnotations(swagger,
                         type, annotations, beanName);
                if (param != null) {
                    body.add(param);
                }
            }
            return body;
        }
    }

    public Parameter extractParameters(List<Annotation> annotations, Type type,
            Set<Type> typesToSkip, Iterator<SwaggerExtension> chain, String beanName) {

        FabricParam baasParam = null;
        for (Annotation annotaion : annotations) {
            if (annotaion instanceof FabricParam) {
                baasParam = (FabricParam) annotaion;
            }
        }
        Parameter parameter = null;
        if (this.baseType(type.getTypeName())) {
            QueryParameter qp = new QueryParameter();
            if (baasParam != null) {
                qp.name(baasParam.value());
                qp.setRequired(baasParam.required());
            } else {
                String parameterName = type.getTypeName();
                String shortParameterName = Introspector
                        .decapitalize(ClassUtils.getShortName(parameterName));
                qp.name(shortParameterName);
                qp.setRequired(false);
            }

            Property schema = createProperty(type);
            if (schema != null) {
                qp.setProperty(schema);
            }
            parameter = qp;
        } else {
            parameter =  ParameterProcessor.applyAnnotations(swagger, 
                                                 type,annotations, beanName);
        }

        return parameter;
    }

 

    public boolean baseType(String className) {
        String[] baseTypeNames = { "boolean", "byte", "char", "short", "int",
                "long", "float", "double", "java.util.Date", "javax",
                "java.lang.String", "java.lang.Integer", "java.lang.Double",
                "java.lang.Boolean", "java.lang.Float", "java.lang.Byte",
                "java.lang.Character", "java.lang.Short", "java.lang.Long",
                "java.sql.Date", "java.sql.Timestamp", "java.sql.Time" };
        for (String name : baseTypeNames) {
            if (className.startsWith(name)) {
                return true;
            }
        }
        return false;
    }

}
