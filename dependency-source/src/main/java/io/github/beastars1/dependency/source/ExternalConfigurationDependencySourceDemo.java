package io.github.beastars1.dependency.source;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

// 外部配置注入
@Configuration
@PropertySource(value = "META-INF/default.properties")
public class ExternalConfigurationDependencySourceDemo {
    @Value("${source.name}")
    private String name;

    @Value("${source.age:20}")
    private int age;

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        // 导入DependencySourceDemo类
        applicationContext.register(ExternalConfigurationDependencySourceDemo.class);

        applicationContext.refresh();

        ExternalConfigurationDependencySourceDemo demo = applicationContext.getBean(ExternalConfigurationDependencySourceDemo.class);

        System.out.println(demo.name);
        System.out.println(demo.age);

        applicationContext.getBeansOfType(String.class).forEach((k, v) -> System.out.println(k + " == " + v));

        applicationContext.close();
    }
}
