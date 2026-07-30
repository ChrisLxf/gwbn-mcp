package cn.net.gwbn.ai.mcp.utils;

/**
 * @author lixiaofeng
 * @date 7/3/26 PM3:43
 **/
public class StringUtils {

    /**
     * 是否为空 null 或 ""
     */
    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    /**
     * 是否不为空
     */
    public static boolean isNotEmpty(CharSequence str) {
        return !isEmpty(str);
    }

    /**
     * 是否为空白 null / "" / 全空格
     */
    public static boolean isBlank(CharSequence str) {
        if (isEmpty(str)) {
            return true;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否非空白
     */
    public static boolean isNotBlank(CharSequence str) {
        return !isBlank(str);
    }

    /**
     * 去除首尾空格，null转为空串
     */
    public static String trim(String str) {
        return str == null ? "" : str.trim();
    }

    /**
     * 安全判等
     */
    public static boolean equals(CharSequence s1, CharSequence s2) {
        if (s1 == s2) {
            return true;
        }
        if (s1 == null || s2 == null) {
            return false;
        }
        return s1.toString().equals(s2.toString());
    }



    /**
     * 安全截取字符串
     */
    public static String sub(String str, int start, int end) {
        if (isEmpty(str)) {
            return "";
        }
        int len = str.length();
        start = Math.max(start, 0);
        end = Math.min(end, len);
        if (start >= end) {
            return "";
        }
        return str.substring(start, end);
    }
}