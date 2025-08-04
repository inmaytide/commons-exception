package com.inmaytide.exception;

import com.inmaytide.exception.web.HttpResponseException;
import com.inmaytide.exception.web.domain.DefaultResponse;
import com.inmaytide.exception.web.domain.ErrorCode;
import com.inmaytide.exception.web.domain.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DefaultResponse 测试类
 *
 * @author inmaytide
 * @since 2024/01/15
 */
@DisplayName("DefaultResponse 测试")
class DefaultResponseTest {

    @Test
    @DisplayName("测试默认响应构建")
    void testDefaultResponseBuilder() {
        DefaultResponse response = DefaultResponse.builder()
                .code("TEST_001")
                .message("测试消息")
                .URL("/test")
                .placeholders("param1", "param2")
                .build();

        assertEquals("TEST_001", response.getCode());
        assertEquals("测试消息", response.getMessage());
        assertEquals("/test", response.getUrl());
        assertEquals(500, response.getStatus());
        assertNotNull(response.getTimestamp());
        assertEquals(2, response.getPlaceholders().size());
        assertEquals("param1", response.getPlaceholders().get(0));
        assertEquals("param2", response.getPlaceholders().get(1));
    }

    @Test
    @DisplayName("测试从异常构建响应")
    void testResponseFromException() {
        ErrorCode errorCode = new ErrorCode() {
            @Override
            public String value() {
                return "EXCEPTION_001";
            }

            @Override
            public String description() {
                return "异常测试{0}";
            }
        };

        HttpResponseException exception = new HttpResponseException(
                HttpStatus.BAD_REQUEST,
                errorCode,
                "参数错误"
        );

        DefaultResponse response = DefaultResponse.withException(exception)
                .URL("/api/test")
                .build();

        assertEquals("EXCEPTION_001", response.getCode());
        assertEquals("异常测试参数错误", response.getMessage());
        assertEquals("/api/test", response.getUrl());
        assertEquals(400, response.getStatus());
        assertEquals(exception.getTimestamp(), response.getTimestamp());
        assertEquals(1, response.getPlaceholders().size());
        assertEquals("参数错误", response.getPlaceholders().get(0));
    }

    @Test
    @DisplayName("测试响应序列化")
    void testResponseSerialization() {
        DefaultResponse response = DefaultResponse.builder()
                .code("SERIAL_001")
                .message("序列化测试")
                .URL("/serial")
                .build();

        String jsonString = response.toString();

        // 验证JSON格式
        assertTrue(jsonString.contains("\"code\": \"SERIAL_001\""));
        assertTrue(jsonString.contains("\"message\": \"序列化测试\""));
        assertTrue(jsonString.contains("\"url\": \"/serial\""));
        assertTrue(jsonString.contains("\"status\": 500"));
        assertTrue(jsonString.contains("\"timestamp\""));
    }

    @Test
    @DisplayName("测试字节数组转换")
    void testAsBytes() {
        DefaultResponse response = DefaultResponse.builder()
                .code("BYTES_001")
                .message("字节测试")
                .build();

        byte[] bytes = response.asBytes();
        String result = new String(bytes, StandardCharsets.UTF_8);

        assertTrue(result.contains("BYTES_001"));
        assertTrue(result.contains("字节测试"));
    }

    @Test
    @DisplayName("测试DataBuffer转换")
    void testAsDataBuffer() {
        DefaultResponse response = DefaultResponse.builder()
                .code("BUFFER_001")
                .message("Buffer测试")
                .build();

        DefaultDataBufferFactory factory = new DefaultDataBufferFactory();
        DataBuffer buffer = response.asDataBuffer(factory);

        String result = buffer.toString(StandardCharsets.UTF_8);
        assertTrue(result.contains("BUFFER_001"));
        assertTrue(result.contains("Buffer测试"));
    }

    @Test
    @DisplayName("测试空字段处理")
    void testNullFieldHandling() {
        DefaultResponse response = DefaultResponse.builder().build();

        assertNull(response.getCode());
        assertNull(response.getMessage());
        assertNull(response.getUrl());
        assertEquals(500, response.getStatus());
        assertNotNull(response.getTimestamp());
        assertNull(response.getPlaceholders());
    }

    @Test
    @DisplayName("测试时间戳处理")
    void testTimestampHandling() {
        Instant customTime = Instant.parse("2024-01-15T10:30:00Z");

        DefaultResponse response = DefaultResponse.builder()
                .build();

        // 验证时间戳在合理范围内
        Instant now = Instant.now();
        assertTrue(response.getTimestamp().isBefore(now.plusSeconds(1)));
        assertTrue(response.getTimestamp().isAfter(now.minusSeconds(1)));
    }

    @Test
    @DisplayName("测试占位符列表")
    void testPlaceholdersList() {
        DefaultResponse response = DefaultResponse.builder()
                .placeholders("first", "second", "third")
                .build();

        assertEquals(3, response.getPlaceholders().size());
        assertEquals("first", response.getPlaceholders().get(0));
        assertEquals("second", response.getPlaceholders().get(1));
        assertEquals("third", response.getPlaceholders().get(2));
    }

    @Test
    @DisplayName("测试响应接口实现")
    void testResponseInterface() {
        DefaultResponse response = DefaultResponse.builder()
                .code("INTERFACE_001")
                .message("接口测试")
                .build();

        // 验证实现了Response接口
        assertInstanceOf(Response.class, response);

        // 验证接口方法正常工作
        byte[] bytes = response.asBytes();
        assertNotNull(bytes);
        assertTrue(bytes.length > 0);
    }

    @Test
    @DisplayName("测试异常响应构建器链式调用")
    void testExceptionBuilderChaining() {
        ErrorCode errorCode = new ErrorCode() {
            @Override
            public String value() {
                return "CHAIN_001";
            }

            @Override
            public String description() {
                return "链式调用测试";
            }
        };

        HttpResponseException exception = new HttpResponseException(
                HttpStatus.NOT_FOUND,
                errorCode
        );

        DefaultResponse response = DefaultResponse.withException(exception)
                .URL("/chain/test")
                .message("自定义消息")
                .code("CUSTOM_CODE")
                .placeholders("custom", "params")
                .build();

        assertEquals("CUSTOM_CODE", response.getCode());
        assertEquals("自定义消息", response.getMessage());
        assertEquals("/chain/test", response.getUrl());
        assertEquals(404, response.getStatus());
        assertEquals(2, response.getPlaceholders().size());
    }

    @Test
    @DisplayName("测试JSON格式完整性")
    void testJsonFormatCompleteness() {
        DefaultResponse response = DefaultResponse.builder()
                .code("JSON_001")
                .message("JSON格式测试")
                .URL("/json")
                .placeholders("param1")
                .build();

        String json = response.toString();

        // 验证所有必需字段都存在
        assertTrue(json.contains("\"timestamp\""));
        assertTrue(json.contains("\"status\""));
        assertTrue(json.contains("\"code\""));
        assertTrue(json.contains("\"message\""));
        assertTrue(json.contains("\"url\""));
        assertTrue(json.contains("\"placeholders\""));

        // 验证JSON结构
        assertTrue(json.startsWith("{"));
        assertTrue(json.endsWith("}"));
    }
} 
