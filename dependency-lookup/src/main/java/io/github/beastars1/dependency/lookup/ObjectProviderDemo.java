package io.github.beastars1.dependency.lookup;

import io.github.beastars1.ioc_container.entity.User;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

public class ObjectProviderDemo {
    public static void main(String[] args) {
        // 声明 BeanFactory 容器
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        // 注册配置类
        applicationContext.register(ObjectProviderDemo.class);
        System.out.println("before refresh");
        // 启动上下文
        applicationContext.refresh();
        System.out.println("after refresh");

//        lookupByObjectProvider(applicationContext);
        lookupAllByObjectProvider(applicationContext);

        // 关闭上下文
        applicationContext.close();
    }

    private static void lookupAllByObjectProvider(AnnotationConfigApplicationContext applicationContext) {
        ObjectProvider<User> userObjectProvider = applicationContext.getBeanProvider(User.class);
        userObjectProvider.stream().iterator().forEachRemaining(System.out::println); // 不会出现异常，是一个空的集合
        userObjectProvider.stream().forEach(System.out::println);

        ObjectProvider<String> stringObjectProvider = applicationContext.getBeanProvider(String.class);
        stringObjectProvider.stream().iterator().forEachRemaining(System.out::println);
        stringObjectProvider.stream().forEach(System.out::println);

//        System.out.println("getIfAvailable: " + stringObjectProvider.getIfAvailable()); // NoUniqueBeanDefinitionException
        System.out.println("getIfUnique: " + stringObjectProvider.getIfUnique()); // null
//        System.out.println("getIfAvailable: " + userObjectProvider.getIfAvailable()); // null
//        System.out.println("getIfUnique: " + userObjectProvider.getIfUnique()); // null

        Object t = applicationContext.getBean("t");
        System.out.println(t);
        System.out.println(t.getClass()); // org.springframework.beans.factory.support.NullBean
    }

    private static void lookupByObjectProvider(AnnotationConfigApplicationContext applicationContext) {
        ObjectProvider<User> objectProvider = applicationContext.getBeanProvider(User.class);
        System.out.println(objectProvider.getObject()); // no such Bean 异常
        ObjectFactory<String> beanProvider = applicationContext.getBeanProvider(String.class);
        System.out.println(beanProvider.getObject()); // 多个相同类型 Bean 异常
    }

    @Bean
//    @Primary
    public String helloWorld() {
        System.out.println("helloWorld...");
        return "hello, world";
    }

    @Bean
    public String message() {
        return "message";
    }

    @Bean
    @Lazy
    public void t() {
        System.out.println("ttt");
    }
}
