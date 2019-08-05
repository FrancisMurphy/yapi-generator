package com.fintech.common.generator.yapi.pojo.deseriate;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.StringJoiner;

@Data
@EqualsAndHashCode(callSuper = true)
public class YapiDto extends YapiObject
{
    /**
     * 响应标题
     */
    private String title;

    public YapiDto()
    {
    }

    @Override
    public String toString()
    {
        return new StringJoiner(", ",
                YapiDto.class.getSimpleName() + "[", "]")
                .add("title='" + title + "'")
                .toString();
    }
}
