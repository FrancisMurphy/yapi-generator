package com.fintech.common.generator.api.yapi.pojo.analysis;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Set;

/**
 * @author frank
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AYapiObject extends AYapiBasicData
{

    List<AYapiBasicData> properties;

    Set<String> importPkg;

    public AYapiObject()
    {
    }

    public AYapiObject(String desc, String clazz, String name,
            List<AYapiBasicData> properties)
    {
        super(desc, clazz, name);
        this.properties = properties;
    }
}
