package io.github.beastars1.dependency.injection.constructor;

import io.github.beastars1.dependency.injection.UserHolder;
import io.github.beastars1.ioc_container.entity.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * 基于注解使用Constructor方式注入依赖
 */
public class AnnotationDependencyConstructorInjectionDemo {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(AnnotationDependencyConstructorInjectionDemo.class);

        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(applicationContext);
        reader.loadBeanDefinitions("classpath:/META-INF/dependency-constructor-injection.xml");

        applicationContext.refresh();

        System.out.println(applicationContext.getBean(UserHolder.class));

        applicationContext.close();
    }

    @Bean
    @Primary
    public UserHolder userHolder(@Qualifier("user") User user) {
        return new UserHolder(user);
    }
}
