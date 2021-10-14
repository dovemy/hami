# 项目简介

&emsp;Hami是一款开箱即用的，基于SpringBoot的快速启动框架。  
&emsp;如果你熟悉SpringBoot开发，那么一定对Maven/Gradle等依赖引入不会陌生。几乎每个项目开发，我们都要引入相同Jar包，进行各种Bean注入和配置，这些都属于“重复劳动”。  
&emsp;Hami旨在整合常用依赖项，通过最少的配置，快速搭建SpringBoot项目，减少依赖管理和配置的复杂度，让开发人员更专注于业务开发。  
&emsp;（顺便一提，如果你是SpringBoot开发的新手，建议先尝试手动引入各个依赖项，亲手配置一遍）  

国内Gitee地址：https://gitee.com/dovemy/hami

# 版本说明
- JDK：8+
- SpringBoot：2.4.11
- Knife4j：2.0.9
- EasyExcel：2.2.6
- FastJson：1.2.70
- MybatisPlus：3.4.0  

（仅罗列的核心依赖，具体详情参见父pom->properties）

# 组成部分
## hami-web
&emsp;Web开发模块，包含以下功能：  
1. 跨域支持：支持开关式启动/关闭接口跨域支持
2. SwaggerAPI文档：  
    - 集成Knife4j：接口分组功能需需参考：https://doc.xiaominfo.com/
    - 内置ApiModel字段排序插件，让字段按照定义顺序出现在文档中
3. 异常处理
    - 内置通用异常类`BusinessException`
    - 内置全局异常处理器`GlobalExceptionHandler`，处理常见的参数异常（JSR303），并格式化提示信息抛出异常
    （提示：基于SpringBoot的自动扫描实现，启用时需要通过手动配置`@SpringBootApplication`的`scanBasePackages`属性 ）
4. Web常用工具  
    - AssertUtil：空指针、空串、空集合等断言工具
    - CookieUtil：添加、删除、查询Cookie
    - IPUtil：IP地址工具类
    - easyexcel包：excel上传、下载工具

本模块完整配置项：  
```yaml
hami:
  web:
    cors:
      enable: true
      allowed-header: *
      allowed-origin: *
    swagger:
          production: true
          dockets:
            test:
              groupName: 测试分组
              basePackage: io.github.dovemy.hamidemo.controller
              docTitle: 文档标题
              author: 作者名
              description: 文档描述
              version: 1.0.0
              
# 以下配置为knife4j原生配置：enable填true即可，production控制生产环境屏蔽露出
knife4j:
  enable: true
  production: false
```
## hami-mybatis-plus
&emsp;MybatisPlus模块，配置了分页器，默认数据库类型是MySQL  
本模块完整配置项：  
```yaml
hami:
  mybatis-plus:
    db-type: mysql
```
## hami-redis
&emsp;Redis模块，包含以下功能：  
1. RedisTemplate：使用JSON序列化器实现，支持JDK8的日期属性
2. Redis连接池：引入apache的`commons-pool2`，支持Redis连接池配置
3. SpringCache：配置了`CacheManager`，内置常用的过期时间，通过声明`cacheNames`即可指定过期时间，支持自定义拓展
4. 自定义全局RedisKey前缀：通过给`RedisTemplate`注入带前缀的key序列化器实现全局自定义key前缀（注意：需要使用`RedisTemplate`提供的API，前缀才能注入）
5. Redis锁：内置`RedisLockService`，快速使用分布式锁（可自行结合业务需求拓展）  
本模块完整配置项：  
```yaml
hami:
  redis:
    key-prefix: hami
    cache-ttl-entry:
      MINUTE_20: 1200
```
# 完整配置
```yaml
hami:
  web:
    cors:
      enable: true
      allowed-header: *
      allowed-origin: *
  mybatis-plus:
    db-type: mysql
  redis:
    key-prefix: hami
    cache-ttl-entry:
      MINUTE_20: 1200
```

# 快速开始
&emsp;按照项目需求，引入依赖。最新版依赖请查询[Maven中央仓库](https://search.maven.org/search?q=g:io.github.dovemy)  
```xml
<dependencies>
        <dependency>
            <groupId>io.github.dovemy</groupId>
            <artifactId>hami-web-spring-boot-starter</artifactId>
            <version>1.0.2</version>
        </dependency>
        <dependency>
            <groupId>io.github.dovemy</groupId>
            <artifactId>hami-mybatis-plus-spring-boot-starter</artifactId>
            <version>1.0.2</version>
        </dependency>
        <dependency>
            <groupId>io.github.dovemy</groupId>
            <artifactId>hami-redis-spring-boot-starter</artifactId>
            <version>1.0.2</version>
        </dependency>
    </dependencies>
```
若无法下载，请检查maven的settings.xml配置，阿里云镜像仓库存在多个版本，请用如下配置
```xml
<mirror>
  <id>alimaven</id>
  <mirrorOf>central</mirrorOf>
  <name>aliyun maven</name>
  <url>https://maven.aliyun.com/repository/central</url>
</mirror>
```

&emsp;详情请参见[Hami-Demo](https://github.com/dovemy/hami-demo)项目（[Gitee项目](https://gitee.com/dovemy/hami-demo)）

# 敬请期待
新增hami-rbac模块