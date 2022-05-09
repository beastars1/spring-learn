# spring-bean

## Alias

给 Bean 起别名，获取 Bean 时可以通过别名获取。
如果 Bean 是单例，那么通过 BeanName 和通过别名获取到的 Bean 对象，是相同的。

1. 在 xml 文件中指定别名
    ```xml
   <!-- 对user启用别名 -->
    <alias name="user" alias="user1"/> 
   ```
2. 在 @Bean 时指定
   ```java
   @Bean(name = {"user", "user1"})
   public User user() {
       return new User();
   }
   ```
   可以通过 user 和 user1 获取 Bean。

## Bean
BeanDefinition 是 Spring 中定义 Bean 的配置元信息接口，包含：

- Bean 的类名

- Bean 行为配置元素，如作用域、自动绑定的模式，生命周期回调等

- 其他 Bean 引用，又可称作合作者（collaborators）或者依赖（dependencies）

- 配置设置，比如 Bean

### 1. 构建 BeanDefinition

1. 通过 BeanDefinitionBuilder 创建

   ```java
   BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(User.class);
   // 设置bean属性
   beanDefinitionBuilder.addPropertyValue("name", "John")
       .addPropertyValue("age", 30);
   // 获取BeanDefinition实例
   AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
   // beanDefinition并不是bean的最终状态，还可以进行自定义修改
   beanDefinition.setScope("singleton");
   beanDefinition.setAutowireMode(2); // byType
   ```

2. 通过 AbstractBeanDefinition 以及派生类生成

   ```java
   GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition();
   // 设置bean类型
   genericBeanDefinition.setBeanClass(User.class);
   // 设置bean属性
   MutablePropertyValues propertyValues = genericBeanDefinition.getPropertyValues();
   propertyValues.add("name", "John")
       .add("age", 40);
   genericBeanDefinition.setPropertyValues(propertyValues);
   ```

BeanDefinition 构建之后还获取不到 Bean，还需要将其注册到对应的 Bean 容器中才行。

### 2. 注册 Bean

将 BeanDefinition 注册为 Bean。

1. 通过 xml 注册

   ```xml
   <bean id="user" class="io.github.beastars1.ioc_container.entity.User">
       <property name="name" value="Jack"/>
       <property name="age" value="12"/>
   </bean>
   ```

2. 通过注解注册

   - @Bean：只能用于方法和注解上
   
     > 默认 BeanName 是方法名，类型是方法返回值，默认是单例，可以额外添加 @Scope 来指定作用域
     > 
     > 多个同类型 Bean，可以使用 @Primary 指定主 Bean
     > 
     > @Bean 使用在静态方法上，会优先注册

   - @Component：只能用于类上
   
     > 默认 BeanName 是类名，默认是单例，可以额外添加 @Scope 来指定作用域
     >
     > 派生类中 @Repository、@Service、@Controller 默认都是单例

   - @Import：引入要导入的类

5. 手动进行注册

   1. 自定义 BeanName 注册

      ```java
      // 1.创建BeanDefinition
      BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(User.class);
      builder.addPropertyValue("name", "Mark")
          .addPropertyValue("age", 20);
      // 2.注册BeanDefinition
      registry.registerBeanDefinition(beanName, builder.getBeanDefinition());
      ```

   2. 自动生成 BeanName 注册

      ```java
      // 1.创建BeanDefinition
      BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(User.class);
      builder.addPropertyValue("name", "Mark")
          .addPropertyValue("age", 20);
      // 2.注册BeanDefinition，不提供beanName，自动生成一个：全限定类名#编号
      BeanDefinitionReaderUtils.registerWithGeneratedName(builder.getBeanDefinition(), registry);
      ```

   3. 手动注册
   也会注册指定的类内部的 Bean 

      ```java
      AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
      // 导入AnnotationBeanDefinitionDemo类
      applicationContext.register(AnnotationBeanDefinitionDemo.class);
      ```

   5. 注册外部单例 Bean

      如 SingletonBeanRegisterDemo

      ```java
      beanFactory.registerSingleton("userFactory", userFactory)
      ```

### 3. 实例化 Bean

1. 默认通过构造器实现

2. 通过静态工厂方法实现

   xml 中添加 factory-method，指向 User.createUser() 方法。

   ```xml
   <bean id="userByStaticMethod" class="io.github.beastars1.ioc_container.entity.User" factory-method="createUser"/>
   ```

   或者直接在静态方法上添加 @Bean，使用 applicationContext.register(User.class) 方法加入 Bean 容器中。

3. 通过 Bean 工厂方法实现

   xml 中添加 factory-bean 和 factory-method，factory-bean 指向工厂实现类，factory-method 指向工厂实例化方法。

   或者直接在静态方法上添加 @Bean

   ```xml
   <bean id="userByFactoryMethod" factory-bean="userFactory" factory-method="create"/>
   <bean id="userFactory" class="io.github.beastars1.bean.factory.DefaultUserFactory"/>
   ```

   或者直接在 UserFactory 的 create() 方法上添加 @Bean，使用 applicationContext.register(DefaultUserFactory.class) 方法加入 Bean 容器中。

4. 通过 FactoryBean 实现

   创建一个类实现 `FactoryBean<User>` 方法，将创建的类加入到 Bean 容器中。

   getObject() 就是生成 Bean 的方法；

   getObjectType() 是 Bean 的类型；

   isSingleton() 是每次获取 Bean 时是否是单例。

5. 通过 spi 机制加载

   将接口实现类的全限定名配置在文件中，并由 ServerLoader 读取配置文件，加载实现类。如 `SPIBeanInstantiationDemo`。

### 4. Bean 的初始化

#### 1. 初始化处理

Bean 在 Spring 加载上下文进行初始化时，先执行以下步骤。

1. @PostConstruct 注解
2. @Bean 中指定 initMethod / xml 指定 init-method
3. Bean 实现 InitializingBean 接口中的 afterPropertiesSet() 方法

三种方法的执行顺序是 1>3>2。

#### 2. 延迟初始化

Bean 是在 Spring 加载的时候初始化的，也就是在 AbstractApplicationContext.refresh() 时进行加载的，但是这时加载只会加载单例并且不延迟初始化的 Bean。

非延迟初始化的 Bean 会在加载上下文时调用初始化方法进行加载，延迟初始化的 Bean 会在真正调用时才进行初始化。

1. 在 Bean 上添加 @Lazy 注解；
2. xml 中添加 `<bean lazy-init="true"/>`。

### 5. 销毁 Bean

Bean 在 Spring 关闭上下文前，先执行以下步骤。

1. @PreDestroy 注解
2. @Bean 中指定 destroyMethod / xml 指定 destroy-method
3. Bean 实现 DisposableBean 接口中的 destroy() 方法

三种方法的执行顺序是 1>3>2。