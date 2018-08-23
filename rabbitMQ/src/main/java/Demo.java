import com.alibaba.fastjson.JSON;
import com.rabbitmq.spring.config.SpringConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Demo {
    /*public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        System.out.println("------" + JSON.toJSONString(context,true));
        System.out.println("------" + JSON.toJSONString(context.getBean("rabbitConnectionFactory")));
    }*/
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
//        System.out.println(JSON.toJSONString(context.getBean("connectionFactory"),true));
//        System.out.println(JSON.toJSONString(context.getBean("rabbitAdmin"),true));
        System.out.println(JSON.toJSONString(context.getBean("amqpProducer"),true));
        System.out.println(context.getBean("amqpProducer"));
    }
}
