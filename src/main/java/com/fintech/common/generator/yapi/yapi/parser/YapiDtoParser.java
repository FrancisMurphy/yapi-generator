package com.fintech.common.generator.yapi.yapi.parser;

import com.fintech.common.generator.yapi.common.StringUtils;
import com.fintech.common.generator.yapi.freemarker.FreeMarkerManager;
import com.fintech.common.generator.yapi.yapi.pojo.analysis.AYapiBasicData;
import com.fintech.common.generator.yapi.yapi.pojo.analysis.AYapiObject;
import com.fintech.common.generator.yapi.yapi.pojo.deseriate.YapiDataType;
import com.fintech.common.generator.yapi.yapi.pojo.deseriate.YapiDto;
import com.fintech.common.generator.yapi.yapi.pojo.deseriate.YapiObject;
import freemarker.template.TemplateException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;


public class YapiDtoParser
{
    private FreeMarkerManager freeMarkerManager;

    public void init()
    {
        this.freeMarkerManager = FreeMarkerManager.getInstance();
    }

    public void generateDto(final String pkg, final String dtoBody,
            final String upperCaseName,final String dtoSuffix)
            throws IOException, TemplateException
    {
        //解析
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(
                DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
                false);

        //反序列化dto
        YapiDto dto = objectMapper.readValue(dtoBody, YapiDto.class);

        //初始化resp
        AYapiObject aYapiObject = new AYapiObject();
        aYapiObject.setClazz(upperCaseName+dtoSuffix);
        aYapiObject.setDesc(dto.getTitle());
        aYapiObject.setName(upperCaseName+dtoSuffix);
        aYapiObject.setProperties(new ArrayList<>(dto.getProperties().size()));
        aYapiObject.setImportPkg(new HashSet<>());

        //解析成员并且生成成员对象代码
        recursiveParsingField(dto.getProperties(), aYapiObject, pkg);

        //生成resp代码
        freeMarkerManager.parsePojo(pkg,aYapiObject);
    }

    private void recursiveParsingField(Map<String, YapiObject> properties, AYapiObject aYapiFatherObject,
            String basePkg) throws IOException, TemplateException
    {
        for(Map.Entry<String, YapiObject> entry : properties.entrySet())
        {
            final String propertyName = entry.getKey();
            final YapiObject property = entry.getValue();

            //基础数据类型
            if(!(property.getType().equals(YapiDataType.OBJECT)
                    ||property.getType().equals(YapiDataType.NULL)))
            {
                //获取需要引入基础数据包名
                String pkgNeedImport = property.getType().getClazz().getName();
                aYapiFatherObject.getImportPkg().add(pkgNeedImport);

                AYapiBasicData aYapiBasicData = new AYapiObject();
                aYapiBasicData.setClazz(property.getType().getClazz().getSimpleName());
                aYapiBasicData.setName(propertyName);
                aYapiBasicData.setDesc(property.getDescription());

                aYapiFatherObject.getProperties().add(aYapiBasicData);
            }
            //引用数据类型 递归
            else if(property.getType().equals(YapiDataType.OBJECT))
            {
                //由于模版中会自动添加上对应接口req或者resp的包名引用，所以显式不用添加
                AYapiObject aYapiFieldObject = new AYapiObject();
                aYapiFieldObject.setProperties(new ArrayList<>(property.getProperties().size()));
                aYapiFieldObject.setClazz(StringUtils.getUpperCase(propertyName));
                aYapiFieldObject.setDesc(property.getDescription());
                aYapiFieldObject.setName(propertyName);
                aYapiFieldObject.setImportPkg(new HashSet<>());

                //递归解析成员
                recursiveParsingField(property.getProperties(),aYapiFieldObject,basePkg);

                //生成dto.java
                freeMarkerManager.parsePojo(basePkg,aYapiFieldObject);

                aYapiFatherObject.getProperties().add(aYapiFieldObject);
            }
        }
    }

    /**
     * 单例
     */
    private static YapiDtoParser instance = new YapiDtoParser();

    private YapiDtoParser()
    {
    }

    public static YapiDtoParser getInstance(){
        return instance;
    }

}
