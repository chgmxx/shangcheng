﻿﻿/**
 * 删除拍卖
 */
function deleteGroup(obj,type) {
	var id = $(obj).attr("id");
	if (id != null && id != "") {
		var msg = "删除拍卖";
		if(type*1 == -2){
			msg = "使失效";
		}
		// 询问框
		parent.layer.confirm('您确定要'+msg+'？', {
			offset : "30%",
			btn : [ '确定', '取消' ]
		// 按钮
		}, function() {
			// loading层
			var layerLoad = layer.load(1, {
				shade : [ 0.1, '#fff' ]
			// 0.1透明度的白色背景
			});
			$.ajax({
				type : "post",
				url : "mAuction/group_remove.do",
				data : {
					id : id,
					type : type
				},
				dataType : "json",
				success : function(data) {
					parent.layer.close(layerLoad);
					if (data.code == 1) {
						var tip = parent.layer.alert(msg+"成功", {
							offset : "30%",
							closeBtn : 0
						}, function(index) {
							parent.layer.close(tip);
							location.href = window.location.href;
						});
					} else {// 编辑失败
						var tip = parent.layer.alert(msg+"失败", {
							offset : "30%"
						});
					}
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					parent.layer.close(layerLoad);
					parent.layer.alert(msg+"失败", {
						offset : "30%"
					});
					return;
				}
			});
			parent.layer.closeAll();
		});
	}

}

function loadLayDate(){
	//初始化预计发货开始时间
	var datebox_1 = {
		elem : '#sStartTime',
		event : 'focus',
		format: 'YYYY-MM-DD hh:mm:ss',
		festival : true,
		istime : true,
		min : laydate.now(),
		choose : function(datas) {
			$('#sStartTime').parent().find("span.red").html("");
			if(datas < laydate.now()){
				datas = laydate.now();
				$('#sStartTime').val(datas)
			}
			datebox_2.min = datas; // 开始日选好后，重置结束日的最小日期
			datebox_2.start = datas; // 将结束日的初始值设定为开始日
			loadWindow();
		}
	}
	// 初始化预计发结束时间
	var datebox_2 = {
		elem : '#sEndTime',
		event : 'focus',
		format: 'YYYY-MM-DD hh:mm:ss',
		festival : true,
		istime : true,
		//min : laydate.now(),
		choose : function(datas) {
			//datebox_1.max = datas;
			$('#sEndTime').parent().find("span.red").html("");
			loadWindow();
		}
	}
	
	laydate(datebox_1);
	laydate(datebox_2);
	var startTime = $("#sStartTime").val();
	if(startTime != null && startTime != ""){
		datebox_2.min =startTime;
		datebox_2.start =startTime;
	}
	
	/*var isSpec = $("#isSpec").val();
	if(isSpec == 1){
		getProductId($("#productId").val());
	}*/
	loadWindow();
}
function getProductId(proId){
	$.ajax({
		type : "post",
		url : "mGroupBuy/getSpecificaByProId.do",
		data : {
			proId : proId
		},
		dataType : "json",
		success : function(data) {
			if(data != null && data.list != null){
				proSpecArr = data.list;
			}
			eval(step.Creat_Table());// 初始化创建库存表格
		}
		
	});
}
$(".times_span").click(function(){
	if(!valiForm()){
		layer.msg('请完善商品信息', {
			offset : "30%",
			icon : 1
		});
		return; 
	}
	endTime();
	
});
$(document).ready(function(){
	if(typeof $("#sStartTime").val() != "undefined")
		loadLayDate();
	
	if($("#aucStartPrice").length > 0 && $("#ids").val()!='' && $(".aucType:checked").val() == 1)
		endTime();
	if($(".isMargin").is(":checked")){
		$(".aucMarginDiv").show();
		$(".hidePrice").addClass("vali");
	}else{
		$(".aucMarginDiv").hide();
		$(".hidePrice").removeClass("vali");
	}
	
	if($(".aucType:checked").val() == 1){
		$(".diffDiv").show();
		$(".addDiv").hide();
		
		$(".addDiv input").removeClass("vali");
		$(".diffDiv input").addClass("vali");
	}else{
		$(".diffDiv").hide();
		$(".addDiv").show();
		$(".diffDiv input").removeClass("vali");
		$(".addDiv input").addClass("vali");
	}
	
	
});

function endTime(){
	var startPrice = $("#aucStartPrice").val()*1;//起拍价格
	var aucLowestPrice = $("#aucLowestPrice").val()*1;//最低价格
	var aucStartTime = $("#sStartTime").val();//活动生效开始时间
	var minuTimes = $("#aucLowerPriceTime").val()*1;//降价幅度，分钟
	var aucLowerPrice = $("#aucLowerPrice").val()*1;//每分钟价格
	if(startPrice <= aucLowestPrice){
		layer.msg('最低价格必须小于起拍价格', {
			offset : "30%",
			icon : 1
		});
	}else{
		var price = startPrice-aucLowestPrice;
		var diff = Math.ceil(price/aucLowerPrice)*minuTimes;
		var diffHtml = "";

		var day = 0;
		if(diff > (24 * 60)){
			day = parseInt(diff / (24 * 60));
			diffHtml += day+"天";
		}
		var hour = 0;
		if(diff > 60){
			hour = parseInt(diff / (60) - day * 24);
			diffHtml += hour+"小时";
		}
		var min = parseInt(diff - day * 24 * 60 - hour * 60)
		if(min > 0){
			diffHtml += min+"分钟";
		}
		$("span.cxTimes").html(diffHtml);
		
		var endTimes = AddDays(aucStartTime,diff*60*1000);
		$(".endTimes_span").html("预计 "+endTimes+" 结束");
		$("#sEndTime").val(endTimes);
		
//		console.log(day+"--"+hour+"---"+min)
		
	}
}
function AddDays(date,diff){
	var nd = new Date(date);
	   nd = nd.valueOf();
	   nd = nd + diff;
	   nd = new Date(nd);
	   console.log(nd)
	var y = nd.getFullYear();
	var m = nd.getMonth()+1;
	var d = nd.getDate();
	var h = nd.getHours();
	var mi = nd.getMinutes();
	var s = nd.getSeconds();
	if(m <= 9) m = "0"+m;
	if(d <= 9) d = "0"+d; 
	var cdate = y+"-"+m+"-"+d+" "+h+":"+mi+":"+s;
	return cdate;
}
/**
 * 验证表单
 */
function valiForm(){
	var flag = true;
	$("input.vali").each(function() {
		var bol = true;
		/*if($(this).attr("name") == "sPrice" && isSpec == 1){
			bol = false;
		}*/
		if(bol){
			resultFlag = valiReg($(this));
			
			if(!resultFlag){
				flag = resultFlag;
				return;
			}
		}
	});
	return flag;
}
/**
 * 编辑拍卖
 */
function editAuction() {
	var shopId = $(".shopId").find("option:selected").val();//店铺id
	var aucStartTime = $("#sStartTime").val();//活动生效开始时间
	var aucEndTime = $("#sEndTime").val();//活动结束时间
	var productId = $("#productId").val();//商品id
	var aucLowestPrice = $("#aucLowestPrice").val();
	var ids = $("#ids").val();
	var flag = valiForm();
	
	if(productId == null || productId == ""){
		layer.msg('请选择商品', {
			offset : "30%",
			icon : 1
		});
	}else if(!flag){
		layer.msg('请完善商品信息', {
			offset : "30%",
			icon : 1
		});
	}else if(aucStartTime == null || $.trim(aucStartTime)==""){
		layer.msg('请选择活动开始时间', {
			offset : "30%",
			icon : 1
		});
	}else if(aucStartPrice*1 < aucLowestPrice*1){
		layer.msg('最低价格必须小于起拍价格', {
			offset : "30%",
			icon : 1
		});
	}else {
		var auction = $("#groupForm").serializeObject();
		var aucType = $(".aucType:checked").val();//拍卖类型
		var isMargin = $(".isMargin").is(":checked");//是否缴纳保证金
		var aucMargin = $("#aucMargin").val();
		if(aucType == 1){//降序
			endTime();
			auction.aucEndTime = $("#sEndTime").val();
			//把升序幅度清空
			auction.aucAddPrice = 0;
		}else{//升序
			//把降序幅度清空
			auction.aucLowerPriceTime = 0;
			auction.aucLowerPrice = 0;
			auction.aucRestrictionNum = 0;
			auction.aucLowestPrice=0;
			if(aucStartTime >= aucEndTime){
				layer.msg('活动开始时间要小于活动结束时间', {
					icon : 1
				});
				return;
			}
		}
		if(isMargin){
			isMargin = 1;
		}else{
			isMargin = 0;
			aucMargin = 0;
		}
		auction.isMargin = isMargin;
		auction.aucMargin = aucMargin;
		auction.aucType = aucType;
		
		
		// loading层
		var layerLoad = parent.layer.load(1, {
			offset : "30%",
			shade : [ 0.1, '#fff' ]
		// 0.1透明度的白色背景
		});
		$.ajax({
			type : "post",
			url : "mAuction/edit_auction.do",
			data : {
				auction : JSON.stringify(auction)
			},
			dataType : "json",
			success : function(data) {
				parent.layer.close(layerLoad);
				if (data.code == 1) {
					var tip = parent.layer.alert("编辑成功", {
						offset : "30%",
						closeBtn : 0
					}, function(index) {
						parent.layer.close(tip);
						location.href = "/mAuction/index.do";
					});
				}else if (data.code == -2) {
					var tip = parent.layer.alert("正在进行拍卖的商品不能修改", {
						offset : "30%",
						closeBtn : 0
					});
				}else if (data.code == 0) {
					var tip = parent.layer.alert("同一个商品只能参与一个拍卖活动", {
						offset : "30%",
						closeBtn : 0
					});
				} else {// 编辑失败
					parent.layer.alert("编辑失败", {
						offset : "30%"
					});
				}

			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				parent.layer.close(layerLoad);
				parent.layer.alert("编辑失败", {
					offset : "30%"
				});
				return;
			}
		});
	}
}
$("#gName").focus(function() {
	valName($(this));
});
$("#gName").blur(function() {
	valName($(this));
});
$("#gName").keyup(function() {
	valName($(this));
});

function valName(obj){
	var val = obj.val();
	var pattern = /^[\u4E00-\u9FA5\uf900-\ufa2d\w\.\s]{1,20}$/g;
	var parent = obj.parent();
	if(val != null && val != ""){
		if (!getStrLen(val, 100)) {
			//val = val.replace(pattern, "");
			parent.find("span.red").css("color", "red");
			return false;
		} else {
			parent.find("span.red").css("color", "#000");
			return true;
		}
	}else{
		parent.find("span.red").css("color", "red");
		return false;
	}
	
}
/**
 * 鼠标选中事件
 */
$("input.vali").focus(function() {
	valiReg($(this));
});
/**
 * 鼠标失去焦点
 */
$("input.vali").blur(function() {
	valiReg($(this));
});
/**
 * 验证正则表达式
 * 
 * @param obj
 */
function valiReg(obj) {
	var datatype = obj.attr("datatype");// 正则表达式
	var errormsg = obj.attr("errormsg");// 错误提示
	datatype = new RegExp(datatype);
	if(obj.hasClass("vali")){
		if (!datatype.test($.trim(obj.val()))) {
			obj.css("border-color", "red");
			obj.next().css("color","red");
			return false;
		}else if(obj.attr("id") == "gPeopleNum" || obj.attr("id") == "maxNum" || obj.hasClass("gPrice")){
			var flag = false;
			if(obj.attr("id") == "maxNum"){
				if(obj.val() != null && obj.val() != "" || obj.hasClass("gPrice")){
					flag = true;
				}
			}else{
				flag = true;
			}
			if(flag && obj.val()*1 <= 0){
				obj.css("border-color", "red");
				obj.next().css("color","red");
				return false;
			}else{
				obj.css("border-color", "#c5c5c5");
				obj.next().css("color","#000");
				return true;
			}
		} else {
			var flag = true;
			if(obj.attr("notNull") != null){
				if(obj.val() == null || $.trim(obj.val()) == ""){
					obj.css("border-color", "red");
					obj.next().css("color","red");
					flag = false;
				}
			}
			if(flag){
				obj.css("border-color", "#c5c5c5");
				obj.next().css("color","#000");
			}
			return flag;
		}
	}
}
function getStrLen(message, MaxLenght) {
	var strlenght = 0; // 初始定义长度为0
	var txtval = $.trim(message);
	for (var i = 0; i < txtval.length; i++) {
		if (isCN(txtval.charAt(i)) == true) {
			strlenght = strlenght + 2; // 中文为2个字符
		} else {
			strlenght = strlenght + 1; // 英文一个字符
		}
	}
	return strlenght > MaxLenght ? false : true;
}
function isCN(str) { // 判断是不是中文
	var regexCh = /[u00-uff]/;
	return !regexCh.test(str);
}

function closewindow() {
	layer.closeAll();
}


/**
 *选择商品 
 */
function choosePro(){
	var shopId = $(".shopId").find("option:selected").val();//店铺id
	var defaultProId = $("#defaultProId").val();
	if((typeof defaultProId) == "undefined"){
		defaultProId = "";
	}
	loadWindow();
	if(shopId != null && shopId != ""){
		parent.openIframe("选择商品","600px","480px","/mGroupBuy/getProductByGroup.do?shopId="+shopId+"&defaultProId="+defaultProId);//check==0代表多选，check==1代表单选
	}else{
		parent.alertMsg("请选择商品");
	}
};
/**
 * 选择商品回调函数
 * @param obj
 */
function getProductGroup(obj,arr){
	if(obj != null){
		$(".goodDiv a").css({
			"background-image":"url("+obj.src+")",
			"background-position":"center center",
			"background-size":"cover"
		}).html("");
		$("input[name='productId']").val(obj.id);
		$("#isSpec").val(obj.isSpe);
		$("span.proName").html(obj.title);
		$(".proPrice").html("￥"+obj.price);
		
		$("#aucNum").val(obj.stockTotal);
		//console.log(obj.stockTotal)
		/*if(arr != null && obj.isSpe == 1){
			proSpecArr = arr;
//			console.log(proSpecArr)
			
			eval(step.Creat_Table());// 初始化创建库存表格
			
			$("div.proPrices").hide();
		}else{
			$("#createTable").hide();
			$("div.proPrices").show();
		}*/
		
	}
	loadWindow();
}
/**
 * 验证表格
 * 
 * @param obj
 */
function valiTable(obj) {
	var priceTest = /^[0-9]{1,6}(\.\d{1,2})?$/;
	var numTest = /^\d{1,6}$/;
	if (obj.hasClass("js-price")) {
		if (!priceTest.test($.trim(obj.val()))) {
			obj.next().html("价格最多只能是6位小数或整数");
			obj.next().show();
			obj.next().css("color","red");
			return false;
		} else if(obj.val()*1 <= 0){
			obj.next().html("价格最多只能是6位小数或整数");
			obj.next().show();
			obj.next().css("color","red");
			return false;
		}else{
			obj.next().css("color","#000");
			obj.next().hide();
		}
	}
	return true;
}

/**
 * 批量修改价格
 */
$(".js-batch-price").click(function() {
	$(".js-batch-form").show();// 显示批量修改框
	$(".js-batch-txt").attr("vali", "^[0-9]{1}\\d{0,5}(\\.\\d{1,2})?$");
	$(".js-batch-txt").val("");
});
/**
 * 关闭批量修改价格或库存
 */
$(".js-batch-cancel").click(function() {
	$(".js-batch-form").hide();// 隐藏修改框
	$(".js-batch-price").show();// 显示批量修改价格按钮
});
/**
 * 批量修改价格或库存
 */
$(".js-batch-save").click(function() {
	var inp = $(".js-batch-txt");
	var tests = inp.attr("vali")
	// console.log(tests)
	tests = new RegExp(tests);
	// console.log(tests)
	// console.log(inp.val() + "=======" + tests.test(inp.val()));
	if (!tests.test(inp.val())) {
		var msg = $(".js-batch-price").attr("errormsg");
		layer.tips(msg, $(".js-batch-save"), {
			tips : [ 3, '#3595CC' ]
		});
	} else {
		if(inp.val()*1 <= 0){
			msg = $(".js-batch-price").attr("errormsg");
			layer.tips(msg, $(".js-batch-save"), {
				tips : [ 3, '#3595CC' ]
			});
		}else{
			$(".js-price").val(inp.val());
			$(".js-price").next().hide();
			$(".js-batch-form").hide();// 隐藏修改框
			$(".js-batch-price").show();// 显示批量修改价格按钮
		}
		//eval(step.saveInp());
	}
});

/** 自定义一个序列化表单的方法* */
$.fn.serializeObject = function()    
{    
   var o = {};    
   $(this).find("input").each(function(index){
	   if($(this).attr("name") != undefined){
		   if($(this).attr("type")=="text" || $(this).attr("type")=="password" || $(this).attr("type")=="hidden"){
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
$(".shopId").change(function(){
	$("a.add-goods").css("background-image","");
	$("span.proName").html("");
	$("span.proPrice").html("");
	$(".table").hide();
	$(".sPrice").show();
	$(".sPrice").val("");
});
function findGroup(obj){
	var html = "/mAuction/index.do";
	var type = $(obj).find("option:selected").val();
	if(type != null && type != ""){
		html += "?type="+type;
	}
	location.href = html;
}
$(".isMargin").change(function(){
	if($(this).is(":checked")){
		$(".aucMarginDiv").show();
		$(".hidePrice").addClass("vali");
	}else{
		$(".aucMarginDiv").hide();
		$(".hidePrice").removeClass("vali");
	}
});
$(".aucType").change(function(){
	var type = $(this).val();
	if(type == 1){
		$(".diffDiv").show();
		$(".addDiv").hide();
		
		$(".addDiv input").removeClass("vali");
		$(".diffDiv input").addClass("vali");
	}else{
		$(".diffDiv").hide();
		$(".addDiv").show();
		$(".diffDiv input").removeClass("vali");
		$(".addDiv input").addClass("vali");
	}
});