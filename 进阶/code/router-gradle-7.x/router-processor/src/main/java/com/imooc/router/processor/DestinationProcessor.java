package com.imooc.router.processor;

import com.google.auto.service.AutoService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.imooc.router.annotations.Destination;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

@AutoService(Processor.class)
public class DestinationProcessor extends AbstractProcessor {

    private static final String TAG = "DestinationProcessor";

    /**
     * 编译器找到我们关心的注解后，会回调这个方法
     * @param set
     * @param roundEnvironment
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> set,
                           RoundEnvironment roundEnvironment) {

        // 避免多次调用 process
        if (roundEnvironment.processingOver()) {
            return false;
        }

        System.out.println(TAG + " >>> process start ...");

        String rootDir = processingEnv.getOptions().get("root_project_dir");

        // 获取所有标记了 @Destination 注解的 类的信息
        Set<Element> allDestinationElements = (Set<Element>)
                roundEnvironment.getElementsAnnotatedWith(Destination.class);

        System.out.println(TAG + " >>> all Destination elements count = "
                + allDestinationElements.size());

        // 当未收集到 @Destination 注解的时候，跳过后续流程
        if (allDestinationElements.size() < 1) {
            return false;
        }

        // 将要自动生成的类的类名
        String className = "RouterMapping_" + System.currentTimeMillis();

        StringBuilder builder = new StringBuilder();

        builder.append("package com.imooc.router.mapping;\n\n");
        builder.append("import java.util.HashMap;\n");
        builder.append("import java.util.Map;\n\n");
        builder.append("public class ").append(className).append(" {\n\n");
        builder.append("    public static Map<String, String> get() {\n\n");
        builder.append("        Map<String, String> mapping = new HashMap<>();\n\n");

        final JsonArray destinationJsonArray = new JsonArray();

        // 遍历所有 @Destination 注解信息，挨个获取详细信息
        for (Element element : allDestinationElements) {

            final TypeElement typeElement = (TypeElement) element;

            // 尝试在当前类上，获取 @Destination 的信息
            final Destination destination =
                    typeElement.getAnnotation(Destination.class);

            if (destination == null) {
                continue;
            }

            final String url = destination.url();
            final String description = destination.description();
            final String realPath = typeElement.getQualifiedName().toString();

            System.out.println(TAG + " >>> url = " + url);
            System.out.println(TAG + " >>> description = " + description);
            System.out.println(TAG + " >>> realPath = " + realPath);

            builder.append("        ")
                    .append("mapping.put(")
                    .append("\"" + url + "\"")
                    .append(", ")
                    .append("\"" + realPath + "\"")
                    .append(");\n");

            JsonObject item = new JsonObject();
            item.addProperty("url", url);
            item.addProperty("description", description);
            item.addProperty("realPath", realPath);

            destinationJsonArray.add(item);
        }

        builder.append("        return mapping;\n");
        builder.append("    }\n");
        builder.append("}\n");

        String mappingFullClassName = "com.imooc.router.mapping." + className;

        System.out.println(TAG + " >>> mappingFullClassName = "
                + mappingFullClassName);

        System.out.println(TAG + " >>> class content = \n" + builder);

        // 写入自动生成的类到本地文件中
        try {
            JavaFileObject source = processingEnv.getFiler()
                    .createSourceFile(mappingFullClassName);
            Writer writer = source.openWriter();
            writer.write(builder.toString());
            writer.flush();
            writer.close();
        } catch (Exception ex) {
            throw new RuntimeException("Error while create file", ex);
        }

        // 写入JSON到本地文件中

        // 检测父目录是否存在
        File rootDirFile = new File(rootDir);
        if (!rootDirFile.exists()) {
            throw new RuntimeException("root_project_dir not exist!");
        }

        // 创建 router_mapping 子目录
        File routerFileDir = new File(rootDirFile, "router_mapping");
        if (!routerFileDir.exists()) {
            routerFileDir.mkdir();
        }

        File mappingFile = new File(routerFileDir,
                "mapping_" + System.currentTimeMillis() + ".json");

        // 写入json内容
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(mappingFile));
            String jsonStr = destinationJsonArray.toString();
            out.write(jsonStr);
            out.flush();
            out.close();
        } catch (Throwable throwable) {
            throw new RuntimeException("Error while writing json", throwable);
        }

        System.out.println(TAG + " >>> process finish.");

        return false;
    }

    /**
     * 告诉编译器，当前处理器支持的注解类型
     * @return
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(
                Destination.class.getCanonicalName()
        );
    }
}
