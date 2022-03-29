package com.gproto.common;

import com.google.common.collect.Maps;
import com.gproto.enum1.ProtoJavaType;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

import static com.gproto.enum1.ProtoJavaType.*;


@Component
public class ProtobufDefaultValueMock {

    public Object getDefaultValue(String javaType){
        ProtoJavaType type =  valueOf(javaType);
        Object result;
        switch (type){
            case BOOLEAN:
                result = true;
                break;
            case STRING:
                result = UUID.randomUUID().toString().replaceAll("-", "").substring(0,16);
                break;
            case FLOAT:
                result = 0.02f;
                break;
            case INT:
                result = 2;
                break;
            case LONG:
                result = 10086L;
                break;
            case DOUBLE:
                result = 0.04d;
                break;
            case BYTE_STRING:
                result = "abc".getBytes(StandardCharsets.UTF_8).toString();
                break;
            case ENUM:
                result = 0;
                break;
            default:
                result = null;
        }
        return result;
    }
}
