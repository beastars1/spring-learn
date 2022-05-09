package io.github.beastars1.dependency.injection;

import io.github.beastars1.dependency.injection.annotation.UserGroup;
import io.github.beastars1.ioc_container.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.List;

public class QualifierAnnotationDependencyInjectionDemo {
    @Autowired
    private User son; // son -> primary

    @Autowired
    @Qualifier("user")
    private User user;

    @Autowired
    @Qualifier
    private User user1;

    @Autowired
    private List<User> allUsers;

    @Autowired
    @Qualifier // 逻辑分组
    private List<User> anotherUsers;

    @Autowired
    @UserGroup // 逻辑分组
    private List<User> groupUsers;

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(QualifierAnnotationDependencyInjectionDemo.class);

        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(applicationContext);
        reader.loadBeanDefinitions("classpath:/META-INF/dependency-lookup-context.xml");

        applicationContext.refresh();

        QualifierAnnotationDependencyInjectionDemo demo = applicationContext.getBean(QualifierAnnotationDependencyInjectionDemo.class);

        System.out.println("demo.son:" + demo.son);
        System.out.println("demo.user:" + demo.user);
        System.out.println("demo.user1:" + demo.user1);
        System.out.println("demo.allUsers:" + demo.allUsers);
        System.out.println("demo.qualifierUsers:" + demo.anotherUsers);
        System.out.println("demo.userGroupUsers:" + demo.groupUsers);

//        applicationContext.getBeansOfType(User.class).forEach((k, v) -> System.out.println(k + " == " + v));

        applicationContext.close();
    }

    @Bean
    @Qualifier
    public User user1() {
        User user = new User();
        user.setName("user1");
        return user;
    }

    @Bean
    @Qualifier
    public User user2() {
        User user = new User();
        user.setName("user2");
        return user;
    }

    @Bean
    @UserGroup
    public User user3() {
        User user = new User();
        user.setName("user3");
        return user;
    }
}
