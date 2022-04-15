## log-collector-agent
#### 基于ja-netfilter 的字节码增强插件；
用于日志收集 ，跨应用的日志收集 并把日志推送到redis 指定的channel中；

#### 使用方法：
##### 1、指定javaagent 
在jvm参数中加入
``-javaagent:.\ja-netfilter.jar={appName}``

其他参数见ja文档  ;
其中appName 用于标识应用
如果指定了appName， 则需要复制plugins 到 plugins-{appName}
##### 2、打印日志
在不同应用中，通过使用log.info函数打印日志，如果日志需要用于统一收集
则可以参考如下代码
```
        <dependency>
            <groupId>com.hybzzz</groupId>
            <artifactId>log-collector-common</artifactId>
            <version>1.0-SNAPSHOT</version>
            <scope>compile</scope>
        </dependency>


```
```
// 获取log marker， 用于标记是否被收集 ，以及日志批次 
Marker test_marker = LogCollectorMarkerFactory.getMarker("本次输出的日志标记");
// 使用marker 做日志打印
log.info(test_marker,"ppppppasa:{}",123123);
```
##### 3、 日志输出
使用LogCollectorMarker 标记的日志 将会被收集，并推送到redis的channel中，channel名称为：csplice_log_{appName}_{marker}
同时 会在ja目录下 生成logs-{appName}文件夹 ，打印应用的日志

#### 4、配置文件
配置文件遵循ja-netfilter项目规范，在config-{appName}文件夹下，logplugin.conf

```
[REDIS]# redis 地址
EQUAL,redis://localhost:6379
[CHANNEL] # 推送频道
PREFIX,log_collector
[LOG] # 日志拦截规则
PREFIX,test

```