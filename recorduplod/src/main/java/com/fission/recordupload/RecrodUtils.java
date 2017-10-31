package com.fission.recordupload;

import android.app.Activity;
import android.os.Environment;

import com.alibaba.fastjson.JSON;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lining on 2017/6/8.
 */

public class RecrodUtils {
    public static final String recordDir = getAppFilePath()+"/record/event/";
    public static final String event = "event";
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

    /**
     * 文件信息已json格式保存到本地文件
     */
    public static void saveRecordByJsonToFile(BaseEvent baseEvent) {
        try {
            List<BaseEvent> baseEvents = getRecordsFormFile();
            if(baseEvents == null){
                createDir(recordDir);
                baseEvents = new ArrayList<>();
            }
            if(!baseEvents.contains(baseEvent)){
                baseEvents.add(baseEvent);
                String fileStr = JSON.toJSONString(baseEvents).toString();
                // 创建文件对象
                File fileText = new File(recordDir+event);
                // 向文件写入对象写入信息
                FileWriter fileWriter = new FileWriter(fileText);
                // 写文件
                fileWriter.write(fileStr);
                // 关闭
                fileWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**根据文件查找对应的BaseEvent*/
    public static List<BaseEvent> getRecordsFormFile(){
        List<BaseEvent> baseEvents = null;
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
                baseEvents = JSON.parseArray(str, BaseEvent.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return baseEvents;
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
