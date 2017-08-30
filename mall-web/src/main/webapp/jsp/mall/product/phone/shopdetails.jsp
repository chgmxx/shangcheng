<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE>
<html>
<head>
<title>${article.title}</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta http-equiv="pragma" content="no-cache" /> 
<meta http-equiv="cache-control" content="no-cache" /> 
<meta http-equiv="expires" content="0" />  
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<base href="<%=basePath%>" />
<script src="/js/plugin/jquery-1.8.3.min.js"></script>
 <script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
        <style type="text/css">
        	*{font-family: "Microsoft YaHei", "黑体","宋体", Arial, Helvetica, sans-serif;}
        	a { color:#2888e2; text-decoration:none; }
            a:hover { text-decoration:none; }
            img { border:none; }
            .text{width:100%;overflow:hidden;}
            .text img{width:100%}
            p{table-layout:fixed; word-break: break-all; /* overflow:hidden; */}
            .text{line-height: 1.4;}
            .picture img{max-width: 100%;height: auto;text-align: center;display: block;margin: 0 auto;padding-bottom: 10px;}
            article{
				padding: 0.4rem;
				/* word-break: break-all;
		        white-space: pre-wrap; */
		        font-size : 12px;
		        overflow-x: auto; 
		        width: 95%!important
			}
			article div{
			  width: 100%!important;
			  margin: 0!important
			 /*  overflow: auto!important  */
			  
			}
        </style>
	</head>
	<body>
		<div class="content">
			
			<article>
				<div class="text" id="webContent"> ${obj.productDetail} </div>
			</article>
			<!-- <div class="picture">
				<img src="img/head.png" alt="img" />
				<img src="img/re-dan1.png" alt="img"/>
				<img src="img/1.jpg" alt="img" />
				
			</div> -->
		</div>
		
		<script type="text/javascript">
			 wx.config({
					debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
				    appId: "${record.get('appid')}", // 必填，公众号的唯一标识
				    timestamp: "${record.get('timestamp')}", // 必填，生成签名的时间戳
				    nonceStr: "${record.get('nonce_str')}", // 必填，生成签名的随机串
				    signature: "${record.get('signature')}",// 必填，签名，见附录1
				    jsApiList: ['previewImage'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
			});
			 $(function(){
				 $(".content img").click(function(){
					 var imgArray = [];
				        var curImageSrc = $(this).attr('src');
				        if (curImageSrc!=undefined) {
				            $('#webContent img').each(function(index, el) {
				                var itemSrc = $(this).attr('src');
				                imgArray.push(itemSrc);
				            });
				            wx.previewImage({
				                current: curImageSrc,
				                urls: imgArray
				            });
				        }
				 });
			 })
		 </script>
	</body>
</html>