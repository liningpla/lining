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
    private static BaseEvent baseEvent;

    private String category;
    private String action_type;//标记，区分是充值，购买，登录等事务类型
    private String transactionId = "0";//事务id,所有事件归属于一个事务id
    private static RecordBean recrodBean;
    /**
     * 事务类型构造
     * @param action_type 标记，区分是充值，购买，登录，点击等动作
     * @param transactionId 事务id,所有事件归属于一个事务id
     * */
    public static BaseEvent transaction(String action_type, String transactionId){
        baseEvent = new BaseEvent(RECORD_TRANSACTION, action_type, transactionId);
        return baseEvent;
    }
    /**
     * 普通类型构造
     * @param action_type 标记，区分是充值，购买，登录，点击等动作
     * */
    public static BaseEvent normal(String action_type){
        baseEvent = new BaseEvent(RECORD_NORMAL, action_type);
        return baseEvent;
    }
    public BaseEvent(String category, String action_type){
        this.category = category;
        this.action_type = action_type;
    }
    public BaseEvent(String category, String action_type, String transactionId){
        this.category = category;
        this.action_type = action_type;
        this.transactionId = transactionId;
    }
    public BaseEvent obtainBean(RecordBean recrodBean){
        recrodBean.setCategory(category);
        recrodBean.setAction_type(action_type);
        recrodBean.setTransactionId(transactionId);
        this.recrodBean = recrodBean;
        return baseEvent;
    }

    public void analytic(String url){
       AnalyticUtil.analytic(baseEvent, url);
    }

    public String getCategory() {
        return category;
    }

    public String getAction_type() {
        return action_type;
    }

    public RecordBean getRecrodBean() {
        return recrodBean;
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

        return recrodBean != null ? recrodBean.equals(baseEvent.recrodBean) : baseEvent.recrodBean == null;

    }

    @Override
    public int hashCode() {
        return recrodBean != null ? recrodBean.hashCode() : 0;
    }
}
