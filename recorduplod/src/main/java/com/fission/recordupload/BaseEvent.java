package com.fission.recordupload;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lining on 2017/10/30.
 * 上报事务类
 */

public class BaseEvent<T>{
    /**
     * RECORD_NORMAL 普通上报类型
     * RECORD_TRANSACTION 事务上报类型
     */
    public enum Type {
        RECORD_NORMAL, RECORD_TRANSACTION
    }
    private BaseEvent baseEvent;
    public Type recordType;
    private T recrodBean;
    public T getRecrodBean() {
        return recrodBean;
    }
    public BaseEvent(Type recordType, T recrodBean){
        this.recordType = recordType;
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
}
