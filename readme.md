# **工作日志**

## 一、Ioc容器的初始化

1) WinterClassPathXmlApplicationContext读取配置文件
将配置文件路径解析加载成WinterBeanDefinition对象、将生成的所有
WinterBeanDefinition对象存入kv键值对的beanDefinitionMap中

2) 配置文件的解析交给WinterBeanDefinitionReader处理
将所有的application.properties定义的包下的所有文件放入文件List集合中

3) 将得到的所有List<String> 解析为List<WinterBeanDefinition>
之后将List<WinterBeanDefinition>进行注册到beanDefinitionMap中完成ioc的初始化

## 二、DI初始化

1) 当需要依赖注入时、通过getBean方法启动手动注入、并且扫描当前需要注入的对象中拥有哪些
加上了WinterAutoWired的注解、将其自动注入、自动注入完成
### 2.1 解决循环依赖的问题
    
##  三、mvc初始化

1) 底层采用servlet完成9大组件的初始化

2) WinterHandleMapping初始化完成 doDisPatcher处理用户请求, 在handlemapping中、寻找对应的handlemapping、再对其进行解析

3) 解析用户发送的parametermap参数、完成方法中形参与实参的相互对应、完成@WinterRequestParm()注解初始化

4) 返回结果封装成ModelAndView对象交给视图解析器解析

5) 视图解析器解析成功后返回WinterView对象渲染页面视图

## 四、aop初始化

# 更新
1) DI模块中先注入所有属性、再注入对象 
2) 修改于2021.11.20 10:28