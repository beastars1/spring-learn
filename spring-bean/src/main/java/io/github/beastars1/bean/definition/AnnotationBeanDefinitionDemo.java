package io.github.beastars1.bean.definition;

import io.github.beastars1.ioc_container.entity.User;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 注解方式注册BeanDefinition
 */
@Import(AnnotationBeanDefinitionDemo.Config.class) // 通过Import导入要导入的类
public class AnnotationBeanDefinitionDemo {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        // 导入AnnotationBeanDefinitionDemo类，AnnotationBeanDefinitionDemo类通过Import导入Config类
        applicationContext.register(AnnotationBeanDefinitionDemo.class);
        applicationContext.refresh();

        // 命名Bean
        registerUserBeanDefinition(applicationContext, "user3");
        // 不命名Bean
        registerUserBeanDefinition(applicationContext);
        registerUserBeanDefinition(applicationContext);

        System.out.println("user bean：");
        applicationContext.getBeansOfType(User.class).forEach((k, v) -> System.out.println(k + " >> " + v));
        System.out.println("config bean：");
        applicationContext.getBeansOfType(Config.class).forEach((k, v) -> System.out.println(k + " >> " + v));

        applicationContext.close();
    }

    // 命名bean的注册方式
    public static void registerUserBeanDefinition(BeanDefinitionRegistry registry, String beanName) {
        // 1.创建BeanDefinition
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(User.class);
        builder.addPropertyValue("name", "Mark")
                .addPropertyValue("age", 20);
        // 2.注册BeanDefinition
        if (StringUtils.hasText(beanName)) {
            registry.registerBeanDefinition(beanName, builder.getBeanDefinition());
        } else {
            // 不提供beanName，自动生成一个：全限定类名#编号
            BeanDefinitionReaderUtils.registerWithGeneratedName(builder.getBeanDefinition(), registry);
        }
    }

    // 命名bean的注册方式
    public static void registerUserBeanDefinition(BeanDefinitionRegistry registry) {
        registerUserBeanDefinition(registry, null);
    }

    @Component
    static class Config {
        @Bean(name = {"user", "user1"})
        public User user() {
            User user = new User();
            user.setName("Jack");
            user.setAge(20);
            return user;
        }
    }
}
