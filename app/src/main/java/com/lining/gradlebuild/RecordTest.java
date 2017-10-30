package com.lining.gradlebuild;

import com.fission.recordupload.BaseEvent;

/**
 * Created by lining on 2017/10/30.
 */

public class RecordTest {

    public static void initRecord(){
        TextBean textBean = new TextBean();
        textBean.userId = "111111";
        textBean.userName = "lining";
        BaseEvent<TextBean> baseEvent = new BaseEvent(BaseEvent.Type.RECORD_TRANSACTION, textBean);
        baseEvent.toJosn();
        baseEvent.obtainMap();
    }

}