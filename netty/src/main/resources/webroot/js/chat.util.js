/*重写原型方法format，用来处理时间戳*/
/**prototype 属性可以 向对象 添加属性和方法。*/
Date.prototype.format = function(format){
    var o = {
        "M+" : this.getMonth()+1, //月
        "d+" : this.getDate(), //日
        "h+" : this.getHours(), //时
        "m+" : this.getMinutes(), //分
        "s+" : this.getSeconds(), //秒
        "q+" : Math.floor((this.getMonth()+3)/3), //刻
        "S" : this.getMilliseconds() //毫秒
    }

    if(/(y+)/.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
    }

    for(var k in o) {
        if(new RegExp("("+ k +")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length));
        }
    }
    return format;
};
$(document).ready(function () {
    /*获取请求中的 host，此处 host = localhost/chat.html，即去掉请求中的http://协议*/
    var host = location.href.replace(/http:\/\//i,"");
    /*定义 CHAT 对象*/
    window.CHAT = {
        /*定义全局变量*/
        /*保存服务器端WebSocket的请求地址*/
        serverAddr:"ws://" + host.replace("chat.html","") + "im",
        /*保存浏览器socket对象*/
        socket:null,
		/*用户昵称*/
		nickname:"匿名",
        //将滚动条设置到最顶部，以便能看到最新的消息
        scrollToBottom:function(){
            window.scrollTo(0, $("#onlinemsg")[0].scrollHeight);
        },
        /*init()方法在login()方法中被调用，此处开启 websocket 协议*/
        init:function (nickname) {
            if (!window.WebSocket) {
                window.WebSocket = window.MozWebSocket;
            }
            /*如果浏览器支持 websocket*/
            if (window.WebSocket) {
                CHAT.socket = new WebSocket(CHAT.serverAddr);
                console.log("CHAT.serverAddr = " + CHAT.serverAddr)
                /*当接收到消息时，调用这个onmessage()方法*/
                CHAT.socket.onmessage = function(e) {
                    /*console.log(e.data);*/
                    /*将消息添加到聊天面板*/
                    appendToPanel(e.data);
                };
                /*当创建 websocket连接时，调用这个onopen方法*/
                CHAT.socket.onopen = function(e) {
                    /*window.alert("websocket已开启");*/
                    /*客户端websocket向服务端websocket发送内容，服务端WebSocketHandler的channelRead0()方法接收到该内容*/
                    /*按自定义协议规范内容发送字符串内容*/
                    CHAT.socket.send("[LOGIN][" + new Date().getTime() +"][" + nickname + "]");
                };
                CHAT.socket.onclose = function(e) {
                    window.alert("websocket已关闭");
                };
            } else {
                alert("你的浏览器不支持 WebSocket！");
            };
            //定义 addSystemTip()方法，添加系统提示
            var addSystemTip = function(c){
                var html = "";
                html += '<div class="msg-system">';
                html += c;
                html += '</div>';
                var section = document.createElement('section');
                section.className = 'system J-mjrlinkWrap J-cutMsg';
                section.innerHTML = html;

                $("#onlinemsg").append(section);
            };
            //定义 appendToPanel()方法，将消息添加到聊天面板
            var appendToPanel = function(message){
                var regx = /^\[(.*)\](\s\-\s(.*))?/g;
                var group = '',label = "",content = "",cmd="",time=0,name="";
                while(group = regx.exec(message)){
                    label = group[1];
                    content = group[3];
                }
                var labelArr = label.split("][");
                cmd = labelArr[0];
                time = labelArr[1];
                name = labelArr[2];

                if(cmd == "SYSTEM"){
                    var total = labelArr[2];
                    $("#onlinecount").html("" + total);
                    addSystemTip(content);
                }else if(cmd == "CHAT"){
                    var date = new Date(parseInt(time));
                    addSystemTip('<span class="time-label">' + date.format("hh:mm:ss") + '</span>');
                    var isme = (name == "you") ? true : false;
                    var contentDiv = '<div>' + content + '</div>';
                    var usernameDiv = '<span>' + name + '</span>';

                    var section = document.createElement('section');
                    if (isme) {
                        section.className = 'user';
                        section.innerHTML = contentDiv + usernameDiv;
                    } else {
                        section.className = 'service';
                        section.innerHTML = usernameDiv + contentDiv;
                    }
                    $("#onlinemsg").append(section);
                }else if(cmd == "FLOWER"){
                    addSystemTip(content);
                    //鲜花特效
                    $(document).snowfall('clear');
                    $(document).snowfall({
                        image:"/images/face/50.gif",
                        flakeCount:60,
                        minSize:20,
                        maxSize:40
                    });
                    window.flowerTimer = window.setTimeout(function(){
                        $(document).snowfall('clear');
                        window.clearTimeout(flowerTimer);
                    },5000);

                }
                //有新的消息过来以后，自动切到最底部
                CHAT.scrollToBottom();
            };
        },
        /*定义 CHAT.login() 方法，绑定 onclick="CHAT.login();" 事件*/
        login:function () {
            $("#error-msg").empty();
        	/*获取用户昵称*/
        	var nickname = $("#nickname").val();
        	if (nickname != null){
                /*隐藏当前 div，打开下面的 div*/
                $("#loginbox").hide();
                $("#chatbox").show();
                $("#shownikcname").html(nickname);
                /*登录后创建 websocket 连接*/
                CHAT.init(nickname);
			}else {
        		$("#error-msg").html("输入昵称才能进入聊天室");
        		return false;
			}
			return false;
        },
        /*定义 CHAT.logout() 方法，绑定 onclick="CHAT.logout();" 事件*/
        logout:function () {
            /*刷新*/
            window.location.reload();
        },
        //清空聊天记录
        clear:function(){
            CHAT.box.innerHTML = "";
        },
        //发送聊天消息
        sendText:function() {
            var message = $("#send-message");
            //去掉空格
            if(message.html().replace(/\s/ig,"") == ""){ return; }
            if (!window.WebSocket) { return; }
            if (CHAT.socket.readyState == WebSocket.OPEN) {
                var msg = ("[CHAT][" + new Date().getTime() + "]" + "[" + CHAT.nickname + "] - " + message.html().replace(/\n/ig,"<br/>"));
                CHAT.socket.send(msg);
                message.empty();
                message.focus();
            } else {
                alert("与服务器连接失败.");
            }
        },
        //发送鲜花
        sendFlower:function(){
            if (!window.WebSocket) { return; }
            if (CHAT.socket.readyState == WebSocket.OPEN) {
                var message = ("[FLOWER][" + new Date().getTime() + "]" + "[" + CHAT.nickname + "]");
                CHAT.socket.send(message);
                $("#send-message").focus();
            } else {
                alert("与服务器连接失败.");
            }
        },
        //选择表情
        selectFace:function(img){
            var faceBox = $("#face-box");
            faceBox.hide();
            faceBox.removeClass("open");
            var i = '<img src="' + img + '" />';
            $("#send-message").html($("#send-message").html() + i);
            $("#send-message").focus();
        },
        //打开表情弹窗
        openFace:function(e){
            var faceBox = $("#face-box");
            if(faceBox.hasClass("open")){
                faceBox.hide();
                faceBox.removeClass("open");
                return;
            }
            faceBox.addClass("open");
            faceBox.show();
            var box = '';
            for(var i = 1;i <= 130; i ++){
                var img = '/images/face/' + i + '.gif';
                box += '<span class="face-item" onclick="CHAT.selectFace(\'' + img + '\');">';
                box += '<img src="' + img + '"/>';
                box += '</span>';
            }
            faceBox.html(box);
        }
    }
})