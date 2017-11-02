package com.fission.recordupload;

import android.os.Build;

import java.io.Serializable;

/**
 * Created by lining on 2017/10/31.
 */

public class RecordBean implements Serializable {

    private String action_type;
    private String transactionId;//该数据归属此id的事务
    private long time;
    public String dev;
    public int os_version;
    public RecordBean() {
        this.time = System.currentTimeMillis();
        this.dev = String.valueOf(Build.BRAND + " " + Build.MODEL);
        this.os_version = Build.VERSION.SDK_INT;
    }
    public long getTime() {
        return time;
    }
    public void setTime(long time) {
        this.time = time;
    }
    public void setAction_type(String action_type) {
        this.action_type = action_type;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecordBean that = (RecordBean) o;

        if (time != that.time) return false;
        if (os_version != that.os_version) return false;
        if (!action_type.equals(that.action_type)) return false;
        if (!transactionId.equals(that.transactionId)) return false;
        return dev.equals(that.dev);

    }

    @Override
    public int hashCode() {
        int result = action_type.hashCode();
        result = 31 * result + transactionId.hashCode();
        result = 31 * result + (int) (time ^ (time >>> 32));
        result = 31 * result + dev.hashCode();
        result = 31 * result + os_version;
        return result;
    }
}
