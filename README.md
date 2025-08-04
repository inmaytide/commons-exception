# Commons Exception

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.org/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

一个基于 Spring Boot 的统一异常处理库，支持 Servlet 和 Reactive 两种 Web 架构，提供标准化的错误响应格式和灵活的异常转换机制。

## ✨ 特性

- 🚀 **双架构支持** - 同时支持 Servlet 和 Reactive Web 应用
- 🔧 **自动配置** - 零配置开箱即用，支持条件化加载
- 🌍 **国际化友好** - 错误码与描述分离，支持占位符替换
- 🔌 **高度可扩展** - 翻译器模式支持自定义异常转换逻辑
- 📦 **轻量级** - 最小化依赖，不影响应用性能
- 🛡️ **类型安全** - 基于接口设计，确保实现一致性

## 📋 系统要求

- Java 21+
- Spring Boot 3.x
- Spring Framework 6.x

## 🚀 快速开始

### 1. 添加依赖

#### Maven

```xml
<dependency>
    <groupId>com.inmaytide</groupId>
    <artifactId>commons-exception-starter</artifactId>
    <version>3.1.5</version>
</dependency>
```

#### Gradle

```gradle
implementation 'com.inmaytide:commons-exception-starter:3.1.5'
```

### 2. 自动配置

项目会自动配置异常处理器，无需额外配置即可使用。

### 3. 定义错误码

```java
public enum UserErrorCode implements ErrorCode {
    USER_NOT_FOUND("USER_001", "未找到id为{0}的用户信息"),
    USER_ALREADY_EXISTS("USER_002", "用户{0}已存在"),
    INVALID_USER_DATA("USER_003", "用户数据格式错误");

    private final String code;
    private final String description;

    UserErrorCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

    @Override
    public String value() {
        return code;
    }

    @Override
    public String description() {
        return description;
    }
}
```

### 4. 抛出异常

```java
@RestController
public class UserController {
    
    @GetMapping("/users/{id}")
    public User getUser(@PathVariable Long id) {
        User user = userService.findById(id);
        if (user == null) {
            throw new HttpResponseException(
                HttpStatus.NOT_FOUND, 
                UserErrorCode.USER_NOT_FOUND, 
                String.valueOf(id)
            );
        }
        return user;
    }
}
```

### 5. 响应格式

异常会自动转换为标准 JSON 响应：

```json
{
    "timestamp": "2024-01-15T10:30:00Z",
    "status": 404,
    "code": "USER_001",
    "message": "未找到id为123的用户信息",
    "url": "/users/123",
    "placeholders": ["123"]
}
```

## 📖 详细使用指南

### 错误码定义

错误码接口支持占位符功能，便于动态生成错误信息：

```java
public interface ErrorCode {
    String value();                    // 错误码

    String description();              // 错误描述（支持占位符）

    boolean requirePlaceholders();     // 是否需要占位符

    String getReplacedDescription(String... placeholders); // 替换占位符
}
```

#### 占位符使用示例

```java
// 定义带占位符的错误码
ErrorCode errorCode = new ErrorCode() {
            @Override
            public String value() {
                return "FILE_TOO_LARGE";
            }

            @Override
            public String description() {
                return "文件大小{0}MB超过限制{1}MB";
            }
        };

// 使用占位符
String message = errorCode.getReplacedDescription("50", "10");
// 结果: "文件大小50MB超过限制10MB"
```

### 异常类型

项目提供了多种预定义异常类型：

#### 基础异常

- `HttpResponseException` - 通用 HTTP 响应异常
- `BadRequestException` - 400 错误请求异常
- `UnauthorizedException` - 401 未授权异常
- `AccessDeniedException` - 403 访问被拒绝异常
- `ObjectNotFoundException` - 404 对象未找到异常
- `PathNotFoundException` - 404 路径未找到异常
- `ServiceUnavailableException` - 503 服务不可用异常

#### 使用示例

```java
// 使用预定义异常
throw new BadRequestException(UserErrorCode.INVALID_USER_DATA);

// 使用通用异常
throw new HttpResponseException(
    HttpStatus.CONFLICT,
    UserErrorCode.USER_ALREADY_EXISTS,
    "john.doe@example.com"
);
```

### 自定义异常转换器

如果需要处理特定的第三方异常，可以实现自定义转换器：

```java
@Component
public class CustomExceptionTranslator implements HttpExceptionTranslator {
    
    @Override
    public Optional<HttpResponseException> execute(Throwable e) {
        if (e instanceof CustomBusinessException cbe) {
            return Optional.of(new HttpResponseException(
                HttpStatus.BAD_REQUEST,
                BusinessErrorCode.CUSTOM_ERROR,
                cbe.getDetails()
            ));
        }
        return Optional.empty();
    }
}
```

## ⚙️ 配置选项

### 自动配置

项目支持条件化自动配置：

- `@ConditionalOnWebApplication` - 仅在 Web 应用中启用
- `@ConditionalOnClass` - 根据类路径中的依赖启用特定功能
- `@ConditionalOnMissingBean` - 避免与用户自定义 Bean 冲突

### 支持的第三方库

- **Feign** - 自动处理 `FeignException`
- **WebClient** - 自动处理 `WebClientResponseException`
- **Validation** - 自动处理 `MethodArgumentNotValidException`

## 🧪 测试

### 运行测试

```bash
# 运行所有测试
mvn test

# 运行特定测试类
mvn test -Dtest=RequirePlaceholdersTest

# 生成测试报告
mvn surefire-report:report
```

### 测试覆盖

项目包含以下测试：

- 占位符功能测试
- 异常转换测试
- 响应格式测试
- 集成测试

## 📦 项目结构

```
commons-exception/
├── commons-exception/              # 核心库
│   ├── src/main/java/
│   │   └── com/inmaytide/exception/
│   │       ├── web/               # Web 异常处理
│   │       │   ├── domain/        # 响应域对象
│   │       │   ├── reactive/      # Reactive 异常处理
│   │       │   ├── servlet/       # Servlet 异常处理
│   │       │   └── translator/    # 异常转换器
│   │       └── translator/        # 通用转换器接口
│   └── src/test/java/             # 测试代码
└── commons-exception-starter/      # 自动配置模块
    └── src/main/java/
        └── com/inmaytide/exception/starter/
            └── config/            # 自动配置类
```

## 🔧 开发指南

### 构建项目

```bash
# 编译
mvn compile

# 打包
mvn package

# 安装到本地仓库
mvn install

# 部署到远程仓库
mvn deploy
```

### 添加新异常类型

1. 在 `web` 包下创建新的异常类
2. 继承 `HttpResponseException`
3. 实现相应的转换器
4. 在自动配置中注册转换器

### 添加新转换器

1. 实现 `HttpExceptionTranslator` 接口
2. 添加 `@Component` 注解
3. 在自动配置中注册（可选）

## 🤝 贡献指南

1. Fork 项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 🙏 致谢

感谢所有为这个项目做出贡献的开发者。

## 📞 联系方式

- 项目维护者: inmaytide
- 项目地址: [GitHub Repository](https://github.com/inmaytide/commons-exception)
- 问题反馈: [Issues](https://github.com/inmaytide/commons-exception/issues)

---

⭐ 如果这个项目对您有帮助，请给我们一个星标！ 
