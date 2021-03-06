package com.frank.generator.yapi.core.pojo.deseriate;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 基本数据类型
 * @author frank
 */
@Data
@EqualsAndHashCode
public abstract class DYapiBasicData
{

    /**
     * 数据类型{@Link com.kaylves.yapi.pojo.common.DataType}
     */
    private String type;

    /**
     * 成员描述
     */
    private String description;

    public DYapiDataType getType()
    {
        return DYapiDataType.getYapiDataType(type);
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
