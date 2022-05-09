package io.github.beastars1.dependency.injection;

import io.github.beastars1.dependency.injection.annotation.MyAutowired;
import io.github.beastars1.dependency.injection.annotation.MyAutowiredExpand;
import io.github.beastars1.ioc_container.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

public class AnnotationDependencyInjectionResolutionDemo {
    @Autowired
    private User user; // user -> son -> primary 实时注入

    @MyAutowired
    private User myUser;

    @MyAutowiredExpand
    private User myUserExpand;

/*    @Bean
    public static AutowiredAnnotationBeanPostProcessor beanPostProcessor() {
        AutowiredAnnotationBeanPostProcessor processor = new AutowiredAnnotationBeanPostProcessor();
        Set<Class<? extends Annotation>> set = new LinkedHashSet<>(Arrays.asList(Autowired.class, MyAutowired.class));
        processor.setAutowiredAnnotationTypes(set);
        return processor;
    }*/

    @Bean // 额外添加一个新的 AutowiredAnnotationBeanPostProcessor Bean
//    @Bean(AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME) // 替换默认的AutowiredAnnotationBeanPostProcessor Bean，会Clear
//    @Order(Ordered.LOWEST_PRECEDENCE - 3)
    public static AutowiredAnnotationBeanPostProcessor beanPostProcessor() {
        AutowiredAnnotationBeanPostProcessor processor = new AutowiredAnnotationBeanPostProcessor();
        processor.setAutowiredAnnotationType(MyAutowired.class);
        return processor;
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(AnnotationDependencyInjectionResolutionDemo.class);

        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(applicationContext);
        reader.loadBeanDefinitions("classpath:/META-INF/dependency-lookup-context.xml");

        applicationContext.refresh();

        AnnotationDependencyInjectionResolutionDemo demo = applicationContext.getBean(AnnotationDependencyInjectionResolutionDemo.class);

        System.out.println("demo.user:" + demo.user);
        System.out.println("demo.myUser:" + demo.myUser);
        System.out.println("demo.myUserExpand:" + demo.myUserExpand);

        applicationContext.close();
    }
}
