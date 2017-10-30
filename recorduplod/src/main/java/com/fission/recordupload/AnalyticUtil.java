package com.fission.recordupload;


import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.GZIPOutputStream;

/**
 * Created by hemeng on 16/2/22.
 */
public class AnalyticUtil {
    public static final int ANALYTIC_COUNT_LIMIT = 10;
    public static final String ANALYTIC_API = "";
    static final String CHARSET = "UTF-8";
    private static final List<BaseEvent> queue = new ArrayList<>();
    public static ExecutorService threadPool = Executors.newSingleThreadExecutor();
    /**
     * 正常统计操作事件
     *
     * @param event
     */
    public static synchronized void analytic(BaseEvent event) {
        if (event == null) {
            return;
        }
        //当囤积的数量 超过 100 个的话，开始删除
        if (queue.size() > 10 * ANALYTIC_COUNT_LIMIT) {
            queue.remove(0);
        }
        queue.add(event);
        if (queue.size() >= ANALYTIC_COUNT_LIMIT) {
            JSONArray array = new JSONArray();
            //fastjson 的 bean 类 必须有set 个 get 方法
            array.addAll(queue);
            saveAnalyticData(queue);
            queue.clear();
            sendAnalyticData(array.toString());
        }
        GoogleAnalyticsUtils.onAnalyticEvent(event);
    }

    /**
     * 当用户选择退出时，发送剩下的事件
     */
    public static void onFinish() {
        if (queue != null && queue.size() > 0) {
            JSONArray array = new JSONArray();
            //fastjson 的 bean 类 必须有set 个 get 方法
            array.addAll(queue);
            saveAnalyticData(queue);
            queue.clear();
            sendAnalyticData(array.toString());
        }
    }
    /**
     * 添加充值打点日志及写入到sdcard测试
     *
     * @param queue
     */
    private static void saveAnalyticData(List<BaseEvent> queue) {
        JSONArray array = new JSONArray();
        for (BaseEvent baseEvent : queue) {
            if (baseEvent.getAction_type().startsWith("recharge") || "monitor".equals(baseEvent.getCategory())) {
                array.add(baseEvent);
            }
        }
        if (array.size() < 1) {
            return;
        }
        final String data = array.toString();
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                if (TextUtils.isEmpty(data)) {
                    return;
                }
                String newData = data;
//                String json = SdCardUtil.getJson(KEY_LOG_DATA);
//                if (!TextUtils.isEmpty(json)) {
//                    newData = json + data;
//                }
//                SdCardUtil.saveJson(KEY_LOG_DATA, newData);
            }
        });
    }
    private static void sendAnalyticData(final String data) {
        if (TextUtils.isEmpty(data)) {
            return;
        }
        try {
            sendByteArray(ANALYTIC_API, compress(data), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /***
     * @param data
     * @param requestUrl
     * @param useGzip  是否使用了 gzip
     *
     * */
    public static void sendByteArray(final String requestUrl, byte[] data, boolean useGzip) {
        try {
//            OkHttpClient okHttpClient = new OkHttpClient();
//            final RequestBody body = RequestBody.create(MediaType.parse("application/octet-stream"), data);
//            Call call = null;
//            if (useGzip) {
//                Request request = new Request.Builder()
//                        .addHeader("Content-Encoding", "gzip")
//                        .addHeader("user-agent", System.getProperty("http.agent"))
//                        .post(body)
//                        .url(requestUrl)
//                        .build();
//                call = okHttpClient.newCall(request);
//            } else {
//                Request request = new Request.Builder()
////                        .addHeader("Content-Encoding", "gzip")
//                        .addHeader("user-agent", System.getProperty("http.agent"))
//                        .post(body)
//                        .url(requestUrl)
//                        .build();
//                call = okHttpClient.newCall(request);
//            }
//            call.enqueue(new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//                    LogUtils.e("", "");
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                }
//            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /***
     * Gzip 压缩字符串
     * @param data 需要压缩的字串
     * @return 二进制数组
     * */
    public static byte[] compress(String data) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length());
        GZIPOutputStream gzip = new GZIPOutputStream(bos);
        gzip.write(data.getBytes(CHARSET));
        gzip.close();
        byte[] compressed = bos.toByteArray();
        bos.close();
        return compressed;
    }
}
