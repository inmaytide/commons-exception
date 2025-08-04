# Commons Exception 使用示例

本文档提供了 `commons-exception` 库的详细使用示例。

## 快速开始示例

### 1. 基础异常处理

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

### 2. 错误码定义

```java
public enum UserErrorCode implements ErrorCode {
    USER_NOT_FOUND("USER_001", "未找到id为{0}的用户信息"),
    USER_ALREADY_EXISTS("USER_002", "用户{0}已存在"),
    INVALID_USER_DATA("USER_003", "用户数据格式错误"),
    USER_PERMISSION_DENIED("USER_004", "用户{0}没有权限访问{1}");

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

### 3. 使用预定义异常

```java

@Service
public class UserService {

    public User createUser(UserCreateRequest request) {
        // 验证用户数据
        if (!isValidUserData(request)) {
            throw new BadRequestException(UserErrorCode.INVALID_USER_DATA);
        }

        // 检查用户是否已存在
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new HttpResponseException(
                    HttpStatus.CONFLICT,
                    UserErrorCode.USER_ALREADY_EXISTS,
                    request.getEmail()
            );
        }

        // 创建用户
        return userRepository.save(request.toUser());
    }

    public void deleteUser(Long userId, String currentUser) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(
                        UserErrorCode.USER_NOT_FOUND,
                        String.valueOf(userId)
                ));

        if (!hasDeletePermission(currentUser, user)) {
            throw new AccessDeniedException(
                    UserErrorCode.USER_PERMISSION_DENIED,
                    currentUser,
                    "删除用户"
            );
        }

        userRepository.delete(user);
    }
}
```

## 高级使用示例

### 1. 自定义异常转换器

```java

@Component
public class CustomBusinessExceptionTranslator implements HttpExceptionTranslator {

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

    @Override
    public int getOrder() {
        return 100; // 高优先级
    }

    @Override
    public Logger getLogger() {
        return LoggerFactory.getLogger(this.getClass());
    }
}
```

### 2. 复杂错误码使用

```java
public enum FileErrorCode implements ErrorCode {
    FILE_TOO_LARGE("FILE_001", "文件{0}大小{1}MB超过限制{2}MB"),
    FILE_TYPE_NOT_ALLOWED("FILE_002", "文件类型{0}不被允许，支持的类型：{1}"),
    FILE_UPLOAD_FAILED("FILE_003", "文件{0}上传失败，原因：{1}");

    private final String code;
    private final String description;

    FileErrorCode(String code, String description) {
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

@RestController
public class FileController {

    @PostMapping("/upload")
    public FileInfo uploadFile(@RequestParam("file") MultipartFile file) {
        // 检查文件大小
        long fileSizeMB = file.getSize() / (1024 * 1024);
        if (fileSizeMB > MAX_FILE_SIZE_MB) {
            throw new HttpResponseException(
                    HttpStatus.PAYLOAD_TOO_LARGE,
                    FileErrorCode.FILE_TOO_LARGE,
                    file.getOriginalFilename(),
                    String.valueOf(fileSizeMB),
                    String.valueOf(MAX_FILE_SIZE_MB)
            );
        }

        // 检查文件类型
        String fileType = getFileExtension(file.getOriginalFilename());
        if (!ALLOWED_FILE_TYPES.contains(fileType)) {
            throw new HttpResponseException(
                    HttpStatus.BAD_REQUEST,
                    FileErrorCode.FILE_TYPE_NOT_ALLOWED,
                    fileType,
                    String.join(", ", ALLOWED_FILE_TYPES)
            );
        }

        // 上传文件
        try {
            return fileService.upload(file);
        } catch (IOException e) {
            throw new HttpResponseException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    FileErrorCode.FILE_UPLOAD_FAILED,
                    file.getOriginalFilename(),
                    e.getMessage()
            );
        }
    }
}
```

### 3. 异常链处理

```java

@Service
public class OrderService {

    public Order createOrder(OrderRequest request) {
        try {
            // 验证库存
            validateInventory(request.getItems());

            // 处理支付
            processPayment(request.getPaymentInfo());

            // 创建订单
            return orderRepository.save(request.toOrder());

        } catch (InventoryException e) {
            // 库存不足异常
            throw new HttpResponseException(
                    HttpStatus.CONFLICT,
                    OrderErrorCode.INSUFFICIENT_INVENTORY,
                    e.getProductId(),
                    String.valueOf(e.getAvailableQuantity())
            );
        } catch (PaymentException e) {
            // 支付异常
            throw new HttpResponseException(
                    HttpStatus.BAD_REQUEST,
                    OrderErrorCode.PAYMENT_FAILED,
                    e.getReason()
            );
        } catch (Exception e) {
            // 其他未知异常
            throw new HttpResponseException(e);
        }
    }
}
```

### 4. 响应格式自定义

```java

@RestController
public class ApiController {

    @ExceptionHandler(HttpResponseException.class)
    public ResponseEntity<DefaultResponse> handleHttpResponseException(
            HttpResponseException e,
            HttpServletRequest request) {

        DefaultResponse response = DefaultResponse.withException(e)
                .URL(request.getRequestURL().toString())
                .build();

        return ResponseEntity
                .status(e.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
```

## 配置示例

### 1. 自动配置

```yaml
# application.yml
spring:
    application:
        name: my-service

# 异常处理会自动配置，无需额外配置
```

### 2. 自定义配置

```java

@Configuration
public class ExceptionConfig {

    @Bean
    public HttpExceptionTranslator customTranslator() {
        return new CustomExceptionTranslator();
    }

    @Bean
    @Primary
    public HandlerExceptionResolver customExceptionResolver(
            HttpExceptionTranslatorDelegator delegator) {
        return new DefaultHandlerExceptionResolver(delegator);
    }
}
```

## 测试示例

### 1. 单元测试

```java

@SpringBootTest
class UserControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testUserNotFound() {
        ResponseEntity<DefaultResponse> response = restTemplate.getForEntity(
                "/users/999",
                DefaultResponse.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        DefaultResponse body = response.getBody();
        assertNotNull(body);
        assertEquals("USER_001", body.getCode());
        assertEquals("未找到id为999的用户信息", body.getMessage());
        assertEquals(404, body.getStatus());
    }

    @Test
    void testInvalidUserData() {
        UserCreateRequest request = new UserCreateRequest();
        // 设置无效数据

        ResponseEntity<DefaultResponse> response = restTemplate.postForEntity(
                "/users",
                request,
                DefaultResponse.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        DefaultResponse body = response.getBody();
        assertNotNull(body);
        assertEquals("USER_003", body.getCode());
    }
}
```

### 2. 集成测试

```java

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ExceptionHandlingIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testCompleteExceptionFlow() {
        // 测试完整的异常处理流程
        ResponseEntity<DefaultResponse> response = restTemplate.getForEntity(
                "/api/nonexistent",
                DefaultResponse.class
        );

        assertNotNull(response.getBody());
        assertTrue(response.getBody().toString().contains("timestamp"));
        assertTrue(response.getBody().toString().contains("status"));
        assertTrue(response.getBody().toString().contains("code"));
        assertTrue(response.getBody().toString().contains("message"));
    }
}
```

## 最佳实践

### 1. 错误码命名规范

```java
// 推荐：使用模块前缀
public enum UserErrorCode implements ErrorCode {
    USER_NOT_FOUND("USER_001", "用户不存在"),
    USER_INVALID_DATA("USER_002", "用户数据无效")
}

public enum OrderErrorCode implements ErrorCode {
    ORDER_NOT_FOUND("ORDER_001", "订单不存在"),
    ORDER_INVALID_STATUS("ORDER_002", "订单状态无效")
}
```

### 2. 异常处理层次

```java
// 1. 业务异常 - 可预期的异常
throw new BadRequestException(UserErrorCode.INVALID_USER_DATA);

// 2. 系统异常 - 不可预期的异常
throw new

HttpResponseException(e);

// 3. 权限异常 - 访问控制异常
throw new

AccessDeniedException(PermissionErrorCode.ACCESS_DENIED);
```

### 3. 日志记录

```java

@Slf4j
@Service
public class UserService {

    public User getUser(Long id) {
        try {
            return userRepository.findById(id)
                    .orElseThrow(() -> new ObjectNotFoundException(
                            UserErrorCode.USER_NOT_FOUND,
                            String.valueOf(id)
                    ));
        } catch (ObjectNotFoundException e) {
            log.warn("用户不存在: {}", id);
            throw e;
        } catch (Exception e) {
            log.error("获取用户信息失败: {}", id, e);
            throw new HttpResponseException(e);
        }
    }
}
```

## 常见问题

### 1. 如何禁用自动配置？

```java

@SpringBootApplication(exclude = {
        WebExceptionHandlerAutoConfiguration.class
})
public class MyApplication {
    // 自定义异常处理配置
}
```

### 2. 如何自定义响应格式？

```java

@Bean
public DefaultResponse customResponse(HttpResponseException e) {
    return DefaultResponse.withException(e)
            .message("自定义错误消息")
            .build();
}
```

### 3. 如何处理第三方库异常？

```java

@Component
public class ThirdPartyExceptionTranslator implements HttpExceptionTranslator {

    @Override
    public Optional<HttpResponseException> execute(Throwable e) {
        if (e instanceof ThirdPartyException) {
            return Optional.of(new HttpResponseException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    SystemErrorCode.THIRD_PARTY_ERROR,
                    e.getMessage()
            ));
        }
        return Optional.empty();
    }
}
```

这些示例展示了 `commons-exception` 库的主要功能和使用方法。根据具体需求，您可以进一步定制和扩展这些功能。 
