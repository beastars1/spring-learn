# Spring 依赖来源

## 依赖查找的来源

1. 自定义 Bean

   - xml 定义 bean 标签

   - @Bean 注解

   - 手动定义 BeanDefinition 进行注册

     ```java
     beanDefinitionRegistry.registerBeanDefinition(beanName, beanDefinition);
     ```

2. 注册外部单例对象

    ```java
    singletonBeanRegistry.registerSingleton(beanName, beanObject);
    ```

    单例对象可以在 IoC 容器启动后进行注册。因为 BeanDefinition 会被  ConfigurableListableBeanFactory.freezeConfiguration() 方法影响，从而冻结注册，而 registerSingleton() 是将单例对象放入一个 ConcurrentHashMap 中，也不会有初始化方法，可以在 IoC 启动后进行注册。

2. Spring 内建 BeanDefinition

3. Spring 内建单例对象

## 依赖注入的来源

1. 自定义 Bean （同上）

2. 注册外部单例对象

3. 非 Spring 容器管理对象

   -  Resolvable Dependency 游离对象

     Resolvable Dependency 只能用来依赖注入，并且不由 Spring 容器管理，不是一个真正的 Spring Bean。

     默认情况下 Spring 注入了四个游离对象：`BeanFactory.class`、`ResourceLoader.class`、`ApplicationEventPublisher.class`、`ApplicationContext.class`。

     AbstractApplicationContext.prepareBeanFactory(ConfigurableListableBeanFactory):

     ```java
     beanFactory.registerResolvableDependency(BeanFactory.class, beanFactory);
     beanFactory.registerResolvableDependency(ResourceLoader.class, this);
     beanFactory.registerResolvableDependency(ApplicationEventPublisher.class, this);
     beanFactory.registerResolvableDependency(ApplicationContext.class, this);
     ```

     以上注入过程在 InstantiationAwareBeanPostProcessor.postProcessProperties() 方法处执行，早于  setter 注入，也早于 @PostConstruct。

     以上四个类型不能通过依赖查找找到，但是可以正常进行依赖注入。

     除了 BeanFactory 是注册了 beanFactory 对象，其他三个注册的都是同一个 AbstractApplicationContext 对象，也就是说对其他三个类型进行依赖注入，这三个注入对象是相等的。

   - 外部化配置

     @Value(${xxx:default})

## 依赖来源

1. BeanDefinition 作为依赖来源

   - 元数据：BeanDefinition
   - 注册：BeanDefinitionRegistry.registerBeanDefinition()
   - 类型：延迟和非延迟
   - 顺序：Bean 生命周期顺序按照注册顺序

2. 外部单例对象作为依赖来源

   - 来源：外部普通 Java 对象
   - 注册：SingletonBeanRegistry.registerSingleton()
   - 不能被 Spring 管理生命周期
   - 无法延迟初始化 Bean

3. 非 Spring 容器管理对象作为依赖来源

   - 注册：ConfigurableListableBeanFactory.registerResolvableDependency()
   - 不能被 Spring 管理生命周期
   - 无法延迟初始化 Bean
   - 无法通过依赖查找，只能进行依赖注入

4. 外部化配置作为依赖来源

   - 类型：非常规 Spring 对象依赖来源
   - 不能被 Spring 管理生命周期
   - 无法延迟初始化 Bean
   - 无法通过依赖查找，只能进行依赖注入

   