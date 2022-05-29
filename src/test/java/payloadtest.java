import com.artillery.util.helper;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

import static com.artillery.util.PayloadHelper.getJar;

public class payloadtest {
    public static void main(String[] args) throws MalformedURLException, ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        String targetType="weblogic";
        //引入 依赖jar包构建classloader
        ArrayList<URL> urllist=new ArrayList<>();
        File file=new File("Plugin/"+targetType+"/payloads");
        URL url=file.toURI().toURL();
        urllist.add(url);
        ArrayList<String> jars=getJar("Plugin/"+targetType+"/libs");
        for(String jar:jars){
            urllist.add((new File("Plugin/"+targetType+"/libs/"+jar)).toURI().toURL());
        }
        URL[] urls=urllist.toArray(new URL[urllist.size()]);
        ClassLoader loader=new URLClassLoader(urls);//创建类载入器

        //获取 target
        String  targetString = "CVE_2016_3510.jar";

        Class<?> cls = loader.loadClass(targetString.split("\\.")[0]);
        Constructor constructor = cls.getConstructor();
        Object obj = constructor.newInstance();
        String iphost="http://127.0.0.1:7001";
        Method method=cls.getMethod("Check",String.class);

        String result = "完成但无结果";
            System.out.println(iphost);
            Object o = method.invoke(obj, iphost);
            result = String.valueOf(o);
        System.out.println(result);


    }
}
