package com.gupaoedu.chat.server.handler;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URL;

import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import org.apache.log4j.Logger;

public class HttpHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
	private static Logger LOG = Logger.getLogger(HttpHandler.class);
	
	//获取class路径
    private URL baseURL = HttpHandler.class.getProtectionDomain().getCodeSource().getLocation();
    private final String webroot = "webroot";
    /*从跟路径 resources/webroot 下去找文件*/
    private File getResource(String fileName) throws Exception{
    		String path = baseURL.toURI() + webroot + "/" + fileName;
        path = !path.contains("file:") ? path : path.substring(5);
        path = path.replaceAll("//", "/");
        return new File(path);
    }
    /*在Netty中 只要后面加 0 ，都是实现类方法，不是接口方法*/
    /*用于输出 resources下 webroot中的文件内容*/

    @Override                                           /*请发送来的内容解析为 FullHttpRequest*/
    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        /*获取请求中的 uri*/
        String uri = request.getUri();
        RandomAccessFile file = null;
        try{
            /*静态资源路径*/
            String page = uri.equals("/") ? "chat.html" : uri;
            file =	new RandomAccessFile(getResource(page), "r");
        }catch(Exception e){
            ctx.fireChannelRead(request.retain());
            return;
        }
        String contextType = "text/html;";
        /*文件类型解释*/
        if(uri.endsWith(".css")){
            contextType = "text/css;";
        }else if(uri.endsWith(".js")){
            contextType = "text/javascript;";
        }else if(uri.toLowerCase().matches("(jpg|png|gif)$")){
            String ext = uri.substring(uri.lastIndexOf("."));
            contextType = "image/" + ext;
        }
        HttpResponse response = new DefaultHttpResponse(request.getProtocolVersion(), HttpResponseStatus.OK);
        /*判断是否为长链接*/
        boolean keepAlive = HttpHeaders.isKeepAlive(request);
        /*如果是长连接，response的headers中需要增加内容*/
        if (keepAlive) {
            response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, file.length());
            response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        }
        /*先将协议内容写出去*/
        ctx.write(response);
        ctx.write(new DefaultFileRegion(file.getChannel(), 0, file.length()));
        /*清空*/
        ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        if (!keepAlive) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
        file.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        Channel client = ctx.channel();
        LOG.info("Client:"+client.remoteAddress()+"异常");
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }
}

