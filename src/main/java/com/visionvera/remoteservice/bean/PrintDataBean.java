package com.visionvera.remoteservice.bean;

import java.io.Serializable;
import java.util.List;

public class PrintDataBean implements Serializable {

    private Integer type;
    private List<EmbeddedBean> files;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<EmbeddedBean> getFiles() {
        return files;
    }

    public void setFiles(List<EmbeddedBean> files) {
        this.files = files;
    }
}
