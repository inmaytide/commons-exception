package com.inmaytide.exception;

import com.inmaytide.exception.web.HttpResponseException;
import com.inmaytide.exception.web.domain.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * HttpResponseException 测试类
 *
 * @author inmaytide
 * @since 2024/01/15
 */
@DisplayName("HttpResponseException 测试")
class HttpResponseExceptionTest {

    @Test
    @DisplayName("测试默认构造函数")
    void testDefaultConstructor() {
        HttpResponseException exception = new HttpResponseException();

        assertEquals("未知异常", exception.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());
        assertEquals("exception_unknown", exception.getCode());
        assertNotNull(exception.getTimestamp());
        assertArrayEquals(new String[0], exception.getPlaceholders());
    }

    @Test
    @DisplayName("测试带参数的构造函数")
    void testParameterizedConstructor() {
        ErrorCode errorCode = new ErrorCode() {
            @Override
            public String value() {
                return "TEST_001";
            }

            @Override
            public String description() {
                return "测试错误{0}";
            }
        };

        HttpResponseException exception = new HttpResponseException(
                HttpStatus.BAD_REQUEST,
                errorCode,
                "参数错误"
        );

        assertEquals("测试错误参数错误", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("TEST_001", exception.getCode());
        assertArrayEquals(new String[]{"参数错误"}, exception.getPlaceholders());
    }

    @Test
    @DisplayName("测试带cause的构造函数")
    void testConstructorWithCause() {
        RuntimeException cause = new RuntimeException("原始异常");
        ErrorCode errorCode = new ErrorCode() {
            @Override
            public String value() {
                return "WRAPPED_001";
            }

            @Override
            public String description() {
                return "包装异常";
            }
        };

        HttpResponseException exception = new HttpResponseException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                errorCode,
                cause
        );

        assertEquals("包装异常", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals("WRAPPED_001", exception.getCode());
    }

    @Test
    @DisplayName("测试null参数处理")
    void testNullParameterHandling() {
        HttpResponseException exception = new HttpResponseException(null, null, (String[]) null);

        assertEquals("未知异常", exception.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());
        assertEquals("exception_unknown", exception.getCode());
        assertArrayEquals(new String[0], exception.getPlaceholders());
    }

    @Test
    @DisplayName("测试时间戳生成")
    void testTimestampGeneration() {
        Instant before = Instant.now();
        HttpResponseException exception = new HttpResponseException();
        Instant after = Instant.now();

        assertTrue(exception.getTimestamp().isAfter(before) || exception.getTimestamp().equals(before));
        assertTrue(exception.getTimestamp().isBefore(after) || exception.getTimestamp().equals(after));
    }

    @Test
    @DisplayName("测试多个占位符")
    void testMultiplePlaceholders() {
        ErrorCode errorCode = new ErrorCode() {
            @Override
            public String value() {
                return "MULTI_001";
            }

            @Override
            public String description() {
                return "用户{0}在{1}时间执行了{2}操作";
            }
        };

        HttpResponseException exception = new HttpResponseException(
                HttpStatus.OK,
                errorCode,
                "admin",
                "2024-01-15",
                "登录"
        );

        assertEquals("用户admin在2024-01-15时间执行了登录操作", exception.getMessage());
        assertArrayEquals(new String[]{"admin", "2024-01-15", "登录"}, exception.getPlaceholders());
    }

    @Test
    @DisplayName("测试序列化兼容性")
    void testSerializationCompatibility() {
        HttpResponseException exception = new HttpResponseException();

        // 验证所有字段都可以正常访问
        assertDoesNotThrow(() -> {
            exception.getTimestamp();
            exception.getStatus();
            exception.getCode();
            exception.getPlaceholders();
            exception.getMessage();
        });
    }

    @Test
    @DisplayName("测试异常链")
    void testExceptionChain() {
        RuntimeException original = new RuntimeException("原始错误");
        HttpResponseException wrapped = new HttpResponseException(original);

        assertEquals("未知异常", wrapped.getMessage());
        assertEquals(original, wrapped.getCause());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, wrapped.getStatus());
    }

    @Test
    @DisplayName("测试自定义错误码")
    void testCustomErrorCode() {
        ErrorCode customCode = new ErrorCode() {
            @Override
            public String value() {
                return "CUSTOM_001";
            }

            @Override
            public String description() {
                return "自定义错误信息";
            }
        };

        HttpResponseException exception = new HttpResponseException(
                HttpStatus.NOT_FOUND,
                customCode
        );

        assertEquals("自定义错误信息", exception.getMessage());
        assertEquals("CUSTOM_001", exception.getCode());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }
} 
