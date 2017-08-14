<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017/8/14 0014
  Time: 11:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>头部</title>
</head>

<body>
<input type="hidden" class="wxmpdomain" value="${wxmpDomain}"/>
<input type="hidden" class="webUrl" value="${webUrl}" />


<script type="text/javascript">

    //别人推送给我的方法推送给我的方法
    window.addEventListener("message", function(event) {
        // 把父窗口发送过来的数据显示在子窗口中
        console.log("回调："+event.data);
        eval(event.data);
    }, false );

    //推送给我子跨域的方法
    function  messageSocket(method){
        //var url = $("#ifr").attr("src");
        var urls = $(".wxmpdomain").val();
        console.log(urls)
        parent.window.postMessage(method , urls);

    }

    /**
     * 弹出层提示
     * @param msg 提示内容
     */
    function parentAlertMsg(msg){
        messageSocket("alertMsg("+msg+")");
    }

    /**
     * 加载层  layer.load(1);
     */
    function parentLayerLoad(){
        messageSocket("loadFrom()");
    }

    /**
     * 打开弹出框
     * @param title 标题
     * @param width 宽度
     * @param height 高度
     * @param content 弹出框显示的类容
     */
    function parentOpenIframe(title,width,height,content){
        content = $(".webUrl").val()+content;
        messageSocket("openIframe(\""+title+"\",\""+width+"\",\""+height+"\",\""+content+"\")");
    }

    /**
     * 调用素材库的方法
     * @param type 1多选  0单选
     */
    function fhmater(type){
        messageSocket("fhmateriallayer("+type+")");
    }

    /**
     * 关闭所有弹出框  layer.closeAll();
     */
    function parentCloseAll(){
        messageSocket("go_back()");
    }

</script>

</body>
</html>
