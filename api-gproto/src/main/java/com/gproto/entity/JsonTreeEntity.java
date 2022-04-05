package com.gproto.entity;

import java.io.Serializable;
import java.util.List;

//   {
//           label: "一级 1",
//           children: [
//           {
//           label: "二级 1-1",
//           children: [
//           {
//           label: "三级 1-1-1",
//           },
//           ],
//           },
//           ],
//           }
public class JsonTreeEntity implements Serializable {
    private String label;
    private List<JsonTreeEntity> children;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<JsonTreeEntity> getChildren() {
        return children;
    }

    public void setChildren(List<JsonTreeEntity> children) {
        this.children = children;
    }

}
