package com.gproto.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gproto.common.ProtobufProcessor;
import com.gproto.entity.RequestEntity;
import com.gproto.entity.ToBasisEntity;
import com.gproto.service.ValProtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/gproto/v1")
public class ValProtoController {
    private static final Logger logger = Logger.getLogger(ValProtoController.class.getName());
    @Autowired
    private ValProtoService valProtoService;



    @RequestMapping(path = "/valJsonToProto", method = RequestMethod.POST)
    public String protobufDataToJson(@RequestBody RequestEntity requestEntity)throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
//        Map<String, Object> param = new HashMap<>();
//        param.put("action", "action1111");
//        param.put("key", 1);
//        param.put("sceneId", 12121);
//        param.put("source", "source11212");
//        param.put("userId", 6658654);
//
//        String paramStr = objectMapper.writeValueAsString(param);
//
//        requestEntity.setJsonData(paramStr);
//
//        logger.info(paramStr);
//        String paramStr = "{\"srvHeader\":{\"servicetype\":1,\"instanceid\":\"1\",\"method\":\"BindingKeyUpdate\",\"servicename\":\"\",\"serviceid\":\"0x00011\",\"version\":\"0.2\"},\"BindingKeyUpdateParam\":{\"input\":{\"sceneId\":12121,\"action\":\"action1111\",\"source\":\"source11212\",\"userId\":6658654,\"key\":1}}}";

        ToBasisEntity toBasisEntity = new ToBasisEntity();
        toBasisEntity.setServiceid("0x00011");
        toBasisEntity.setServicename("");
        toBasisEntity.setServiceversion("0.2");
        toBasisEntity.setMethodname("BindingKeyUpdate");
        String jsonParam = requestEntity.getJsonData();

        Map<String, Object> objectMap = objectMapper.readValue(jsonParam, Map.class);
        toBasisEntity.setParam(objectMap);
        toBasisEntity.setRequesttype(1);

        String result = valProtoService.jsonToProto(requestEntity.getClassName(), toBasisEntity);

        return result;
    }


}
