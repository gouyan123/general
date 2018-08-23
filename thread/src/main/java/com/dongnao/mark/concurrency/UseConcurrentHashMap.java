package com.dongnao.mark.concurrency;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class UseConcurrentHashMap {
    HashMap<String,String> hashMap = new HashMap<String,String>();
    ConcurrentHashMap<String,String> chm = new ConcurrentHashMap<String,String>();

    /**如果不存在这个 key 就存入这个 map，使用 HashMap*/
    public String putIfAbsent(String key,String value){
        synchronized (this.hashMap){
            if (!this.hashMap.keySet().contains(key)){
                return this.hashMap.put(key,value);
            }else{
                return this.hashMap.get(key);
            }
        }
    }
    /**如果不存在这个 key 就存入这个 map，使用 ConcurrentHashMap*/
    public String useConcurrentHashMap(String key,String value){
        return this.chm.putIfAbsent(key,value);
    }
}
