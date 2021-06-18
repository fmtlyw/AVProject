package com.lyw.avproject;

/**
 * 功能描述:
 * Created on 2021/6/18.
 *
 * @author lyw
 */
public class PartBeen {
    /**
     * 关卡名字
     */
    private String partName;
    /**
     * 跳转地址
     */
    private String path;


    public PartBeen(String partName, String path) {
        this.partName = partName;
        this.path = path;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
