package com.inmaytide.exception.web.domain;

import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 错误码定义
 *
 * @author inmaytide
 * @since 2023/01/31
 */
public interface ErrorCode {

    Pattern REQUIRE_PLACEHOLDERS = Pattern.compile("\\{([0-9]+)}");

    /**
     * 获取错误码
     */
    String value();

    /**
     * 获取错误码描述信息, 可存在占位符
     * <p>
     * 占位符格式: "{n}" (n为正整数, 替换占位符替换值数组下标)
     */
    String description();

    /**
     * 判断该错误码描述信息是否存在需要替换的占位符
     *
     * <ul>
     *     <li>未找到id为{0}的用户信息 - true</li>
     *     <li>上传的文件大小超过服务器限制 - false</li>
     * </ul>
     */
    default boolean requirePlaceholders() {
        if (!StringUtils.hasText(description())) {
            return false;
        }
        return REQUIRE_PLACEHOLDERS.matcher(description()).groupCount() > 0;
    }

    /**
     * 获取替换过占位符的错误描述信息
     *
     * @param placeholders 占位符替换值数组
     */
    default String getReplacedDescription(String... placeholders) {
        String value = description();
        if (!StringUtils.hasText(value) || !requirePlaceholders()) {
            return value;
        }
        Matcher matcher = REQUIRE_PLACEHOLDERS.matcher(value);
        while (matcher.find()) {
            String key = matcher.group();
            int index = NumberUtils.parseNumber(key.replaceAll("[{}]", ""), Integer.class);
            value = value.replace(key, placeholders[index]);
        }
        return value;
    }


}
