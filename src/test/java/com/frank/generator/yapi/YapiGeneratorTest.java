package com.frank.generator.yapi;

import freemarker.template.TemplateException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class YapiGeneratorTest
{

    @Autowired
    private YapiAutoGenerator yapiAutoGenerator;

    @Test
    public void hello() throws IOException, TemplateException
    {
        yapiAutoGenerator.generate(1028l,"com.frank.test","testFunc","TestApi","TEST-MS");
    }


}
