package io.github.beastars1.dependency.injection.field;

import io.github.beastars1.dependency.injection.UserHolder;
import io.github.beastars1.ioc_container.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.annotation.Resource;

public class AnnotationDependencyFieldInjectionDemo {
    @Autowired // byType
    private UserHolder userHolder;
    @Resource
    private UserHolder userHolder2;
    @Autowired
    @Qualifier("son") // byName
    private User son;
    @Resource // byName,fallback:byType
    private User user;

    public static void main(String[] args) {
        // 创建 BeanFactory 容器
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        // 注册 Configuration Class（配置类） -> Spring Bean
        applicationContext.register(AnnotationDependencyFieldInjectionDemo.class);

        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(applicationContext);
        // 加载 XML 资源，解析并且生成 BeanDefinition
        beanDefinitionReader.loadBeanDefinitions("classpath:/META-INF/autowiring-dependency-constructor-injection.xml");

        applicationContext.refresh();

        AnnotationDependencyFieldInjectionDemo demo = applicationContext.getBean(AnnotationDependencyFieldInjectionDemo.class);

        System.out.println(demo.userHolder);
        System.out.println(demo.userHolder2);
        System.out.println(demo.userHolder == demo.userHolder2);

        System.out.println(demo.son);
        System.out.println(demo.user);

        applicationContext.close();
    }
}
