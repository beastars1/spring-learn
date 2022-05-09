package io.github.beastars1.dependency.injection.setter;

import io.github.beastars1.dependency.injection.UserHolder;
import io.github.beastars1.ioc_container.entity.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * 基于注解使用setter方式注入依赖
 */
public class AnnotationDependencySetterInjectionDemo {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(AnnotationDependencySetterInjectionDemo.class);

        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(applicationContext);
        reader.loadBeanDefinitions("classpath:/META-INF/dependency-setter-injection.xml");

        applicationContext.refresh();

        System.out.println(applicationContext.getBean(UserHolder.class));

        applicationContext.close();
    }

    @Bean
    @Primary
    public UserHolder userHolder(@Qualifier("user") User user) {
        UserHolder userHolder = new UserHolder();
        userHolder.setUser(user);
        return userHolder;
    }
}
