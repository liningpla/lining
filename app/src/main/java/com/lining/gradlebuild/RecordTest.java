package com.lining.gradlebuild;

import com.fission.recordupload.AnalyticUtil;
import com.fission.recordupload.BaseEvent;

/**
 * Created by lining on 2017/10/30.
 */

public class RecordTest {

    public static void initRecord(){
        addRecord();
//        AnalyticUtil.doUplaodFailureEvent();
    }

    private static void addRecord(){
        for(int i = 0; i < 400; i ++){
            String category = BaseEvent.RECORD_NORMAL;
            if(i % 2 == 0){
                category = BaseEvent.RECORD_TRANSACTION;
            }
            TextBean textBean = new TextBean(category, "click_action");
            textBean.userId = "111111"+"___"+i;
            textBean.userName = "lining"+"___"+i;
            AnalyticUtil.analytic(new BaseEvent(textBean));
        }
    }
}
