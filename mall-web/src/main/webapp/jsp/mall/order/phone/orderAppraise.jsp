<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>	
<!DOCTYPE html>
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
    <link rel="stylesheet" href="/css/mall/my/eval.css?<%=System.currentTimeMillis()%>">
    <title>评价</title>
</head>
<body>
<section class="loading">
    <div class="load3">
        <div class="double-bounce1"></div>
        <div class="double-bounce2"></div>
    </div>
</section>
<script src="/js/plugin/jquery-1.8.3.min.js"></script>
	
    <div class="mWrapper">
        <!-- <form action="mMember/79B4DE7C/addAppraise.do" method="post" id="aForm"> -->
        	<input type="hidden" name="orderDetailId" value="${orderDetail.id }" />
        	<input type="hidden" name="orderId" value="${orderDetail.orderId }" />
        	<input type="hidden" name="productId" value="${orderDetail.productId }" />
        	<input type="hidden" name="shopId" value="${orderDetail.shopId }" />
        	<input type="hidden" name="descriptStart" id="descriptStart" />
        	<input type="hidden" name="serviceStart" id="serviceStart" />
        	<input type="hidden" name="speeedStart" id="speeedStart" />
        	<input type="hidden" name="feel" id="feel" />
        	<input type="hidden" name="isUploadImage" id="isUploadImage" value="0" />
            <div class="e-item flex-row-b bcf">
                <img src="${imgHttp }${orderDetail.productImageUrl }" alt="" class="e-img">
                <p class="txt-overflow pdlr2">${orderDetail.detProName }</p>
                <div>
                    <p class="c-r2"><span class="yen">&yen;</span><span>${orderDetail.detProPrice }</span></p>
                    <p>x<span>${orderDetail.detProNum }</span></p>
                </div>
            </div>
            <div class="e-say bcf">
                <textarea name="content" placeholder="说点什么吧，你的感受对其他朋友很重要哦" class="e-txt fw" id="content"></textarea>
                <div class="ph-area">
               			<!-- <i class="ph-real"></i> -->
               		<span class="ph_span">
               			<i class="ph-icon"></i>
	                    <form action="/mMember/79B4DE7C/updateImage.do" id="uploadForm" enctype="multipart/form-data" method="post">
	                    <input type="file" multiple="true"  name="file" id="file2" value="+" accept="image/*" class="uploadImage" onchange="uploadImages(this);">
	                    </form>
               		</span>
                    <input type="hidden" class="imageUrls" value="" id="imageUrls"/>
                </div>
            </div>
            <div class="e-feel bcf">
                <div class="feel-wrap">
                    <p>总体感受</p>
                    <ul class="feelUl flex-row-bt">
                        <li onclick="isWell(this,1)">好评</li>
                        <li onclick="isWell(this,0)">中评</li>
                        <li onclick="isWell(this,-1)">差评</li>
                    </ul>
                </div>
            </div>
            <div class="e-score bcf">
                <p class="bline">服务打分</p>
                <div class="sw-line flex-row-sc">
                    <p>商品描述</p>
                    <ul class="scoreUl">
                        <li class="score-icon" onclick="isScore(this,1,1)"></li>
                        <li class="score-icon" onclick="isScore(this,2,1)"></li>
                        <li class="score-icon" onclick="isScore(this,3,1)"></li>
                        <li class="score-icon" onclick="isScore(this,4,1)"></li>
                        <li class="score-icon" onclick="isScore(this,5,1)"></li>
                    </ul>
                </div>
                <div class="sw-line flex-row-sc">
                    <p>卖家服务</p>
                    <ul class="scoreUl">
                        <li class="score-icon" onclick="isScore(this,1,2)"></li>
                        <li class="score-icon" onclick="isScore(this,2,2)"></li>
                        <li class="score-icon" onclick="isScore(this,3,2)"></li>
                        <li class="score-icon" onclick="isScore(this,4,2)"></li>
                        <li class="score-icon" onclick="isScore(this,5,2)"></li>
                    </ul>
                </div>
                <div class="sw-line flex-row-sc">
                    <p>发货速度</p>
                    <ul class="scoreUl">
                        <li class="score-icon" onclick="isScore(this,1,3)"></li>
                        <li class="score-icon" onclick="isScore(this,2,3)"></li>
                        <li class="score-icon" onclick="isScore(this,3,3)"></li>
                        <li class="score-icon" onclick="isScore(this,4,3)"></li>
                        <li class="score-icon" onclick="isScore(this,5,3)"></li>
                    </ul>
                </div>
            </div>
	        <c:if test="${!empty isAdvert }">
		    	<div class="isAdvert" style="">
		    		<jsp:include page="/jsp/common/technicalSupport.jsp"></jsp:include>
		    	</div>
		    </c:if>
            <div class="e-btn">
                <input type="button" value="提交" class="btn-sub fw" id="subBtn">
            </div>
        <!-- </form> -->
    </div>
    <script src="/js/plugin/layer-mobile/layer/layer.js"></script>
	<script type="text/javascript" src="/js/plugin/jquery-form.js"></script>
	<script type="text/javascript" src="/js/mall/phone/phone_public.js"></script>
    <script>
    $(window).load(function() {
    	setTimeout(function() {
    		$(".loading").hide();
    	}, 1000);
    });
    
    $(function(){
    	$.fn.serializeObject = function()    
    	{    
    	   var o = {};    
    	   $(this).find("input").each(function(index){
    		   if($(this).attr("name") != undefined){
    			   if($(this).attr("type")=="text" || $(this).attr("type")=="password" || $(this).attr("type")=="hidden" ||
    				$(this).attr("type")=="tel"){
    				   o[$(this).attr("name")] = $(this).val();
    			   }
    			   
    			   if($(this).attr("type")=="checkbox" || $(this).attr("type")=="radio"){
    				  if($(this).is(":checked")){
    					  o[$(this).attr("name")] = 1;
    				  }else{
    					  o[$(this).attr("name")] = 0;
    				  }
    			   }
    		   }
    	   });
    	   
    	   $(this).find("select").each(function(index){
    		   if($(this).attr("name") != undefined){
    			   o[$(this).attr("name")] = $(this).val(); 
    		   }
    	   });
    	   
    	   $(this).find("textarea").each(function(index){
    		   if($(this).attr("name") != undefined){
    			   o[$(this).attr("name")] = $(this).val(); 
    		   }
    	   });
    	   return o;    
    	};
    });
    	
    	$("#subBtn").click(function(){
    		var imageUrls = $("#imageUrls").val();
    		var content = $("#content").val();
    		var feel = $("#feel").val();
    		if(content == ""){
    			alert("请填写评论内容");
    			return false;
    		}else if(content.lenght > 50){
    			alert("评论内容不能超过50个字");
    			return false;
    		}else if(feel == ""){
    			alert("请选择总体感受");
    			return false;
    		}else{
    			if(imageUrls != ""){
    				$("#isUploadImage").val(1);    			
        		}
    			var mallComment = $(".mWrapper").serializeObject();
    			var obj = JSON.stringify(mallComment);
    			var index = layer.open({
    			    title: "",
    			    content: "",
    			    type:2,
    			    shadeClose:false
    			});
    			$.ajax({
    	   			type : "post",
    	   			url : "/mMember/79B4DE7C/addAppraise.do",
    	   			data : {obj :obj,imageUrls:imageUrls},
    	   			dataType : "json",
    	   			success : function(data) {
    	   				layer.closeAll();
    	   				if (data.count > 0 || data.count == -1) {// 重新登录
    	   					var content = '添加评论成功';
    	   					if(data.count == -1){
    	   						content = '您已经评论过了，不能再次评论';
    	   					}
    	   					layer.open({
    	   					  title:"",
    	   					  content: content,
   	    	   				  btn: ['确认'],
   	    	   				  shadeClose: true,
   	    	   				  yes: function(){
   	    	   					layer.closeAll();
   	    	   					location.href = "/mMember/79B4DE7C/myAppraise.do?type=1&uId=${userid }";
   	    	   				  }
   	    	   				});
    	   				} else {// 编辑失败
    	   					layer.open({
    	   					  title:"",
    	   					  content: "添加评论失败，请稍后重试",
   	    	   				  btn: ['确认'],
   	    	   				  shadeClose: true,
   	    	   				  yes: function(){
   	    	   					layer.closeAll();
   	    	   				  }
   	    	   				});
    	   				}

    	   			},
    	   			error : function(XMLHttpRequest, textStatus, errorThrown) {
    	   				layer.closeAll();
    	   				layer.open({
    	   				  title:"",
    	   				  content: '添加评论失败',
    	   				  btn: ['确认'],
    	   				  shadeClose: true,
    	   				  yes: function(){
    	   					layer.closeAll();
    	   				  }
    	   				});
    	   				return;
    	   			}
    	   		});
    		}
    	});
    
        function isScore(obj,num,type) {
        	if(type == 1){
        		$("#descriptStart").val(num);
        	}else if(type == 2){
        		$("#serviceStart").val(num);
        	}else{
        		$("#speeedStart").val(num);
        	}
            var index = $(obj).index();
            if($(obj).is(".score-icon")) {
                $(obj).parent().find("li:lt("+(index+1)+")").removeClass().addClass("score-icon-y");
            }else {
                $(obj).parent().find("li:gt("+index+")").removeClass().addClass("score-icon");
            }
        }
        function isWell(obj,num) {
        	$("#feel").val(num);
            if(!$(obj).is(".level-active")) {
                $(obj).addClass("level-active").siblings().removeClass("level-active");
            }
        }
        $(".uploadImage").click(function(){
        	var len = $("i.ph-real").length;
        	if(len > 6){
        		alert("您已经上传了6张图片，请重新选择");
        		return false;
        	}
        });
        //上传图片
        function uploadImages(obj){
        	var files= obj.files;
        	var fs=files.length;
        	var len = $("i.ph-real").length;
        	if(len > 6){
        		alert("您已经上传了6张图片，请重新选择");
        		return false;
        	}else if(len+fs > 6 ){
        	    alert("上传的图片数量超过6个了！请重新选择！");  
        	    return false;
        	}
        	
        	var index = layer.open({
			    title: "",
			    content: "",
			    type:2,
			    shadeClose:false
			});
        	
        	$("#uploadForm").ajaxSubmit({
	    		url:"/mMember/79B4DE7C/updateImage.do",
	    		type:"POST",
	    		dataType:"JSON",
	    		success:function(data){
	    			layer.closeAll();
	    			//console.log(data);
	    			if(data.result==true){
	    				var http = data.path;
	    				var imagePath = data.imagePath;
	    				if(imagePath != null && imagePath != ""){
	    					var imgArr = imagePath.split(",");
	    					for(var i in imgArr){
	    						//console.log(imgArr[i])
	    						if(imgArr[i] != null && imgArr[i] != ""){
	    							var imgUrls = http+imgArr[i];
	    							$('<i class="ph-real img_i" style="background-image: url('+imgUrls+');" img="'+imgArr[i]+'"></i>').insertBefore($(".ph_span"));
	    						}
	    					}
	    					var newImgUrls = getImagesUrl();
	    					if(newImgUrls != null && newImgUrls != ""){
								$(".imageUrls").val(newImgUrls);
	    					}
	    				}
	    			}else{
	    				alert("上传图片失败，请稍后重试");
	    			}
	    		},error:function(){
					layer.closeAll();
				 }
	    	}); 		
        	
        }
        
        function getImagesUrl(){
        	var imageUrl = null;
        	if($("i.img_i").length > 0){
        		$("i.img_i").each(function(){
            		var img = $(this).attr("img");
            		if(img != null && img != "" && typeof(img) != "undefined"){
            			if(imageUrl != null){
            				imageUrl += ";";
            			}else{
            				imageUrl = "";
            			}
            			imageUrl += img;
            		}
            	});
        	}
        	return imageUrl;
        }
    </script>
</body>
</html>