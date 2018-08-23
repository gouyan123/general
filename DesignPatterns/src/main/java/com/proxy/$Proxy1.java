package com.proxy;
import java.lang.reflect.*;
public class $Proxy1 implements com.proxy.Moveable{
private com.proxy.InvocationHandler h;
public $Proxy1(com.proxy.InvocationHandler h) {
this.h = h;
}
@Override
public void move(){
try{
Method md = com.proxy.Moveable.class.getMethod("move");
h.invoke(md,this,null);
}catch(Exception e){};
}
}