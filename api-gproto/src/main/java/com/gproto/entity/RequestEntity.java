package com.gproto.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author qianzhm
 */
public class RequestEntity implements Serializable {
    private String className;
    private String base64Data;
    private String jsonData;
    private String messageName;
    private String fieldName;
    private String subFieldName;
    private List<MessageField> fields;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getBase64Data() {
        return base64Data;
    }

    public void setBase64Data(String base64Data) {
        this.base64Data = base64Data;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getSubFieldName() {
        return subFieldName;
    }

    public void setSubFieldName(String subFieldName) {
        this.subFieldName = subFieldName;
    }

    public String getMessageName() {
        return messageName;
    }

    public void setMessageName(String messageName) {
        this.messageName = messageName;
    }

    public List<MessageField> getFields() {
        return fields;
    }

    public void setFields(List<MessageField> fields) {
        this.fields = fields;
    }

    @Override
    public String toString() {
        return "RequestEntity{" +
                "className='" + className + '\'' +
                ", base64Data='" + base64Data + '\'' +
                ", jsonData='" + jsonData + '\'' +
                ", messageName='" + messageName + '\'' +
                ", fieldName='" + fieldName + '\'' +
                ", subFieldName='" + subFieldName + '\'' +
                ", fields=" + fields +
                '}';
    }
}
