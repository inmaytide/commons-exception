package com.inmaytide.exception;

import com.inmaytide.exception.web.domain.ErrorCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.NumberUtils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

/**
 * @author inmaytide
 * @since 2023/1/31
 */
public class RequirePlaceholdersTest {

    @Test
    public void testRegMatches() {
        Assertions.assertFalse(ErrorCode.REQUIRE_PLACEHOLDERS.matcher("上传的文件大小超过服务器限制").find());

        Matcher matcher = ErrorCode.REQUIRE_PLACEHOLDERS.matcher("未找到id为{0}的用户信息");
        Assertions.assertTrue(matcher.find());
        Assertions.assertEquals(matcher.group(), "{0}");

        matcher = ErrorCode.REQUIRE_PLACEHOLDERS.matcher("A{0}B{1}C{2}D{3}E{4}F{11}");
        Assertions.assertTrue(matcher.find());
        Assertions.assertEquals(matcher.group(), "{0}");
        Assertions.assertTrue(matcher.find());
        Assertions.assertEquals(matcher.group(), "{1}");
        Assertions.assertTrue(matcher.find());
        Assertions.assertEquals(matcher.group(), "{2}");
        Assertions.assertTrue(matcher.find());
        Assertions.assertEquals(matcher.group(), "{3}");
        Assertions.assertTrue(matcher.find());
        Assertions.assertEquals(matcher.group(), "{4}");
        Assertions.assertTrue(matcher.find());
        Assertions.assertEquals(matcher.group(), "{11}");
        Assertions.assertFalse(matcher.find());
    }

    @Test
    public void testGetReplacedDescription() {
        ErrorCode entity = new ErrorCode() {
            @Override
            public String value() {
                return "code";
            }

            @Override
            public String description() {
                return "A{0}B{1}C{2}D{3}E{4}F{5}";
            }
        };
        Assertions.assertEquals("AABBCCDDEEFF", entity.getReplacedDescription("A", "B", "C", "D", "E", "F"));
    }

    @Test
    public void testExceptions() {
        ErrorCode entity = new ErrorCode() {
            @Override
            public String value() {
                return "code";
            }

            @Override
            public String description() {
                return "A{0}B{1}C{2}D{3}E{4}F{5}G{6}";
            }
        };
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> entity.getReplacedDescription("A", "B", "C", "D", "E", "F"));
    }

}
