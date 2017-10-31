package com.fission.recordupload;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lining on 2017/10/30.
 * 上报事务类
 */

public class BaseEvent{
    /**
     * RECORD_NORMAL 普通上报类型
     * RECORD_TRANSACTION 事务上报类型
     */
    public static final String RECORD_NORMAL = "record_normal";
    public static final String RECORD_TRANSACTION = "record_transaction";


    private String category;
    private String action_type;
    private long time;
    private RecordBean recrodBean;

    public String getCategory() {
        return category;
    }

    public String getAction_type() {
        return action_type;
    }

    public RecordBean getRecrodBean() {
        return recrodBean;
    }

    public BaseEvent(RecordBean recrodBean){
        this.category = recrodBean.getCategory();
        this.action_type = recrodBean.getAction_type();
        this.time = recrodBean.getTime();
        this.recrodBean = recrodBean;
    }
    public String toJosn() {
        String jsonStr = "";
        try{
            jsonStr = JSON.toJSONString(recrodBean);
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonStr;
    }
    public Map obtainMap() {
        return ConvertObjToMap(recrodBean);
    }
    private Map ConvertObjToMap(Object obj){
        Map<String,Object> reMap = new HashMap<>();
        if (obj == null)
            return null;
        Field[] fields = obj.getClass().getDeclaredFields();
        try {
            for(int i=0;i<fields.length;i++){
                try {
                    Field f = obj.getClass().getDeclaredField(fields[i].getName());
                    f.setAccessible(true);
                    Object o = f.get(obj);
                    reMap.put(fields[i].getName(), o);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return reMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseEvent baseEvent = (BaseEvent) o;

        if (time != baseEvent.time) return false;
        if (category != null ? !category.equals(baseEvent.category) : baseEvent.category != null)
            return false;
        return action_type != null ? action_type.equals(baseEvent.action_type) : baseEvent.action_type == null;

    }

    @Override
    public int hashCode() {
        int result = category != null ? category.hashCode() : 0;
        result = 31 * result + (action_type != null ? action_type.hashCode() : 0);
        result = 31 * result + (int) (time ^ (time >>> 32));
        return result;
    }
}
