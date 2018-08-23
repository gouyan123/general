package com.dongnao.mark.delayqueue;

import java.util.concurrent.Delayed;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.TimeUnit;

public class CacheBean<T> implements Delayed {
    private Integer id;
    private String name;
    private T t;
    private Long activeTime;//当前元素到期时间

    public CacheBean() {
    }
    public CacheBean(Integer id, String name, T t, Long activeTime) {
        this.id = id;
        this.name = name;
        this.t = t;
        this.activeTime = TimeUnit.NANOSECONDS.convert(activeTime,TimeUnit.MICROSECONDS)+System.nanoTime();
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setT(T t) {
        this.t = t;
    }

    public void setActiveTime(Long activeTime) {
        this.activeTime = activeTime;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.activeTime-System.nanoTime(),TimeUnit.NANOSECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        long d = this.getDelay(TimeUnit.NANOSECONDS) - o.getDelay(TimeUnit.NANOSECONDS);
        return (d == 0) ? 0 : ((d < 0) ? -1 : 1);
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public T getT() {
        return t;
    }

    public Long getActiveTime() {
        return activeTime;
    }
}
