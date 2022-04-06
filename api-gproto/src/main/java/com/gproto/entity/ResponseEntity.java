package com.gproto.entity;

import java.io.Serializable;

public class ResponseEntity implements Serializable {
    private String code;
    private Object data;

    public ResponseEntity(String code) {
        this.code = code;
    }

    public ResponseEntity(String code, Object data) {
        this.code = code;
        this.data = data;
    }

    public static ResponseEntity respErrorInstance(String code){
        return new ResponseEntity(code);
    }

    public static ResponseEntity respSuccess(Object data) {
        return new ResponseEntity("00000", data);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
