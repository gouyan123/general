package com.gupaoedu.vip.spring.framework.context;

public abstract class GPAbstractApplicationContext {
    //提供给子类重写；定义为 protected ，因此只能被子类重写
    protected void onRefresh(){
        // For subclasses: do nothing by default.
    }
    protected abstract void refreshBeanFactory();
}
