package com.lining.gradlebuild;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Map<String, String> map = new HashMap<String, String>();
        map.put("lining","lining");
        testLog("testLog", map);
    }

    private void testLog(String param, Map<String, String> map){
        Log.e("lining","----------------------testLog");
    }
}
