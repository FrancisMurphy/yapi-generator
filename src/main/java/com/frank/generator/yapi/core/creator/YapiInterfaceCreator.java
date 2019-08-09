package com.frank.generator.yapi.core.creator;

import com.fintech.common.generator.yapi.common.utils.YapiPkgUtils;
import com.fintech.common.generator.yapi.core.manager.FreeMarkerManager;
import com.fintech.common.generator.yapi.core.pojo.analysis.AYapiFunc;
import com.fintech.common.generator.yapi.core.pojo.original.OYapiInterface;
import freemarker.template.TemplateException;

import java.io.IOException;

public class YapiInterfaceCreator
{
    private FreeMarkerManager freeMarkerManager;

    private YapiDtoCreator yapiDtoParser;

    public void init()
    {
        this.freeMarkerManager = FreeMarkerManager.getInstance();
        this.yapiDtoParser = YapiDtoCreator.getInstance();
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
            String interfaceName, OYapiInterface targetInterface,
            String msName)
            throws IOException, TemplateException
    {
        final String resBody = targetInterface.getResBody();
        final String reqBody = targetInterface.getReqBodyOther();
        final String path = targetInterface.getPath();
        final String title = targetInterface.getTitle();

        final String upperCaseName = YapiPkgUtils.getUpperCase(funcName);

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
        freeMarkerManager.createInterface(pkg,aYapiFunc);
    }

    /**
     * 单例
     */
    private static YapiInterfaceCreator instance = new YapiInterfaceCreator();

    private YapiInterfaceCreator()
    {
    }

    public static YapiInterfaceCreator getInstance(){
        return instance;
    }


}
