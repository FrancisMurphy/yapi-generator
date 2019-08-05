package com.fintech.common.generator.yapi.config;

import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class YapiGeneratorConfig
{
    private Map<String,String> yapiConfig = new HashMap<>();

    private final String[] yapiAttributes = {"id","pkg","name","funcname","msname"};

    public void init() throws IOException
    {
        URL baseClazzUrl = YapiGeneratorConfig.class.getClassLoader().getResource("");
        String classpath = baseClazzUrl.getPath();

        //获取上级目录中的yapiGenerator.properties
        File classpathDir = new File(classpath);
        String parentDirPath = classpathDir.getParent();
        Properties yapiConfigProperties = new Properties();
        FileInputStream in = new FileInputStream(parentDirPath+"/yapiGenerator.properties");
        yapiConfigProperties.load(in);

//        interface.id=1028
//        interface.pkg=com.frank.test
//        interface.name=TestApi
//        interface.funcname=testFunc
//        interface.msname=TEST-MS
        Set<Map.Entry<Object, Object>> entrySet = yapiConfigProperties.entrySet();
        for (Map.Entry<Object, Object> entry : entrySet)
        {
            final String key = String.valueOf(entry.getKey());

            if(key.startsWith("interface."))
            {
                final String interfaceKey = key.substring("interface.".length());
                yapiConfig.put(interfaceKey,String.valueOf(entry.getValue()));
            }
        }

        for(String attribute:yapiAttributes)
        {
            //检查必要配置
            if(StringUtils.isEmpty(yapiConfig.get(attribute)))
            {
                System.out.println("YapiAssert:["+attribute+"] can not empty...");
            }
        }
    }

    public String getInterfaceConfig(String key)
    {
        return yapiConfig.get(key);
    }

    private static YapiGeneratorConfig instance = new YapiGeneratorConfig();

    private YapiGeneratorConfig()
    {
    }

    public static YapiGeneratorConfig getInstance(){
        return instance;
    }
}
