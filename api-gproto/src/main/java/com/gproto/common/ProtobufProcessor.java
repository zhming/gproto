package com.gproto.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gproto.classloader.DynamicJarLoader;
import com.gproto.entity.JsonTreeEntity;
import com.gproto.enumtype.ProtoJavaType;
import com.gproto.exception.ProtobufCastException;
import com.gproto.exception.ProtobufNoFieldException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.util.*;
import java.util.logging.Logger;

/**
 * @author qianzhm
 */
@Component
public class ProtobufProcessor {
    private static final Logger logger = Logger.getLogger(ProtobufProcessor.class.getName());

    @Autowired
    private ProtobufDefaultValueMock protobufDefaultValueMock;

    /**
     * base64 protobuf to json string
     *
     * @param className
     * @param base64Data
     * @return
     */
    public String protobufDataToJson(String className, String base64Data) {
        logger.info("className: " + className);
        try {
            Object messageObj = protobufDataToMessageObject(className, base64Data);
            return (String) toJson(messageObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * json string to base64 protobuf string
     *
     * @param className
     * @param jsonData
     * @return
     */
    public String jsonToProtobuf(String className, String jsonData) {
        logger.info("className: " + className);
        try {
            Object messageObj = jsonToMessage(className, jsonData);

            Method byteArrayMethod = messageObj.getClass().getMethod("toByteArray", null);

            Object byteArrayObj = byteArrayMethod.invoke(messageObj, null);

            String base64Data = Base64.getEncoder().encodeToString((byte[]) byteArrayObj);

            return base64Data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json string to protobuf message object
     *
     * @param className
     * @param jsonData
     * @return
     */
    public Object jsonToMessage(String className, String jsonData) {
        logger.info("className: " + className);
        URLClassLoader urlClassLoader = null;
        Object messageObj = null;

        try {
            Class<?> clazz = null;
            urlClassLoader = DynamicJarLoader.getInstance().getClassLoader();
            //获取外部jar里面的具体类对象
            clazz = urlClassLoader.loadClass(className);
            //创建对象实例
            Class messageClazz = urlClassLoader.loadClass("com.google.protobuf.Message$Builder");
            Method newBuilderMethod = clazz.getMethod("newBuilder");

            Object messageBuilderObj = newBuilderMethod.invoke(clazz);

            Class jsonFormatClazz = urlClassLoader.loadClass("com.google.protobuf.util.JsonFormat");

            Method parserMethod = jsonFormatClazz.getDeclaredMethod("parser");

            Object parserObj = parserMethod.invoke(null);

            Method printMethod = parserObj.getClass().getMethod("merge", String.class, messageClazz);

            if (Strings.isNullOrEmpty(jsonData)) {
                jsonData = "{\n" +
                        "  \"binding\": {\n" +
                        "    \"deleteFlag\": true,\n" +
                        "    \"key\": 1,\n" +
                        "    \"modifyTime\": \"0\",\n" +
                        "    \"sceneId\": 0,\n" +
                        "    \"uuid\": \"uuid11111\"\n" +
                        "  },\n" +
                        "  \"code\": 1,\n" +
                        "  \"message\": \"abc\",\n" +
                        "  \"serverTime\": \"0\",\n" +
                        "  \"transactionId\": \"\",\n" +
                        "  \"type\": \"\"\n" +
                        "}";
                logger.info("测试数据: " + jsonData);
            }

            printMethod.invoke(parserObj, jsonData, messageBuilderObj);

            Method messageBuildMethod = messageBuilderObj.getClass().getMethod("build", null);

            messageObj = messageBuildMethod.invoke(messageBuilderObj, null);

            return messageObj;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return messageObj;
    }

    /**
     * base64 protobuf string to protobuf message object
     *
     * @param className
     * @param base64Data
     * @return
     */
    public Object protobufDataToMessageObject(String className, String base64Data) {
        logger.info("className: " + className);
        URLClassLoader urlClassLoader = null;
        try {
            Class<?> clazz = null;
            urlClassLoader = DynamicJarLoader.getInstance().getClassLoader();
            //获取外部jar里面的具体类对象
            clazz = urlClassLoader.loadClass(className);
            //创建对象实例
            byte[] bytes = Base64.getDecoder().decode(base64Data);
            Method method = clazz.getDeclaredMethod("parseFrom", byte[].class);
            Object sourceMessage = method.invoke(null, bytes);
            return sourceMessage;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取protobuf Message对象的field json
     *
     * @param className
     * @param protobufData
     * @param fieldName
     * @return
     * @throws Exception
     */
    public Object getMessageFieldJson(String className, String protobufData, String fieldName) throws Exception {
        Object result = null;
        // full protobuf
        Object messageObj = getMessageField(className, protobufData, fieldName);
        result = toJson(messageObj);
        return result;
    }


    /**
     * proto file default json
     *
     * @param className
     * @return
     */
    public String getDefaultJson(String className) {
        logger.info("className: " + className);
        try {

            Class<?> clazz = null;
            URLClassLoader urlClassLoader = DynamicJarLoader.getInstance().getClassLoader();
            //获取外部jar里面的具体类对象
            clazz = urlClassLoader.loadClass(className);
            //创建对象实例
            Class messageClazz = urlClassLoader.loadClass("com.google.protobuf.Message$Builder");
            Method newBuilderMethod = clazz.getMethod("newBuilder");

            Object messageBuilderObj = newBuilderMethod.invoke(clazz);
            Method getDescriptorForType = messageBuilderObj.getClass().getDeclaredMethod("getDescriptorForType");

            Object descriptor = getDescriptorForType.invoke(messageBuilderObj);


            Map<String, Object> toDefaultJson = toDefaultJson(descriptor, Maps.newHashMap());

            ObjectMapper objectMapper = new ObjectMapper();

            return objectMapper.writeValueAsString(toDefaultJson);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * json tree
     *
     * @param className
     * @return
     */
    public String getJsonTree(String className) {
        logger.info("className: " + className);
        List<JsonTreeEntity> result = new ArrayList<>();
        try {

            Class<?> clazz = null;
            URLClassLoader urlClassLoader = DynamicJarLoader.getInstance().getClassLoader();
            //获取外部jar里面的具体类对象
            clazz = urlClassLoader.loadClass(className);
            //创建对象实例
            Class messageClazz = urlClassLoader.loadClass("com.google.protobuf.Message$Builder");
            Method newBuilderMethod = clazz.getMethod("newBuilder");

            Object messageBuilderObj = newBuilderMethod.invoke(clazz);
            Method getDescriptorForType = messageBuilderObj.getClass().getDeclaredMethod("getDescriptorForType");

            Object descriptor = getDescriptorForType.invoke(messageBuilderObj);

            Map<String, Object> toDefaultJson = toDefaultJson(descriptor, Maps.newHashMap());
            logger.info(toDefaultJson.toString());

            result = mapDataToJsonTree(toDefaultJson);

            ObjectMapper objectMapper = new ObjectMapper();

            return objectMapper.writeValueAsString(result);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取protobuf Message对象的field json
     *
     * @param className
     * @param protobufData
     * @param fieldName
     * @return
     * @throws Exception
     */
    public Object getMessageSubFieldJson(String className, String protobufData, String fieldName, String subFieldName) throws Exception {
        Object result = null;
        // full protobuf
        Object messageObj = getMessageField(className, protobufData, fieldName);
        Object subFieldMessageObject = getMessageField(messageObj, subFieldName);
        result = toJson(subFieldMessageObject);
        return result;
    }


    /**
     * 获取protobuf Message对象的field Message Object
     *
     * @param className
     * @param protobufData
     * @param fieldName
     * @return
     * @throws Exception
     */
    public Object getMessageField(String className, String protobufData, String fieldName) throws Exception {
        Object result = null;
        // full protobuf
        Object message = protobufDataToMessageObject(className, protobufData);

        result = getMessageField(message, fieldName);
        return result;
    }

    /**
     * 获取protobuf Message对象的field Message Object
     *
     * @param fieldName
     * @return Message Object
     * @throws Exception
     */
    public Object getMessageField(Object message, String fieldName) throws Exception {
        Object result = null;

        if (Objects.isNull(message)) {
            throw new ProtobufCastException("className");
        }

        Method getAllFieldsMethod = message.getClass().getMethod("getAllFields");

        Map<Object, Object> fieldsMap = (Map<Object, Object>) getAllFieldsMethod.invoke(message);

        if (Objects.isNull(fieldsMap) || fieldsMap.size() == 0) {
            throw new ProtobufNoFieldException(fieldName);
        }

        Set<Object> keySet = fieldsMap.keySet();

        Iterator<Object> iterator = keySet.iterator();

        while (iterator.hasNext()) {
            Object fieldDescriptor = iterator.next();
            Method fieldDescriptorGetNameMethod = fieldDescriptor.getClass().getMethod("getName");
            Object currentFieldName = fieldDescriptorGetNameMethod.invoke(fieldDescriptor);

            if (currentFieldName.equals(fieldName)) {
                Method fieldDescriptorGetJavaTypeMethod = fieldDescriptor.getClass().getMethod("getJavaType");
                Object javaType = fieldDescriptorGetJavaTypeMethod.invoke(fieldDescriptor);
                Method javaTypeNameMethod = javaType.getClass().getMethod("name");
                Object javaTypeName = javaTypeNameMethod.invoke(javaType);
                Object obj = fieldsMap.get(fieldDescriptor);
                if (ProtoJavaType.MESSAGE.name().equals(javaTypeName)) {
                    if (obj instanceof Collection) {
                        result = message;
                    } else {
                        result = obj;
                    }
                } else {
                    result = obj;
                    logger.info("---------");
                }

            }
        }
        return result;
    }

    /**
     * Protobuf Message to json
     *
     * @param messageObj
     * @return
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private Object toJson(Object messageObj) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Object result;
        if (Objects.isNull(messageObj)) {
            result = "";
        } else if (!messageObj.getClass().getName().startsWith("java.lang")) {
            ClassLoader urlClassLoader = messageObj.getClass().getClassLoader();
            Class jsonFormatClazz = urlClassLoader.loadClass("com.google.protobuf.util.JsonFormat");

            Method printerMethod = jsonFormatClazz.getDeclaredMethod("printer", null);

            Object printerObj = printerMethod.invoke(null, null);

            Class messageClazz = urlClassLoader.loadClass("com.google.protobuf.MessageOrBuilder");

            Method printMethod = printerObj.getClass().getMethod("print", messageClazz);

            Object jsonObj = printMethod.invoke(printerObj, messageObj);

            logger.info("Protobuf Message to json:" + jsonObj);
            result = jsonObj;
        } else {
            result = messageObj;
        }

        return result;
    }


    private Map<String, Object> toDefaultJson(Object descriptor, Map<String, Object> jsonMap) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        Method getFields = descriptor.getClass().getDeclaredMethod("getFields");

        List<Object> fields = (List<Object>) getFields.invoke(descriptor);

        fields.forEach(fieldObj -> {

            try {
                String key = getKey(fieldObj);


                List<Object> array = null;

                Method isPackableMethod = fieldObj.getClass().getDeclaredMethod("isRepeated");
                Boolean isRepeated = (Boolean) isPackableMethod.invoke(fieldObj);

                logger.info( key + "isRepeated: " + isRepeated.toString());

                if(isRepeated){
                    array = Lists.newArrayList();
                }

                Object javaType = getJavaType(fieldObj);
                if (ProtoJavaType.MESSAGE.name().equals(javaType.toString())) {
                    Method getMessageTypeMethod = fieldObj.getClass().getDeclaredMethod("getMessageType");
                    Object descriptorFieldObj = getMessageTypeMethod.invoke(fieldObj);

                    if (!Objects.isNull(array)) {
                        array.add(toDefaultJson(descriptorFieldObj, Maps.newHashMap()));
                        jsonMap.put(key, array);
                    } else {
                        jsonMap.put(key, toDefaultJson(descriptorFieldObj, Maps.newHashMap()));
                    }
                } else {
                    if (!Objects.isNull(array)) {
                        array.add(protobufDefaultValueMock.getDefaultValue(javaType.toString()));
                        jsonMap.put(key, array);
                    } else {
                        jsonMap.put(key, protobufDefaultValueMock.getDefaultValue(javaType.toString()));
                    }
                }

            } catch (Exception exception) {
                exception.printStackTrace();
            }

        });

        return jsonMap;
    }

    private Object getJavaType(Object fieldDescriptor) throws Exception {
        Method getContainingTypeMethod = fieldDescriptor.getClass().getDeclaredMethod("getJavaType");

        Object getJavaTypeObj = getContainingTypeMethod.invoke(fieldDescriptor);

        return getJavaTypeObj;
    }

    private String getKey(Object descriptor) throws Exception {
        Method nameMethod = descriptor.getClass().getDeclaredMethod("getName");

        return (String) nameMethod.invoke(descriptor);
    }


    private List<JsonTreeEntity> mapDataToJsonTree(Map<String, Object> mapData){
        List<JsonTreeEntity> result = Lists.newArrayList();
        mapData.forEach((key, value) -> {
            JsonTreeEntity jsonTreeEntity = new JsonTreeEntity();
            jsonTreeEntity.setLabel(key);
            if(value instanceof Map){
                jsonTreeEntity.setChildren(mapDataToJsonTree((Map<String, Object>)value));
            }
            result.add(jsonTreeEntity);
        });

        return result;
    }


}
