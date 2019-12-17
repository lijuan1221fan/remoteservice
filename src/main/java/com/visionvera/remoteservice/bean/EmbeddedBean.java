package com.visionvera.remoteservice.bean;

public class EmbeddedBean extends TypeBean {

    //需要打印的文件   以byte数组方式传递
    private byte[] fileData;
    //文件名
    private String fileName;
    //文件后缀
    private String format;
    //升级包文件
    private byte[] updateData;

    private String shaValue;
    //人证比对传输给x86 文件
    private byte[] photo;

    private String url;

    private String version;

    //设备类型： 1代表一体机     2代表叫号机
    private Integer devType;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getShaValue() {
        return shaValue;
    }

    public void setShaValue(String shaValue) {
        this.shaValue = shaValue;
    }

    public Integer getDevType() {
        return devType;
    }

    public void setDevType(Integer devType) {
        this.devType = devType;
    }

    public byte[] getUpdateData() {
        return updateData;
    }

    public void setUpdateData(byte[] updateData) {
        this.updateData = updateData;
    }
}
