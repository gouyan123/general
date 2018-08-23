package javax.core.common.jdbc.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.Map;

/**动态数据源*/
/**AbstractRoutingDataSource为 spring 提供的路由数据源，执行数据连接前，根据业务规则动态获得连接，
 * 从而达到动态切换数据库连接的效果，主要用于分库分表*/
public class DynamicDataSource extends AbstractRoutingDataSource {  
    /*entry的目的，主要是用来给每个数据源打个标记，由此知道什么业务对应什么数据源*/
	private DynamicDataSourceEntry dataSourceEntry;
	/*隐藏父类 2 个成员变量*/
    /*private Map<Object, Object> targetDataSources;
    private Object defaultTargetDataSource;*/
    @Override  
    protected Object determineCurrentLookupKey() {
        return this.dataSourceEntry.get();  
    }  
  
    public void setDataSourceEntry(DynamicDataSourceEntry dataSourceEntry) {  
        this.dataSourceEntry = dataSourceEntry;
    }
    
    public DynamicDataSourceEntry getDataSourceEntry(){
        return this.dataSourceEntry;
    }
}
