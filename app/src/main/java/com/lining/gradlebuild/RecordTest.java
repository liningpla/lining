package com.lining.gradlebuild;

import com.fission.recordupload.AnalyticUtil;
import com.fission.recordupload.BaseEvent;

/**
 * Created by lining on 2017/10/30.
 */

public class RecordTest {

    public static void initRecord(){
        TextBean textBean = new TextBean(BaseEvent.RECORD_TRANSACTION, "click_action");
        textBean.userId = "111111";
        textBean.userName = "lining";
        BaseEvent baseEvent = new BaseEvent(textBean);
        AnalyticUtil.analytic(baseEvent);
    }

}
