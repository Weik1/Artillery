package com.artillery.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PayloadHelper {
    public static ArrayList<String> getPayload(String path) {
        ArrayList<String> plugins = new ArrayList<String>();
        String[] blackList = {".DS_Store"};
        File file = new File(path);
        try {
            if (file.exists() & file.isDirectory()) {
                for (String vuln : file.list()) {
                    if (!Arrays.asList(blackList).contains(vuln)) {
                        plugins.add(vuln);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("[-] POC 加载错误");
        }
        return plugins;
    }

    //加载lib下的jar包
    public static ArrayList<String> getJar(String path) {
        ArrayList<String> jars = new ArrayList<String>();
        File file = new File(path);
        try {
            if (file.exists() & file.isDirectory()) {
                for (String jar : file.list()) {
                    if (jar.endsWith(".jar")) {
                        jars.add(jar);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jars;
    }

    //读取payload的类型文件夹
    public static List<String> getPayloadType(String path) {
        List<String> typeList = new ArrayList<String>();
        File file = new File(path);
        File[] tempList = file.listFiles();
        if (tempList.length != 0) {
            for (int i = 0; i < tempList.length; i++) {
                if (tempList[i].isDirectory()) {
                    File file1 = new File(tempList[i].toString() + "/payloads");
                    File file2 = new File(tempList[i].toString() + "/libs");
                    if (file1.exists() && file2.exists()) {
                        typeList.add(tempList[i].getName());
                    }
                }
            }
        } else {
            System.out.println("[-] POC类型加载为空，请检查/Plugin目录");
        }
        return typeList;
    }

}
