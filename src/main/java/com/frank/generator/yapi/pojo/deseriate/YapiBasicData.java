package com.frank.generator.yapi.pojo.deseriate;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 基本数据类型
 * @author frank
 */
@Data
@EqualsAndHashCode
public abstract class YapiBasicData
{

    /**
     * 数据类型{@Link com.kaylves.yapi.pojo.common.DataType}
     */
    private String type;

    /**
     * 成员描述
     */
    private String description;

    public YapiDataType getType()
    {
        return YapiDataType.getYapiDataType(type);
    }

    @Override
    public String toString()
    {
        return "YapiBasicData{" +
                "type='" + type + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
