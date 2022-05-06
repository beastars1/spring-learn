package io.github.beastars1.bean.definition;

import io.github.beastars1.bean.factory.DefaultUserFactory;
import io.github.beastars1.bean.factory.UserFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

public class BeanInitializationDemo {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(BeanInitializationDemo.class);
        applicationContext.refresh();
        System.out.println("上下文加载完成");

        UserFactory userFactory = applicationContext.getBean(UserFactory.class);
        System.out.println(userFactory);
        System.out.println(userFactory.create());

        applicationContext.close();
        System.out.println("上下文关闭完成");
    }

    @Bean(initMethod = "initFactory", destroyMethod = "destroyFactory")
    @Scope()
//    @Lazy
    public DefaultUserFactory userFactory() {
        System.out.println("userFactory()...");
        return new DefaultUserFactory();
    }
}
