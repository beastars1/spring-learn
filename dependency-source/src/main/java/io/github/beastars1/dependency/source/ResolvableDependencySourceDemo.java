package io.github.beastars1.dependency.source;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.annotation.PostConstruct;

public class ResolvableDependencySourceDemo {
    @Autowired
    private String hello;

    @PostConstruct
    public void init() {
        System.out.println(hello);
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        // 导入DependencySourceDemo类
        applicationContext.register(ResolvableDependencySourceDemo.class);

        // addBeanFactoryPostProcessor 在刷新上下文时进行回调
        applicationContext.addBeanFactoryPostProcessor(beanFactory -> {
            beanFactory.registerResolvableDependency(String.class, hello());
        });

        applicationContext.refresh();

        // registerResolvableDependency注册的Bean不能进行依赖查找，只能依赖注入
        applicationContext.getBeansOfType(String.class).forEach((k, v) -> System.out.println(k + " == " + v));

        applicationContext.close();
    }

    public static String hello() {
        System.out.println("world");
        return "hello";
    }
}
