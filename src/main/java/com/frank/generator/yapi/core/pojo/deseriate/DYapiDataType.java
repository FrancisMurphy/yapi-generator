package com.frank.generator.yapi.core.pojo.deseriate;

import lombok.Getter;

import java.math.BigDecimal;

/**
 * @author frank
 */
public enum DYapiDataType
{
    /**
     * String 类型
     * 映射包名：java.lang.String
     */
    STRING("string",String.class),
    /**
     * number 类型
     * 映射包名：java.math.BigDecimal
     */
    NUMBER("number", BigDecimal.class),
    /**
     * 引用类型 类型
     * 映射包名一般为接口对应req或resp包下的
     */
    OBJECT("object",null),
    /**
     * 空 类型
     * 不生成
     */
    NULL("null",null);

    @Getter
    private String type;

    @Getter
    private Class clazz;

    DYapiDataType(String type,Class clazz)
    {
        this.type = type;
        this.clazz = clazz;
    }

    public static DYapiDataType getYapiDataType(String targetType)
    {
        for (DYapiDataType type : DYapiDataType.values())
        {
            if (type.getType().equals(targetType))
            {
                return type;
            }
        }
        return null;
    }
}
