package com.fintech.common.generator.yapi.yapi.pojo.analysis;

import lombok.Data;

@Data
public class AYapiFunc
{
    /**
     * 对应方法在Yapi的id
     */
    private String id;

    /**
     * 基本包名
     */
    private String pkg;

    /**
     * 请求url
     */
    private String path;

    /**
     * 接口名称(首字母小写驼峰)
     */
    private String name;

    /**
     * 接口名称(首字母大写驼峰)
     */
    private String upperCaseName;

    /**
     * dto包名 一般都为basePkg+".dto"
     */
    private String dtoPkg;

    /**
     * 方法描述
     */
    private String desc;

    /**
     * 请求描述
     */
    private String reqDesc;

    /**
     * 响应描述
     */
    private String respDesc;

    /**
     * 响应具体描述
     */
    private AYapiObject respDtos;

    /**
     * 请求具体描述
     */
    private AYapiObject reqDtos;

    /**
     * 所属微服务的接口
     */
    private String msName;

    /**
     * 微服务接口名
     */
    private String msInterface;

    /**
     * 微服务描述
     */
    private String msDesc;

}
