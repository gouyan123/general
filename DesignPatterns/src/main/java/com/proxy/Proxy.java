package com.proxy;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/*Proxy.newProxyInstance()方法实质：拼凑目标类的代理类的代码的字符串，然后利用反射获得代理类实例化对象*/
public class Proxy {
    static String rt = "\r\n";
    public static Object newProxyInstance(Class<?> interf,InvocationHandler h) throws Exception {
        /**********第 1 阶段：目标类代理类的代码的字符串是死的***********************************
        String src =
        "package com.proxy;" + rt +
        "public class $Proxy1 implements Moveable {" + rt +
            "private Moveable t;" + rt +
            "public $Proxy1(Moveable t) {" + rt +
                "this.t = t;" + rt +
            "}" + rt +
            "@Override" + rt +
            "public void move() throws Exception {" + rt +
                "long start = System.currentTimeMillis();" + rt +
                "System.out.println(\"start at : \" + start);" + rt +
                "this.t.move();" + rt +
                "long end = System.currentTimeMillis();" + rt +
                "System.out.println(\"end at : \" + end);" + rt +
                "System.out.println(\"spend : \" + (end - start));" + rt +
            "}" + rt +
        "}";
        *********************************************************************************/
        /*目标类代理类的方法代码字符串*/
        /**********第 2 阶段：目标类代理类的代码的字符串中传递接口变量****************************
        String methodStr = "";
        Method[] methods = interf.getDeclaredMethods();
        for (Method method : methods) {
            methodStr += "@Override" + rt +
            "public void " + method.getName() + "() throws Exception {" + rt +
            "long start = System.currentTimeMillis();" + rt +
            "this.t." + method.getName() + "();" + rt +
            "long end = System.currentTimeMillis();" + rt +
            "System.out.println(\"time = \" + (end - start));" + rt +
            "}";
        }
        String src =
        "package com.proxy;" + rt +
        "public class $Proxy1 implements " + interf.getName() + "{" + rt +
            "private " + interf.getName() + " t;" + rt +
            "public $Proxy1( " + interf.getName() + "  t) {" + rt +
                "this.t = t;" + rt +
            "}" + rt +
            methodStr + rt +
        "}";
         *********************************************************************************/
        String methodStr = "";
        Method[] methods = interf.getDeclaredMethods();
        for (Method method : methods) {
            methodStr += "@Override" + rt +
            "public void " + method.getName() + "(){" + rt +
                "try{" + rt +
                    "Method md = " + interf.getName() + ".class.getMethod(\"" + method.getName() + "\");" + rt +
                    "h.invoke(md,this,null);" + rt +
                "}catch(Exception e){};" + rt +
            "}";
        }
        String src =
        "package com.proxy;" + rt +
        "import java.lang.reflect.*;" + rt +
        "public class $Proxy1 implements " + interf.getName() + "{" + rt +
            "private com.proxy.InvocationHandler h;" + rt +
            "public $Proxy1(com.proxy.InvocationHandler h) {" + rt +
                "this.h = h;" + rt +
            "}" + rt +
        methodStr + rt +
        "}";
        /*1、将字符串保存到 *.java 中，该 *.java文件就是代理类，当被编译后，就会被删除，因此只需要生成一个代理类的字符串*/
        System.out.println("*表示当前项目根路径*" + System.getProperty("user.dir"));
        String fileName = System.getProperty("user.dir")
                + "/DesignPatterns/src/main/java/com/proxy/$Proxy1.java";
        //String fileName = "/D:/$Proxy1.java";

        File file = new File(fileName);
        FileWriter fw = new FileWriter(file);
        fw.write(src);
        fw.flush();
        fw.close();
        /*2、把生成的 *.java 文件编译为 *.class */
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        System.out.println("类名字：" + compiler.getClass().getName());
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null,null,null);
        Iterable units = fileManager.getJavaFileObjects(fileName);
        JavaCompiler.CompilationTask t = compiler.getTask(null,fileManager,null,null,null,units);
        t.call();
        fileManager.close();
        /*3、将 *.class 文件加载到内存得到 Class<?>类对象c */
        /*ClassLoader：将硬盘中的*.class文件加载到内存中，需要指定加载路径URL*/
        /*URL指定加载范围，在这个文件夹下查找 *.class*/
        URL[] urls = new URL[]{new URL("file:/" + System.getProperty("user.dir") +"/DesignPatterns/src/main/java/")};
        //URL[] urls = new URL[]{new URL("file:/D:")};

        URLClassLoader urlClassLoader = new URLClassLoader(urls);
        Class<?> c = urlClassLoader.loadClass("com.proxy.$Proxy1");
        System.out.println("c = " + c.getName());
        /*4、在内存中生成该对象*/
        /*因为构造函数参数为 Moveable t，所以参数类型为 Moveable.class，参数值为 new Tank()*/
        /*获得参数类型为 Moveable.class 的构造方法*/
        /*第 1 2 阶段*/
        /*Constructor constructor = c.getDeclaredConstructor(Moveable.class);*/
        /*Moveable m = (Moveable) constructor.newInstance(new Tank());*/
        /*第 3 阶段*/
        Constructor constructor = c.getDeclaredConstructor(InvocationHandler.class);
        Object obj = constructor.newInstance(h);
        return obj;
    }

    public static void main(String[] args) throws Exception {
        /*****************第 2 阶段测试 ************************************************
        //获得代理对象 m
        Moveable m = (Moveable) Proxy.newProxyInstance(Moveable.class);
        //调用代理类目标类共同实现的接口中的方法
        m.move();
        ******************************************************************************/
        /*第 3 阶段*/
        InvocationHandler h = new TimeHandler(new Tank());
        Moveable m = (Moveable) Proxy.newProxyInstance(Moveable.class,h);
        m.move();
    }
}
