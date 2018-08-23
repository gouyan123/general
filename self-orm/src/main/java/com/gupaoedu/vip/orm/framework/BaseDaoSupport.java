package com.gupaoedu.vip.orm.framework;

import com.alibaba.fastjson.JSON;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import javax.core.common.utils.GenericsUtils;
import javax.core.common.utils.StringUtils;
import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**①抽象类里面可以写一些默认的功能，即普通方法，子类继承以后，直接可以调用改方法；
 * ②客户端操作数据库服务端，必须先有连接对象，所以，先配置好数据库连接参数，再进行实例化；
 * ③通过子类继承父类，new 子类的时候，将连接参数传递给 new 父类；
 * */
public abstract class BaseDaoSupport<T extends Serializable,PK extends Serializable> {
    /**获取数据源，由各子类去获取自己的数据源*/
    private Logger log = Logger.getLogger(BaseDaoSupport.class);
    private String tableName = "";
    private JdbcTemplate jdbcTemplateWrite;
    private JdbcTemplate jdbcTemplateReadOnly;
    private DataSource dataSourceReadOnly;
    private DataSource dataSourceWrite;
    private EntityOperation<T> op;
    /**------------构造方法----------------------------------*/
    @SuppressWarnings("unchecked")
    protected BaseDaoSupport(){
        try{
            /**重点重点：new 子类()时，先调用父类构造方法，但是父类中 this 指向子类对象*/
            /*MemberDao是该类子类，加了@Repository注解，启动SpringIOC容器时，会调用new MemberDao()
            * 创建 memberDao对象，会先调用该父类 BaseDaoSupport 的构造方法，但是该父类中 this 指向
            * 子类 memberDao对象*/
            String className = this.getClass().getName();
            Class<T> entityClass = GenericsUtils.getSuperClassGenricType(getClass(), 0);
            /**this指向子类对象，this.getPKColumn()获取主键字段名，固定为 id*/
            this.op = new EntityOperation<T>(entityClass,this.getPKColumn());
            /*将表名封装到该类中*/
            this.setTableName(this.op.tableName);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    /**获取主键 字段名称 具体由子类实现*/
    protected abstract String getPKColumn();
    /**动态切换表名*/
    protected void setTableName(String tableName) {
        if(StringUtils.isEmpty(tableName)){
            this.tableName = this.op.tableName;
        }else{
            this.tableName = tableName;
        }
    }
    /**------------构造结束----------------------------------*/
    /**------------设置数据源---------------------------------*/
    protected void setDataSourceReadOnly(DataSource dataSourceReadOnly) {
        this.dataSourceReadOnly = dataSourceReadOnly;
        this.jdbcTemplateReadOnly = new JdbcTemplate(dataSourceReadOnly);
    }
    protected void setDataSourceWrite(DataSource dataSourceWrite) {
        this.dataSourceWrite = dataSourceWrite;
        this.jdbcTemplateWrite = new JdbcTemplate(dataSourceWrite);
    }
    /**------------设置完成-----------------------------------*/
    /**------------增---------------------------------------*/
    /*插入数据，entity为vo对象*/
    protected boolean insert(T entity) throws Exception{
        return this.doInsert(this.parse(entity));
    }
    /**将 vo 对象解析为Map；key:字段名,value:字段值*/
    protected Map<String,Object> parse(T entity){
        return this.op.parse(entity);
    }

    /*插入 params key:字段名,value:字段值*/
    private boolean doInsert(Map<String, Object> params) {
        String sql = this.makeSimpleInsertSql(this.getTableName(), params);
        int ret = this.jdbcTemplateWrite().update(sql, params.values().toArray());
        return ret > 0;
    }
    /**生成对象INSERT语句，简化sql拼接；params key:字段名,value:字段值*/
    private String makeSimpleInsertSql(String tableName, Map<String, Object> params){
        if(StringUtils.isEmpty(tableName) || params == null || params.isEmpty()){
            return "";
        }
        StringBuffer sb = new StringBuffer();
        sb.append("insert into ").append(tableName);
        StringBuffer sbKey = new StringBuffer();
        StringBuffer sbValue = new StringBuffer();
        sbKey.append("(");
        sbValue.append("(");
        //添加参数
        Set<String> set = params.keySet();
        int index = 0;
        for (String key : set) {
            sbKey.append(key);
            sbValue.append(" ?");
            if(index != set.size() - 1){
                sbKey.append(",");
                sbValue.append(",");
            }
            index++;
        }
        sbKey.append(")");
        sbValue.append(")");
        sb.append(sbKey).append("VALUES").append(sbValue);
        return sb.toString();
    }
    /*获取 jdbcTemplateWrite 对象*/
    private JdbcTemplate jdbcTemplateWrite() {
        return this.jdbcTemplateWrite;
    }
    /**插入并返回id，该方法被子类 insert()方法里面调用*/
    protected PK insertAndReturnId(T entity) throws Exception{
        return (PK)this.doInsertAndReturnKey(this.parse(entity));
    }
    /**插入数据并返回 id；params key:字段名,value:字段值*/
    private Serializable doInsertAndReturnKey(Map<String,Object> params){
        final List<Object> values = new ArrayList<Object>();
        /*获取 sql 语句 " insert into t_member(name,age,addr) values(?,?,?) "*/
        final String sql = this.makeSimpleInsertSql(this.getTableName(),params,values);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        final JdbcTemplate jdbcTemplate = new JdbcTemplate(this.getDataSourceWrite());
        try {
            /**定义update(T t)方法时，update()方法内部会调用参数 t 的方法
             * update()方法中会调用 createPreparedStatement()方法，jdbcTemplate中已经封装了
             * 连接对象 Connection con*/
            jdbcTemplate.update(new PreparedStatementCreator() {
                /*创建语句集方法*/
                public PreparedStatement createPreparedStatement(
                        Connection con) throws SQLException {
                    PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    for (int i = 0; i < values.size(); i++) {
                        ps.setObject(i+1, values.get(i)==null?null:values.get(i));
                    }
                    return ps;
                }
            }, keyHolder);
        } catch (DataAccessException e) {
            log.error("error",e);
        }
        if (keyHolder == null) { return ""; }
        String keyHolderStr = JSON.toJSONString(keyHolder);
        Map<String, Object> keys = keyHolder.getKeys();
        if (keys == null || keys.size() == 0 || keys.values().size() == 0) {
            return "";
        }
        Object key = keys.values().toArray()[0];
        if (key == null || !(key instanceof Serializable)) {
            return "";
        }
        if (key instanceof Number) {
            Class clazz = key.getClass();
            return (clazz == int.class || clazz == Integer.class) ? ((Number) key).intValue() : ((Number)key).longValue();
        } else if (key instanceof String) {
            return (String) key;
        } else {
            return (Serializable) key;
        }
    }
    /**拼接并获取sql语句 " insert into t_member(name,age,addr) values(?,?,?) "；其中表名字段
     * 名由vo对象反射类获取
     * params key:字段名,value:字段值；values sql语句中 ? 的值数组，
     * jdbcTemplate.update(sql,values)时使用values替代sql语句中 占位符 ?*/
    private String makeSimpleInsertSql(String tableName, Map<String, Object> params,List<Object> values){
        if(StringUtils.isEmpty(tableName) || params == null || params.isEmpty()){
            return "";
        }
        StringBuffer sb = new StringBuffer();
        sb.append("insert into ").append(tableName);
        StringBuffer sbKey = new StringBuffer();
        StringBuffer sbValue = new StringBuffer();
        /*sql语句中字段名与 sql values中占位符 ? 一一对应*/
        sbKey.append("(");
        sbValue.append("(");
        //添加参数
        Set<String> set = params.keySet();
        int index = 0;
        for (String key : set) {
            sbKey.append(key);
            sbValue.append(" ?");
            if(index != set.size() - 1){
                sbKey.append(",");
                sbValue.append(",");
            }
            index++;
            /**将sql语句中占位符 ? 对应的值，保存到 values 数组中*/
            values.add(params.get(key));
        }
        sbKey.append(")");
        sbValue.append(")");
        sb.append(sbKey).append("VALUES").append(sbValue);
        return sb.toString();
    }
    protected DataSource getDataSourceWrite() {
        return this.dataSourceWrite;
    }
    /**------------增结束-------------------------------------*/
    /**------------删----------------------------------------*/
    /**删除对象，例如：以下删除entity对应的记录 service.remove(entity); entity 待删除的实体对象*/
    protected boolean delete(T entity) throws Exception {
        /*获取 vo对象的主键Field对象的值*/
        return this.doDelete(this.op.pkField.get(entity)) > 0;
    }
    /**删除默认实例对象，返回删除记录数*/
    private int doDelete(Object pkValue){
        return this.doDelete(getTableName(), getPKColumn(), pkValue);
    }
    /**删除实例对象，返回删除记录数，tableName表名，pkName主键字段名，pkValue主键Field对象的值*/
    private int doDelete(String tableName, String pkName, Object pkValue) {
        StringBuffer sb = new StringBuffer();
        sb.append("delete from ").append(tableName).append(" where ").append(pkName).append(" = ?");
        int ret = this.jdbcTemplateWrite().update(sb.toString(), pkValue);
        return ret;
    }
    /**------------删结束-------------------------------------*/
    /**------------改----------------------------------------*/
    /**更新对象*/
    protected boolean update(T entity) throws Exception {
        /*this.op.pkField.get(entity) 主键值；将vo对象解析为Map；key:字段名,value:字段值*/
        return this.doUpdate(this.op.pkField.get(entity), this.parse(entity)) > 0;
    }
    /**更新实例对象，返回删除记录数*/
    private int doUpdate(Object pkValue, Map<String, Object> params){
        String sql = this.makeDefaultSimpleUpdateSql(pkValue, params);
        params.put(this.getPKColumn(), pkValue);
        int ret = this.jdbcTemplateWrite().update(sql, params.values().toArray());
        return ret;
    }
    /**生成默认的对象UPDATE语句，简化sql拼接 pk*/
    private String makeDefaultSimpleUpdateSql(Object pkValue, Map<String, Object> params){
        return this.makeSimpleUpdateSql(getTableName(), getPKColumn(), pkValue, params);
    }
    /**生成简单对象UPDATE语句，简化sql拼接*/
    private String makeSimpleUpdateSql(String tableName, String pkName, Object pkValue, Map<String, Object> params){
        if(StringUtils.isEmpty(tableName) || params == null || params.isEmpty()){
            return "";
        }
        StringBuffer sb = new StringBuffer();
        sb.append("update ").append(tableName).append(" set ");
        //添加参数
        Set<String> set = params.keySet();
        int index = 0;
        for (String key : set) {
            sb.append(key).append(" = ?");
            if(index != set.size() - 1){
                sb.append(",");
            }
            index++;
        }
        sb.append(" where ").append(pkName).append(" = ?");
        params.put("where_" + pkName,params.get(pkName));
        return sb.toString();
    }
    /**------------改结束-------------------------------------*/
    /**------------查----------------------------------------*/
    /**所有的查询都传 QueryRule 对象，即可统一参数*/
    public List<T> select(QueryRule queryRule){
        /*创建 QueryRuleSqlBuilder对象，将 QueryRule里面封装的内容拼凑成sql语句*/
        QueryRuleSqlBuilder bulider = new QueryRuleSqlBuilder(queryRule);
        String ws = this.removeFirstAnd(bulider.getWhereSql());
        String whereSql = ("".equals(ws) ? ws : (" where " + ws));
        String sql = "select " + this.op.allColumn + " from " + this.getTableName() + whereSql;
        Object [] values = bulider.getValues();
        String orderSql = bulider.getOrderSql();
        orderSql = (StringUtils.isEmpty(orderSql) ? " " : (" order by " + orderSql));
        sql += orderSql;
        this.log.debug(sql);
        /**RowMapper为行映射接口，用于将查询结果集中每一行记录包装为指定对象。该接口中有一个方法需要实现：
         * public Object mapRow(ResultSet rs, int rowNum) 参数rowNum表示总的结果集中当前行的行号，
         * 但参数rs并不表示总的结果集，而是表示rowNum所代表的当前行的记录所定义的结果集*/
        return (List<T>) this.jdbcTemplateReadOnly().query(sql, this.op.rowMapper, values);
    }
    private String removeFirstAnd(String sql){
        if(StringUtils.isEmpty(sql)){return sql;}
        return sql.trim().toLowerCase().replaceAll("^\\s*and", "") + " ";
    }
    protected String getTableName() {
        return this.tableName;
    }
    private JdbcTemplate jdbcTemplateReadOnly() {
        return this.jdbcTemplateReadOnly;
    }
    /**------------查结束-------------------------------------*/
}
