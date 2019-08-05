package com.fintech.common.generator.yapi;

import com.fintech.common.generator.yapi.config.YapiGeneratorConfig;
import com.fintech.common.generator.yapi.manager.FreeMarkerManager;
import com.fintech.common.generator.yapi.pojo.original.OYapiInterface;
import com.fintech.common.generator.yapi.parser.YapiDtoParser;
import com.fintech.common.generator.yapi.parser.YapiInterfaceParser;
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

        //读取配置文件
        YapiGeneratorConfig yapiGeneratorConfig = YapiGeneratorConfig.getInstance();
        yapiGeneratorConfig.init();

        //interface-> 配置"id","pkg","name","funcname","msname"
        final String id = yapiGeneratorConfig.getInterfaceConfig("id");
        final String pkg = yapiGeneratorConfig.getInterfaceConfig("pkg");
        final String name = yapiGeneratorConfig.getInterfaceConfig("name");
        final String funcname = yapiGeneratorConfig.getInterfaceConfig("funcname");
        final String msname = yapiGeneratorConfig.getInterfaceConfig("msname");
        generate(Long.valueOf(id),pkg,funcname,name,msname);
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
        OYapiInterface targetInterface = mongoTemplate
                .findOne(new Query(Criteria.where("_id").is(interfaceId)),
                        OYapiInterface.class);

        //Step2:生成dto
        yapiInterfaceParser.dealInterface(pkg, funcName,interfaceName ,targetInterface,msName);
    }

}
