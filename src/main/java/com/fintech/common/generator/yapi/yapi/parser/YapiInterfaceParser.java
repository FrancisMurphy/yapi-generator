package com.fintech.common.generator.yapi.yapi.parser;

import com.fintech.common.generator.yapi.common.StringUtils;
import com.fintech.common.generator.yapi.freemarker.FreeMarkerManager;
import com.fintech.common.generator.yapi.yapi.pojo.original.YapiInterface;
import com.fintech.common.generator.yapi.yapi.pojo.analysis.AYapiFunc;
import freemarker.template.TemplateException;

import java.io.IOException;

public class YapiInterfaceParser
{
    private FreeMarkerManager freeMarkerManager;

    private YapiDtoParser yapiDtoParser;

    public void init()
    {
        this.freeMarkerManager = FreeMarkerManager.getInstance();
        this.yapiDtoParser = YapiDtoParser.getInstance();
    }

    /**
     * 解析并且生成对应的dto
     * @param pkg 基础包名
     * @param funcName 接口名
     * @param targetInterface
     * @throws IOException
     * @throws TemplateException
     */
    public void dealInterface(String pkg, String funcName,
            String interfaceName, YapiInterface targetInterface,
            String msName)
            throws IOException, TemplateException
    {
        final String resBody = targetInterface.getResBody();
        final String reqBody = targetInterface.getReqBodyOther();
        final String path = targetInterface.getPath();
        final String title = targetInterface.getTitle();

        final String upperCaseName = StringUtils.getUpperCase(funcName);

        AYapiFunc aYapiFunc = new AYapiFunc();
        aYapiFunc.setId(targetInterface.getId());
        aYapiFunc.setPkg(pkg);
        aYapiFunc.setName(funcName);
        aYapiFunc.setUpperCaseName(upperCaseName);
        aYapiFunc.setPath(path);
        aYapiFunc.setDesc(title);
        aYapiFunc.setDtoPkg(pkg+".dto");
        aYapiFunc.setReqDesc("请求");
        aYapiFunc.setRespDesc("响应");

        aYapiFunc.setMsDesc(title);
        aYapiFunc.setMsInterface(interfaceName);
        aYapiFunc.setMsName(msName);

        yapiDtoParser.generateDto(pkg, resBody, upperCaseName, "Resp");
        yapiDtoParser.generateDto(pkg, reqBody, upperCaseName, "Req");

        //生成resp代码
        freeMarkerManager.parseInterface(pkg,aYapiFunc);
    }

    /**
     * 单例
     */
    private static YapiInterfaceParser instance = new YapiInterfaceParser();

    private YapiInterfaceParser()
    {
    }

    public static YapiInterfaceParser getInstance(){
        return instance;
    }


}
