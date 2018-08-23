package com.bjsxt.集合.map;

public class SxtHashMap001 {
    /**定义数组存键值对 SxtEntry 类对象*/
    private SxtEntry[] arr = new SxtEntry[999];
    private int size;
    public void put(Object key,Object value){
        /**map中不包含这个key，则新创建一个 Entry 对象*/
        if (!this.containKeys(key)){
            SxtEntry e = new SxtEntry(key,value);
            this.arr[size++] = e;
        }
        /**map中包含这个key，则获取这个 Entry 对象，并对其value重写赋值*/
        else {
            for (int i = 0;i < size;i++){
                if (key.equals(arr[i].getKey())){
                    arr[i].setValue(value);
                }
            }
        }
    }
    public Object get(Object key){
        for (int i = 0;i < size;i++){
            if (key.equals(arr[i].getKey())){
                return arr[i].getValue();
            }
        }
        return null;
    }
    /**判断 map 中是否含有此 key*/
    public boolean containKeys(Object key){
        for (int i = 0;i < size;i++){
            if (key.equals(arr[i].getKey())){
                return true;
            }
        }
        return false;
    }
    public static void main(String[] args) {
        SxtHashMap001 map = new SxtHashMap001();
        map.put("James",23);
        map.put("James",6);
        System.out.println(map.get("James"));
    }
}

class SxtEntry{
    private Object key;
    private Object value;

    public SxtEntry(Object key, Object value) {
        this.key = key;
        this.value = value;
    }

    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
