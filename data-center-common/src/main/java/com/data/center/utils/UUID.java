package com.data.center.utils;

import com.alibaba.cloud.commons.lang.StringUtils;
import org.springframework.util.DigestUtils;

public class UUID {

    // 生成随机字符串
    public static String getUUID() {
        return java.util.UUID.randomUUID().toString().replaceAll("-", "");
    }

    // MD5加密
    // hello -> abc123def456
    // hello + 3e4a8 -> abc123def456abc
    public static String md5(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

}
