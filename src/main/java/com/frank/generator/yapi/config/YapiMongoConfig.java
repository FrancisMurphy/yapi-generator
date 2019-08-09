package com.frank.generator.yapi.config;

import com.mongodb.MongoClient;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * mongodb 初始化器
 * @author frank
 */
@Lazy
@Configuration("yapiMongoConfig")
public class YapiMongoConfig
{
    /**
     * mongodb host
     */
    @Setter
    private String host;

    /**
     * mongodb 端口
     */
    @Setter
    private String port;

    /**
     * yapi 对应的库
     */
    @Setter
    private String database;

    public YapiMongoConfig()
    {
        //读取Mongodb所需的配置
        YapiGeneratorConfig yapiGeneratorConfig = YapiGeneratorConfig.getInstance();
        host = yapiGeneratorConfig.getMongoConfig("host");
        port = yapiGeneratorConfig.getMongoConfig("port");
        database = yapiGeneratorConfig.getMongoConfig("database");
    }

    @Bean
    public MongoClientFactoryBean mongo()
    {
        MongoClientFactoryBean mongo = new MongoClientFactoryBean();
        mongo.setHost(host);
        mongo.setPort(Integer.valueOf(port));
        return mongo;
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongo)
    {
        return new MongoTemplate(mongo,database);
    }
}
