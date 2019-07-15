package org.hyperledger.fabric.chaincode.util;



import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;
import io.swagger.converter.ModelConverters;
import io.swagger.models.Model;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.AbstractSerializableParameter;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.FileProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.PropertyBuilder;
import io.swagger.util.AllowableValues;
import io.swagger.util.AllowableValuesUtils;

import org.apache.commons.lang3.StringUtils;
import org.hyperledger.fabric.chaincode.annotation.FabricParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ClassUtils;

import java.beans.Introspector;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ParameterProcessor {
    static Logger LOGGER = LoggerFactory.getLogger(ParameterProcessor.class);

    public static Parameter applyAnnotations(Swagger swagger,  Type type, List<Annotation> annotations,String beanName) {
        FabricParam baasParam = null;
        for (Annotation annotaion : annotations) {
            if (annotaion instanceof FabricParam) {
                baasParam = (FabricParam) annotaion;
            }
        }

            // must be a body param
            BodyParameter bp = new BodyParameter();

            if (baasParam != null) {
                bp.setRequired(baasParam.required());
                bp.setName(baasParam.value());
            } else {
                bp.setRequired(false);
                String parameterName = type.getTypeName();
                String shortParameterName = Introspector
                        .decapitalize(ClassUtils.getShortName(parameterName));
                bp.setName(shortParameterName);
            }
           

                bp.setDescription(bp.getName());
             
            /* Object o  = SpringContextUtil.getBean(beanName);
             if (o != null && o instanceof DefaultController) {
                 //默认控制器类，查找实际参数类型
                 DefaultController defaultController = (DefaultController) o;
                 type = defaultController.getEntityClass();
             }
         */
            final Property property = ModelConverters.getInstance().readAsProperty(type);
            if (property != null) {
                final Map<PropertyBuilder.PropertyId, Object> args = new EnumMap<PropertyBuilder.PropertyId, Object>(PropertyBuilder.PropertyId.class);
                if (StringUtils.isNotEmpty(bp.getName())) {
                    args.put(PropertyBuilder.PropertyId.DEFAULT, bp.getName());
                }
                bp.setSchema(PropertyBuilder.toModel(PropertyBuilder.merge(property, args)));
                for (Map.Entry<String, Model> entry : ModelConverters.getInstance().readAll(type).entrySet()) {
                    swagger.addDefinition(entry.getKey(), entry.getValue());
                }
            }
           
        return bp;
    }

    private static void processAllowedValues(AllowableValues allowableValues, AbstractSerializableParameter<?> p) {
        if (allowableValues == null) {
            return;
        }
        Map<PropertyBuilder.PropertyId, Object> args = allowableValues.asPropertyArguments();
        if (args.containsKey(PropertyBuilder.PropertyId.ENUM)) {
            p.setEnum((List<String>) args.get(PropertyBuilder.PropertyId.ENUM));
        } else {
            if (args.containsKey(PropertyBuilder.PropertyId.MINIMUM)) {
                p.setMinimum((Double) args.get(PropertyBuilder.PropertyId.MINIMUM));
            }
            if (args.containsKey(PropertyBuilder.PropertyId.MAXIMUM)) {
                p.setMaximum((Double) args.get(PropertyBuilder.PropertyId.MAXIMUM));
            }
            if (args.containsKey(PropertyBuilder.PropertyId.EXCLUSIVE_MINIMUM)) {
                p.setExclusiveMinimum((Boolean) args.get(PropertyBuilder.PropertyId.EXCLUSIVE_MINIMUM) ? true : null);
            }
            if (args.containsKey(PropertyBuilder.PropertyId.EXCLUSIVE_MAXIMUM)) {
                p.setExclusiveMaximum((Boolean) args.get(PropertyBuilder.PropertyId.EXCLUSIVE_MAXIMUM) ? true : null);
            }
        }
    }

    private static void processJsr303Annotations(AnnotationsHelper helper, AbstractSerializableParameter<?> p) {
        if (helper == null) {
            return;
        }
        if (helper.getMin() != null) {
            p.setMinimum((Double) helper.getMin().doubleValue());
        }
        if (helper.getMax() != null) {
            p.setMaximum((Double) helper.getMax().doubleValue());
        }
    }

    /**
     * Wraps either an @ApiParam or and @ApiImplicitParam
     */

    public interface ParamWrapper<T extends Annotation> {
        String getName();

        String getDescription();

        String getDefaultValue();

        String getAllowableValues();

        boolean isRequired();

        String getAccess();

        boolean isAllowMultiple();

        String getDataType();

        String getParamType();

        T getAnnotation();

        boolean isHidden();

        String getExample();
    }

    /**
     * The <code>AnnotationsHelper</code> class defines helper methods for
     * accessing supported parameter annotations.
     */
    private static class AnnotationsHelper {
        private static final ApiParam DEFAULT_API_PARAM = getDefaultApiParam(null);
        private boolean context;
        private ParamWrapper<?> apiParam = new ApiParamWrapper(DEFAULT_API_PARAM);
        private String defaultValue;
        private Integer minItems;
        private Integer maxItems;
        private  Boolean required;
        private  Long min;
        private  Long max;

        /**
         * Constructs an instance.
         *
         * @param annotations array or parameter annotations
         */
        public AnnotationsHelper(List<Annotation> annotations) {
            String rsDefault = null;
            for (Annotation item : annotations) {
                if ("javax.ws.rs.core.Context".equals(item.annotationType().getName())) {
                    context = true;
                } else if (item instanceof ApiParam) {
                    apiParam = new ApiParamWrapper((ApiParam) item);
                } else if (item instanceof ApiImplicitParam) {
                    apiParam = new ApiImplicitParamWrapper((ApiImplicitParam) item);
                } else if ("javax.ws.rs.DefaultValue".equals(item.annotationType().getName())) {
                    try {
                        rsDefault = (String) item.getClass().getMethod("value").invoke(item);
                    } catch (Exception ex) {
                        LOGGER.error("Invocation of value method failed", ex);
                    }
                } else if (item instanceof Size) {
                    final Size size = (Size) item;
                    minItems = size.min();
                    maxItems = size.max();
                } else if (item instanceof NotNull) {
                    required = true;
                } else if (item instanceof Min) {
                    min = ((Min)item).value();
                } else if (item instanceof Max) {
                    max = ((Max)item).value();
                }
            }
            defaultValue = StringUtils.isNotEmpty(apiParam.getDefaultValue()) ? apiParam.getDefaultValue() : rsDefault;
        }

        /**
         * Returns a default @{@link ApiParam} annotation for parameters without it.
         *
         * @param annotationHolder a placeholder for default @{@link ApiParam}
         *                         annotation
         * @return @{@link ApiParam} annotation
         */
        private static ApiParam getDefaultApiParam(@ApiParam String annotationHolder) {
            for (Method method : AnnotationsHelper.class.getDeclaredMethods()) {
                if ("getDefaultApiParam".equals(method.getName())) {
                    return (ApiParam) method.getParameterAnnotations()[0][0];
                }
            }
            throw new IllegalStateException("Failed to locate default @ApiParam");
        }

        /**
         * Checks whether the @{@link Context} annotation is present.
         *
         * @return <code>true<code> if parameter is defined with the @{@link Context} annotation
         */
        public boolean isContext() {
            return context;
        }

        /**
         * Returns @{@link ApiParam} annotation. If no @{@link ApiParam} is present
         * a default one will be returned.
         *
         * @return @{@link ApiParam} annotation
         */
        public ParamWrapper<?> getApiParam() {
            return apiParam;
        }

        /**
         * Returns default value from annotation.
         *
         * @return default value from annotation
         */
        public String getDefaultValue() {
            return defaultValue;
        }

        public Integer getMinItems() {
            return minItems;
        }

        public Integer getMaxItems() {
            return maxItems;
        }

        public Boolean isRequired() {
            return required;
        }

        public Long getMax() {
            return max;
        }

        public Long getMin() {
            return min;
        }
    }

    /**
     * Wrapper implementation for ApiParam annotation
     */

    private final static class ApiParamWrapper implements ParamWrapper<ApiParam> {

        private final ApiParam apiParam;

        private ApiParamWrapper(ApiParam apiParam) {
            this.apiParam = apiParam;
        }

        @Override
        public String getName() {
            return apiParam.name();
        }

        @Override
        public String getDescription() {
            return apiParam.value();
        }

        @Override
        public String getDefaultValue() {
            return apiParam.defaultValue();
        }

        @Override
        public String getAllowableValues() {
            return apiParam.allowableValues();
        }

        @Override
        public boolean isRequired() {
            return apiParam.required();
        }

        @Override
        public String getAccess() {
            return apiParam.access();
        }

        @Override
        public boolean isAllowMultiple() {
            return apiParam.allowMultiple();
        }

        @Override
        public String getDataType() {
            return null;
        }

        @Override
        public String getParamType() {
            return null;
        }

        @Override
        public ApiParam getAnnotation() {
            return apiParam;
        }

        @Override
        public boolean isHidden() {
            return apiParam.hidden();
        }

        @Override
        public String getExample() {
            return apiParam.example();
        }

        ;

        public Example getExamples() {
            return apiParam.examples();
        }

        ;
    }

    /**
     * Wrapper implementation for ApiImplicitParam annotation
     */
    private final static class ApiImplicitParamWrapper implements ParamWrapper<ApiImplicitParam> {

        private final ApiImplicitParam apiParam;

        private ApiImplicitParamWrapper(ApiImplicitParam apiParam) {
            this.apiParam = apiParam;
        }

        @Override
        public String getName() {
            return apiParam.name();
        }

        @Override
        public String getDescription() {
            return apiParam.value();
        }

        @Override
        public String getDefaultValue() {
            return apiParam.defaultValue();
        }

        @Override
        public String getAllowableValues() {
            return apiParam.allowableValues();
        }

        @Override
        public boolean isRequired() {
            return apiParam.required();
        }

        @Override
        public String getAccess() {
            return apiParam.access();
        }

        @Override
        public boolean isAllowMultiple() {
            return apiParam.allowMultiple();
        }

        @Override
        public String getDataType() {
            return apiParam.dataType();
        }

        @Override
        public String getParamType() {
            return apiParam.paramType();
        }

        @Override
        public ApiImplicitParam getAnnotation() {
            return apiParam;
        }

        @Override
        public boolean isHidden() {
            return false;
        }

        @Override
        public String getExample() {
            return apiParam.example();
        }

        public Example getExamples() {
            return apiParam.examples();
        }
    }
}
