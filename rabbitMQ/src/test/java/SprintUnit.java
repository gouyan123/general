import com.rabbitmq.spring.produce.AmqpProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
/*@ContextConfiguration(classes = SpringConfig.class)*/
@ContextConfiguration("applicationContext.xml")
public class SprintUnit {
    @Autowired
    private AmqpProducer amqpProducer;

    @Test
    public void testDirectConsumer() throws InterruptedException {
        String[] routingKey = new String[]{"hello.world", "world", "test1"};
        for (int i = 0; i < 10; i++) {
            amqpProducer
                    .publishMsg("direct.exchange", routingKey[i % 3], ">>> hello " + routingKey[i % 3] + ">>> " + i);
        }
        System.out.println("-------over---------");

        Thread.sleep(1000 * 60 * 10);
    }
}