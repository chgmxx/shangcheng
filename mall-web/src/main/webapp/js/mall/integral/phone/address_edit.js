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
	
	$("#city").change(function(){
		if($(this).val()!="0"&&$(this).val()!=undefined){
			queryCity($(this).val(),$("#area"));
		}
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
				var json = getJson();
				$(".orders").val(JSON.stringify(json));
				$('#toAddList').submit(); 
				return true;
			}
		},"json");
	});
	var memDefault = $("#memDefault").val();
	if(memDefault == null || memDefault == "" || typeof(memDefault) == "undefined"){
		 $("#memDefault").val(2);
	}
});
function getJson(){
	var json = {};
	$(".formCla").each(function(){
		json[$(this).attr("name")] = $(this).val();
	});
	return json;
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
        			var userid = $(".userid").val();
        			var url="/mMember/79B4DE7C/tencentMap.do?params="+obj+"&uId="+userid+"&type=1";

        			var shopId = $(".shopId").val();
        			if(shopId != null && shopId != "" && typeof(shopId) != "undefined"){
        				url += "&shopId="+shopId;
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
		var userid = $(".userid").val();
		var url="/mMember/79B4DE7C/tencentMap.do?params="+obj+"&uId="+userid+"&type=1";

		var shopId = $(".shopId").val();
		if(shopId != null && shopId != "" && typeof(shopId) != "undefined"){
			url += "&shopId="+shopId;
		}
		location.href=url;
	}
}

function clearAddress(){
	$("#memAddress").val("");
}