package com.visionvera.common.enums;

/**
 * @author jlm
 * @ClassName: x86 枚举
 * @Description: device_type 1-高拍仪，2-身份证识别器，3-签批板
 * @date 2018/12/24
 */
public enum CallDeviceEnum {

    CheckHeartHealth(0,"心跳检测"),
    HighShotMeter(1, "高拍仪"),
    CardReader(2, "身份证识别器"),
    SignatureBoard(3, "签名板"),
    Printer(4, "打印机"),
    FaceAlignment(5, "人脸比对"),
    X86Config(9, "Scu配置"),
    X86VersionUP(10, "Scu版本升级"),
    EquipmentStatus(8,"设备状态"),
    scuVersion(11,"Scu版本号"),
    scuRestart(12, "重启Scu"),
    ScuDetection(14, "检测SCU"),
    DeviceDetection(13, "检测启明"),
    ANOMALOUS(400, "通信异常"),
    PrintState(15,"打印机状态");



    private Integer value;
    private String name;

    CallDeviceEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public static CallDeviceEnum valueToEnum(int value) {
        for (int k = 0; k < values().length; k++) {
            if (values()[k].getValue() == value) {
                return values()[k];
            }
        }
        return null;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
