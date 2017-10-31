package com.fission.recordupload;

import android.os.Build;

import java.io.Serializable;

/**
 * Created by lining on 2017/10/31.
 */

public class RecordBean implements Serializable {

    private String category;
    private String action_type;
    private long time;

    public String dev;
    public int os_version;

    public RecordBean(String category, String action_type) {
        this.category = category;
        this.action_type = action_type;
        this.time = System.currentTimeMillis();
        this.dev = String.valueOf(Build.BRAND + " " + Build.MODEL);
        this.os_version = Build.VERSION.SDK_INT;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAction_type() {
        return action_type;
    }

    public void setAction_type(String action_type) {
        this.action_type = action_type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
