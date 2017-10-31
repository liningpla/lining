package com.fission.recordupload;

import android.app.Activity;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by lining on 2017/6/8.
 */

public class RecrodUtils {
    public static final String recordDir = getAppFilePath()+"/record/event/";
    public static final String event = "event";
    private static Object object = new Object();
    /**
     * 创建文件目录
     */
    public static boolean createDir(String destDirName) {
        File dir = new File(destDirName);
        if (dir.exists()) {
            System.out.println("创建目录" + destDirName + "失败，目标目录已经存在");
            return false;
        }
        if (!destDirName.endsWith(File.separator)) {
            destDirName = destDirName + File.separator;
        }
        //创建目录
        if (dir.mkdirs()) {
            System.out.println("创建目录" + destDirName + "成功！");
            return true;
        } else {
            System.out.println("创建目录" + destDirName + "失败！");
            return false;
        }
    }

    /**
     * 获取内设sd卡路径
     */
    public static String getAppFilePath() {
        File nameFile = null;
        try {
            boolean sdCardExist = Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
            if (sdCardExist) {
                nameFile = Environment.getExternalStorageDirectory();// 获取跟目录
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nameFile.getAbsolutePath();
    }
    static volatile Integer count = 0;
    /**
     * 文件信息已json格式保存到本地文件
     */
    public static void saveRecordByJsonToFile(BaseEvent baseEvent) {
        synchronized (object){
            count ++;
            try {
                JSONObject jsonObject = JSONObject.parseObject(baseEvent.toJosn());
                String strJson = getRecordJsonFormFile();
                JSONArray jsonArray = null;
                boolean isAdd = false;
                createDir(recordDir);
                if(TextUtils.isEmpty(strJson)){
                    isAdd = true;
                    jsonArray = new JSONArray();
                }else{
                    jsonArray = JSON.parseArray(strJson);
                    if(jsonArray !=null && !jsonArray.contains(jsonObject)){
                        isAdd = true;
                    }
                }
                if(isAdd){
                    jsonArray.add(jsonObject);
                    String fileStr = jsonArray.toString();
                    // 创建文件对象
                    File fileText = new File(recordDir+event);
                    // 向文件写入对象写入信息
                    FileWriter fileWriter = new FileWriter(fileText);
                    // 写文件
                    fileWriter.write(fileStr);
                    // 关闭
                    fileWriter.close();
                    Log.e("lining"," saveRecordByJsonToFile = "+fileStr);
                }
                Log.e("lining","  ---------------count = "+count);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**根据文件查找对应的BaseEvent*/
    public static List<RecordBean> getRecordsFormFile(){
        List<RecordBean> recordBeens = null;
        File fileEvent = new File(recordDir + event);
        if(fileEvent.exists()){
            try {
                FileInputStream in=new FileInputStream(fileEvent);
                // size  为字串的长度 ，这里一次性读完
                int size=in.available();
                byte[] buffer=new byte[size];
                in.read(buffer);
                in.close();
                String str =new String(buffer,"UTF-8");
                recordBeens = JSON.parseArray(str, RecordBean.class);
                Log.e("lining"," str = "+str);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return recordBeens;
    }

    /**根据文件查找对应的FileInfo*/
    public static String getRecordJsonFormFile(){
        String str = "";
        File fileEvent = new File(recordDir + event);
        if(fileEvent.exists()){
            try {
                FileInputStream in=new FileInputStream(fileEvent);
                // size  为字串的长度 ，这里一次性读完
                int size=in.available();
                byte[] buffer=new byte[size];
                in.read(buffer);
                in.close();
                str =new String(buffer,"UTF-8");
                Log.e("lining"," getRecordJsonFormFile = "+str);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return str;
    }

    /**根据Md5删除对应文件*/
    public static void deleteRecrodsFile(){
        File file = new File(recordDir + event);
        if(file.exists()){
            file.delete();
        }
    }

    /**
     * @param  assetPath "/assets/文件名"
     * */
    public static String getAccetPath(Activity activity, String assetPath){
        InputStream abpath = activity.getClass().getResourceAsStream(assetPath);
        String path = new String(InputStreamToByte(abpath));
        return path;
    }
    private static byte[] InputStreamToByte(InputStream is){
        try {
            ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
            int ch;
            while ((ch = is.read()) != -1) {
                bytestream.write(ch);
            }
            byte imgdata[] = bytestream.toByteArray();
            bytestream.close();
            return imgdata;
        }catch (Exception e){

        }
        return null;
    }
}
