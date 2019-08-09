package com.frank.generator.yapi.config;

import com.fintech.common.generator.yapi.common.exception.YapiAbandonException;
import org.springframework.util.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 解析配置
 * @author frank
 */
public class YapiGeneratorConfig
{
    /**
     * 基础配置
     */
    private Map<String,String> mongoConfig = new HashMap<>();

    /**
     * 创建配置
     */
    private Map<String,String> yapiConfig = new HashMap<>();

    /**
     * 必填基础配置
     */
    private static final String[] MONGO_ATTRIBUTES = {"host","port","database"};

    /**
     * 必填代码生成配置
     */
    private static final String[] YAPI_ATTRIBUTES = {"id","pkg","name","funcname","msname"};

    /**
     * 配置文件名
     */
    private static final String CONFIG_FILE_NAME = "yapiGenerator.yml";

    public void init()
    {
        //解析配置文件
        parserYaml();
        //检查配置参数是否正确
        checkConfig();
    }

    private void parserYaml()
    {
        Yaml yaml = new Yaml();

        InputStream ymlInputStream = getFile();
        Map<String, Map<String,Object>> ret = yaml.load(ymlInputStream);

        Set<Map.Entry<String, Map<String,Object>>> entrySet = ret.entrySet();
        for (Map.Entry<String, Map<String,Object>> entry : entrySet)
        {
            final String key = entry.getKey();
            //解析yapi基础配置
            if("yapi".equals(key))
            {
                Map<String,String> mongodbConfig = (Map<String,String>)entry.getValue().get("mongodb");
                mongodbConfig.entrySet().forEach(yapiConfigItem->
                        mongoConfig.put(yapiConfigItem.getKey(),String.valueOf(yapiConfigItem.getValue())));
            }

            //解析interface配置
            if("interface".equals(key))
            {
                entry.getValue().entrySet().forEach(yapiConfigItem->
                        yapiConfig.put(yapiConfigItem.getKey(),String.valueOf(yapiConfigItem.getValue())));
            }
        }
    }

    private void checkConfig()
    {
        boolean isConfigValidFlag = true;

        //基础配置检查
        //生成代码配置检查
        for(String attribute: MONGO_ATTRIBUTES)
        {
            if(StringUtils.isEmpty(mongoConfig.get(attribute)))
            {
                System.out.println("###YAPI-GENERATOR ERROR### BaseAssert:["+attribute+"] can not empty...");
                isConfigValidFlag = false;
            }
        }

        //生成代码配置检查
        for(String attribute: YAPI_ATTRIBUTES)
        {
            if(StringUtils.isEmpty(yapiConfig.get(attribute)))
            {
                System.out.println("###YAPI-GENERATOR ERROR### YapiAssert:["+attribute+"] can not empty...");
                isConfigValidFlag = false;
            }
        }

        if(!isConfigValidFlag)
        {
            throw new YapiAbandonException("###YAPI-GENERATOR ABANDON EXCEPTION### Config param illegal...");
        }
    }

    public String getInterfaceConfig(String key)
    {
        return yapiConfig.get(key);
    }

    public String getMongoConfig(String key)
    {
        return mongoConfig.get(key);
    }

    /**
     * 获取配置文件流
     * @return
     */
    public InputStream getFile()
    {
        InputStream inputStream  =  Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIG_FILE_NAME);
        if( inputStream==null )
        {
            String configFilePath = System.getProperty("user.dir")+File.separator+CONFIG_FILE_NAME;
            try
            {
                inputStream = new FileInputStream(configFilePath);
            }
            catch (Exception e)
            {
                throw new YapiAbandonException("###YAPI-GENERATOR ABANDON EXCEPTION### Config param illegal...");
            }
        }
        return inputStream;
    }

    private static YapiGeneratorConfig instance = new YapiGeneratorConfig();

    private YapiGeneratorConfig()
    {
    }

    public static YapiGeneratorConfig getInstance(){
        return instance;
    }
}
