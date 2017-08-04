<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<base href="<%=basePath%>" /> 
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <link rel="stylesheet" type="text/css" href="/css/common/init.css?<%=System.currentTimeMillis()%>" />
    <link rel="stylesheet" href="/css/mall/my/public.css?<%=System.currentTimeMillis()%>">
    <link rel="stylesheet" href="/css/mall/my/comment.css?<%=System.currentTimeMillis()%>">
    <title>我的评论</title>
</head>
<body>
	<section class="loading">
	    <div class="load3">
	        <div class="double-bounce1"></div>
	        <div class="double-bounce2"></div>
	    </div>
	</section>
    <div class="cWrapper">
        <nav>
            <ul class="flex-row-c navUl"><!-- onclick="switchOn(this)" -->
                <li class="navLi">
                	<a href="/mMember/79B4DE7C/myAppraise.do?type=1&uId=${userid }" class="<c:if test='${type==1}'>nav-active</c:if>" 
                		item="sec-1">我的评论</a>
                </li>
                <li class="navLi">
                	<a href="/mMember/79B4DE7C/myAppraise.do?type=2&uId=${userid }"  class="<c:if test='${type==2}'>nav-active</c:if>" 
               			item="sec-2">已删除</a>
                </li>
            </ul>
        </nav>
        <section id="sec-1">
            <ul>
            	<c:forEach var="appraise" items="${page.subList }">
	                <li class="comLi">
	                    <div class="flex-row-sc ai">
	                    	<a href="/mallPage/${appraise.product_id}/${appraise.shop_id}/79B4DE7C/phoneProduct.do">
	                        <img class="c-img" src="${http}${appraise.product_image_url }" width="52px" height="52px" />
	                        </a>
	                        <span class="c-level">
	                        	<c:if test="${type == 1 }">
		                        	<c:if test="${appraise.feel == -1}">差评</c:if>
		                        	<c:if test="${appraise.feel == 0}">中评</c:if>
		                        	<c:if test="${appraise.feel == 1}">好评</c:if>
	                        	</c:if>
	                        	<c:if test="${type == 2 }">已删除</c:if>
	                        </span>
	                        <span class="txt-overflow c-item">
	                           <a href="/mallPage/${appraise.product_id}/${appraise.shop_id}/79B4DE7C/phoneProduct.do" class="txt-overflow">
	                           ${appraise.det_pro_name }
	                           </a>
	                        </span>
	                    </div>
	                    <div>
	                        <p class="c-comm"> ${appraise.content }</p>
	                        <div>
	                            <span class="mr">
	                            	<fmt:formatDate value="${appraise.createTime }" type="both"></fmt:formatDate>
	                            </span>
	                            <span class="mr"> ${appraise.product_speciname }</span>
	                            <!-- <span class="mr">42码</span> -->
	                        </div>
	                        <!--图片战士-->
	                        <c:if test="${!empty appraise.imageList }">
	                        <div class="c-comm-img flex-row-sc">
	                        	<c:forEach var="image" items="${appraise.imageList }">
	                            <i class="comm-img" style="background-image: url('${http}${image.imageUrl}');"></i>
	                            </c:forEach>
	                        </div>
	                        </c:if>
	                    </div>
	                    <c:if test="${appraise.resContent != null && appraise.resContent != ''&& !empty appraise.resContent}">
		                    <div class="s-replay">
		                        <i class="up-tri-g"></i>
		                        <p class="s-comm">店家回复：${appraise.resContent.content }</p>
		                    </div>
	                    </c:if>
	                </li>
                </c:forEach>
                <!-- <li class="comLi">
                    <div class="flex-row-bt ai">
                        <i class="c-img"></i>
                        <span class="c-level">好评</span>
                   <span class="txt-overflow c-item">
                       adidas阿迪达斯 跑鞋 女子跑步鞋 爆炸粉啦啦啦啦啦啦啦啦
                   </span>
                    </div>
                    <div>
                        <p class="c-comm">不错，鞋子很舒服</p>
                        <div>
                            <span class="mr">2016-09-08 16:27:22</span>
                            <span class="mr">红色</span>
                            <span class="mr">42码</span>
                        </div>
                        图片战士
                        <div class="c-comm-img flex-row-sc">
                            <i class="comm-img"></i>
                            <i class="comm-img"></i>
                        </div>
                    </div>
                </li>
                <li class="comLi">
                    <div class="flex-row-bt ai">
                        <i class="c-img"></i>
                        <span class="c-level">好评</span>
                       <span class="txt-overflow c-item">
                           adidas阿迪达斯 跑鞋 女子跑步鞋 爆炸粉啦啦啦啦啦啦啦啦
                       </span>
                    </div>
                    <div>
                        <p class="c-comm">不错，鞋子很舒服</p>
                        <div>
                            <span class="mr">2016-09-08 16:27:22</span>
                            <span class="mr">红色</span>
                            <span class="mr">42码</span>
                        </div>
                    </div>
                </li>
                <li class="comLi">
                    <div class="flex-row-bt ai">
                        <i class="c-img"></i>
                        <span class="c-level">好评</span>
                   <span class="txt-overflow c-item">
                       adidas阿迪达斯 跑鞋 女子跑步鞋 爆炸粉啦啦啦啦啦啦啦啦
                   </span>
                    </div>
                    <div>
                        <p class="c-comm">不错，鞋子很舒服</p>
                        <div>
                            <span class="mr">2016-09-08 16:27:22</span>
                            <span class="mr">红色</span>
                            <span class="mr">42码</span>
                        </div>
                    </div>
                </li> -->
            </ul>
        </section>
        <!-- <section id="sec-2" style="display: none">
            <ul>
                <li class="comLi">
                    <div class="flex-row-bt ai">
                        <i class="c-img"></i>
                        <span class="c-level">已删除</span>
                   <span class="txt-overflow c-item">
                       adidas阿迪达斯 跑鞋 女子跑步鞋 爆炸粉啦啦啦啦啦啦啦啦
                   </span>
                    </div>
                    <div>
                        <p class="c-comm">不错，鞋子很舒服</p>
                        <div>
                            <span class="mr">2016-09-08 16:27:22</span>
                            <span class="mr">红色</span>
                            <span class="mr">42码</span>
                        </div>
                        图片战士
                        <div class="c-comm-img flex-row-sc">
                            <i class="comm-img"></i>
                            <i class="comm-img"></i>
                        </div>
                    </div>
                </li>
            </ul>
        </section> -->
        <div class="noMore">没有更多了</div>
    </div>
    <script src="/js/plugin/jquery-1.8.3.min.js"></script>
    <script>
        function switchOn(obj) {
            if(!$(obj).is(".nav-active")){
                var item = $(obj).attr("item");
                $(obj).addClass("nav-active").parent().siblings().find("a").removeClass();
                $("section#"+item).show().siblings("section").hide();
            }
        }

        $(window).load(function() {
        	setTimeout(function() {
        		$(".loading").hide();
        	}, 1000);
        });
    </script>
</body>
</html>