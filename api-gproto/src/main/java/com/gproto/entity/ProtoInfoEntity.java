package com.gproto.entity;

import java.io.Serializable;

public class ProtoInfoEntity implements Serializable {
    private String className;
    private String packageName;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }


    @Override
    public String toString() {
        return "ProtoInfoRespEntity{" +
                "className='" + className + '\'' +
                ", packageName='" + packageName + '\'' +
                '}';
    }
}
