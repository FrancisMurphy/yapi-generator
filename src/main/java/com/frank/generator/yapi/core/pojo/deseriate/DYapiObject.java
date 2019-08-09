/**
  * Copyright 2019 bejson.com
  */
package com.frank.generator.yapi.core.pojo.deseriate;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

/**
 * Yapi对象类型
 * @author frank
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DYapiObject extends DYapiBasicData
{
    /**
     * 必填参数
     */
    private List<String> required;

    /**
     * 对象下的成员
     */
    private Map<String, DYapiObject> properties;

    public DYapiObject()
    {
    }

    @Override
    public String toString()
    {
        return "YapiObject{" +
                "required=" + required +
                ", properties=" + properties +
                "} " + super.toString();
    }
}
