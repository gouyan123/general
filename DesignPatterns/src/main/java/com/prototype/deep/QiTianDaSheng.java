package com.prototype.deep;

import java.io.*;
import java.util.Date;

public class QiTianDaSheng extends Monkey implements Cloneable,Serializable {
    public JinGuBang jinGuBang;
    public  QiTianDaSheng(){
        //只是初始化
        this.birthday = new Date();
        this.jinGuBang = new JinGuBang();
    }
    /*浅拷贝*/
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return this.deepClone();
    }
    /*深拷贝*/
    public Object deepClone(){
        try{
            /*将对象内存状态转换成字节码*/
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            /*将对象转换成字节码，并将内存中内容写入输出流 oos 中*/
            oos.writeObject(this);
            /*用字节码重新创建对象*/
            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bis);
            /*字节码重新创建对象，将 流osi 中的内容读到内存中*/
            QiTianDaSheng copy = (QiTianDaSheng)ois.readObject();
            copy.birthday = new Date();
            return copy;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public QiTianDaSheng copy(QiTianDaSheng target){

        QiTianDaSheng qiTianDaSheng = new QiTianDaSheng();
        qiTianDaSheng.height = target.height;
        qiTianDaSheng.weight = target.height;

        qiTianDaSheng.jinGuBang = new JinGuBang();
        qiTianDaSheng.jinGuBang.h = target.jinGuBang.h;
        qiTianDaSheng.jinGuBang.d = target.jinGuBang.d;

        qiTianDaSheng.birthday = new Date();
        return  qiTianDaSheng;
    }


}
