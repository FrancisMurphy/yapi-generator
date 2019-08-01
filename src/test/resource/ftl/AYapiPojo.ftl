package ${pkg};

import ${pkg}.*;

/**
  * This is Restful dto which was auto generated...
  * @author F&K
  **/
@Data
public class ${pojo.clazz}
{
    <#list pojo.properties as property>
       /**
         * ${property.desc}
         **/
        private ${property.clazz} ${property.name};
    </#list>
}
