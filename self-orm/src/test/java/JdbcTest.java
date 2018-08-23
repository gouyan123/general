import com.gupaoedu.vip.orm.demo.model.Member;
import com.gupaoedu.vip.orm.demo.model.User;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class JdbcTest {
    public static void main(String[] args) {
        /**传一个User.class对象，根据反射自动取出注解中信息，拼接 sql语句，然后将查询出的内容封装到
         * User对象中*/
        /*将实体类作为条件*/
        Member condition = new Member();
        /**查询用户名为Tom的数据，然后封装到User对象中*/
        condition.setName("Tom");
        /*condition.setAge(33);*/
        List<Object> result = select(/*User.class*/condition);
        System.out.println(result.toString());
    }
    public static List<Object> select(/*Class<?> clazz*/ Object condition){
        /**获取对象的class*/
        Class<?> clazz = condition.getClass();
        /**rs数据封装到对象中，对象存到 results 列表中*/
        List<Object> results = new ArrayList<Object>();
        try {
            /**1 加载驱动类，将com.mysql.jdbc.Driver.class文件加载到方法区，得到运行时数据结构*/
            Class.forName("com.mysql.jdbc.Driver");
            /**2 获得连接对象，客户端通过连接对象操作服务端，Connection接口由java定，具体实现由各数据库公司实现*/
            /**客户端连接对象中封装 Socket 对象，将 sql 语句发送到数据库服务端 ，服务端ServerSocket
             * 接收到 sql 语句，进行相应解析和操作*/
            Connection connection = DriverManager.getConnection("jdbc:mysql://101.132.109.12:3306/gupaoedu_demo?characterEncoding=UTF-8&rewriteBatchedStatements=true","root","123456");
            /**3 创建语句集并开启事务*/
            /*由注解获取表名 tableName*/
            Table table = clazz.getDeclaredAnnotation(Table.class);/*获取@Table注解*/
            String tableName = table.name().trim();/*获取注解中name中的值*/
            /**获取查询语句中的字段名称，封装到sql语句中*/
            StringBuffer columns = new StringBuffer();
            StringBuffer where = new StringBuffer(" where 1=1 ");
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields){
                /**判断 field 是否使用 @Column注解*/
                if (field.isAnnotationPresent(Column.class)){
                    Column column = field.getDeclaredAnnotation(Column.class);
                    String columnName = column.name().trim();
                    columns.append(columnName).append(",");
                }
                field.setAccessible(true);
                Object value = field.get(condition);
                if (value != null){
                    Class<?> typeClass = field.getType();
                    if (typeClass == String.class){
                        /*如果该成员变量为String类型，则where中添加引号'' ，否则不添加引号*/
                        where.append(" and " + field.getName() + " = '" + value + "'");
                    }else {
                        where.append(" and " + field.getName() + " = " + value);
                    }
                }
            }
            int index = columns.length();
            /*删除最后一个 , */
            columns.deleteCharAt(index-1);
            /*动态拼接sql*/
            String sql = "SELECT id ," + columns.toString() + " FROM " + tableName + where.toString();
            PreparedStatement pstmt = connection.prepareStatement(sql);
            /**4 执行语句集，获得结果集，rs类似于excel分行分列*/
            ResultSet rs = pstmt.executeQuery();
            /**5 遍历结果集*/
            /*rs.getMetaData()获取元数据(描述数据的数据)；columnCount表示列数；*/
            int columnCount = rs.getMetaData().getColumnCount();

            while (rs.next()){/*循环每一行*/
                /*创建一个新的对象，用来封装rs中的数据*/
                Object obj = clazz.newInstance();
                /**利用反射，将rs 中某一行的每个字段的数据封装到一个 Member对象中的每个成员变量中*/
                for (int i=1;i<=columnCount;i++){
                    /*通过元数据获取字段名*/
                    String columnName = rs.getMetaData().getColumnName(i);
                    Field field = clazz.getDeclaredField(columnName);
                    field.setAccessible(true);
                    /**通过数据类型映射，从rs中取得相应的属性值*/
                    Object type = field.getType();
                    /*if (type == Long.class){
                        field.set(obj,rs.getLong(columnName));
                    }else if (type == String.class){
                        field.set(obj,rs.getString(columnName));
                    }else if (type == int.class){
                        field.set(obj,rs.getInt(columnName));
                    }else if (type == Integer.class){
                        field.set(obj,rs.getInt(columnName));
                    }*/
                    /**代替以上注释代码*/
                    field.set(obj,rs.getObject(columnName));
                }
                /**将封装好的对象存到 results 列表中*/
                results.add(obj);
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return results;
    }
}
