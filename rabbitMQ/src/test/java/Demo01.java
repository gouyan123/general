//import com.alibaba.fastjson.JSON;
//import com.config.*;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.connection.RabbitConnectionFactoryBean;
//import org.springframework.amqp.rabbit.core.RabbitAdmin;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//@RunWith(SpringJUnit4ClassRunner.class)
///*@ContextConfiguration(classes = {MyConfig.class, SpringConfig.class})*/
//@ContextConfiguration(locations = "classpath:application-common.xml")
//public class Demo01 {
//    @Autowired
//    private Student student;
//
//    @Autowired
//    public RabbitAdmin rabbitAdmin;
//    @Test
//    public void test01(){
//        System.out.println(JSON.toJSONString(student,true));
//    }
//
//    @Test
//    public void test03(){
//        System.out.println(JSON.toJSONString(rabbitAdmin,true));
//    }
//}
