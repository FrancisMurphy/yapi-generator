package com.frank.generator.yapi.core;

import com.fintech.common.generator.yapi.common.exception.YapiAbandonException;
import com.fintech.common.generator.yapi.config.YapiGeneratorConfig;
import com.fintech.common.generator.yapi.core.creator.YapiDtoCreator;
import com.fintech.common.generator.yapi.core.creator.YapiInterfaceCreator;
import com.fintech.common.generator.yapi.core.manager.FreeMarkerManager;
import com.fintech.common.generator.yapi.core.pojo.original.OYapiInterface;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception
    {
        FreeMarkerManager.getInstance().init();
        YapiDtoCreator.getInstance().init();
        YapiInterfaceCreator.getInstance().init();
        YapiGeneratorConfig.getInstance().init();

        //load MongoTemplate
        applicationContext.getBean("yapiMongoConfig");
        MongoTemplate mongoTemplate = (MongoTemplate) applicationContext.getBean("mongoTemplate");

        generate(mongoTemplate);
    }

    /**
     * 自动生成触发接口
     */
    public void generate(MongoTemplate mongoTemplate)
            throws IOException, TemplateException
    {
        YapiGeneratorConfig yapiGeneratorConfig = YapiGeneratorConfig.getInstance();

        //interface-> 配置"id","pkg","name","funcname","msname"
        final Long id = Long.valueOf(yapiGeneratorConfig.getInterfaceConfig("id"));
        final String pkg = yapiGeneratorConfig.getInterfaceConfig("pkg");
        final String name = yapiGeneratorConfig.getInterfaceConfig("name");
        final String funcName = yapiGeneratorConfig.getInterfaceConfig("funcname");
        final String msName = yapiGeneratorConfig.getInterfaceConfig("msname");

        //根据id查询yapi记录
        OYapiInterface targetInterface = getYapiRecord(mongoTemplate, id);

        dealYapiDescribe(targetInterface,pkg,funcName,
                name,msName);
    }

    private OYapiInterface getYapiRecord(MongoTemplate mongoTemplate, Long id)
    {
        OYapiInterface targetInterface = mongoTemplate
                .findOne(new Query(Criteria.where("_id").is(id)),
                        OYapiInterface.class);

        if(targetInterface == null)
        {
            throw new YapiAbandonException("###YAPI-GENERATOR ERROR### Can not find yapi interface(id:"+ id +") record...");
        }

        targetInterface.isValid();
        return targetInterface;
    }

    /**
     *  获取Yapi中的接口注册信息，并且将其转换为模版使用的内容
     * @throws IOException
     */
    private void dealYapiDescribe(OYapiInterface targetInterface, String pkg,
            String funcName,String interfaceName,String msName)
            throws IOException, TemplateException
    {
        YapiInterfaceCreator.getInstance().dealInterface(pkg, funcName,interfaceName ,targetInterface,msName);
    }

}
