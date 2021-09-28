package com.example.annotation_processor;

import com.example.annotation.ARouter;
import com.google.auto.service.AutoService;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.TypeElement;

/**
 * 注解处理器
 */

@AutoService(Processor.class) //编译期绑定
@SupportedAnnotationTypes({"com.example.annotation.ARouter"}) //支持的注解类型
public class ARouterProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        return false;
    }
}