package ${basePkg};

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.hbfintech.frame.common.bean.ResponseBodyBean;

import ${basePkg}.dto.*;

/**
  * ${msDesc}
  * YAPI Id:${func.id}
  * @author F&K
  */
@FeignClient(value = "${msName}")
public interface ${msInterface}
{
        /**
          * ${func.desc}
          * @param req    ${func.reqDesc}
          * @param resq   ${func.respDesc}
          * @return
          */
        @PostMapping(value = "${func.path}", headers = "Content-Type: application/json")
        ResponseBodyBean<${func.upperCaseName}Resp> ${func.name}(@RequestBody ${func.upperCaseName}Req req);
}
