<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>${updateAddress}</title>
	<%
		String path = request.getContextPath();
		String basePath = request.getScheme()+"://"+request.getServerName()+":"
			+request.getServerPort()+path+"/";
	%>
	<base href="<%=basePath%>">
	<meta http-equiv="X-UA-Compatible"content="IE=edge,chrome=1" />
    <meta id="meta" name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <meta name="format-detection" content="telephone=no"/>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="HandheldFriendly" content="true">
    <meta name="MobileOptimized" content="320">
    <meta name="screen-orientation" content="portrait">
    <meta name="x5-orientation" content="portrait">
    <meta name="full-screen" content="yes">
    <meta name="x5-fullscreen" content="true">
    <meta name="browsermode" content="application">
    <meta name="x5-page-mode" content="app">
    <meta name="msapplication-tap-highlight" content="no">
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
    <meta http-equiv="Pragma" content="no-cache" />
    <meta http-equiv="Expires" content="0" />
    <link rel="stylesheet" href="/css/common/init.css"/>
<style type="text/css">
body{min-height: 100%;position: relative;font-family: SimHei,"微软雅黑"; font-size: 28px;color:#000;}
.Warp{width: 830px;height: 100%;margin: 0 auto;padding: 20px;}
.main-list li{ height: 100px;line-height: 100px;border-bottom:1px solid #edeef1;}
.main-list li:last-child{border-bottom: none; }
.main-list li label{padding-left: 20px;}
.main-list li input{width: 650px;height: 100%;border: none;appearance: none;-webkit-appearance: none; }
.main-list li select{width: 200px;appearance: none;-webkit-appearance: none;height: 100%;border: none;}
.circle1{position: relative;width: 34px;height: 34px;border-radius: 50%;border: 2px solid #9b9b9b;left: 800px;
     top: -70px;display: -webkit-box;-webkit-box-orient: horizontal;-webkit-box-pack: center; -webkit-box-align: center;
     display: box; box-orient: horizontal;box-pack: center; box-align: center;}
.circle2{width: 14px;height: 14px;background-color: #F20000;border-radius: 50%;margin: 0 auto;}
.bottom{/* position: absolute; */width: 870px;bottom: 20px;height: 192px;background-color: #FFFFFF; }
.bottom .btn{width: 726px;height: 84px;line-height: 84px;border: none;color: #333;display: block;
	border-radius: 12px;background-color: #E4E4E4;text-align: center;}
.bottom #save{margin:0px auto 24px auto;}
.bottom #cancel{background-color: #969da1;margin:0px auto ;}
.bottom .backColor{background-color: #f23030;color: #FFFFFF;}
</style>
</head>
<body>
<!--加载动画-->
<section class="loading">
    <div class="load3">
        <div class="double-bounce1"></div>
        <div class="double-bounce2"></div>
    </div>
</section>
<link rel="stylesheet" type="text/css" href="/css/mall/reset.css"/>
<script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
<div class="Warp">
	<form action="/phoneOrder/79B4DE7C/addAddress.do?uId=${userid }" method="post" id="addressForm">
	<input type="hidden" name="id" id="id" value="${mem.id }" />
	<input type="hidden" name="memDefault" id="memDefault" value="${mem.memDefault }" />
	<input type="hidden" name="addressManage" id="addressManage" value="${addressManage }" />
	<input type="hidden" id="addType" value="${addType }"/>
		<section class="main"> 
		   <ul class="main-list">
		   	<li>
		   		<label for="">收货人:</label>
		   		<input type="text" name="memName" id="memName" value="${mem.memName }" />
		   	</li>
		   	<li>
		   		<label for="">联系方式:</label>
		   		<input type="tel" name="memPhone" id="memPhone" maxlength="11" value="${mem.memPhone }" />
		   	</li>
		   	<li>
		   		<label for="">所在地区:</label>
		   		<span>
		   			<select id="province" name="memProvince" onchange="clearAddress();">
		   				<option value="0">请选择</option>
		   				<c:forEach var="map" items="${maps }">
		   					<option <c:if test="${mem.memProvince==map.id}"> selected="selected"</c:if> value="${map.id }">${map.city_name}</option>
		   				</c:forEach>
		   			</select>
		   			<select name="memCity" id="city" cityid="${mem.memCity }" onchange="clearAddress();"></select>
		   			<select name="memArea" id="area" disid="${mem.memArea }"  onchange="getLng(1);clearAddress();"></select>
		   		</span>
		   	</li>
		   	<li>
		   		<label for="">收货地址:</label>
		   		<c:if test="${!empty isJuliFreight }">
		   			<input type="text" name="memAddress" id="memAddress" value="${mem.memAddress }" placeholder="点击选择详细地址"  onclick="openMap();" readonly="readonly" />
		   		</c:if>
		   		<c:if test="${empty isJuliFreight }">
		   			<input type="text" name="memAddress" id="memAddress" value="${mem.memAddress }" placeholder="点击选择详细地址" />
		   		</c:if>
		   		<input type="hidden" id="memLongitude" value="${mem.memLongitude }" name="memLongitude" />
		   		<input type="hidden" id="memLatitude" value="${mem.memLatitude }" name="memLatitude" />
		   	</li>
		   	<li style="display: none;">
		   		<label for="">门牌号:</label>
		   		<input type="text" name="memHouseMember" id="memHouseMember" value="${mem.memHouseMember }" placeholder="街道，楼牌号等" />
		   	</li>
		   	<li>
		   		<label for="">邮政编码:</label>
		   		<input type="tel" name="memZipCode" id="memZipCode" value="${mem.memZipCode }" maxlength="6" />
		   	</li>
		   	<%-- <li>
		   		<label for="">设为默认地址:</label>
		   		<input type="hidden" name="memDefault" id="memDefault" value="1" />
		   		<div class="circle1">
		   			<div class="circle2" style="display: ${mem.memDefault == 2?'none':'block' };">
		   			</div>
		   		</div>
		   	</li> --%>
		   </ul>
		</section>
	</form>
	
   	<iframe id="geoPage" width=0 height=0 frameborder=0  style="display:none;" scrolling="no"
	    src="https://apis.map.qq.com/tools/geolocation?key=OB4BZ-D4W3U-B7VVO-4PJWW-6TKDJ-WPB77&referer=myapp">
	</iframe> 
	
	<div class="bottom">
		<a href="javascript:void(0);" name="save" id="save" class="btn backColor">保存</a>
		<!-- <input type="button" name="save" id="save" class="btn backColor" value="保存" disabled="disabled" /> -->
		<!-- <input type="button" name="cancel" id="cancel" class="btn" value="取消" onclick="history.go(-1)" /> -->
	</div>
</div>
<input type="hidden" class="userid" value="${userid }"/>
<script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=ZpmmFnpf7ZalTwV2Urf3Q4N2"></script>
<c:if test="${!empty isJuliFreight }">
	<input type="hidden" class="isJuliFreight" value="${isJuliFreight }"/>
</c:if>

<script type="text/javascript" src="/js/mall/phone/phone_public.js"></script>
<script> 
var userid = $("input.userid").val();
wx.config({
	debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
    appId: "${record.get('appid')}", // 必填，公众号的唯一标识
    timestamp: "${record.get('timestamp')}", // 必填，生成签名的时间戳
    nonceStr: "${record.get('nonce_str')}", // 必填，生成签名的随机串
    signature: "${record.get('signature')}",// 必填，签名，见附录1
    jsApiList: ['hideOptionMenu'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
});

wx.ready(function(){
 wx.hideOptionMenu();
});

$(window).load(function(){
	var a=$(window).width(),
	b=$(window).height(),
	d=870,
	meta=$("#meta");
	meta.attr("content","width=870,initial-scale="+a/d+", minimum-scale="+a/d+", maximum-scale="+a/d+", user-scalable=no");
	setTimeout(function(){
		$(".loading").hide();
	},1000);
});

$(window).resize(function() {
	/* $(".bottom").toggle(); */
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
	queryCity($("#province").val(),$("#city"));
	
	$("#province").change(function(){
		if($(this).val()!="0" && $(this).val()!=undefined){
			queryCity($(this).val(),$("#city"));
			$("#area").val("");
			$("#area").html("");
		}
	});
	//$("#province").change();
	
	$("#city").change(function(){
		if($(this).val()!="0"&&$(this).val()!=undefined){
			queryCity($(this).val(),$("#area"));
		}
	});
	//$("#city").change();	
	
	$('#memName').keyup(function(){
		saveDisable();
	});
	
	$('#memPhone').keyup(function(){
		saveDisable();
	});
	
	$('#memAddress').keyup(function(){
		saveDisable();
	});
	
	$('#province').change(function(){
		saveDisable();
	});
	
	$('#city').change(function(){
		saveDisable();
	});
	
	$('#area').change(function(){
		saveDisable();
	});
	
	$('#save').click(function(){
		var memName = $("#memName").val();
		var memPhone = $("#memPhone").val();
		var province = $("#province").val();
		var city = $("#city").val();
		var area = $("#area").val();
		var address = $("#memAddress").val();
		var memLongitude = $("#memLongitude").val();
		var memLatitude = $("#memLatitude").val();
		var isJuliFreight = $(".isJuliFreight").val();
		if(memName == null || memName == ""){
			alert("请输入收货人");
			$("#memName").focus();
			return;
		}else if(memPhone == null || memPhone == ""){
			alert("请输入联系方式");
			$("#memPhone").focus();
			return;
		}else if(province == null || province == "" || province == "0"){
			alert("请选择所在地区的省份");
			return;
		}else if(city == null || city == "" || city == "0"){
			alert("请选择所在地区的城市");
			return;
		}else if(area == null || area == "" || area == "0"){
			alert("请选择所在地区的区域");
			return;
		}else if(address == null || address == "" ){
			alert("请完善详细地址");
			$("#address").focus();
			return;
		}else if(address.length > 100){
			alert("详细地址最多只能输入100个字符");
			$("#address").focus();
			return;
		}else{
			if(isJuliFreight != null && isJuliFreight != "" && typeof(isJuliFreight) != "undefined" && isJuliFreight == 1){
				if(memLongitude == null || memLongitude == "" || memLatitude == null || memLatitude == ""){
					alert("请重新选择详细地址");
					$("#address").focus();
					return;
				}
			}
		}
		$('#save').attr("disabled",true);
		var memOrderAddress = $("#addressForm").serializeObject();
		var obj = JSON.stringify(memOrderAddress);
		$.post("phoneOrder/79B4DE7C/addAddress.do", {
			params: obj
		}, function(result) {
			if(!result.result){
				$('#save').attr("disabled",false);
				alert(result.message);
				return false;
			}else{
				var addressManage = $("#addressManage").val();
				var addType = $("#addType").val();
				urls = "/phoneOrder/79B4DE7C/addressList.do?uId="+userid;
				if(addType != null && addType != "" && typeof(addType) != "undefined"){
					urls += "&addType="+addType;
				}
				if(addressManage != "" && addressManage != undefined){
					urls += "&addressManage="+addressManage;
				}
				window.location.href= urls;
				return true;
			}
		},"json");
	});
	var memDefault = $("#memDefault").val();
	if(memDefault == null || memDefault == "" || typeof(memDefault) == "undefined"){
		 $("#memDefault").val(2);
	}
});

function saveDisable() {
	var province = $("#province").val();
	var city = $("#city").val();
	var area = $("#area").val();
	var memName = $("#memName").val();
	var memPhone = $("#memPhone").val();
	var address = $("#memAddress").val();
	if(province != 0 && city != 0 && area != 0 && memName != ''&& memPhone != '' && address != ''){
		//$('#save').addClass("backColor");
		//$('#save').attr("disabled",false);
	}else{
		//$('#save').removeClass("backColor");
		//$('#save').attr("disabled",true);
	}
}

/**
 * 根据省份选市/县/区
 * @param pid
 * @param obj
 */
function queryCity(pid,obj){
	var options={
			url:"phoneOrder/"+pid+"/79B4DE7C/queryCity.do",
			dataType:"json",
			success:function(data){
				var html='<option value="0">请选择</option>';
				for (var i = 0; i < data.length; i++) {
					var item=data[i];
					if((obj.attr("cityid")!=undefined && obj.attr("cityid")==item.id) || (obj.attr("disid")!=undefined && obj.attr("disid")==item.id )){
						html+="<option value="+item.id+" selected='selected'>"+item.city_name+"</option>";
					}else{
						html+='<option value="'+item.id+'">'+item.city_name+'</option>';
					}
				}
				obj.html(html);
				if(obj.attr("id") == "city"){
					queryCity(obj.attr("cityid"),$("#area"))
				}
			}
	};
	$.ajax(options);
}



var cityCode=null;
window.addEventListener('message', function(event) {
    // 接收位置信息
    var loc = event.data; 
    //console.log(loc)
    if(loc!=null && loc!=""){
    	 cityCode=loc.adcode;
    }
}, false);   


/**获取地点经纬度**/
function getLng(o){
	var data={};
	var address="";
	if($("#province").val()!="0"&&$("#province").val()!=undefined){
		address=$("#province option:selected").text();
	}
	if($("#city").val()!="0"&&$("#city").val()!=undefined){
		address+=$("#city option:selected").text();
	}
	if($("#area").val()!="0"&&$("#area").val()!=undefined){
		address+=$("#area option:selected").text();
	}
	data.output="jsonp";
	data.address=address;
	data.key="2VBBZ-A3C3O-E7XW7-S6RWA-Q676Z-O6FGU";
	 $.ajax({
        type:"get",
        dataType:'jsonp',
        data:data,
        jsonp:"callback",
        url:"http://apis.map.qq.com/ws/geocoder/v1",
        jsonpCallback:"QQmap",
        success:function(json){
        	if(json.status == 0){
        		$("#memLongitude").val(json.result.location.lng);
            	$("#memLatitude").val(json.result.location.lat);
        		if(o == 0){
        			var memOrderAddress = $("#addressForm").serializeObject();
        			var obj = JSON.stringify(memOrderAddress);
        			var url="/mMember/79B4DE7C/tencentMap.do?params="+obj+"&uId=${userid }";

        			var addressManage = $("#addressManage").val();
        			var addType = $("#addType").val();
        			if(addType != null && addType != "" && typeof(addType) != "undefined"){
        				url += "&addType="+addType;
        			}
        			if(addressManage != "" && addressManage != undefined){
        				url += "&addressManage="+addressManage;
        			}
        			location.href=url;
        		}
        		
        	}
        }});
}

/**打开地图**/
function openMap(){
	if($("#province").val() =="0"){
		alert("请选择省份信息！");
		return;
	}
	if($("#city").val()=="0"){
		alert("请选择城市信息！");
		return;
	}

	if($("#city").val()=="0"){
		alert("请选择区域信息！");
		return;
	}
	
	var memOrderAddress = $("#addressForm").serializeObject();
	if(memOrderAddress.memLongitude == null || memOrderAddress.memLongitude == "" || typeof(memOrderAddress.memLongitude) == "undefined"){
		getLng(0);
	}else{
		var obj = JSON.stringify(memOrderAddress);
		var url="/mMember/79B4DE7C/tencentMap.do?params="+obj+"&uId=${userid }";
		
		var addressManage = $("#addressManage").val();
		var addType = $("#addType").val();
		if(addType != null && addType != "" && typeof(addType) != "undefined"){
			url += "&addType="+addType;
		}
		if(addressManage != "" && addressManage != undefined){
			url += "&addressManage="+addressManage;
		}
		location.href=url;
	}
}

function clearAddress(){
	$("#memAddress").val("");
}

</script>
</body>
</html>
    