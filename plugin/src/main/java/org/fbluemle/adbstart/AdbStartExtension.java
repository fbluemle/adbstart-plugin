package org.fbluemle.adbstart;

public class AdbStartExtension {
    private String activity = ".MainActivity";
    private String deviceSerial;
    private String extraAmArgs = "";
    private String adbPath = "adb";

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getDeviceSerial() {
        return deviceSerial;
    }

    public void setDeviceSerial(String deviceSerial) {
        this.deviceSerial = deviceSerial;
    }

    public String getExtraAmArgs() {
        return extraAmArgs;
    }

    public void setExtraAmArgs(String extraAmArgs) {
        this.extraAmArgs = extraAmArgs;
    }

    public String getAdbPath() {
        return adbPath;
    }

    public void setAdbPath(String adbPath) {
        this.adbPath = adbPath;
    }
}
