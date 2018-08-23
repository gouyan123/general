package com.gupaoedu.vip.orm.demo.dao;

import com.alibaba.fastjson.JSON;
import com.gupaoedu.vip.orm.demo.model.Member;
import com.gupaoedu.vip.orm.framework.BaseDaoSupport;
import com.gupaoedu.vip.orm.framework.QueryRule;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import javax.annotation.Resource;
import javax.core.common.jdbc.datasource.DynamicDataSource;
import javax.sql.DataSource;
import java.util.List;

@Repository
public class MemberDao extends BaseDaoSupport<Member,Long> {
    private DynamicDataSource dataSource;
    private JdbcTemplate template;
    /**------------------设置数据源-----------------------------------*/
    /**从IOC容器中获取 DynamicDataSource 对象的地址，并赋给setDataSource(...)方法的参数dataSource*/
    /**重要重要：方法上面加 @Resource( name = "member")注解后，该方法会被自动调用，调用时刻：
     * 当方法参数被注入的时候该方法被调用，此处，当dynamicDataSource被实例化后，调用该setDataSource
     * 方法*/
    @Resource(name="dynamicDataSource")
    public void setDataSource(DataSource dataSource) {
        this.dataSource = (DynamicDataSource)dataSource;
        /*设置父类只读数据源*/
        super.setDataSourceReadOnly(this.dataSource);
        /*设置父类写数据源*/
        super.setDataSourceWrite(this.dataSource);
    }

    /**------------------设置完成------------------------------------*/
    /*获取主键的字段名称*/
    @Override
    protected String getPKColumn() {
        return "id";
    }
    /*插入数据*/
    public boolean insert(Member entity) throws Exception{
        if(entity.getAge() >= 30){
            this.dataSource.getDataSourceEntry().set("db_two");
        }else{
            this.dataSource.getDataSourceEntry().set("db_one");
        }
        Long id = super.insertAndReturnId(entity);
        entity.setId(id);
        return id > 0;
    }
    /*删除单个数据*/
    public boolean delete(Member member) throws Exception{
        return super.delete(member);
    }
    /*修改数据*/
    public boolean update(Member member) throws Exception{
        return super.update(member);
    }
    /*查找数据*/
    public List<Member> select(QueryRule queryRule){
        List<Member> memberList = super.select(queryRule);
        return memberList;
    }
}
