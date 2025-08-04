package com.inmaytide.exception;

import com.inmaytide.exception.translator.ThrowableTranslator;
import com.inmaytide.exception.web.HttpResponseException;
import com.inmaytide.exception.web.domain.DefaultResponse;
import com.inmaytide.exception.web.domain.ErrorCode;
import com.inmaytide.exception.web.translator.HttpExceptionTranslator;
import com.inmaytide.exception.web.translator.HttpExceptionTranslatorDelegator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 集成测试类 - 测试整个异常处理流程
 *
 * @author inmaytide
 * @since 2024/01/15
 */
@DisplayName("异常处理集成测试")
class IntegrationTest {

    private HttpExceptionTranslatorDelegator delegator;
    private List<ThrowableTranslator<HttpResponseException>> translators;

    @BeforeEach
    void setUp() {
        translators = new ArrayList<>();
        delegator = new HttpExceptionTranslatorDelegator(translators);
    }

    @Test
    @DisplayName("测试完整的异常处理流程")
    void testCompleteExceptionHandlingFlow() {
        // 1. 定义错误码
        ErrorCode errorCode = new ErrorCode() {
            @Override
            public String value() {
                return "INTEGRATION_001";
            }

            @Override
            public String description() {
                return "集成测试错误：{0}";
            }
        };

        // 2. 创建异常
        HttpResponseException exception = new HttpResponseException(
                HttpStatus.BAD_REQUEST,
                errorCode,
                "参数验证失败"
        );

        // 3. 创建响应
        DefaultResponse response = DefaultResponse.withException(exception)
                .URL("/api/test")
                .build();

        // 4. 验证响应内容
        assertEquals("INTEGRATION_001", response.getCode());
        assertEquals("集成测试错误：参数验证失败", response.getMessage());
        assertEquals("/api/test", response.getUrl());
        assertEquals(400, response.getStatus());
        assertEquals(1, response.getPlaceholders().size());
        assertEquals("参数验证失败", response.getPlaceholders().get(0));

        // 5. 验证JSON序列化
        String json = response.toString();
        assertTrue(json.contains("\"code\": \"INTEGRATION_001\""));
        assertTrue(json.contains("\"message\": \"集成测试错误：参数验证失败\""));
        assertTrue(json.contains("\"status\": 400"));
    }

    @Test
    @DisplayName("测试异常转换器集成")
    void testExceptionTranslatorIntegration() {
        // 创建自定义转换器
        HttpExceptionTranslator customTranslator = new HttpExceptionTranslator() {
            @Override
            public Optional<HttpResponseException> execute(Throwable e) {
                if (e instanceof IllegalArgumentException) {
                    ErrorCode errorCode = new ErrorCode() {
                        @Override
                        public String value() {
                            return "CUSTOM_001";
                        }

                        @Override
                        public String description() {
                            return "自定义转换错误：{0}";
                        }
                    };
                    return Optional.of(new HttpResponseException(
                            HttpStatus.BAD_REQUEST,
                            errorCode,
                            e.getMessage()
                    ));
                }
                return Optional.empty();
            }

            @Override
            public int getOrder() {
                return 0;
            }

            @Override
            public Logger getLogger() {
                return LoggerFactory.getLogger(this.getClass());
            }
        };

        delegator.addTranslator(customTranslator);

        // 测试转换
        IllegalArgumentException originalException = new IllegalArgumentException("参数错误");
        Optional<HttpResponseException> result = delegator.translate(originalException);

        assertTrue(result.isPresent());
        HttpResponseException converted = result.get();
        assertEquals("CUSTOM_001", converted.getCode());
        assertEquals("自定义转换错误：参数错误", converted.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, converted.getStatus());
    }

    @Test
    @DisplayName("测试异常链处理")
    void testExceptionChainHandling() {
        // 创建处理深层异常的转换器
        HttpExceptionTranslator deepTranslator = new HttpExceptionTranslator() {
            @Override
            public Optional<HttpResponseException> execute(Throwable e) {
                if (e instanceof NullPointerException) {
                    ErrorCode errorCode = new ErrorCode() {
                        @Override
                        public String value() {
                            return "NULL_POINTER_001";
                        }

                        @Override
                        public String description() {
                            return "空指针异常：{0}";
                        }
                    };
                    return Optional.of(new HttpResponseException(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            errorCode,
                            e.getMessage()
                    ));
                }
                return Optional.empty();
            }

            @Override
            public int getOrder() {
                return 0;
            }

            @Override
            public Logger getLogger() {
                return LoggerFactory.getLogger(this.getClass());
            }
        };

        delegator.addTranslator(deepTranslator);

        // 创建异常链：RuntimeException -> NullPointerException
        NullPointerException cause = new NullPointerException("对象为空");
        RuntimeException wrapper = new RuntimeException("包装异常", cause);

        Optional<HttpResponseException> result = delegator.translate(wrapper);

        assertTrue(result.isPresent());
        HttpResponseException converted = result.get();
        assertEquals("NULL_POINTER_001", converted.getCode());
        assertEquals("空指针异常：对象为空", converted.getMessage());
    }

    @Test
    @DisplayName("测试响应构建器链式调用")
    void testResponseBuilderChaining() {
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

        // 测试链式调用
        DefaultResponse response = DefaultResponse.withException(exception)
                .URL("/api/chain")
                .message("自定义消息")
                .code("CUSTOM_CODE")
                .placeholders("param1", "param2")
                .build();

        assertEquals("CUSTOM_CODE", response.getCode());
        assertEquals("自定义消息", response.getMessage());
        assertEquals("/api/chain", response.getUrl());
        assertEquals(404, response.getStatus());
        assertEquals(2, response.getPlaceholders().size());
    }

    @Test
    @DisplayName("测试字节数组和DataBuffer转换")
    void testByteArrayAndDataBufferConversion() {
        DefaultResponse response = DefaultResponse.builder()
                .code("CONVERSION_001")
                .message("转换测试")
                .URL("/api/conversion")
                .build();

        // 测试字节数组转换
        byte[] bytes = response.asBytes();
        String byteString = new String(bytes, java.nio.charset.StandardCharsets.UTF_8);
        assertTrue(byteString.contains("CONVERSION_001"));
        assertTrue(byteString.contains("转换测试"));

        // 测试DataBuffer转换
        org.springframework.core.io.buffer.DefaultDataBufferFactory factory =
                new org.springframework.core.io.buffer.DefaultDataBufferFactory();
        org.springframework.core.io.buffer.DataBuffer buffer = response.asDataBuffer(factory);
        String bufferString = buffer.toString(java.nio.charset.StandardCharsets.UTF_8);
        assertTrue(bufferString.contains("CONVERSION_001"));
        assertTrue(bufferString.contains("转换测试"));
    }

    @Test
    @DisplayName("测试错误码占位符功能")
    void testErrorCodePlaceholderFunctionality() {
        ErrorCode complexErrorCode = new ErrorCode() {
            @Override
            public String value() {
                return "COMPLEX_001";
            }

            @Override
            public String description() {
                return "复杂错误：用户{0}在{1}时间访问{2}资源时发生{3}错误";
            }
        };

        HttpResponseException exception = new HttpResponseException(
                HttpStatus.FORBIDDEN,
                complexErrorCode,
                "admin",
                "2024-01-15 10:30:00",
                "/api/secure",
                "权限不足"
        );

        DefaultResponse response = DefaultResponse.withException(exception)
                .URL("/api/secure")
                .build();

        assertEquals("COMPLEX_001", response.getCode());
        assertEquals("复杂错误：用户admin在2024-01-15 10:30:00时间访问/api/secure资源时发生权限不足错误",
                response.getMessage());
        assertEquals(403, response.getStatus());
        assertEquals(4, response.getPlaceholders().size());
    }
} 
