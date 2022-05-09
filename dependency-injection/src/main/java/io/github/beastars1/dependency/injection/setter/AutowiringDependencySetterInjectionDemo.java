package io.github.beastars1.dependency.injection.setter;

import io.github.beastars1.dependency.injection.UserHolder;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 基于Autowiring使用setter方式自动注入依赖
 */
public class AutowiringDependencySetterInjectionDemo {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();

        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(applicationContext);
        reader.loadBeanDefinitions("classpath:/META-INF/autowiring-dependency-setter-injection.xml");

        applicationContext.refresh();

        System.out.println(applicationContext.getBean(UserHolder.class));

        applicationContext.close();
    }
}
