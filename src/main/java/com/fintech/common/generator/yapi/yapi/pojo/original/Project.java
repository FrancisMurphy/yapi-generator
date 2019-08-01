package com.fintech.common.generator.yapi.yapi.pojo.original;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

/**
 * @author frank
 */
@Data
@Document(collection = "project")
public class Project implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @Field("_id")
    private String id;

    @Field("name")
    private String name;

    @Field("project_type")
    private String projectType;

}
