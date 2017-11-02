package com.fission.recordupload;


import android.text.TextUtils;

import com.fission.threadpool.Priority;
import com.fission.threadpool.RecordThreadManger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by hemeng on 16/2/22.
 */
public class AnalyticUtil {
    static final String CHARSET = "UTF-8";
    /**
     * 正常统计操作事件
     *
     * @param event
     */
    public static void analytic(final BaseEvent event, final String url) {
        if (event == null) {
            return;
        }
        RecordThreadManger.getInstance().execute(Priority.HIGH, new Runnable() {
            @Override
            public void run() {
                sendByteArray(url, event.toJosn(), true, new AnalyticEventListener() {
                    @Override
                    public void onSuccess() {
                    }
                    @Override
                    public void onFailure() {
                        if(TextUtils.equals(event.getCategory(), BaseEvent.RECORD_TRANSACTION)){
                            RecrodUtils.saveRecordByJsonToFile(event);
                        }
                    }
                });
            }
        });
        GoogleAnalyticsUtils.onAnalyticEvent(event);
    }
    public static void doUplaodFailureEvent(final String url){
        final String recordsJson = RecrodUtils.getRecordJsonFormFile();
        if(TextUtils.isEmpty(recordsJson)){
            RecordThreadManger.getInstance().execute(Priority.HIGH, new Runnable() {
                @Override
                public void run() {
                    sendByteArray(url, recordsJson, true, new AnalyticEventListener() {
                        @Override
                        public void onSuccess() {
                            RecrodUtils.deleteRecrodsFile();
                        }
                        @Override
                        public void onFailure() {
                        }
                    });
                }
            });
        }
    }
    /***
     * @param requestUrl
     * @param eventStr
     * @param useGzip  是否使用了 gzip
 *  */
    public static void sendByteArray(final String requestUrl, final String eventStr, boolean useGzip, final AnalyticEventListener listener) {
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            final RequestBody body = RequestBody.create(MediaType.parse("application/octet-stream"), compress(eventStr));
            Call call = null; Request request = null;
            if (useGzip) {
                request = new Request.Builder()
                        .addHeader("Content-Encoding", "gzip")
                        .addHeader("user-agent", System.getProperty("http.agent"))
                        .post(body)
                        .url(requestUrl)
                        .build();
            } else {
                request = new Request.Builder()
                        .addHeader("user-agent", System.getProperty("http.agent"))
                        .post(body)
                        .url(requestUrl)
                        .build();
            }
            call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    listener.onFailure();
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response.code() == 200){
                        listener.onSuccess();
                    }else{
                        listener.onFailure();
                    }
                }
            });
        } catch (Exception e) {
            listener.onFailure();
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

    private interface AnalyticEventListener{
        void onSuccess();
        void onFailure();
    }
}
