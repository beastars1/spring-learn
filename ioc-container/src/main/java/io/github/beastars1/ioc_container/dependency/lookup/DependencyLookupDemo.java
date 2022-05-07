package io.github.beastars1.ioc_container.dependency.lookup;

import io.github.beastars1.ioc_container.annotation.Primary;
import io.github.beastars1.ioc_container.entity.User;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Map;

public class DependencyLookupDemo {
    public static void main(String[] args) {
        // 配置xml文件
        BeanFactory beanFactory = new ClassPathXmlApplicationContext("classpath:/META-INF/dependency-lookup-context.xml");
        lookupInRealTime(beanFactory);
        lookupInLazyByName(beanFactory);
        lookupInLazyByType(beanFactory);
        lookupInRealTimeByType(beanFactory);
        lookupCollectionByType(beanFactory);
        lookupCollectionByAnnotation(beanFactory);
    }

    // 根据name实时查找
    private static void lookupInRealTime(BeanFactory beanFactory) {
        User user = beanFactory.getBean("user", User.class);
        System.out.println("根据name实时查找：" + user);
    }

    // 根据name延迟查找
    private static void lookupInLazyByName(BeanFactory beanFactory) {
        // 此时并没有生成bean对象
        ObjectFactory<User> objectProvider = beanFactory.getBean("objectFactory", ObjectFactory.class);
        // 调用getObject()时才创建bean对象
        User user = objectProvider.getObject();
        System.out.println("根据name延迟查找：" + user);
    }

    // 根据type延迟查找
    private static void lookupInLazyByType(BeanFactory beanFactory) {
        // 此时并没有生成bean对象
        ObjectProvider<User> objectProvider = beanFactory.getBeanProvider(User.class);
        // 调用getObject()时才创建bean对象
        User user = objectProvider.getObject();
        System.out.println("根据type延迟查找：" + user);
    }

    // 根据type实时查找
    private static void lookupInRealTimeByType(BeanFactory beanFactory) {
        User user = beanFactory.getBean(User.class);
        System.out.println("根据type实时查找：" + user);
    }

    // 根据type实时查找集合
    private static void lookupCollectionByType(BeanFactory beanFactory) {
        if (beanFactory instanceof ListableBeanFactory listableBeanFactory) {
            Map<String, User> map = listableBeanFactory.getBeansOfType(User.class);
            System.out.println("根据type实时查找集合:");
            map.forEach((k, v) -> System.out.println(k + " >> " + v));
        }
    }

    // 根据注解查找被注解的bean
    private static void lookupCollectionByAnnotation(BeanFactory beanFactory) {
        if (beanFactory instanceof ListableBeanFactory listableBeanFactory) {
            Map<String, Object> map = listableBeanFactory.getBeansWithAnnotation(Primary.class);
            System.out.println("根据注解查找被注解的bean:");
            map.forEach((k, v) -> System.out.println(k + " >> " + v));
            Primary primary = listableBeanFactory.findAnnotationOnBean("user", Primary.class);
            System.out.println(primary);
        }
    }
}
