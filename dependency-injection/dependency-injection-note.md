# 依赖注入

## 依赖注入的方式

### 1. setter 方法注入

1. 在 xml 中的 bean 标签中添加 property 标签，name 指向要注入的字段的名字，ref 指向要注入的 BeanName。

    ```xml
    <bean class="io.github.beastars1.dependency.injection.UserHolder">
        <!-- 通过setter方法注入 -->
        <property name="user" ref="son"/> 
    </bean>
    ```

    其中 name="user" 的 user 其实是 user 字段的 setter 方法 `setUser()` 去掉 set 后的样子，如果 user 的 setter 方法是 `setUser2()`，那么 property 中就应该是 name="user2"。

2. 也可以手动通过构建 BeanDefinition 的方式进行注入

    ```java
    BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(UserHolder.class);
    // 指定引用的注入
    builder.addPropertyReference("user", "son");
    // 注册UserHolder的BeanDefinition
    applicationContext.registerBeanDefinition("userHolder", builder.getBeanDefinition());
    ```
    
3. 使用 autowire 自动注入时，其中的 byName 方式也是通过 setter 名字进行查找注入的

    ```xml
    <bean class="io.github.beastars1.dependency.injection.UserHolder" autowire="byName" />
    ```

    如果 UserHolder 的 setter 方法是 `setUser()`，就会寻找 BeanName 为 `user` 的 Bean 进行注入，如果 setter 方法是 `setUser123()`，就会寻找 BeanName 为 `user123` 的 Bean 进行注入。

> property 和 constructor-arg  会覆盖 autowire 

### 2. 构造器注入

1. 在 xml 中的 bean 标签中添加 constructor-arg 标签，name 指向构造方法的参数名，ref 指向要注入的 BeanName。会自动根据 bean 标签中的 constructor-arg 标签的数量和类型，自动匹配对应的构造方法。

    ```xml
    <bean class="io.github.beastars1.dependency.injection.UserHolder">
        <constructor-arg name="user" ref="son"/>
    </bean>
    ```

2. 手动通过构建 BeanDefinition 的方式进行注入

    ```java
    BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(UserHolder.class);
    // 指定要注入构造方法的引用，会根据参数自动匹配构造方法
    builder.addConstructorArgReference("son");
    builder.addConstructorArgReference("……");
    // 注册UserHolder的BeanDefinition
    applicationContext.registerBeanDefinition("userHolder", builder.getBeanDefinition());
    ```

3. 使用 autowire 自动注入

    ```xml
    <bean class="io.github.beastars1.dependency.injection.UserHolder" autowire="constructor" />
    ```

    autowire="constructor" 是 byType 模式，与构造方法的参数类型进行匹配。

    如果有多个构造方法，会优先使用多参数的构造方法。

### 3. 字段注入

1. @Autowired

   @Autowired 不能用在静态字段上。

   @Autowired 默认是 byType 查找依赖，如果需要 byName 模式，需要额外添加 @Qualifier("beanName") 注解来指定 BeanName。

   @Autowired 可以用在构造器、方法、参数、成员变量和注解上。

2. @Resource

   @Resource 默认是 byName，也就是将注解的字段名当作 BeanName 查找依赖，如果需要 byType 模式，需要指定 @Resource 的 type 参数。

   如果不指定 type，并且没有字段名对应 BeanName 的 Bean，就会变成 byType 模式。

   @Resource 可以用在类、方法和成员变量上。

### 4. 方法/参数注入

1. @Autowired/@Resource

   ```java
   private UserHolder userHolder;
   @Autowired/@Resource
   public void initHolder(UserHolder userHolder) {
       this.userHolder = userHolder;
   }
   ```

   会注入到方法的参数里。

2. @Bean

   ```java
   @Bean
   public UserHolder userHolder(User user) {
       return new UserHolder(user);
   }
   ```

### 5. 接口回调注入

Aware 系列接口回调

BeanFactoryAware ：获取 IoC 容器 - BeanFactory 

ApplicationContextAware ：获取 Spring 应用上下文 - ApplicationContext 对象 

EnvironmentAware ：获取 Environment 对象 

ResourceLoaderAware ：获取资源加载器 对象 - ResourceLoader 

BeanClassLoaderAware ：获取加载当前 Bean Class 的 ClassLoader 

BeanNameAware ：获取当前 Bean 的名

## 依赖注入方式的选择

- 低依赖：构造器注入（Spring 推荐）
- 多依赖：setter 方法注入
- 便利性：字段注入
- 声明类：方法注入（组合方式）

## 限定注入

@Qualifier

1. @Autowired 默认 byType，使用 @Qualifier 可以通过 byName 查找注入。

2. 逻辑分组，在 Bean 上添加 @Qualifier，会对添加了 @Qualifier 的 Bean 进行逻辑分组，将添加了 @Qualifier 注解的 Bean 分组，集合注入的时候可以和没有添加 @Qualifier 的 Bean 区分开。

   ```java
   @Autowired
   @Qualifier // 逻辑分组
   private List<User> anotherUsers; // user1 user2
   
   @Bean
   @Qualifier
   public User user1() {
       return new User("user1");
   }
   
   @Bean
   @Qualifier
   public User user2() {
       return new User("user2");
   }
   
   @Bean
   public User user3() {
       return new User("user3");
   }
   ```

3. 扩展限定

   @Qualifier 可以用在注解上，通过扩展的注解，可以更细化 Bean 的逻辑分组，同时如果获取 @Qualifier 的分组，也会获取到所有子分组的 Bean。

   ```java
   @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
   @Retention(RetentionPolicy.RUNTIME)
   @Inherited
   @Documented
   @Qualifier
   public @interface UserGroup {
   }
   ```

   ```java
   @Autowired
   @Qualifier 
   private List<User> qualifierUsers; // user1 user2 user3
   @Autowired
   @Qualifier 
   private List<User> userGroupUsers; // user3
   
   @Bean
   @Qualifier
   public User user1() {
       return new User("user1");
   }
   @Bean
   @Qualifier
   public User user2() {
       return new User("user2");
   }
   @Bean
   @UserGroup
   public User user3() {
       return new User("user3");
   }
   ```

## 延迟加载

1. ObjectFactory

2. ObjectProvider（推荐）：继承了 ObjectFactory

   注入 ObjectProvider，使用 Bean 时再通过 ObjectProvider 取出，达到延迟加载的效果。

   ```java
   @Autowired
   private User user; // 实时注入
   @Autowired
   private ObjectProvider<User> userProvider; // 延迟注入
   @Autowired
   private ObjectProvider<List<User>> userListProvider; // 延迟注入集合
   
   // userProvider.getObject()/getIfAvailable()/...
   ```

## 依赖处理过程

- 入口：DefaultListableBeanFactory#resolveDependency
- 依赖描述符：DependencyDescriptor
- 自定绑定候选对象处理器：AutowireCandidateResolve

## 自定义注解依赖注入

将自定义的注解添加到 AutowiredAnnotationBeanPostProcessor 中。

```java
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyAutowired {
}
```

可以额外注册一个自定义的 AutowiredAnnotationBeanPostProcessor Bean，在其中添加自定义的注解，就可以使用自定义的注解进行依赖注入了。

@Bean 使用在静态方法上，这个 Bean 会优先进行注册。

```java
@Bean // @Bean 使用在静态方法上，会优先注册
public static AutowiredAnnotationBeanPostProcessor beanPostProcessor() {
    AutowiredAnnotationBeanPostProcessor processor = new AutowiredAnnotationBeanPostProcessor();
    processor.setAutowiredAnnotationType(MyAutowired.class);
    return processor;
}
```

```java
@MyAutowired
private User myUser;
```
