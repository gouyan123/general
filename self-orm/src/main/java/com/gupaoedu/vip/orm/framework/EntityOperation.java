package com.gupaoedu.vip.orm.framework;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.alibaba.fastjson.JSON;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import javax.core.common.utils.StringUtils;

/**实体对象的反射操作*/
public class EntityOperation<T> {
	private Logger log = Logger.getLogger(EntityOperation.class);
	public Class<T> entityClass = null; // 泛型实体Class对象
	/*key:字段名称*/
	public final Map<String, PropertyMapping> mappings;
	/**查看 MemberDao 中 selectByAge()*/
	/**RowMapper为行映射接口，用于将查询结果集中每一行记录包装为指定对象。该接口中有一个方法需要实现：
	 * public Object mapRow(ResultSet rs, int rowNum) 参数rowNum表示总的结果集中当前行的行号，
	 * 但参数rs并不表示总的结果集，而是表示rowNum所代表的当前行的记录所定义的结果集*/
	public final RowMapper<T> rowMapper;
	
	public final String tableName;
	public String allColumn = "*";
	public Field pkField;
	/**由clazz获取表名 */
	public EntityOperation(Class<T> clazz,String pk) throws Exception{
		if(!clazz.isAnnotationPresent(Entity.class)){
			throw new Exception("在" + clazz.getName() + "中没有找到Entity注解，不能做ORM映射");
		}
		this.entityClass = clazz;
		Table table = entityClass.getAnnotation(Table.class);
	    if (table != null) {
	    		this.tableName = table.name();
	    } else {
	    		this.tableName =  entityClass.getSimpleName();
	    }
	    /*key:getXxx,value:method*/
		Map<String, Method> getters = ClassMappings.findPublicGetters(entityClass);
		String gettersStr = JSON.toJSONString(getters);
		/*key:setXxx,value:method*/
		Map<String, Method> setters = ClassMappings.findPublicSetters(entityClass);
		String settersStr = JSON.toJSONString(getters);
		Field[] fields = ClassMappings.findFields(entityClass);
		String fieldsStr = JSON.toJSONString(getters);
	    this.fillPkFieldAndAllColumn(pk,fields);

	    this.mappings = this.getPropertyMappings(getters, setters, fields);
		String mappingsStr = JSON.toJSONString(getters);
	    this.allColumn = this.mappings.keySet().toString().replace("[", "").replace("]","").replaceAll(" ","");
		String allColumnStr = JSON.toJSONString(getters);
	    this.rowMapper = createRowMapper();
		String rowMapperStr = JSON.toJSONString(getters);
	}
	
	 Map<String, PropertyMapping> getPropertyMappings(Map<String, Method> getters, Map<String, Method> setters, Field[] fields) {
        Map<String, PropertyMapping> mappings = new HashMap<String, PropertyMapping>();
        String name;
        for (Field field : fields) {
            if (field.isAnnotationPresent(Transient.class))
                continue;
            name = field.getName();
            if(name.startsWith("is")){
            	name = name.substring(2);
            }
            name = Character.toLowerCase(name.charAt(0)) + name.substring(1);
            Method setter = setters.get(name);
            Method getter = getters.get(name);
            if (setter == null || getter == null){
                continue;
            }
            Column column = field.getAnnotation(Column.class);
            if (column == null) {
                mappings.put(field.getName(), new PropertyMapping(getter, setter, field));
            } else {
                mappings.put(column.name(), new PropertyMapping(getter, setter, field));
            }
        }
        return mappings;
    }
	 
	RowMapper<T> createRowMapper() {
	        return new RowMapper<T>() {
	            public T mapRow(ResultSet rs, int rowNum) throws SQLException {
	                try {
	                    T t = entityClass.newInstance();
	                    ResultSetMetaData meta = rs.getMetaData();
	                    int columns = meta.getColumnCount();
	                    String columnName;
	                    for (int i = 1; i <= columns; i++) {
	                        Object value = rs.getObject(i);
	                        columnName = meta.getColumnName(i);
	                        fillBeanFieldValue(t,columnName,value);
	                    }
	                    return t;
	                }catch (Exception e) {
	                    throw new RuntimeException(e);
	                }
	            }
	        };
	    }

	protected void fillBeanFieldValue(T t, String columnName, Object value) {
		 if (value != null) {
             PropertyMapping pm = mappings.get(columnName);
             if (pm != null) {
                 try {
					pm.set(t, value);
				} catch (Exception e) {
					e.printStackTrace();
				}
             }
         }
	}
	/**/
	private void fillPkFieldAndAllColumn(String pk, Field[] fields) {
		//设定主键
	    try {
	    	if(!StringUtils.isEmpty(pk)){
		    	this.pkField = this.entityClass.getDeclaredField(pk);
		    	this.pkField.setAccessible(true);
	    	}
	    } catch (Exception e) {
	    		log.debug("没找到主键列,主键列名必须与属性名相同");
	    }
  		for (int i = 0 ; i < fields.length ;i ++) {
  			Field f = fields[i];
  			if(StringUtils.isEmpty(pk)){
  				Id id = f.getAnnotation(Id.class);
  				if(id != null){
  					this.pkField = f;
  					break;
  				}
  			}
  		}
	}
	  
	public T parse(ResultSet rs) {
		T t = null;
		if (null == rs) {
			return null;
		}
		Object value = null;
		try {
			t = (T) entityClass.newInstance();
			for (String columnName : mappings.keySet()) {
				try {
					value = rs.getObject(columnName);
				} catch (Exception e) {
					e.printStackTrace();
				}
				fillBeanFieldValue(t,columnName,value);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return t;
	}
	/**将 vo 对象解析为Map；key:字段名,value:字段值*/
	public Map<String, Object> parse(T t) {
		Map<String, Object> _map = new TreeMap<String, Object>();
		try {
			/*mappings key:字段名,value:PropertyMapping*/
			for (String columnName : this.mappings.keySet()) {
				/*getter：Method method，getter方法对象*/
				Object value = this.mappings.get(columnName).getter.invoke(t);
				if (value == null)
					continue;
				_map.put(columnName, value);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return _map;
	}

	public void println(T t) {
		try {
			for (String columnName : mappings.keySet()) {
				Object value = mappings.get(columnName).getter.invoke(t);
				if (value == null)
					continue;
				System.out.println(columnName + " = " + value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class PropertyMapping {

    final boolean insertable;
    final boolean updatable;
    final String columnName;
    final boolean id;
    final Method getter;
    final Method setter;
	final Class enumClass;
    final String fieldName;

    public PropertyMapping(Method getter, Method setter, Field field) {
        this.getter = getter;
        this.setter = setter;
        this.enumClass = getter.getReturnType().isEnum() ? getter.getReturnType() : null;
        Column column = field.getAnnotation(Column.class);
        this.insertable = column == null || column.insertable();
        this.updatable = column == null || column.updatable();
        this.columnName = column == null ? ClassMappings.getGetterName(getter) : ("".equals(column.name()) ? ClassMappings.getGetterName(getter) : column.name());
        this.id = field.isAnnotationPresent(Id.class);
        this.fieldName = field.getName();
    }

    @SuppressWarnings("unchecked")
    Object get(Object target) throws Exception {
        Object r = getter.invoke(target);
        return enumClass == null ? r : Enum.valueOf(enumClass, (String) r);
    }

    @SuppressWarnings("unchecked")
    void set(Object target, Object value) throws Exception {
        if (enumClass != null && value != null) {
            value = Enum.valueOf(enumClass, (String) value);
        }
        //BeanUtils.setProperty(target, fieldName, value);
        try {
        	 if(value != null){
             	 setter.invoke(target, setter.getParameterTypes()[0].cast(value));
             }
		} catch (Exception e) {
			e.printStackTrace();
			/**
			 * 出错原因如果是boolean字段 mysql字段类型 设置tinyint(1)
			 */
			System.err.println(fieldName + "--" + value);
		}
      
    }
}

