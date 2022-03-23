package com.gproto.entity;

public class ProtoUploadRequestEntity {
    private String fileName;
    private String uid;
    private String content;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ProtoUploadRequestEntity{" +
                "fileName='" + fileName + '\'' +
                ", uid='" + uid + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
