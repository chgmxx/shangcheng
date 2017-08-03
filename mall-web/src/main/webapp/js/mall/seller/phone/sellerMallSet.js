var userid = $("input.userid").val();
var numTest = /^\d{1,20}$/;
function editSellerSet(type){
	var msg = "编辑自选商品";
	var data = new Object();
	if(type == 1){
		msg = "编辑商城基本信息";
		var mallName = $("input.mallName").val();
		var contactNumber = $("input.contactNumber").val();
		var qq = $("input.qq").val();
		var mallHeadPath = $("input.mallHeadPath").val();
		var bannerPath = $("input.bannerPath").val();
		var mallIntroducation = $(".mallName").text();
		if(mallName == null || mallName == ""){
			gtcom.dialog("商城名称不能为空","a");
			return false;
		}else if(contactNumber == null || contactNumber == ""){
			gtcom.dialog("联系电话不能为空","a");
			return false;
		}else if(!numTest.test(contactNumber)){
			gtcom.dialog("联系电话必须是1-20位的数字","a");
			return false;
		}else if(qq == null || qq == ""){
			gtcom.dialog("QQ不能为空","a");
			return false;
		}else if(!numTest.test(qq)){
			gtcom.dialog("qq必须是1-20位的数字","a");
			return false;
		}else if(mallHeadPath == null || mallHeadPath == ""){
			gtcom.dialog("商城头像不能为空,请上传图片","a");
			return false;
		}else if(bannerPath == null || bannerPath == ""){
			gtcom.dialog("banner不能为空,请上传图片","a");
			return false;
		}
		var sellerObj = $(".basic-setting").serializeObject();
		data["type"] = 1;
		data["mallSet"] = JSON.stringify(sellerObj);
	}else{
		var mallSetId = $("input.mallSetId").val();
		var proArray = new Array();
		$("li.selectProLi").each(function(){
			var sellerProObj = {
				"mallsetId" : mallSetId,
				"productId" : $(this).attr("id"),
				"shopId" : $(this).attr("sId"),
			};
			var ids = $(this).attr("joinProductId");
			if(ids != null && ids != ""){
				sellerProObj["id"] = ids;
			}
			proArray[proArray.length] = sellerProObj;
		});
		var is_open_optional = $(".is_open_optional").val();
		if((proArray == null || proArray.length == 0) && is_open_optional == 1){
			gtcom.dialog("请添加您要自选的商品","a");
			return false;
		}
		data["sellerProList"] = JSON.stringify(proArray);
		data["type"] = 2;
	}
	ajaxUrl(data,msg,type);
}
/**
 * 开启自选商品
 */
function isOpenOptional(obj){
	var id = $("input.id").val();
	var isOpenOptional = 0;
	var msg = "关闭自选商品";
	if(!$(obj).hasClass("self-on")){
		isOpenOptional = 1;
		msg = "开启自选商品";
	}
	var mallSet = {
		"id" : id,
		"isOpenOptional" : isOpenOptional
	};
	var data={
		"type" : 1,
		"mallSet" : JSON.stringify(mallSet)
	};
	ajaxUrl(data,msg,3);
}
function ajaxUrl(data,msg,types){
	// loading层
	layer.open({type: 2});
	$.ajax({
		type : "post",
		url : "/phoneSellers/79B4DE7C/addMallSet.do",
		data : data,
		dataType : "json",
		success : function(data) {
			layer.closeAll();
			if (data.flag ) {
				if(types == 2){
					gtcom.dialog(msg+"成功","b","next2");
				}else{
					gtcom.dialog(msg+"成功","b","next");
				}
			} else {// 编辑失败
				gtcom.dialog(msg+"失败，请稍后重试","a");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			layer.closeAll();
			gtcom.dialog(msg+"失败，请稍后重试","a");
			return;
		}
	});
}
function next2(){
	var saleMemberId = $("input.memberId").val();
	location.href= "/phoneSellers/"+saleMemberId+"/79B4DE7C/mallIndex.do?uId="+userid;
}
function next(){
	location.href = "/phoneSellers/79B4DE7C/toMallSet.do?type=2&uId="+userid;
}




//上传图片
function uploadImages(obj,types,form){
	var files= obj.files;
	var fs=files.length;
	
	layer.open({type: 2});
	
	$("#"+form).ajaxSubmit({
		url:"/mMember/79B4DE7C/updateImage.do",
		type:"POST",
		dataType:"JSON",
		success:function(data){
			layer.closeAll();
			if(data.result==true){
				var http = data.path;
				var imagePath = data.imagePath;
				if(imagePath != null && imagePath != ""){
					var imgArr = imagePath.split(";");
					var newImgUrls = "";
					var imgUrls = "";
					if(imgArr.length > 0){
						newImgUrls = imgArr[0];
						imgUrls = http+imgArr[0];
					}
					if(newImgUrls != null && newImgUrls != ""){
						if(types == 1){//上传商城头像
							$(".mallHeadPath").val(newImgUrls);
							$(".headSpan").css({
								"background-image":"url('"+imgUrls+"')",
								"background-size" : "contain",
							    "border-style" : "solid"
							});
						}else{//上传banner
							$(".bannerPath").val(newImgUrls);
							$(".bannerSpan").css({
								"background-image":"url('"+imgUrls+"')",
								"background-size" : "contain",
							    "border-style" : "solid"
							});
						}
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

function addPro(){
	var ids = "";
	$(".selectProLi").each(function(){
		if(ids != ""){
			ids += ",";
		}
		ids += $(this).attr("id");
	});
	if(ids != null && ids != ""){
		$("input#findIds").val(ids);
	}
	document.findProduct.submit(); 
}