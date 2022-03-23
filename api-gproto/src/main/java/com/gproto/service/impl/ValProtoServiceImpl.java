package com.gproto.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gproto.common.ProtobufProcessor;
import com.gproto.entity.ToBasisEntity;
import com.gproto.service.ValProtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class ValProtoServiceImpl implements ValProtoService {
    private static final Logger log = Logger.getLogger(ValProtoServiceImpl.class.getName());

    @Autowired
    private ProtobufProcessor protobufProcessor;

    @Override
    public String jsonToProto(String className, ToBasisEntity toBasisEntity) throws Exception {
        String result = "";
        String jsonData = getJsonData(toBasisEntity);
        result = protobufProcessor.jsonToProtobuf(className, jsonData);
        return result;
    }

    private String getJsonData(ToBasisEntity toBasisEntity){
        Map<String, Object> jsonDataMap = new HashMap<>();
        Map<String, Object> svrHeader = new HashMap<>();
        Map<String, Object> param = new HashMap<>();

        String serviceId = toBasisEntity.getServiceid().substring(0, toBasisEntity.getServiceid().length() - 1);
        String instanceId = toBasisEntity.getServiceid().substring(toBasisEntity.getServiceid().length() - 1);
        /**
         * message serviceheader{
         *     string instanceid = 1;
         *     string servicename = 2;
         *     string serviceid = 3;
         *     string version = 4;
         *     string servicetype = 5;
         *     string fieldname = 6;
         *     string method = 7;
         *     string methodid = 8;
         *     string event = 9;
         *     string eventid = 10;
         *     string eventgroup = 11;
         *     string eventgroupid = 12;
         * }
         */
        svrHeader.put("serviceid", toBasisEntity.getServiceid());
        svrHeader.put("servicename", toBasisEntity.getServicename());
        svrHeader.put("instanceid", instanceId);
        svrHeader.put("version", toBasisEntity.getServiceversion());
        svrHeader.put("method", toBasisEntity.getMethodname());
        svrHeader.put("servicetype", toBasisEntity.getRequesttype());

        param.put("input", toBasisEntity.getParam());

        jsonDataMap.put("srvHeader", svrHeader);
        jsonDataMap.put(toBasisEntity.getMethodname() + "Param", param);

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String jsonData = objectMapper.writeValueAsString(jsonDataMap);

            log.info(jsonData);
            return jsonData;
        }catch (Exception e){
            e.printStackTrace();
        }


        return null;

    }
}
