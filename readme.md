**工作日志**

一、Ioc容器的初始化

1) WinterClassPathXmlApplicationContext读取配置文件
将配置文件路径解析加载成WinterBeanDefinition对象、将生成的所有
WinterBeanDefinition对象存入kv键值对的beanDefinitionMap中

2) 配置文件的解析交给WinterBeanDefinitionReader处理
将所有的application.properties定义的包下的所有文件放入文件List集合中

3) 将得到的所有List<String> 解析为List<WinterBeanDefinition>
之后将List<WinterBeanDefinition>进行注册到beanDefinitionMap中完成ioc的初始化

二、DI初始化

1) 当需要依赖注入时、通过getBean方法启动手动注入、并且扫描当前需要注入的对象中拥有哪些
加上了WinterAutoWired的注解、将其自动注入、自动注入完成

三、mvc初始化

1) 底层采用servlet完成9大组件的初始化

2) WinterHandleMapping初始化完成
   
2) WinterHandleAdapters初始化完成、doDisPatcher处理用户请求, 在handlemapping中、寻找对应的handlemapping、再对其进行解析