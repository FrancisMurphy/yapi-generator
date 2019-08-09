package com.frank.generator.yapi.core.pojo.original;

import com.fintech.common.generator.yapi.common.exception.YapiAbandonException;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.StringJoiner;

/**
 * @author frank
 */
@Data
@Document(collection = "interface")
public class OYapiInterface implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @Field("_id")
    private String id;

    @Field("project_id")
    private String projectId;

    @Field("title")
    private String title;

    @Field("markdown")
    private String markdown;

    @Field("res_body")
    private String resBody;

    @Field("req_body_other")
    private String reqBodyOther;

    @Field("path")
    private String path;

    @Field("method")
    private String method;

    public void isValid()
    {
        if(StringUtils.isEmpty(resBody)||StringUtils.isEmpty(reqBodyOther))
        {
            throw new YapiAbandonException("###YAPI GENERATOR ERROR### The yapi(id:"+ id +") config is invalid, please check...");
        }
    }

    @Override
    public String toString()
    {
        return new StringJoiner(", ",
                OYapiInterface.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("projectId='" + projectId + "'")
                .add("title='" + title + "'")
                .add("markdown='" + markdown + "'")
                .add("resBody='" + resBody + "'")
                .add("reqBodyOther='" + reqBodyOther + "'")
                .toString();
    }
}
