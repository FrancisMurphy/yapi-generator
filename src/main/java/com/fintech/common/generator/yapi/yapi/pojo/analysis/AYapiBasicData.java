package com.fintech.common.generator.yapi.yapi.pojo.analysis;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author frank
 */
@Data
@EqualsAndHashCode
public class AYapiBasicData
{
    /**
     * 字段描述
     */
    private String desc;

    /**
     * 字段类型
     */
    private String clazz;

    /**
     * 字段名称
     */
    private String name;

    public AYapiBasicData()
    {
    }

    public AYapiBasicData(String desc, String clazz, String name)
    {
        this.desc = desc;
        this.clazz = clazz;
        this.name = name;
    }
}
