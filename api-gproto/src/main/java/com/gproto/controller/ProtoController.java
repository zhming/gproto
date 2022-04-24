package com.gproto.controller;

import com.google.common.base.Strings;
import com.gproto.common.ProtobufProcessor;
import com.gproto.entity.RequestEntity;
import com.gproto.entity.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.logging.Logger;

@RestController
@RequestMapping("/gproto/v1")
public class ProtoController {
    private static final Logger logger = Logger.getLogger(ProtoController.class.getName());
    @Autowired
    ProtobufProcessor protobufProcessor;

    @RequestMapping(path = "/protobufDataToJson", method = RequestMethod.POST)
    public String protobufDataToJson(@RequestBody RequestEntity requestEntity)throws Exception {
        if(Objects.isNull(requestEntity) || Strings.isNullOrEmpty(requestEntity.getClassName()) ||
                Strings.isNullOrEmpty(requestEntity.getBase64Data())){
            return ResponseEntity.respErrorInstance("10098").toString();
        }
        String result = protobufProcessor.protobufDataToJson(requestEntity.getClassName(), requestEntity.getBase64Data());
        return result;
    }

    @RequestMapping(path = "/jsonToProtobuf", method = RequestMethod.POST)
    public String jsonToProtobuf(@RequestBody RequestEntity requestEntity)throws Exception {
        if(Objects.isNull(requestEntity) || Strings.isNullOrEmpty(requestEntity.getClassName()) ||
                Strings.isNullOrEmpty(requestEntity.getJsonData()) ){
            return ResponseEntity.respErrorInstance("10098").toString();
        }
        String result = protobufProcessor.jsonToProtobuf(requestEntity.getClassName(),
                requestEntity.getJsonData());
        return result;
    }

    @RequestMapping(path = "/getProtobufField", method = RequestMethod.POST)
    public Object getProtobufField(@RequestBody RequestEntity requestEntity)throws Exception {
        if(Objects.isNull(requestEntity) || Strings.isNullOrEmpty(requestEntity.getClassName()) ||
                Strings.isNullOrEmpty(requestEntity.getBase64Data()) ||
                Strings.isNullOrEmpty(requestEntity.getFieldName()) ){
            return ResponseEntity.respErrorInstance("10098");
        }

        Object result = protobufProcessor.getMessageFieldJson(requestEntity.getClassName(),
                requestEntity.getBase64Data(), requestEntity.getFieldName());
        return result;
    }

    @RequestMapping(path = "/getProtobufSubField", method = RequestMethod.POST)
    public Object getProtobufSubField(@RequestBody RequestEntity requestEntity)throws Exception {
        if(Objects.isNull(requestEntity) || Strings.isNullOrEmpty(requestEntity.getClassName()) ||
                Strings.isNullOrEmpty(requestEntity.getBase64Data()) ||
                Strings.isNullOrEmpty(requestEntity.getFieldName()) ||
                Strings.isNullOrEmpty(requestEntity.getSubFieldName())){
            return ResponseEntity.respErrorInstance("10098");
        }
        Object result = protobufProcessor.getMessageSubFieldJson(requestEntity.getClassName(),
                requestEntity.getBase64Data(), requestEntity.getFieldName(), requestEntity.getSubFieldName());
        return result;
    }

    @RequestMapping(path = "/getDefaultJson", method = RequestMethod.POST)
    public Object getDefaultJson(@RequestBody RequestEntity requestEntity)throws Exception {
        if(Objects.isNull(requestEntity) || Strings.isNullOrEmpty(requestEntity.getClassName())){
            return ResponseEntity.respErrorInstance("10098");
        }
        Object result = protobufProcessor.getDefaultJson(requestEntity.getClassName());
        return result;
    }

    @RequestMapping(path = "/getFieldDefaultJson", method = RequestMethod.POST)
    public Object getFieldDefaultJson(@RequestBody RequestEntity requestEntity)throws Exception {
        logger.info(requestEntity.toString());
        if(Objects.isNull(requestEntity) || Strings.isNullOrEmpty(requestEntity.getClassName())
                || Strings.isNullOrEmpty(requestEntity.getFieldName())
                || Strings.isNullOrEmpty(requestEntity.getSubFieldName())){
            return ResponseEntity.respErrorInstance("10098");
        }
        Object result = protobufProcessor.getFieldDefaultJson(requestEntity.getClassName(),
                requestEntity.getFieldName(),
                requestEntity.getSubFieldName());
        return result;
    }


    @RequestMapping(path = "/getJsonTree", method = RequestMethod.POST)
    public Object getJsonTree(@RequestBody RequestEntity requestEntity)throws Exception {
        if(Objects.isNull(requestEntity) || Strings.isNullOrEmpty(requestEntity.getClassName())){
            return ResponseEntity.respErrorInstance("10098");
        }
        Object result = protobufProcessor.getJsonTree(requestEntity.getClassName());
        return result;
    }
}
