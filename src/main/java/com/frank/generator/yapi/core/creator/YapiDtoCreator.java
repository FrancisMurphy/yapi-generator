package com.frank.generator.yapi.core.creator;

import com.fintech.common.generator.yapi.common.utils.YapiPkgUtils;
import com.fintech.common.generator.yapi.core.manager.FreeMarkerManager;
import com.fintech.common.generator.yapi.core.pojo.analysis.AYapiBasicData;
import com.fintech.common.generator.yapi.core.pojo.analysis.AYapiObject;
import com.fintech.common.generator.yapi.core.pojo.deseriate.DYapiDataType;
import com.fintech.common.generator.yapi.core.pojo.deseriate.DYapiDto;
import com.fintech.common.generator.yapi.core.pojo.deseriate.DYapiObject;
import freemarker.template.TemplateException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

public class YapiDtoCreator
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
        DYapiDto dto = objectMapper.readValue(dtoBody, DYapiDto.class);

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
        freeMarkerManager.createPojo(pkg,aYapiObject);
    }

    private void recursiveParsingField(Map<String, DYapiObject> properties, AYapiObject aYapiFatherObject,
            String basePkg) throws IOException, TemplateException
    {
        for(Map.Entry<String, DYapiObject> entry : properties.entrySet())
        {
            final String propertyName = entry.getKey();
            final DYapiObject property = entry.getValue();

            //基础数据类型
            if(!(property.getType().equals(DYapiDataType.OBJECT)
                    ||property.getType().equals(DYapiDataType.NULL)))
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
            else if(property.getType().equals(DYapiDataType.OBJECT))
            {
                //由于模版中会自动添加上对应接口req或者resp的包名引用，所以显式不用添加
                AYapiObject aYapiFieldObject = new AYapiObject();
                aYapiFieldObject.setProperties(new ArrayList<>(property.getProperties().size()));
                aYapiFieldObject.setClazz(YapiPkgUtils.getUpperCase(propertyName));
                aYapiFieldObject.setDesc(property.getDescription());
                aYapiFieldObject.setName(propertyName);
                aYapiFieldObject.setImportPkg(new HashSet<>());

                //递归解析成员
                recursiveParsingField(property.getProperties(),aYapiFieldObject,basePkg);

                //生成dto.java
                freeMarkerManager.createPojo(basePkg,aYapiFieldObject);

                aYapiFatherObject.getProperties().add(aYapiFieldObject);
            }
        }
    }

    /**
     * 单例
     */
    private static YapiDtoCreator instance = new YapiDtoCreator();

    private YapiDtoCreator()
    {
    }

    public static YapiDtoCreator getInstance(){
        return instance;
    }

}
