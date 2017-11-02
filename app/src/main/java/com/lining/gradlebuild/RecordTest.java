package com.lining.gradlebuild;

import android.util.Log;

import com.fission.recordupload.BaseEvent;

/**
 * Created by lining on 2017/10/30.
 */

public class RecordTest {

    public static void initRecord(){
        addRecord();
//        AnalyticUtil.doUplaodFailureEvent();
//        main();
    }

    private static void addRecord(){
        for(int i = 0; i < 40; i ++){
            TextBean textBean = new TextBean();
            textBean.userId = "111111"+"___"+i;
            textBean.userName = "lining"+"___"+i;
            BaseEvent.transaction("111","1111").obtainBean(textBean).analytic("");
//            BaseEvent.normal("").obtainBean(textBean).analytic("");
        }
    }

    //不能包括特定符号：尖括号<>、双引号""、双括号《》、斜线/
    public static void main() {
        String input = "asd\\ff...";
        String reg=".*<.*|.*>.*|.*《.*|.*》.*|.*\".*|.*/.*|.*\\.";
        System.out.println (input.matches (".*<.*"));
        Log.e("lining","input.matches (\".*x.*\") = "+input.matches (reg));
    }
}
