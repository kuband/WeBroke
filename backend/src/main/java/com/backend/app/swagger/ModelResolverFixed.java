package com.backend.app.swagger;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.Annotations;
import io.swagger.v3.core.jackson.TypeNameResolver;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ModelResolverFixed extends io.swagger.v3.core.jackson.ModelResolver {
    public ModelResolverFixed(ObjectMapper mapper) {
        super(mapper);
    }

    public ModelResolverFixed(ObjectMapper mapper, TypeNameResolver typeNameResolver) {
        super(mapper, typeNameResolver);
    }

    @Override
    protected Set<String> resolveIgnoredProperties(Annotations a, Annotation[] annotations) {
        Set<String> propertiesToIgnore = new HashSet();
        JsonIgnoreProperties ignoreProperties =
                a.get(JsonIgnoreProperties.class);
        KeepSwaggerJson keepSwaggerJson = a.get(KeepSwaggerJson.class);
        if (keepSwaggerJson == null) {
            if (ignoreProperties != null && !ignoreProperties.allowGetters()) {
                propertiesToIgnore.addAll(Arrays.asList(ignoreProperties.value()));
            }
        }

        propertiesToIgnore.addAll(this.resolveIgnoredProperties(annotations));
        return propertiesToIgnore;
    }

    @Override
    protected Set<String> resolveIgnoredProperties(Annotation[] annotations) {
        Set<String> propertiesToIgnore = new HashSet();
        if (annotations != null) {
            Annotation[] var3 = annotations;
            int var4 = annotations.length;
            if (Arrays.stream(annotations).noneMatch(annotation -> annotation instanceof KeepSwaggerJson)) {
                for (int var5 = 0; var5 < var4; ++var5) {
                    Annotation annotation = var3[var5];
                    if (annotation instanceof JsonIgnoreProperties && !((JsonIgnoreProperties) annotation).allowGetters()) {
                        propertiesToIgnore.addAll(Arrays.asList(((JsonIgnoreProperties) annotation).value()));
                        break;
                    }
                }
            }
        }

        return propertiesToIgnore;
    }

}
