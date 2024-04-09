package com.data.center.utils;

import java.io.InputStream;

public class FileUtil {

    /**
     * 解析字段去除单引号
     */
    public static String splitResult(String once) {
        String result = "";
        for (int i = 0; i < once.length(); i++) {
            if (once.charAt(i) != '\'') {
                result += once.charAt(i);
            }
        }
        return result;
    }

    /**
     * 分析编码格式
     */
    public static String getFileCode(InputStream in) {
        try {
            int p = (in.read() << 8) + in.read();
            String code = "GBK";

            switch (p) {
                case 59524:
                    code = "UTF-8";
                    break;
                case 0xfffe:
                    code = "Unicode";
                    break;
                case 0xfeff:
                    code = "UTF-16BE";
                    break;
                case 48581:
                    code = "GBK";
                    break;
                default:
            }
            return code;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
