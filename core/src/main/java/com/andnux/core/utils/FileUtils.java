package com.andnux.core.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shenbin on 17/6/14.
 */

public class FileUtils {

    /**
     * 判断sdcrad是否已经安装
     * @return boolean true安装 false 未安装
     */
    public static boolean isSDCardMounted() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 得到sdcard的路径
     * @return
     */
    public static String getSDCardRoot() {
        if (isSDCardMounted()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return "";
    }

    /**
     * 创建文件的路径及文件
     * @param path 路径，方法中以默认包含了sdcard的路径，path格式是"/path...."
     * @param filename 文件的名称
     * @return 返回文件的路径，创建失败的话返回为空
     */
    public static String createMkdirsAndFiles(String path, String filename) {
        if (TextUtils.isEmpty(path)) {
            throw new RuntimeException("路径为空");
        }
        path = getSDCardRoot() + path;
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.mkdirs();
            } catch (Exception e) {
                throw new RuntimeException("创建文件夹不成功");
            }
        }
        File f = new File(file, filename);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("创建文件不成功");
            }
        }
        return f.getAbsolutePath();
    }

    /**
     * 把内容写入文件
     * @param path 文件路径
     * @param text 内容
     */
    public static void write2File(String path, String text, boolean append) {
        BufferedWriter bw = null;
        try {
            // 1.创建流对象
            bw = new BufferedWriter(new FileWriter(path, append));
            // 2.写入文件
            bw.write(text);
            // 换行刷新
            bw.newLine();
            bw.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 4.关闭流资源
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 删除文件
     * @param path
     * @return
     */
    public static void deleteFile(String path) {
        if (TextUtils.isEmpty(path)) {
            Log.d("FileUtils", "路径为空:"+path);
        }
        File file = new File(path);
        if (file.exists()) {
            try {
                file.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除目录
     * @param path
     */
    public static  void deleteDir(String path){
        if (TextUtils.isEmpty(path)) {
            Log.d("FileUtils", "路径为空:"+path);
        }
        File file = new File(path);
        if (file.exists()) {//判断文件是否存在
            if (file.isFile()) {//判断是否是文件
                file.delete();//删除文件
            } else if (file.isDirectory()) {//否则如果它是一个目录
                File[] files = file.listFiles();//声明目录下所有的文件 files[];
                for (int i = 0;i < files.length;i ++) {//遍历目录下所有的文件
                    deleteDir(files[i].getAbsolutePath());//把每个文件用这个方法进行迭代
                }
                file.delete();//删除文件夹
            }
        } else {
            System.out.println("所删除的文件不存在");
        }
    }

    /**
     * 将文件流写入文件
     * @param in
     * @param fileName
     */
    public static void copy(InputStream in,String fileName){
      try {
          FileOutputStream fos = new FileOutputStream(new File(fileName));
          byte[] b = new byte[1024];
          int len = 0;
          while ((len = in.read(b)) != -1) {
              fos.write(b, 0, len);
          }
          fos.flush();
          in.close();
          fos.close();
      }catch (Exception e){
          e.printStackTrace();
      }
    }

    /**
     * 拷贝文件
     * @param inFileName
     * @param outFileName
     */
    public static void copy(String inFileName,String outFileName){
        try {
            FileOutputStream fos = new FileOutputStream(new File(outFileName));
            InputStream in = new FileInputStream(inFileName);
            copy(in,outFileName);
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 复制Asset目录下文件夹到指定目录
     * @param c
     * @param assetDir
     * @param dir
     * @param f
     */
    public static void copyAssetsToDir(Context c, String assetDir, String dir,FileFilter f) {
        String[] files;
        try {
            // 获得Assets一共有几多文件
            files = c.getResources().getAssets().list(assetDir);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        File mWorkingPath = new File(dir);
        // 如果文件路径不存在
        if (!mWorkingPath.exists()) {
            // 创建文件夹
            if (!mWorkingPath.mkdirs()) {
                // 文件夹创建不成功时调用
            }
        }
        if (f == null){
            f = new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return true;
                }
            };
        }
        for (int i = 0; i < files.length; i++) {
            try {
                // 获得每个文件的名字
                String fileName = files[i];
                Log.d("FileUtils", "fileName = " + fileName);
                if (f.accept(new File(fileName))){
                    // 根据路径判断是文件夹还是文件
                    if (!fileName.contains(".")) {
                        if (0 == assetDir.length()) {
                            copyAssetsToDir(c,assetDir, dir + fileName + "/",f);
                        } else {
                            copyAssetsToDir(c,assetDir + "/" + fileName, dir + "/"
                                    + fileName + "/",f);
                        }
                        continue;
                    }
                    File outFile = new File(mWorkingPath, fileName);
                    if (outFile.exists())
                        outFile.delete();
                    InputStream in = null;
                    if (0 != assetDir.length())
                        in = c.getAssets().open(assetDir + "/" + fileName);
                    else
                        in = c.getAssets().open(fileName);
                    OutputStream out = new FileOutputStream(outFile);
                    // Transfer bytes from in to out
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                        out.flush();
                    }
                    in.close();
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获得文件夹大小
     * @param file
     * @return
     */
    public static double getSize(File file) {
        //判断文件是否存在
        if (file.exists()) {
            //如果是目录则递归计算其内容的总大小，如果是文件则直接返回其大小
            if (!file.isFile()) {
                //获取文件大小
                File[] fl = file.listFiles();
                double ss = 0;
                for (File f : fl)
                    ss += getSize(f);
                return ss;
            } else {
                double ss = (double) file.length() / 1024 / 1024;
                System.out.println(file.getName() + " : " + ss + "MB");
                return ss;
            }
        } else {
            System.out.println("文件或者文件夹不存在，请检查路径是否正确！");
            return 0.0;
        }
    }

    public static List<String> getFileList(String strPath, FileFilter filter) {
        return getFileList(null,strPath,filter);
    }

    private static List<String> getFileList(List<String> filelist, String strPath, FileFilter filter) {
        if (filelist == null){
            filelist = new ArrayList<>();
        }
        File dir = new File(strPath);
        File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                if (files[i].isDirectory()) { // 判断是文件还是文件夹
                    getFileList(filelist,files[i].getAbsolutePath(),filter); // 获取文件绝对路径
                } else if (filter.accept(files[i])) { // 判断文件名是否以.avi结尾
                    String strFileName = files[i].getAbsolutePath();
                    filelist.add(files[i].getAbsolutePath());
                } else {
                    continue;
                }
            }
        }
        return filelist;
    }

    /**
     * 获得文件名
     * @param path
     * @return
     */
    public static  String getFileName(String path){
        String name = "";
        File file = new File(path);
        if (file.exists()){
            name = file.getName();
        }
        return  name;
    }

    /**
     * 获得文件扩展名
     * @param path
     * @return
     */
    public static  String getFileExtension(String path){
        String name = "";
        if (path.contains(".")){
            int i = path.lastIndexOf(".");
            name = path.substring(i+1);
        }
        return  name;
    }
}
