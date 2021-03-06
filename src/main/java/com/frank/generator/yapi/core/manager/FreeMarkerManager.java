package com.frank.generator.yapi.core.manager;

import com.frank.generator.yapi.common.utils.YapiPkgUtils;
import com.frank.generator.yapi.core.pojo.analysis.AYapiFunc;
import com.frank.generator.yapi.core.pojo.analysis.AYapiObject;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.Getter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author F&K
 */
public class FreeMarkerManager
{
    /**
     * 所有pojo模版
     */
    private Template pojoTemplate;

    /**
     * 接口模版
     */
    private Template interfaceTemplate;

    @Getter
    private String baseClazzPath;

    public void init() throws IOException
    {
        Configuration configuration = new Configuration(Configuration.getVersion());
        ClassLoader classLoader = FreeMarkerManager.class.getClassLoader();

        URL baseClazzUrl = classLoader.getResource("");
        baseClazzPath = baseClazzUrl.getPath();

        configuration.setClassForTemplateLoading(this.getClass(),"/ftl");
        configuration.setDefaultEncoding("utf-8");

        pojoTemplate = configuration.getTemplate("AYapiPojo.ftl");
        interfaceTemplate =  configuration.getTemplate("AYapiInterface.ftl");
    }

    /**
     * 自动生成Pojo
     * @param basePkg
     * @param aYapiObject
     */
    public void createPojo(String basePkg, AYapiObject aYapiObject)
            throws IOException, TemplateException
    {
        Map dataModel = new HashMap<>(2);
        dataModel.put("pojo", aYapiObject);

        String dtoPkg = basePkg+".dto.*";
        dataModel.put("pkg", dtoPkg);

        String dtoPath = getPathByPkg(basePkg);

        //判断文件夹是否存在，如果不存在则创建
        File dirFile = new File(dtoPath+"dto");
        if(!dirFile.exists())
        {
            dirFile.mkdirs();
        }

        File pojoFile = new File(dtoPath+"dto/" + YapiPkgUtils.getUpperCase(aYapiObject.getName()) + ".java");
        createCode(dataModel, pojoFile, pojoTemplate);
    }

    /**
     * 自动生成Pojo
     * @param basePkg
     * @param aYapiFunc
     */
    public void createInterface(String basePkg, AYapiFunc aYapiFunc)
            throws IOException, TemplateException
    {
        Map dataModel = new HashMap<>(5);
        dataModel.put("func", aYapiFunc);
        dataModel.put("basePkg", basePkg);
        dataModel.put("msName",aYapiFunc.getMsName());
        dataModel.put("msInterface",aYapiFunc.getMsInterface());
        dataModel.put("msDesc",aYapiFunc.getMsDesc());

        String dtoPath = getPathByPkg(basePkg);

        //判断文件夹是否存在，如果不存在则创建

        File dirFile = new File(dtoPath);
        if(!dirFile.exists())
        {
            dirFile.mkdirs();
        }

        File pojoFile = new File(dtoPath + YapiPkgUtils.getUpperCase(aYapiFunc.getMsInterface()) + ".java");
        createCode(dataModel, pojoFile, interfaceTemplate);
    }

    private void createCode(Map dataModel, File pojoFile,
            Template interfaceTemplate) throws IOException, TemplateException
    {
        if(!pojoFile.exists())
        {
            pojoFile.createNewFile();
        }

        Writer out = new FileWriter(pojoFile);
        try
        {
            interfaceTemplate.process(dataModel, out);
        }
        catch (Exception e)
        {
            throw e;
        }
        finally
        {
            out.close();
        }
    }

    public String getPathByPkg(String dtoPkg)
    {
        String dtoPath = "";

        if (dtoPkg.indexOf(".") < 0)
        {
            dtoPath = dtoPkg + "/";
        }
        else
        {
            int start = 0, end = 0;
            end = dtoPkg.indexOf(".");
            while (end != -1)
            {
                dtoPath = dtoPath + dtoPkg.substring(start, end) + "/";
                start = end + 1;
                end = dtoPkg.indexOf(".", start);
            }
            dtoPath = dtoPath + dtoPkg.substring(start) + "/";
        }
        return dtoPath;
    }

    private static FreeMarkerManager instance = new FreeMarkerManager();

    private FreeMarkerManager()
    {
    }

    public static FreeMarkerManager getInstance(){
        return instance;
    }

}
