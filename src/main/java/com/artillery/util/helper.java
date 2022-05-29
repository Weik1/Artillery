package com.artillery.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class helper {
    public static String getNowTime() {
        //获取当前时间
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// a为am/pm的标记
        Date date = new Date();// 获取当前时间
        return sdf.format(date);
    }

    public static ArrayList<String> ParseTargetList(String targetString) {
        ArrayList<String> targetList = new ArrayList<>();
        for (String target : targetString.split("\n")) {
            if (UrlCheck(target).equals("0")) {
                System.out.println("[-] Url: " + target + "不符合规范");
            } else {
                targetList.add(UrlCheck(target));
            }

        }
        return targetList;
    }

    public static String UrlCheck(String url) {
        //匹配是否是正常的 url
        String pattern = "(ht|f)tp(s?)\\:\\/\\/[0-9a-zA-Z]([-.\\w]*[0-9a-zA-Z])*(:(0-9)*)*(\\/?)([a-zA-Z0-9\\-\\.\\?\\,\\'\\/\\\\\\+&amp;%\\$#_]*)?";
        if (!url.matches(pattern)) {
            return "0";
        }
        if (!url.substring(url.length() - 1, url.length()).equals("/")) {
            url = url + "/";
        }

        // 创建 Pattern 对象
        return url;
    }

    public static void SystemStatus() {

        System.out.println("[+]   Artillery V1.0 系统加载成功");
        //获取插件所有类型
        List<String> payloadType = PayloadHelper.getPayloadType("Plugin/");
        //检查插件目录
        CheckPluginPath("Plugin");


        for (int i = 0; i < payloadType.size(); i++) {
            // System.out.printf("|%" + (columnMaxLengths[i] + 1) + "s", resultSetMetaData.getColumnName(i + 1));
            ArrayList<String> tmpPayloads = PayloadHelper.getPayload("Plugin/" + payloadType.get(i) + "/payloads");
            System.out.println("[+]   " + payloadType.get(i) + " 插件加载成功【" + tmpPayloads.size() + "】 => " + tmpPayloads + "");


        }
        System.out.println("");
    }

    public static void CheckPluginPath(String pluginPath) {
        //判断插件目录是否存在
        File pluginFolder = new File(pluginPath);
        if (!pluginFolder.exists() && !pluginFolder.isDirectory()) {
            pluginFolder.mkdirs();
            System.out.println("[-] 插件目录 " + pluginPath + " 为空,请检查插件目录");
        }

    }
}
