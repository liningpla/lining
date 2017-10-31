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

    public RecordBean() {}

    public String getCategory() {
        return category;
    }

    public String getAction_type() {
        return action_type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecordBean that = (RecordBean) o;

        if (time != that.time) return false;
        if (!category.equals(that.category)) return false;
        return action_type.equals(that.action_type);

    }

    @Override
    public int hashCode() {
        int result = category.hashCode();
        result = 31 * result + action_type.hashCode();
        result = 31 * result + (int) (time ^ (time >>> 32));
        return result;
    }
}
