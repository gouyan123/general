package com.gupaoedu.chat.server;

import com.gupaoedu.chat.protocol.IMDecoder;
import com.gupaoedu.chat.protocol.IMEncoder;
import com.gupaoedu.chat.server.handler.SocketHandler;
import com.gupaoedu.chat.server.handler.WebSocketHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

import java.io.IOException;

import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.apache.log4j.Logger;
import com.gupaoedu.chat.server.handler.HttpHandler;

public class ChatServer{
	private static Logger LOG = Logger.getLogger(ChatServer.class);
	private int port = 80;
    public void start() {
        /*NioEventLoopGroup 是用来处理I/O操作的多线程事件循环器，Netty提供了许多不同的EventLoopGroup
        * 的实现用来处理不同传输协议；在这个例子中我们实现了一个服务端的应用，因此会有2个NioEventLoopGroup
        * 会被使用；第一个经常被叫做‘boss’，用来接收进来的连接；第二个经常被叫做‘worker’，用来处理已经
        * 被接收的连接， 一旦‘boss’接收到连接，就会把连接信息注册到‘worker’上；如何知道多少个线程已经
        * 被使用，如何映射到已经创建的Channels上都需要依赖于EventLoopGroup的实现，并且可以通过构造函
        * 数来配置他们的关系。
        * */
        /**boss线程，负责服务端接收客户端 连接对象(封装InputStream/OutputStream)
         * Socket socket=serverSocket.accept();然后将 客户端连接对象封装到SelectorKey中，注册
         * 到Selector中*/
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        /**worker线程，负责客户端 连接对象(封装InputStream/OutputStream) IO流的读写
         * 如果客户端连接对象的key为可读，将其输入流读入缓存 buffer，in.read(buffer);
         * 如果客户端连接对象的key为可写，将缓存 buffer写入其输出流，out.write(buffer) */
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            /*ServerBootstrap 辅助工具类，用于服务器通道ServerSocketChannel的一系列配置*/
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)/*绑定两个线程组*/
                    /*创建服务端通道 ServerSocketChannel */
                    .channel(NioServerSocketChannel.class)
                    /*服务端通道 ServerChannel 的初始化*/
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    /*所有自定义的业务从这里开始*/
                    /*设置childHandler，作为新建的NioSocketChannel的初始化Handler*/
                    /*当新建的与客户端通信的NioSocketChannel被注册到EventLoop成功时，该方法会被
                    * 调用，用于添加业务Handler*/
            .childHandler(new ChannelInitializer<SocketChannel>() {/*配置具体的数据处理方式*/
                @Override              /*每个客户端与服务端连接成功都将产生一个socketchannel*/
                public void initChannel(SocketChannel ch) throws Exception {
	                    /*这里的匿名类的实例将加到每个socketchannel的pipeline中。（每个客户端与服
	                    务端连接成功都将产生一个socketchannel）*/
                		ChannelPipeline pipeline = ch.pipeline();
	                	
	                	/** 解析websocket发送的(chat.util.js CHAT.socket.send(...))自定义协议 */
	                	pipeline.addLast(new IMDecoder());
	                	pipeline.addLast(new IMEncoder());
	                	pipeline.addLast(new SocketHandler());
	                
	                	/** 解析Http请求 */
	                	/*HttpServerCodec ：解码和编码HTTP请求的*/
	            		pipeline.addLast(new HttpServerCodec());
	            		//最大头信息；主要是将同一个http请求或响应的多个消息对象变成一个 fullHttpRequest完整的消息对象
	            		pipeline.addLast(new HttpObjectAggregator(64 * 1024));
	            		//主要用于处理大数据流,比如一个1G大小的文件如果你直接传输肯定会撑暴jvm内存的 ,加上这个handler我们就不用考虑这个问题了
	            		pipeline.addLast(new ChunkedWriteHandler());
                        /*绑定一个handler*/
	            		pipeline.addLast(new HttpHandler());
	            		
	            		/** 解析WebSocket请求 */
	            		/*只要以 ws://im开头的，都认为是 websocket协议*/
                        pipeline.addLast(new WebSocketServerProtocolHandler("/im"));
                        pipeline.addLast(new WebSocketHandler());
            		
                }
            });
            /*绑定端口并启动，并等待接收客户端发送过来的连接对象 SocketChannel*/
            /**调用sync()方法会等待前面bind()方法执行完毕，再往下执行*/
            ChannelFuture f = b.bind(this.port).sync();
            LOG.info("服务已启动,监听端口" + this.port);
            /*netty中所有的IO操作都是异步的，也就是说IO操作会立即返回，返回时不保证操作已经完成*/
            /*通过返回一个ChannelFuture来告诉你IO操作的结果和状态*/
            /*调用channelFuture的sync方法，为阻塞线程，等待结果的出现，该方法被打断时会抛出
            * InterruptedException */
            /**调用sync()方法会等待前面的closeFuture()方法执行完毕，再往下执行*/
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    /*服务端到启动过程*/
    /*程序首先实例化了一个ServerBootstrap，可以看出它的构造器并没有参数，因为参数太多，所以在后
     * 面调用了一串链式方法，给它的实例b设置了一系列参数，参数的意义在注释中已经说明。但目前为止并
     * 没有实质性的动作，当调用bind方法时，服务端就开始启动了。
     * 调用bind后，首先了调用AbstractBootstrap中的initAndRegister方法，通过反射机制实例化一个
     * NioServerSocketChannel（调用ServerBootstrap.channel方法传入了它的class），代码中变量
     * 名为channel，为了防止迷惑，称之为serverSocketChannel。然后初始化serverSocketChannel，
     * 设置它的参数（可设置的参数见ChannelOption类），获取它的ChannelPipeline，并添加了一个
     * ChannelHandler（实际上为ServerBootstrapAcceptor的实例，该实例包含了workerGroup，它的
     * handler，以及一些参数），最后向bossGroup注册。返回一个ChannelFuture。到这里启动就结束了*/

    /*客户端与服务端的连接过程*/
    /*当我们使用telnet localhost 8080连接服务端时，NioServerSocketChannel通过调用accept()
     * 方法完成连接，获得客户端通道 socketChannel。触发channelRead事件，通过回调从serverSocketChannel
     * 的ChannelPipeline中选择第一个ChannelHandler（也即head，默认情况下serverSocketChannel
     * 也只有这么一个handler，貌似也没有添加多个的必要，毕竟serverSocketChannel只是用来处理连接
     * 而已，上文说过，这个head也就是ServerBootstrapAcceptor的实例，这个类是ServerBootstrap的
     * 内部类）。然后调用head这个handler的channelRead(ChannelHandlerContext ctx, Object msg)
     * 方法。（事实上，这个msg对象就是这个socketChannel），然后获取socketChannel的ChannelPipeline，
     * 现在这个管道中是没有任何handler的，随后将我们启动服务端时，调用ServerBootstrap.childChannel()
     * 方法传入的handler加入到这个socketChannel的管道中。随后将这个socketChannel向workerGroup
     * 注册，到此为止，客户端和服务端连接成功。*/
    public static void main(String[] args) throws IOException{
        new ChatServer().start();
    }
}
