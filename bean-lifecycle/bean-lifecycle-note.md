# Bean 生命周期

### 1. Bean 元信息配置阶段

1. 面向资源
   - XML
   - Properties
2. 面向注解
3. 面向 API

### 2. Bean 元信息解析阶段

1. 面向资源

   - BeanDefinitionReader
   - XML 解析器 - BeanDefinitio

2. 面向注解

   - AnnotatedBeanDefinitionReader - 和 BeanDefinitionReader 没有关系

     普通的 Class 作为 Component 注册到 Spring IoC 容器后，通常 Bean 名称为第一个字符小写，Bean 名称生成来自于 BeanNameGenerator，注解实现 AnnotationBeanNameGenerator。

### 3. Bean 注册阶段

- BeanDefinition 注册接口 - BeanDefinitionRegistry

### 4. BeanDefinition 合并阶段

- BeanDefinition 合并
  - 父子 BeanDefinition 合并
    1. 当前 BeanFactory 查找
    2. 层次性 BeanFactory 查找

### 5. Bean Class 加载阶段

### 6. Bean 实例化阶段

1. Bean 实例化前阶段
2. Bean 实例化阶段
3. Bean 实例化后阶段

### 7. Bean 属性赋值前阶段

### 8. Bean Aware 接口回调阶段

### 9. Bean 初始化阶段

1. Bean 初始化前阶段
2. Bean 初始化阶段
3. Bean 初始化后阶段
4. Bean 初始化完成阶段

### 10. Bean 销毁阶段

1. Bean 销毁前阶段
2. Bean 销毁阶段
3. Bean 垃圾收