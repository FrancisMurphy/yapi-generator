package com.fintech.common.generator.yapi.yapi;

import com.fintech.common.generator.yapi.freemarker.FreeMarkerManager;
import com.fintech.common.generator.yapi.yapi.pojo.original.YapiInterface;
import com.fintech.common.generator.yapi.yapi.parser.YapiDtoParser;
import com.fintech.common.generator.yapi.yapi.parser.YapiInterfaceParser;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * 根据Yapi注册信息自动生成基于接口代码
 * @author F&K
 */
@Service
public class YapiAutoGenerator implements InitializingBean
{
    @Autowired
    private MongoTemplate mongoTemplate;

    private FreeMarkerManager freeMarkerManager;

    private YapiDtoParser yapiDtoParser;

    private YapiInterfaceParser yapiInterfaceParser;

    @Override
    public void afterPropertiesSet() throws Exception
    {
        freeMarkerManager = FreeMarkerManager.getInstance();
        freeMarkerManager.init();

        yapiDtoParser = YapiDtoParser.getInstance();
        yapiDtoParser.init();

        yapiInterfaceParser = YapiInterfaceParser.getInstance();
        yapiInterfaceParser.init();
    }

    /**
     * 自动生成触发接口
     */
    public void generate(Long interfaceId,String pkg,String funcName,
            String interfaceName,String msName)
            throws IOException, TemplateException
    {
        //进行解析
        dealYapiDescribe(interfaceId, pkg,funcName,
                interfaceName,msName);
    }

    /**
     *  获取Yapi中的接口注册信息，并且将其转换为模版使用的内容
     * @param interfaceId
     * @throws IOException
     */
    private void dealYapiDescribe(Long interfaceId,String pkg,
            String funcName,String interfaceName,String msName)
            throws IOException, TemplateException
    {
        //Step1:将YAPI中所有的接口查找出来
        YapiInterface targetInterface = mongoTemplate
                .findOne(new Query(Criteria.where("_id").is(interfaceId)),
                        YapiInterface.class);

        //Step2:生成dto
        yapiInterfaceParser.dealInterface(pkg, funcName,interfaceName ,targetInterface,msName);
    }

//    /**
//     * 解析并且生成对应的dto
//     * @param pkg 基础包名
//     * @param funcName 接口名
//     * @param targetInterface
//     * @throws IOException
//     * @throws TemplateException
//     */
//    private void dealInterface(String pkg, String funcName,
//            String interfaceName, YapiInterface targetInterface,
//            String msName)
//            throws IOException, TemplateException
//    {
//        final String resBody = targetInterface.getResBody();
//        final String reqBody = targetInterface.getReqBodyOther();
//        final String path = targetInterface.getPath();
//        final String title = targetInterface.getTitle();
//
//        final String upperCaseName = getUpperCase(funcName);
//
//        AYapiFunc aYapiFunc = new AYapiFunc();
//        aYapiFunc.setId(targetInterface.getId());
//        aYapiFunc.setPkg(pkg);
//        aYapiFunc.setName(funcName);
//        aYapiFunc.setUpperCaseName(upperCaseName);
//        aYapiFunc.setPath(path);
//        aYapiFunc.setDesc(title);
//        aYapiFunc.setDtoPkg(pkg+".dto");
//        aYapiFunc.setReqDesc("请求");
//        aYapiFunc.setRespDesc("响应");
//
//        aYapiFunc.setMsDesc(title);
//        aYapiFunc.setMsInterface(interfaceName);
//        aYapiFunc.setMsName(msName);
//
//        generateDto(pkg, resBody, upperCaseName, "Resp");
//        generateDto(pkg, reqBody, upperCaseName, "Req");
//
//        //生成resp代码
//        freeMarkerManager.parseInterface(pkg,aYapiFunc);
//    }

//    private void generateDto(final String pkg, final String dtoBody,
//            final String upperCaseName,final String dtoSuffix)
//            throws IOException, TemplateException
//    {
//        //解析
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.configure(
//                DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
//                false);
//
//        //反序列化dto
//        YapiDto dto = objectMapper.readValue(dtoBody, YapiDto.class);
//
//        //初始化resp
//        AYapiObject aYapiObject = new AYapiObject();
//        aYapiObject.setClazz(upperCaseName+dtoSuffix);
//        aYapiObject.setDesc(dto.getTitle());
//        aYapiObject.setName(upperCaseName+dtoSuffix);
//        aYapiObject.setProperties(new ArrayList<>(dto.getProperties().size()));
//        aYapiObject.setImportPkg(new HashSet<>());
//
//        //解析成员并且生成成员对象代码
//        recursiveParsingField(dto.getProperties(), aYapiObject, pkg);
//
//        //生成resp代码
//        freeMarkerManager.parsePojo(pkg,aYapiObject);
//    }
//
//    private void recursiveParsingField(Map<String, YapiObject> properties, AYapiObject aYapiFatherObject,
//            String basePkg) throws IOException, TemplateException
//    {
//        for(Map.Entry<String, YapiObject> entry : properties.entrySet())
//        {
//            final String propertyName = entry.getKey();
//            final YapiObject property = entry.getValue();
//
//            //基础数据类型
//            if(!(property.getType().equals(YapiDataType.OBJECT)
//                    ||property.getType().equals(YapiDataType.NULL)))
//            {
//                //获取需要引入基础数据包名
//                String pkgNeedImport = property.getType().getClazz().getName();
//                aYapiFatherObject.getImportPkg().add(pkgNeedImport);
//
//                AYapiBasicData aYapiBasicData = new AYapiObject();
//                aYapiBasicData.setClazz(property.getType().getClazz().getSimpleName());
//                aYapiBasicData.setName(propertyName);
//                aYapiBasicData.setDesc(property.getDescription());
//
//                aYapiFatherObject.getProperties().add(aYapiBasicData);
//            }
//            //引用数据类型 递归
//            else if(property.getType().equals(YapiDataType.OBJECT))
//            {
//                //由于模版中会自动添加上对应接口req或者resp的包名引用，所以显式不用添加
//                AYapiObject aYapiFieldObject = new AYapiObject();
//                aYapiFieldObject.setProperties(new ArrayList<>(property.getProperties().size()));
//                aYapiFieldObject.setClazz(getUpperCase(propertyName));
//                aYapiFieldObject.setDesc(property.getDescription());
//                aYapiFieldObject.setName(propertyName);
//                aYapiFieldObject.setImportPkg(new HashSet<>());
//
//                //递归解析成员
//                recursiveParsingField(property.getProperties(),aYapiFieldObject,basePkg);
//
//                //生成dto.java
//                freeMarkerManager.parsePojo(basePkg,aYapiFieldObject);
//
//                aYapiFatherObject.getProperties().add(aYapiFieldObject);
//            }
//        }
//    }

}
