package com.bjsxt.集合.map;

import java.util.LinkedList;

public class SxtHashMap002 {
    private LinkedList[] arr = new LinkedList[999];
    public static void main(String[] args) {
        SxtHashMap002 map = new SxtHashMap002();
        map.put("James",23);
        map.put("James",6);
        System.out.println(map.get("James"));
    }
    public void put(Object key,Object value){
        /**取对象key的地址的hash值*/
        int hashCode = key.hashCode();
        /**hash值对数组长度取余，得到存放下标*/
        int index = hashCode % arr.length;
        if (this.arr[index] != null){
            LinkedList linkedList = this.arr[index];
            /**map中包含该key，则覆盖其value*/
            if (this.containKeys(key)){
                for (int i = 0;i<linkedList.size();i++){
                    SxtEntry e = (SxtEntry) linkedList.get(i);
                    if (e.getKey().equals(key)){
                        e.setValue(value);
                    }
                }

            }else {/**map中不包含该key，直接将Entry对象存入链表中*/
                SxtEntry e = new SxtEntry(key,value);
                linkedList.add(e);
            }
        }else {
            this.arr[index] = new LinkedList();
            this.arr[index].add(new SxtEntry(key,value));
        }
    }
    public Object get(Object key){
        /**取对象key的地址的hash值*/
        int hashCode = key.hashCode();
        /**hash值对数组长度取余，得到存放下标*/
        int index = hashCode % arr.length;
        LinkedList linkedList = this.arr[index];
        for (int i = 0;i<linkedList.size();i++){
            SxtEntry e = (SxtEntry) linkedList.get(i);
            if (e.getKey().equals(key)){
                return e.getValue();
            }
        }
        return null;
    }
    public boolean containKeys(Object key){
        /**取对象key的地址的hash值*/
        int hashCode = key.hashCode();
        /**hash值对数组长度取余，得到存放下标*/
        int index = hashCode % arr.length;
        LinkedList linkedList = this.arr[index];
        for (int i = 0;i<linkedList.size();i++){
            SxtEntry e = (SxtEntry) linkedList.get(i);
            if (e.getKey().equals(key)){
                return true;
            }
        }
        return false;
    }
}
