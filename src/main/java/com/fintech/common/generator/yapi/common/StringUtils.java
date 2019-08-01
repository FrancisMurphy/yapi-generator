package com.fintech.common.generator.yapi.common;

public class StringUtils
{
    public static String getUpperCase(String humpName)
    {
        char[] chars = humpName.toCharArray();
        if (chars[0] >= 'a' && chars[0] <= 'z') {
            chars[0] = (char)(chars[0] - 32);
        }
        return new String(chars);
    }
}
