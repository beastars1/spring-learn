package io.github.beastars1.dependency.injection.method;

import io.github.beastars1.dependency.injection.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.annotation.Resource;

public class AnnotationDependencyMethodInjectionDemo {
    private UserHolder userHolder;
    private UserHolder userHolder2;

    @Autowired
    public void initHolder(UserHolder userHolder) {
        this.userHolder = userHolder;
    }

    @Resource
    public void initHolder2(UserHolder userHolder2) {
        this.userHolder2 = userHolder2;
    }

    public static void main(String[] args) {
        // 创建 BeanFactory 容器
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        // 注册 Configuration Class（配置类） -> Spring Bean
        applicationContext.register(AnnotationDependencyMethodInjectionDemo.class);

        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(applicationContext);
        // 加载 XML 资源，解析并且生成 BeanDefinition
        beanDefinitionReader.loadBeanDefinitions("classpath:/META-INF/autowiring-dependency-constructor-injection.xml");

        applicationContext.refresh();

        AnnotationDependencyMethodInjectionDemo demo = applicationContext.getBean(AnnotationDependencyMethodInjectionDemo.class);

        System.out.println(demo.userHolder);
        System.out.println(demo.userHolder2);
        System.out.println(demo.userHolder == demo.userHolder2);

        applicationContext.close();
    }
}
