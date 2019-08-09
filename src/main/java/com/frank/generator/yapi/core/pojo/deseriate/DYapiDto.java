package com.frank.generator.yapi.core.pojo.deseriate;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.StringJoiner;

@Data
@EqualsAndHashCode(callSuper = true)
public class DYapiDto extends DYapiObject
{
    /**
     * 响应标题
     */
    private String title;

    public DYapiDto()
    {
    }

    @Override
    public String toString()
    {
        return new StringJoiner(", ",
                DYapiDto.class.getSimpleName() + "[", "]")
                .add("title='" + title + "'")
                .toString();
    }
}
