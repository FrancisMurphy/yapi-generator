package com.fintech.common.generator.api.mongodb.doc;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.StringJoiner;

/**
 * @author frank
 */
@Data
@Document(collection = "interface")
public class YapiInterface implements Serializable
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

    @Override
    public String toString()
    {
        return new StringJoiner(", ",
                YapiInterface.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("projectId='" + projectId + "'")
                .add("title='" + title + "'")
                .add("markdown='" + markdown + "'")
                .add("resBody='" + resBody + "'")
                .add("reqBodyOther='" + reqBodyOther + "'")
                .toString();
    }
}
