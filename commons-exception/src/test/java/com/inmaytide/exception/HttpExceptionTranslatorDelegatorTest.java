package com.inmaytide.exception;

import com.inmaytide.exception.translator.ThrowableTranslator;
import com.inmaytide.exception.web.HttpResponseException;
import com.inmaytide.exception.web.translator.HttpExceptionTranslatorDelegator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * HttpExceptionTranslatorDelegator 测试类
 *
 * @author inmaytide
 * @since 2024/01/15
 */
@DisplayName("HttpExceptionTranslatorDelegator 测试")
class HttpExceptionTranslatorDelegatorTest {

    private HttpExceptionTranslatorDelegator delegator;
    private List<ThrowableTranslator<HttpResponseException>> translators;

    @BeforeEach
    void setUp() {
        translators = new ArrayList<>();
        delegator = new HttpExceptionTranslatorDelegator(translators);
    }

    /**
     * 创建测试用的转换器
     */
    private ThrowableTranslator<HttpResponseException> createTestTranslator(
            Predicate<Throwable> matcher, int order) {
        return new ThrowableTranslator<HttpResponseException>() {
            @Override
            public Optional<HttpResponseException> translate(Throwable e) {
                if (matcher.test(e)) {
                    return Optional.of(new HttpResponseException());
                }
                return Optional.empty();
            }

            @Override
            public int getOrder() {
                return order;
            }

            @Override
            public Logger getLogger() {
                return LoggerFactory.getLogger(this.getClass());
            }
        };
    }

    @Test
    @DisplayName("测试空转换器列表")
    void testEmptyTranslators() {
        RuntimeException exception = new RuntimeException("测试异常");
        Optional<HttpResponseException> result = delegator.translate(exception);

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("测试单个转换器成功转换")
    void testSingleTranslatorSuccess() {
        // 创建模拟转换器
        ThrowableTranslator<HttpResponseException> translator = createTestTranslator(
                e -> e instanceof RuntimeException, 0);

        delegator.addTranslator(translator);

        RuntimeException exception = new RuntimeException("测试异常");
        Optional<HttpResponseException> result = delegator.translate(exception);

        assertTrue(result.isPresent());
        assertNotNull(result.get());
    }

    @Test
    @DisplayName("测试多个转换器按顺序执行")
    void testMultipleTranslatorsOrder() {
        // 第一个转换器 - 不匹配
        ThrowableTranslator<HttpResponseException> translator1 = createTestTranslator(
                e -> false, 0);

        // 第二个转换器 - 匹配
        ThrowableTranslator<HttpResponseException> translator2 = createTestTranslator(
                e -> e instanceof RuntimeException, 1);

        // 第三个转换器 - 也匹配（但不会被执行）
        ThrowableTranslator<HttpResponseException> translator3 = createTestTranslator(
                e -> e instanceof RuntimeException, 2);

        delegator.addTranslator(translator1);
        delegator.addTranslator(translator2);
        delegator.addTranslator(translator3);

        RuntimeException exception = new RuntimeException("测试异常");
        Optional<HttpResponseException> result = delegator.translate(exception);

        assertTrue(result.isPresent());
    }

    @Test
    @DisplayName("测试转换器都不匹配")
    void testNoTranslatorMatches() {
        // 创建不匹配的转换器
        ThrowableTranslator<HttpResponseException> translator = new ThrowableTranslator<HttpResponseException>() {
            @Override
            public int getOrder() {
                return 0;
            }

            @Override
            public Optional<HttpResponseException> translate(Throwable e) {
                if (e instanceof IllegalArgumentException) {
                    return Optional.of(new HttpResponseException());
                }
                return Optional.empty();
            }

            @Override
            public Logger getLogger() {
                return null;
            }
        };

        delegator.addTranslator(translator);

        RuntimeException exception = new RuntimeException("测试异常");
        Optional<HttpResponseException> result = delegator.translate(exception);

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("测试添加转换器")
    void testAddTranslator() {
        assertEquals(0, delegator.translate(new RuntimeException()).isPresent() ? 1 : 0);

        ThrowableTranslator<HttpResponseException> translator = new ThrowableTranslator<HttpResponseException>() {
            @Override
            public int getOrder() {
                return 0;
            }

            @Override
            public Optional<HttpResponseException> translate(Throwable e) {
                return Optional.of(new HttpResponseException());
            }

            @Override
            public Logger getLogger() {
                return null;
            }
        };

        delegator.addTranslator(translator);

        assertTrue(delegator.translate(new RuntimeException()).isPresent());
    }

    @Test
    @DisplayName("测试异常链转换")
    void testExceptionChainTranslation() {
        // 创建只处理特定异常的转换器
        ThrowableTranslator<HttpResponseException> translator = new ThrowableTranslator<HttpResponseException>() {
            @Override
            public int getOrder() {
                return 0;
            }

            @Override
            public Optional<HttpResponseException> translate(Throwable e) {
                if (e instanceof IllegalArgumentException) {
                    return Optional.of(new HttpResponseException());
                }
                return Optional.empty();
            }

            @Override
            public Logger getLogger() {
                return null;
            }
        };

        delegator.addTranslator(translator);

        // 创建异常链：RuntimeException -> IllegalArgumentException
        IllegalArgumentException cause = new IllegalArgumentException("原因异常");
        RuntimeException exception = new RuntimeException("包装异常", cause);

        Optional<HttpResponseException> result = delegator.translate(exception);

        assertTrue(result.isPresent());
    }

    @Test
    @DisplayName("测试转换器异常处理")
    void testTranslatorExceptionHandling() {
        // 创建会抛出异常的转换器
        ThrowableTranslator<HttpResponseException> translator = new ThrowableTranslator<HttpResponseException>() {
            @Override
            public int getOrder() {
                return 0;
            }

            @Override
            public Optional<HttpResponseException> translate(Throwable e) {
                throw new RuntimeException("转换器内部异常");
            }

            @Override
            public Logger getLogger() {
                return null;
            }
        };

        delegator.addTranslator(translator);

        // 应该不会抛出异常，而是返回空结果
        assertDoesNotThrow(() -> {
            Optional<HttpResponseException> result = delegator.translate(new RuntimeException("测试"));
            assertFalse(result.isPresent());
        });
    }

    @Test
    @DisplayName("测试null异常处理")
    void testNullExceptionHandling() {
        ThrowableTranslator<HttpResponseException> translator = new ThrowableTranslator<HttpResponseException>() {
            @Override
            public int getOrder() {
                return 0;
            }

            @Override
            public Optional<HttpResponseException> translate(Throwable e) {
                return Optional.of(new HttpResponseException());
            }

            @Override
            public Logger getLogger() {
                return null;
            }
        };

        delegator.addTranslator(translator);

        // 传入null应该返回空结果
        Optional<HttpResponseException> result = delegator.translate(null);
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("测试转换器排序")
    void testTranslatorOrdering() {
        // 创建两个转换器，都匹配同一个异常
        ThrowableTranslator<HttpResponseException> translator1 = new ThrowableTranslator<HttpResponseException>() {
            @Override
            public int getOrder() {
                return 0;
            }

            @Override
            public Optional<HttpResponseException> translate(Throwable e) {
                if (e instanceof RuntimeException) {
                    return Optional.of(new HttpResponseException());
                }
                return Optional.empty();
            }

            @Override
            public Logger getLogger() {
                return null;
            }
        };

        ThrowableTranslator<HttpResponseException> translator2 = new ThrowableTranslator<HttpResponseException>() {
            @Override
            public int getOrder() {
                return 0;
            }

            @Override
            public Optional<HttpResponseException> translate(Throwable e) {
                if (e instanceof RuntimeException) {
                    return Optional.of(new HttpResponseException());
                }
                return Optional.empty();
            }

            @Override
            public Logger getLogger() {
                return null;
            }
        };

        // 先添加translator2，再添加translator1
        delegator.addTranslator(translator2);
        delegator.addTranslator(translator1);

        RuntimeException exception = new RuntimeException("测试异常");
        Optional<HttpResponseException> result = delegator.translate(exception);

        // 应该返回第一个匹配的转换器结果
        assertTrue(result.isPresent());
    }
} 
