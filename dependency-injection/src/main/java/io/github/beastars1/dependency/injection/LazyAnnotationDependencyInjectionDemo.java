package io.github.beastars1.dependency.injection;

import io.github.beastars1.ioc_container.entity.User;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class LazyAnnotationDependencyInjectionDemo {
    @Autowired
    private User son; // son -> primary 实时注入

    @Autowired
    private ObjectProvider<User> userProvider; // 延迟注入

    @Autowired
    private ObjectProvider<List<User>> userListProvider; // 延迟注入集合

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(LazyAnnotationDependencyInjectionDemo.class);

        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(applicationContext);
        reader.loadBeanDefinitions("classpath:/META-INF/dependency-lookup-context.xml");

        applicationContext.refresh();

        LazyAnnotationDependencyInjectionDemo demo = applicationContext.getBean(LazyAnnotationDependencyInjectionDemo.class);

        System.out.println("demo.son:" + demo.son);
        System.out.println("demo.userProvider:" + demo.userProvider.getIfAvailable());
        System.out.println("demo.userListProvider:" + demo.userListProvider.getIfAvailable());

        System.out.println("demo.userProvider.forEach:");
        demo.userProvider.forEach(System.out::println);

        applicationContext.close();
    }
}
