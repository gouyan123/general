## 消息中间件 Rabbitmq
### 1、为什么要用消息中间件？
```text
解决分布式系统之间消息的传递；举例如下：
电商场景：用户下单减库存模块，调用物流系统发货模块，如果在一个JVM里面，直接调方法就可以，但是这些模块部署到不同主机，不在一个JVM里时，需要消息中间件在分布式系统中传递消息；系统扩充后要进行服务化和业务拆分，拆分
后要考虑 各子系统的交互问题；当接口较少时，可以使用RPC（远程协议调用），接口较多时，要使用消息中间件。
```

### 2、消息中间件和RPC有何区别？
```text
①弱依赖；例如，用户下单后，需要短信通知，但是短信通知并不是下单环节必须的，即没有短信通知也不应该影响用户下单，短信通知不是下单的强依赖，所以，下单系统不应该依赖短信服务，这种情况一般使用消息中间件；
②量（业务量，数据量，访问量）；例如，统计前一天销售总量，一般使用消息中间件；？？？
```

### 3、消息中间件有些什么使用场景？
```text
场景：用户注册后，将用户信息，通过邮件或者短信发送给用户；
异步处理
用户注册（50ms）→ 发送邮件（50ms）→ 短信（50ms）
串行：（150ms）用户注册 → 发送邮件 → 发送短信
并行：（100ms）：用户注册 → 发送邮件
                       | → 发送短信
消息中间件（56ms）：
用户注册（50ms）→（6ms）消息中间件 ← 发送邮件
                                 ← 发送短信
用户注册后，将注册内容发送给消息中间件，然后发送邮件和发送短信两个线程来消息中间件获取注册内容 然后发送邮件或者短信；

应用的解耦
订单系统 通知 库存系统 发货，订单系统直接调用库存系统属于 强耦合，库存是否发货不应该影响用户下单；
消息中间件：订单系统 →订单信息→ 消息中间件 ←订单信息← 库存系统（解耦）

流量的削峰填谷
背景：秒杀活动，流量很大；
用户请求 → 消息队列 ← 秒杀应用
用户将请求发送给消息队列，秒杀应用去消息队列中获取请求，否则直接调用的话，系统一下子处理太多东西，容易瘫痪，使用消息队列后，取来多少，处理多少，不容易瘫痪； 
日志处理
错误日志 → 消息队列 ← 日志处理：所有模块的 错误日志 发送到 消息队列，然后日志处理线程去 消息对列中获取；
用户行为日志 → 消息队列(kafka) ← 日志的存储或流式处理：所有模块的 用户行为日志 发送到 消息队列，然后日志处理线程去 消息对列中获取；

纯粹的消息通信
客户端A和客户端B使用同一个队列，就可以进行点对点的通讯；
```

### RabbtiMQ 组成
```text
RabbitMQ结构 = rabbitmq服务端 + rabbitmq客户端(生产者) + rabbitmq客户端(消费者)；
生产者 消费者 流程：
消费者声明交换空间，声明队列，将队列及其路由绑定到交换空间，消费队列并监听；
生产者声明交换空间，将消息及其路由发送到交换空间，交换空间将消息路由到相应队列；

AMQP：协议，支持不同语言和不同的产品；
生产者：创建消息，并发送到 rabbitmq服务端的交换空间；
消费者：订阅到某个队列上，并获取该队列消息。分为持续订阅（basicConsumer）和单条订阅(basicGet，不建议使用)；
消息：有效载荷 + 标签，有效载荷 = 要传输的数据，标签 = 有效载荷的属性，rabbitmq用标签来决定哪个消费者获得当前消息，消费者只能拿到有效载荷拿不到标签；
信道Channel：虚拟的连接，建立在真实的tcp/ip连接之上的，避免自己创建tcp连接浪费资源；信道的创建是没有限制的；

rabbit构成：交换器、队列、绑定、路由键(AMQP精华之一)
队列通过路由键routingkey(某种确定的规则)绑定到交换器，生产者把 消息和消息的路由键 发送到了交换器，交换器根据绑定的路由键将消息路由到特定的队列，订阅了队列的消费者进行订阅接收；
```

### RabbitMQ异常情况
```text
1、如果消息达到无人订阅的队列会怎么办？
    消息会一直在队列中等待，rabbitmq会默认队列是无限长度的；
2、多个消费者订阅到同一队列怎么办？
    消息会轮询的方式发送给消费者，每个消息只会发送给其中一个消费者；
3、消息路由到了不存在的队列怎么办？
    rabbitmq会当消息不存在，消息丢失了；
```
### RabbitMQ 消息确认机制
```text
目的：避免使用事务降低效率；
消费者 收到的每一条消息 都必须 跟rabbitmq服务端 进行确认：分为 系统自动确认； 消费者自行确认；
消费者 在声明队列时，通过autoAck参数，指定 消息确认模式，true表示系统自动确认；false表示rabbitmq服务端会等到消费者显示的 返回一个ack信号，rabbitmq服务端才会将队列中该消费者已取出消息删除；当autoAck=false，消费
者才有足够时间来处理消息，直到消费者显示调用basicAck(消费者给rabbitmq服务端显示的返回一个ack信号，服务端将该消费者取走的消息从队列中删除)为止；
Rabbitmq中消息分为了两部分：1、等待投递给消费者的消息；2、已经投递给消费者，但是还没有收到消费者ack信号的。如果消费者挂了，服务端会把消息重新入队列，投递给下一个消费者，未ack的消息是没有超时时间的。即只有当消费者
调用了 basicAck()方法，返回 ack信号时，rabbitmq服务端才会删除队列信息；
```

### 消费者如何明确拒绝消息？
```text
1、消费者断连；
2、消费者使用reject命令（requeue=true，重新分发消息，false移除消息）；
3、nack命令（批量的拒绝）；
```

### RabbitMQ 队列Queue 操作
```text
创建队列
生产/消费者都可以创建队列(declareQueue)，如果消费者订阅了队列，不能再声明队列了。
相关参数：exclusive 队列为应用程序私有；auto-delete 最后一个消费者取消订阅时，队列会自动删除，durable 队列持久化，消息持久化到磁盘中；

检测队列是否存在
Declare 时的passive参数，只是检查队列是否存在，而不是创建队列；
```

### RabbitMQ 四种交换器：direct，fanout，topic，headers
```text
direct：交换空间将消息发送到路由完全匹配的队列(绑定在该交换空间上的)上。Amqp实现都必须有一个direct交换器（默认交换器），名称为空白字符。队列不声明交换器，会自动绑定到默认交换器，队列的名称作为路由键。
Fanout：交换空间将消息发送到所有的队列(绑定在该交换空间上的)上；
Topic：交换空间将消息发送到路由正则匹配的队列(绑定在该交换空间上的)上；
Headers: 匹配消息头，其余与direct一样，实用性不大
```

```text
日志处理场景：
①topic交换器，名字log_exchange，日志级别有 error,info,warning，应用模块有 user,order,email，路
由键的规则是：日志级别 + ”.” + 应用模块名(例如info.user)
②发送邮件失败，报告一个email的error，basicPublic(message,’log-exchange’,’error.email’)
队列的绑定：queueBind(“email-error-queue”,’log-exchange’,’error.email’)
要监听email所有的日志怎么办？
queueBind(“email-log-queue”,’log-exchange’,’*.email’) 表示将该队列绑定到log-exchange交换空间，并接收 所有路由匹配*.email的信息；
监听所有模块所有级别日志？
queuebind(“all-log-queue”,’log-exchange’,’#’) 表示将该队列绑定到log-exchange交换空间，并接收 所有匹配 #的信息；
“.”会把路由键分为好几个标识符，“*”匹配一个标识符，“#”匹配一个或者多个（xxx.yyy.zzzz 可以： xxx.*. zzzz ， xxx.# ， #.zzzz）。
虚拟主机
Vhost，真实rabbitmq服务器上的mini型虚拟的mq服务器。有自己的权限机制。Vhost提供了一个逻辑上的分离，可以区分客户端，避免队列和交换器的名称冲突。RabbitMq包含了一个缺省的vhost :“/”，用户名guest，口令 guest（guest用户只能在本机访问）。
消息持久化
如果不持久化，rabbitmq服务端重启后，交换器，队列都会消失；开启持久化以后，会写入持久化日志，保存到磁盘上面；
1、	队列是必须持久化
2、	交换器也必须是持久化
3、	消息的投递模式必须（int型） 2
以上条件全部满足，消息才能持久化
问题：性能（下降10倍）
```

`事务：如果不开启事务，各命令分别执行；开启事务后，事务中的命令暂不执行，等到提交事务的时候，各命令作为一个整体一起执行，不存在一部分执行，一部分不执行；`

```text
发送方确认：
异步：发送方发送消息后，发送方应用程序 在等待信道返回确认后，才可以继续发送下一条消息；
rabbitmq服务端会调用消费者监听方法，通知生产者，消息已成功发送到服务器上；如果服务端由于自己原因，没有接收到数据，就会给生产者发送一个 ack信息，生产者回调程序可以处理这个 ack信息；
原理：发送方要想进行发送确认，信道必须设置成 confirm，信道发送的每个消息都会分配一个id，一旦消息被发送到匹配队列后，服务端就会发送一个确认给生产者，里面包含消息id，让生产者知道消息已正确发送到目的地了；
注意：发送方确认模式 和 消费者确认模式 不同；
发送方确认模式：生产者给rabbitmq服务端发送消息，服务端接收到消息后，给生产者发送消息，告诉生产者消息已收到，可以发送下一条消息了；
消费者确认模式：消费者从rabbitmq服务端获取消息，消费者获取到消息后，给服务端发送消息，告诉服务端消息已收到，可以发送下一条消息了；
```

### RabbitMQ原生操作
```java
/**创建 DirectProducer类，向交换空间发送消息(日志类型即路由 + 日志内容即消息内容)*/
public class DirectProducer {
    private final static String EXCHANGE_NAME = "direct_logs";
    public static void main(String[] args) throws IOException, TimeoutException { 
        ConnectionFactory factory = new ConnectionFactory();        /**创建工厂*/
        factory.setHost("47.100.49.95");                            /**配置工厂*/
        factory.setPort(5672);
        factory.setUsername("gouyan");
        factory.setPassword("123456");
        factory.setVirtualHost("/");
        Connection connection = factory.newConnection();            /**工厂创建 tcp/ip 长连接，避免反复重复创建连接*/
        Channel channel = connection.createChannel();               /**在 长连接connection上面创建 虚拟信道 channel，节约资源；虚拟信道可以无限创建；*/
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);      /**生产者 声明 交换空间(name + type)*/
        String[]serverities = {"error","info","warning"};
        for(int i=0;i<3;i++){
            String server = serverities[i];                         /**路由key*/
            String message = "Hello world_"+(i+1);                  /**消息*/
            channel.basicPublish(EXCHANGE_NAME,server,null,message.getBytes()); /**生产者将 消息及路由 通过信道 发布到 交换空间*/
            System.out.println("Sent "+server+":"+message);
        }
        channel.close();
        connection.close();
    }
}
/**创建 DirectConsumer类，声明交换空间，队列，并将队列及其路由绑定到交换空间上，从队列获取消息，并监听*/
public class DirectConsumer {
    private static final String EXCHANGE_NAME = "direct_logs";
    public static void main(String[] argv) throws IOException, InterruptedException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();                             /**创建工厂*/
        factory.setHost("47.100.49.95");                                                 /**配置工厂*/
        factory.setPort(5672);
        factory.setUsername("gouyan");
        factory.setPassword("123456");
        factory.setVirtualHost("/");
        Connection connection = factory.newConnection();                                 /**工厂创建 tcp/ip 连接*/
        Channel channel = connection.createChannel();                                    /**在 长连接connection上面创建 虚拟信道 channel，节约资源；虚拟信道可以无限创建；*/
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);              /**消费者 声明 交换空间(name + type)*/
        String queueName = channel.queueDeclare().getQueue();                            /**消费者 声明随机队列，声明队列后无法订阅队列*/
        String[]serverities = {"error","info","warning"};
        for(String server:serverities){ 
            channel.queueBind(queueName,EXCHANGE_NAME,server);                           /**消费者将 队列及其路由 绑定到交换空间*/
        }
        System.out.println("Waiting message.......");
        Consumer consumerA = new DefaultConsumer(channel){                               /**要监听，需要创建 consumer接口对象*/
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body,"UTF-8");
                System.out.println("Accept:"+envelope.getRoutingKey()+":"+message);
            }
        };
        channel.basicConsume(queueName,true,consumerA);                                  /**消费者 消费队列 并监听*/
    }
}
```

### RabbitMQ原生操作 + 发送方确认模式：
```java
/**创建 ProducerConfirm类，将信道设置为 发送方确认模式，并回调监听器；*/
public class ProducerConfirm {
    private final static String EXCHANGE_NAME = "producer_confirm";
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();                        /**创建工厂*/
        factory.setHost("47.100.49.95");                                            /**配置工厂*/
        factory.setPort(5672);
        factory.setUsername("gouyan");
        factory.setPassword("123456");
        factory.setVirtualHost("/");
        Connection connection = factory.newConnection();                            /**工厂创建 tcp/ip 长连接，避免反复重复创建连接*/
        Channel channel = connection.createChannel();                               /**在 长连接connection上面创建 虚拟信道 channel，节约资源；虚拟信道可以无限创建；*/
        channel.confirmSelect();                                                    /**将信道设置为 发送方确认模式*/
        channel.addConfirmListener(new ConfirmListener() {                          /**信道中增加监听器，当服务端接收到数据后，远程调用监听方法*/
            @Override
            public void handleAck(long l, boolean b) throws IOException {
                System.out.println("发送端确认 服务端 已接收到消息");
            }
            @Override
            public void handleNack(long l, boolean b) throws IOException {
                System.out.println("发送端确认 服务端 未接收到消息");
            }
        });
        
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);         /**生产者 声明 交换空间(name + type)*/
        for(int i=0;i<10;i++){
            String message = "Hello world_"+(i+1);
            channel.basicPublish(EXCHANGE_NAME,"pc",null,message.getBytes());       /**pc为 路由key，message为 消息*/
            if (channel.waitForConfirms()){                                         /**信道等待rabbitmq服务端发送接收确认，接收到后服务端返回true*/
                System.out.println("Sent " + ":"+message);  
            }
        }
        channel.close();
        connection.close();
    }
}
```