package com.inmaytide.exception;

import com.inmaytide.exception.web.domain.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ErrorCode 接口测试类
 *
 * @author inmaytide
 * @since 2024/01/15
 */
@DisplayName("ErrorCode 接口测试")
class ErrorCodeTest {

    @Test
    @DisplayName("测试错误码基本功能")
    void testBasicErrorCode() {
        ErrorCode errorCode = new ErrorCode() {
            @Override
            public String value() {
                return "TEST_001";
            }

            @Override
            public String description() {
                return "测试错误信息";
            }
        };

        assertEquals("TEST_001", errorCode.value());
        assertEquals("测试错误信息", errorCode.description());
        assertFalse(errorCode.requirePlaceholders());
    }

    @Test
    @DisplayName("测试带占位符的错误码")
    void testErrorCodeWithPlaceholders() {
        ErrorCode errorCode = new ErrorCode() {
            @Override
            public String value() {
                return "USER_001";
            }

            @Override
            public String description() {
                return "用户{0}不存在，ID: {1}";
            }
        };

        assertTrue(errorCode.requirePlaceholders());
        assertEquals("用户张三不存在，ID: 123",
                errorCode.getReplacedDescription("张三", "123"));
    }

    @Test
    @DisplayName("测试空描述的错误码")
    void testErrorCodeWithEmptyDescription() {
        ErrorCode errorCode = new ErrorCode() {
            @Override
            public String value() {
                return "EMPTY_001";
            }

            @Override
            public String description() {
                return "";
            }
        };

        assertFalse(errorCode.requirePlaceholders());
        assertEquals("", errorCode.getReplacedDescription("test"));
    }

    @Test
    @DisplayName("测试null描述的错误码")
    void testErrorCodeWithNullDescription() {
        ErrorCode errorCode = new ErrorCode() {
            @Override
            public String value() {
                return "NULL_001";
            }

            @Override
            public String description() {
                return null;
            }
        };

        assertFalse(errorCode.requirePlaceholders());
        assertNull(errorCode.getReplacedDescription("test"));
    }

    @Test
    @DisplayName("测试复杂占位符替换")
    void testComplexPlaceholderReplacement() {
        ErrorCode errorCode = new ErrorCode() {
            @Override
            public String value() {
                return "COMPLEX_001";
            }

            @Override
            public String description() {
                return "文件{0}上传失败，大小{1}MB，限制{2}MB，用户{3}";
            }
        };

        String result = errorCode.getReplacedDescription("test.jpg", "50", "10", "admin");
        assertEquals("文件test.jpg上传失败，大小50MB，限制10MB，用户admin", result);
    }

    @Test
    @DisplayName("测试占位符索引越界")
    void testPlaceholderIndexOutOfBounds() {
        ErrorCode errorCode = new ErrorCode() {
            @Override
            public String value() {
                return "BOUNDS_001";
            }

            @Override
            public String description() {
                return "测试{0}和{1}以及{2}";
            }
        };

        // 只提供两个参数，但需要三个占位符
        assertThrows(IndexOutOfBoundsException.class, () -> {
            errorCode.getReplacedDescription("A", "B");
        });
    }

    @Test
    @DisplayName("测试占位符格式验证")
    void testPlaceholderFormatValidation() {
        // 测试正确的占位符格式
        assertTrue(ErrorCode.REQUIRE_PLACEHOLDERS.matcher("测试{0}").find());
        assertTrue(ErrorCode.REQUIRE_PLACEHOLDERS.matcher("测试{1}和{2}").find());
        assertTrue(ErrorCode.REQUIRE_PLACEHOLDERS.matcher("测试{10}").find());

        // 测试错误的占位符格式
        assertFalse(ErrorCode.REQUIRE_PLACEHOLDERS.matcher("测试{}").find());
        assertFalse(ErrorCode.REQUIRE_PLACEHOLDERS.matcher("测试{a}").find());
        assertFalse(ErrorCode.REQUIRE_PLACEHOLDERS.matcher("测试{-1}").find());
        assertFalse(ErrorCode.REQUIRE_PLACEHOLDERS.matcher("测试{0.5}").find());
    }
} 
