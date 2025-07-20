package com.backend.app.swagger;

import jakarta.annotation.PostConstruct;
import org.springdoc.core.converters.ModelConverterRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.support.GenericApplicationContext;

import java.util.Collections;

@Lazy(false)
@Configuration
@DependsOn("modelConverterRegistrar")
public class ModelConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void registerModelConverter() {
        GenericApplicationContext gac = (GenericApplicationContext) applicationContext;
        ModelConverterRegistrar modelConverterRegistrar =
                new ModelConverterRegistrar(Collections.emptyList());
        gac.registerBean("modelConverterRegistrar", ModelConverterRegistrarFixed.class);
    }
}
