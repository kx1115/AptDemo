package com.yy.lib_compiler;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.yy.lib_annotation.AptTest;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

@SupportedAnnotationTypes({"com.yy.lib_annotation.AptTest"})//声明需要匹配的注解
@SupportedSourceVersion(SourceVersion.RELEASE_8)//支持的Java版本
@SupportedOptions("prjKey")//指定可以接收从build.gradle中传过来的参数prjKey
public class AptProcessor extends AbstractProcessor {
    private String prjKey;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        Map<String, String> opts = processingEnvironment.getOptions();
        prjKey=opts.get("prjKey");
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        Set<? extends Element> element = roundEnvironment.getElementsAnnotatedWith(AptTest.class);
        StringBuilder sb=new StringBuilder();
        if(null!=prjKey){
            sb.append("get prjKey from gradle is :"+prjKey);
            sb.append("   ");
        }
        sb.append("all aptTest values is:");
        for (Element ele:element) {
            TypeElement typeelement = (TypeElement) ele;
            AptTest aptTest=typeelement.getAnnotation(AptTest.class);
            sb.append(aptTest.value());
            sb.append("|");
        }

        //新建一个方法
        MethodSpec getString = MethodSpec.methodBuilder("getString")
                .addModifiers(Modifier.PUBLIC)
                .returns(String.class)
                .addCode("return \""+sb.toString()+"\";\n")
                .build();
        //新建一个类
        TypeSpec testType = TypeSpec.classBuilder("APTTest").addModifiers(Modifier.PUBLIC).addMethod(getString).build();

        //生成这个类
        JavaFile javaFile = JavaFile.builder("com.yy.aptdemo", testType)
                .build();
        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e);
        }

        return true;
    }
}
