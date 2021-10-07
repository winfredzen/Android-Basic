package com.example.annotation_processor;

import com.example.annotation.ARouter;
import com.example.annotation.bean.RouterBean;
import com.example.annotation_processor.config.ProcessorConfig;
import com.example.annotation_processor.utils.ProcessorUtils;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/**
 * 注解处理器
 */

@AutoService(Processor.class) //编译期绑定
@SupportedAnnotationTypes({"com.example.annotation.ARouter"}) //表示要处理哪个注解
@SupportedSourceVersion(SourceVersion.RELEASE_8) //支持的版本
//@SupportedOptions({"myvalue"}) //编译选项
@SupportedOptions({ProcessorConfig.MODULE_NAME, ProcessorConfig.APT_PACKAGE_NAME}) //编译选项
public class ARouterProcessor extends AbstractProcessor {

    //需要的工具类
    private Filer filer;       // 文件管理工具类(java文件生成器)
    private Types typesTool;    // 类型处理工具类（类信息）
    private Elements elementsTool;  // Element处理工具类(函数、类、属性都是Element)
    private Messager messager; //编译期打印日志

    private String moduleName;
    private String aptPackageName;

    //Path
    private Map<String, List<RouterBean>> allPathMap = new HashMap<>();
    //Group
    private Map<String, String> allGroupMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        elementsTool = processingEnv.getElementUtils();
        typesTool = processingEnv.getTypeUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();

        //拿到options
//        Map<String, String> options = processingEnv.getOptions();
//        String myValue = options.get("myvalue");
//        messager.printMessage(Diagnostic.Kind.NOTE, "编译参数 myvalue = " + myValue);

        Map<String, String> options = processingEnv.getOptions();
        moduleName = options.get(ProcessorConfig.MODULE_NAME); //模块名
        aptPackageName = options.get(ProcessorConfig.APT_PACKAGE_NAME);
        //校验
        if (moduleName == null || aptPackageName == null) {
            messager.printMessage(Diagnostic.Kind.ERROR, "APT环境出错");
        } else {
            messager.printMessage(Diagnostic.Kind.NOTE, "编译参数 moduleName = " + moduleName + " ,aptPackName = " + aptPackageName);
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.isEmpty()) {
            messager.printMessage(Diagnostic.Kind.NOTE, "没有发现被ARouter注解的类");
            return false; //false表示注解处理器没有工作
        }

        TypeElement activityTypeElement = elementsTool.getTypeElement(ProcessorConfig.ACTIVITY_PACKAGE_NAME);
        TypeMirror activityTypeMirror = activityTypeElement.asType();


        //获取被ARouter注解的类信息（有哪些类被ARouter注解）
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(ARouter.class);
        for (Element element : elements) {
//            //获取类节点，包节点
//            PackageElement packageElement = elementsTool.getPackageOf(element);
//            String packageName = packageElement.getSimpleName().toString();
//            messager.printMessage(Diagnostic.Kind.NOTE, "packageName = " + packageName);
//            //全类名
//            String className = element.getSimpleName().toString();
//            messager.printMessage(Diagnostic.Kind.NOTE, "被ARouter注解的类有：" + className);
//
//            ARouter aRouter = element.getAnnotation(ARouter.class);
            //生成代码
            //generateHelloWorld();


            String className = element.getSimpleName().toString();
            messager.printMessage(Diagnostic.Kind.NOTE, "被ARouter注解的类有：" + className);
            ARouter aRouter = element.getAnnotation(ARouter.class);

            //RouterBean对象
            RouterBean routerBean = new RouterBean();
            routerBean.setGroup(aRouter.group());
            routerBean.setPath(aRouter.path());
            routerBean.setElement(element);

            //当前Element如果是Activity
            TypeMirror elementTypeMirror = element.asType();
            if (typesTool.isSubtype(elementTypeMirror, activityTypeMirror)) {
                routerBean.setTypeEnum(RouterBean.TypeEnum.ACTIVITY);
            } else {
                throw new RuntimeException("@ARouter当前不支持此类型");
            }

            //校验path和group
            boolean checkResult = checkRouterPath(routerBean);
            if (checkResult == true) {
                messager.printMessage(Diagnostic.Kind.NOTE, "check success");
                //添加，通过group获取routerbean
                List<RouterBean> routerBeans = allPathMap.get(routerBean.getGroup());
                if (ProcessorUtils.isEmpty(routerBeans)) {
                    routerBeans = new ArrayList<>();
                    routerBeans.add(routerBean);

                    allPathMap.put(routerBean.getGroup(), routerBeans);
                } else {
                    routerBeans.add(routerBean);
                }
            } else {
                messager.printMessage(Diagnostic.Kind.ERROR, "@ARouter参数格式错误");
            }
        }

        //router_api模块下的interface
        TypeElement pathTypeElement = elementsTool.getTypeElement(ProcessorConfig.ROUTER_API_PATH);
        TypeElement groupTypeElement = elementsTool.getTypeElement(ProcessorConfig.ROUTER_API_GROUP);

        try {
            createPathFile(pathTypeElement);
            createGroupFile(groupTypeElement, pathTypeElement);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return false;
    }

    private void createGroupFile(TypeElement groupType, TypeElement pathType) throws IOException {
        if(ProcessorUtils.isEmpty(allGroupMap) || ProcessorUtils.isEmpty(allPathMap)){
            return;
        }

        /*
         * Map<String, Class<? extends ARouterPath>>
         * -> Class<? extends ARouterPath> ????
         * 返回值
         */
        ParameterizedTypeName thirdParam = ParameterizedTypeName.get(ClassName.get(Class.class),
                WildcardTypeName.subtypeOf(ClassName.get(pathType))); //Class<? extends ARouterPath>
        ParameterizedTypeName methodReturn = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                thirdParam
        );
        // Method
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(ProcessorConfig.GROUP_METHOD_NAME)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(methodReturn);

        /*
         * Map<String, Class<? extends ARouterPath>> groupMap = new HashMap<>();
         */
        methodBuilder.addStatement("$T<$T, $T> $N = new $T<>()",
                ClassName.get(Map.class),
                ClassName.get(String.class),
                thirdParam,
                ProcessorConfig.GROUP_VAR,
                ClassName.get(HashMap.class)
        );

        /*
         * groupMap.put("personal", ARouter$$Path$$personal.class);
         */
        Set<Map.Entry<String, String>> entries = allGroupMap.entrySet();
        for(Map.Entry<String, String> entry: entries){
            methodBuilder.addStatement("$N.put($S, $T.class)",
                    ProcessorConfig.GROUP_VAR,
                    entry.getKey(),
                    ClassName.get(aptPackageName, entry.getValue())
            );
        }

        /*
         * return groupMap;
         */
        methodBuilder.addStatement("return $N", ProcessorConfig.GROUP_VAR);
        String finalClassName = ProcessorConfig.GROUP_FILE_NAME + moduleName;
        messager.printMessage(Diagnostic.Kind.NOTE, "Group:最终生成的文件名称是：" + aptPackageName + "." + finalClassName);

        JavaFile.builder(aptPackageName,
                TypeSpec.classBuilder(finalClassName)
                        .addSuperinterface(ClassName.get(groupType))
                        .addModifiers(Modifier.PUBLIC)
                        .addMethod(methodBuilder.build())
                        .build())
                .build()
                .writeTo(filer);
    }

    private void createPathFile(TypeElement pathTypeElement) throws IOException {
        if(ProcessorUtils.isEmpty(allPathMap)){
            return;
        }

        /*
         * public Map<String, RouterBean> getPathMap() {
         */
        // Map<String, RouterBean> 方法的返回值
        ParameterizedTypeName methodReturn = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ClassName.get(RouterBean.class)
        );

        Set<Map.Entry<String, List<RouterBean>>> entrySet = allPathMap.entrySet();
        for(Map.Entry<String, List<RouterBean>> entry: entrySet){
            /*
             * Map<String, RouterBean> pathMap = new HashMap<>();
             */
            // 方法
            MethodSpec.Builder builder = MethodSpec.methodBuilder(ProcessorConfig.PATH_METHOD_NAME)
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(methodReturn);
            builder.addStatement("$T<$T,$T> $N = new $T<>()",
                    ClassName.get(Map.class),
                    ClassName.get(String.class),
                    ClassName.get(RouterBean.class),
                    ProcessorConfig.PATH_VAR,
                    ClassName.get(HashMap.class));

            List<RouterBean> pathList = entry.getValue();
            for(RouterBean routerBean: pathList){
                /*
                 * pathMap.put("/personal/PersonalMainActivity",
                 *                 RouterBean.create(RouterBean.TypeEnum.ACTIVITY,
                 *                                   Order_MainActivity.class,
                 *                            "/personal/PersonalMainActivity",
                 *                           "personal"));
                 */
                builder.addStatement(
                        "$N.put($S, $T.create($T.$L, $T.class, $S, $S))",
                        ProcessorConfig.PATH_VAR,
                        routerBean.getPath(),
                        ClassName.get(RouterBean.class),
                        ClassName.get(RouterBean.TypeEnum.class),
                        routerBean.getTypeEnum(),
                        ClassName.get((TypeElement) routerBean.getElement()),
                        routerBean.getPath(),
                        routerBean.getGroup()
                );
            }

            // return pathMap;
            builder.addStatement("return $N", ProcessorConfig.PATH_VAR);

            // ARouter$$Path$$personal
            String finalClassName = ProcessorConfig.PATH_FILE_NAME + entry.getKey();
            messager.printMessage(Diagnostic.Kind.NOTE, "Path:最终生成的文件名称是：" + aptPackageName + "." + finalClassName);
            JavaFile.builder(aptPackageName,
                    TypeSpec.classBuilder(finalClassName)
                            .addSuperinterface(ClassName.get(pathTypeElement)) //实现哪个接口
                            .addModifiers(Modifier.PUBLIC)
                            .addMethod(builder.build())
                            .build())
                    .build()
                    .writeTo(filer);
            allGroupMap.put(entry.getKey(), finalClassName);
        }
    }

    private boolean checkRouterPath(RouterBean routerBean) {
        String path = routerBean.getPath();
        String group = routerBean.getGroup();
        if (ProcessorUtils.isEmpty(path) || !path.startsWith("/")) {
            messager.printMessage(Diagnostic.Kind.ERROR, "@ARouter参数格式错误, path = " + path);
            return false;
        }
        if (0 == path.lastIndexOf("/")) {
            messager.printMessage(Diagnostic.Kind.ERROR, "@ARouter参数格式错误, path = " + path);
            return false;
        }
        String groupNameFromPath = path.substring(1, path.indexOf("/", 1));
        if (!ProcessorUtils.isEmpty(group) && !group.equals(moduleName)) {
            messager.printMessage(Diagnostic.Kind.ERROR, "@ARouter参数格式错误, path = " + path);
            return false;
        } else {
            //处理group没有写的情况
            routerBean.setGroup(groupNameFromPath);
        }
        return true;
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























