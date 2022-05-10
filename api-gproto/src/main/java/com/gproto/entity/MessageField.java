package com.gproto.entity;

import java.io.Serializable;
import java.util.List;

public class MessageField implements Serializable {
    private String fieldName;
    private List<MessageField> subFields;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public List<MessageField> getSubFields() {
        return subFields;
    }

    public void setSubFields(List<MessageField> subFields) {
        this.subFields = subFields;
    }

    @Override
    public String toString() {
        return "MessageField{" +
                "fieldName='" + fieldName + '\'' +
                ", subFields=" + subFields +
                '}';
    }
}
