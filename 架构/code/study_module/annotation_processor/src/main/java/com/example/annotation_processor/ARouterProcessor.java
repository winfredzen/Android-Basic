package com.example.annotation_processor;

import com.example.annotation.ARouter;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/**
 * 注解处理器
 */

@AutoService(Processor.class) //编译期绑定
@SupportedAnnotationTypes({"com.example.annotation.ARouter"}) //表示要处理哪个注解
@SupportedSourceVersion(SourceVersion.RELEASE_8) //支持的版本
@SupportedOptions({"myvalue"})
public class ARouterProcessor extends AbstractProcessor {

    //需要的工具类
    private Filer filer;       // 文件管理工具类(java文件生成器)
    private Types typesTool;    // 类型处理工具类（类信息）
    private Elements elementsTool;  // Element处理工具类(函数、类、属性都是Element)
    private Messager messager; //编译期打印日志

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        elementsTool = processingEnv.getElementUtils();
        typesTool = processingEnv.getTypeUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();

        //拿到options
        Map<String, String> options = processingEnv.getOptions();
        String myValue = options.get("myvalue");
        messager.printMessage(Diagnostic.Kind.NOTE, "编译参数 myvalue = " + myValue);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.size() == 0) {
            messager.printMessage(Diagnostic.Kind.NOTE, "没有发现被ARouter注解的类");
            return false; //false表示注解处理器没有工作
        }
        //获取被ARouter注解的类信息（有哪些类被ARouter注解）
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(ARouter.class);
        for (Element element : elements) {
            //获取类节点，包节点
            PackageElement packageElement = elementsTool.getPackageOf(element);
            String packageName = packageElement.getSimpleName().toString();
            messager.printMessage(Diagnostic.Kind.NOTE, "packageName = " + packageName);
            //全类名
            String className = element.getSimpleName().toString();
            messager.printMessage(Diagnostic.Kind.NOTE, "被ARouter注解的类有：" + className);

            ARouter aRouter = element.getAnnotation(ARouter.class);
            //生成代码
            generateHelloWorld();
        }
        return false;
    }

    //测试生成java代码
    private void generateHelloWorld() {
        //JavaPoet
        MethodSpec main = MethodSpec.methodBuilder("main")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(String[].class, "args")
                .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")
                .build();

        TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(main)
                .build();

        JavaFile javaFile = JavaFile.builder("com.example.helloworld", helloWorld)
                .build();

        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}























