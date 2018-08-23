package com.gupaoedu.vip.spring.framework.webmvc;

import java.util.Map;
/**封装 页面名称 参数map<string,value>*/
public class GPModelAndView {
    /*viewName 页面名称*/
    private String viewName;
    /*key:model封装的参数名称,value:model封装的参数的值*/
    private Map<String,?> model;

    public GPModelAndView(String viewName, Map<String, ?> model) {
        this.viewName = viewName;
        this.model = model;
    }
    public String getViewName() {
        return viewName;
    }
    public void setViewName(String viewName) {
        this.viewName = viewName;
    }
    public Map<String, ?> getModel() {
        return model;
    }
    public void setModel(Map<String, ?> model) {
        this.model = model;
    }
}
