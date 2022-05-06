package io.github.beastars1.bean.definition;

import io.github.beastars1.bean.factory.DefaultUserFactory;
import io.github.beastars1.bean.factory.UserFactoryBean;
import io.github.beastars1.ioc_container.entity.User;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class BeanInstantiationDemo2 {
    public static void main(String[] args) {
        // 配置xml文件，启动上下文
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(User.class);
        applicationContext.register(DefaultUserFactory.class);
        applicationContext.register(UserFactoryBean.class);
//        applicationContext.register(TestUserFactory.class);
        applicationContext.refresh();

        System.out.println(applicationContext.getBeansOfType(User.class));

        applicationContext.close();
    }
}
