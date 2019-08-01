package com.fintech.common.generator.yapi.freemarker;

import com.fintech.common.generator.yapi.common.StringUtils;
import com.fintech.common.generator.yapi.yapi.pojo.analysis.AYapiFunc;
import com.fintech.common.generator.yapi.yapi.pojo.analysis.AYapiObject;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

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

    private String tempPath;

    private String baseClazzPath;

    public void init() throws IOException
    {
        Configuration configuration = new Configuration(Configuration.getVersion());
        ClassLoader classLoader = FreeMarkerManager.class.getClassLoader();

        URL classpathUrl = classLoader.getResource("ftl");
        tempPath = classpathUrl.getPath();

        URL baseClazzUrl = classLoader.getResource("");
        baseClazzPath = baseClazzUrl.getPath();

        configuration.setDirectoryForTemplateLoading(new File(tempPath));
        configuration.setDefaultEncoding("utf-8");
        pojoTemplate = configuration.getTemplate("AYapiPojo.ftl");
        interfaceTemplate =  configuration.getTemplate("AYapiInterface.ftl");
    }

    /**
     * 自动生成Pojo
     * @param basePkg
     * @param aYapiObject
     */
    public void parsePojo(String basePkg, AYapiObject aYapiObject)
            throws IOException, TemplateException
    {
        Map dataModel = new HashMap<>(2);
        dataModel.put("pojo", aYapiObject);

        String dtoPkg = basePkg+".dto.*";
        dataModel.put("pkg", dtoPkg);

        String dtoPath = getPathByPkg(basePkg);

        //判断文件夹是否存在，如果不存在则创建
        File dirFile = new File(baseClazzPath+dtoPath+"dto");
        if(!dirFile.exists())
        {
            dirFile.mkdirs();
        }

        File pojoFile = new File(baseClazzPath+dtoPath+"dto/" + StringUtils.getUpperCase(aYapiObject.getName()) + ".java");
        if(!pojoFile.exists())
        {
            pojoFile.createNewFile();
        }

        Writer out = new FileWriter(pojoFile);
        try
        {
            pojoTemplate.process(dataModel, out);
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

    /**
     * 自动生成Pojo
     * @param basePkg
     * @param aYapiFunc
     */
    public void parseInterface(String basePkg, AYapiFunc aYapiFunc)
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
        File dirFile = new File(baseClazzPath+dtoPath);
        if(!dirFile.exists())
        {
            dirFile.mkdirs();
        }

        File pojoFile = new File(baseClazzPath+dtoPath + StringUtils.getUpperCase(aYapiFunc.getMsInterface()) + ".java");
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

    /**
     * 单例
     */
    private static FreeMarkerManager instance = new FreeMarkerManager();

    private FreeMarkerManager()
    {
    }

    public static FreeMarkerManager getInstance(){
        return instance;
    }

}
