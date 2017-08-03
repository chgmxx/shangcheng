$(function(){
	var deliveryMethod = $("#deliveryMethod").val();
	if(deliveryMethod == 2){
		$('#delivery').html("到店自提");
		$("#delivery_way").show();
		$("#addressDiv").hide();
	}else if(deliveryMethod == 3){
		$('#delivery').html("到店购买");
		$("#delivery_way").hide();
		$("#addressDiv").hide();
		$(".huodao").hide();
	}
	var orderPayWay =  $("#orderPayWay").val();
	if(orderPayWay != null && orderPayWay != "" && typeof(orderPayWay) != "undefined"){
		payWay(orderPayWay);
	}
	
	var total = 0;
	var fare = 0;
	var orderOldMoney = 0;
	$('.shopTotal').find("i").each(function(index){
		var price = Math.round(($(this).html()*1)*100)/100;
		$(this).html(price);
		total += parseFloat(price);//购买总价钱
	});
	$('em#proPrice').each(function(index){
		var price = Math.round(($(this).html()*1)*100)/100;
		$(this).html(price);
	});
	var proTypeId = $("#proTypeId").val();
	if(proTypeId == 0){
		$('.shopShipment').find("span").each(function(index){
			fare += parseFloat($(this).html());//总运费
		});
	}
	$('.primaryPrice').each(function(index){
		orderOldMoney += parseFloat($(this).val());//购买所有商品总价
	});
	$('#money').html(total.toFixed(2));
	$("#proMoneyAll").val(total.toFixed(2));
	$('#sum-money').html((total+fare).toFixed(2));
	$('#fare').html(fare);
	$('#orderFreightMoney').val(fare);
	$('#orderMoney').val((total+fare).toFixed(2));
	$('#orderOldMoney').val((orderOldMoney).toFixed(2));
	$('#sumOldMoney').val((total+fare).toFixed(2));
	var groupType = $(".groupType").val();
	if(groupType != null && groupType != "" && typeof groupType != undefined){
		var flag = false;
		var msg = "积分";
		if(groupType == "2"){//积分购买
			$("#payLayer .lay-item").hide();//隐藏所有支付
			msg = "积分";
			//显示积分支付，隐藏其他支付
			$(".lay-item.jfDiv").show();
			$('#orderPayWay').val(4);
			$('#onlinePayment').html("积分支付");
			flag = true;
		}else if(groupType == "5"){//粉币购买
			$("#payLayer .lay-item").hide();//隐藏所有支付
			msg = "粉币";
			//显示积分支付，隐藏其他支付
			$(".lay-item.fbDiv").show();
			$('#orderPayWay').val(8);
			$('#onlinePayment').html("粉币支付");
			flag = true;
		}
		if(flag){
			$("#money").parent().html("<span id='money'>"+total.toFixed(2)+"</span>"+msg);
			$("#sum-money").parent().html("<span id='sum-money'>"+(total+fare).toFixed(2)+"</span>"+msg);
			$('#fare').parent().html("<span id='fare'>免运费</span>");
			$("#orderFreightMoney").val(0);//积分购买免运费
			$("em#proPrice").parent().hide();
		}
		
	}
	//到店自提
	var isTakeTheir = $("input.isTakeTheir").val();
	if(isTakeTheir == 1){
		var dateTime = "";
		var obj = $(".timeLay li:eq(0)");
		if(obj.length > 0){
			var startTime = obj.find("div #startTime").text();
			var endTime = obj.find("div #endTime").text();
			dateTime = obj.find("div:eq(0)").text()+" "+startTime+"-"+endTime;
			$("span#deliveryTime").html(dateTime);
		}
	}
	
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
	
	var loc = null;
	var isMapInit = false;
	/*if($("#geoPage").length > 0){
		//监听定位组件的message事件
		window.addEventListener('message', function(event) { 
		    loc = event.data; // 接收位置信息
		    //console.log('location', loc);
			
		    if(loc  && loc.module == 'geolocation' && !isMapInit) { //定位成功,防止其他应用也会向该页面post信息，需判断module是否为'geolocation'
		    	isMapInit = true;
		    	//console.log(loc.province)
		    	getFreight(loc.province);
		    } else { //定位组件在定位失败后，也会触发message, event.data为null
		        //alert('定位失败'); 
		    }
		
		}, false);
		//为防止定位组件在message事件监听前已经触发定位成功事件，在此处显示请求一次位置信息  
		document.getElementById("geoPage").contentWindow.postMessage('getLocation', '*');
		 
		//设置5s超时，防止定位组件长时间获取位置信息未响应
		var timesIndex = setTimeout(function() {
			//console.log("---")
		    if(loc == null && !isMapInit) {
		        //主动与前端定位组件通信（可选），获取粗糙的IP定位结果
		   		document.getElementById("geoPage").contentWindow.postMessage('getLocation.robust', '*');
		    }else{
		    	ClearTimeout(timesIndex);
		    }
		}, 5000); //5s为推荐值，业务调用方可根据自己的需求设置改时间，不建议太短
	}*/
	
	
	//默认选中联盟优惠
	if($(".unionStatus").length > 0){
		var unionStatus = $(".unionStatus").val();
		var objs = $("#unionDiscountDiv .unionItem:eq(0)");
		if(unionStatus == 1 && objs.length > 0){
			youhui(3,$("#unionDiscountDiv .unionItem:eq(0)"));
		}
	}
	
});

function cancelOrder(){
	$("#fade1").hide();
	$(".seckill_layer").hide();
}

function hideLay(){
	$(".fade").hide();
	$("#payLayer").hide();
	$("#coupon").hide();
	$(".pay-layer").hide();
	$("#deliveryWay").hide();
	$("#timeLay").hide();
	$("#jifenDiv").hide();
	$("#fenbiDiv").hide();
	$("#unionPhoneDiv").hide();
	$("#unionDiscountDiv").hide();
}
/**
 * 商家联盟绑定弹出框
 */
function showUnionLayer(){
	$(".fade").show();
	var unionStatus = $(".unionStatus").val();
	if(unionStatus == -2){
		//弹出绑定联盟卡的页面
		$("#unionPhoneDiv").show();
	}else if(unionStatus == 1){
		$("#unionDiscountDiv").show();
	}
}
var telCode = "";
/**
 * 获取验证码
 */
function getCode(obj){
	var _parent = $(obj).parents(".pay-layer");
	var tel = _parent.find("#phone").val();
	var phoneFlag = Mobilephone(tel);
	if(phoneFlag){
		time($(obj));
		$.ajax({
			  url : "/unionMobile/79B4DE7C/getPhoneCode.do",
			  type:"POST",
			  data:{phone: tel},
			  timeout:300000,
			  dataType:"json",
			  success:function(data){
				  if(data.status == "success"){
					  telCode = data.code;
					  return true;
				  }else if(data.status == "error"){
					  gtcommonDialog("发送失败，重新发送",null);
					  return false;
				  }
			  },error:function(){
				  gtcommonDialog("发送失败，重新发送",null);
				  return false;
			  }
			});
	}else{
		gtcommonDialog("请填写正确的手机号",null);
		return false;
	}
}
/**
 * 验证手机绑定
 */
function valUnionMobile(obj){
	var _parent = $(obj).parents(".pay-layer");
	var tel = _parent.find("#phone").val();
	var unionCode = _parent.find("#unionCode").val();
	var phoneFlag = Mobilephone(tel);
	if(!phoneFlag){
		gtcommonDialog("请填写正确的手机号",null);
	}else if(unionCode == null || unionCode == "" ){
		gtcommonDialog("请填写验证码",null);
	}else if((unionCode != telCode && telCode != "" ) || telCode == ""){
		gtcommonDialog("请填写正确的验证码",null);
	}else{
		$.ajax({
		  url : "/unionMobile/79B4DE7C/bindUnionCard.do",
		  type:"POST",
		  data:{
			  phone: tel,
			  code: unionCode
		  },
		  dataType:"json",
		  success:function(data){
			  if(data.status == "1"){
				  //gtcommonDialog("绑定成功",bindOk);
				  location.reload(true);
			  }else if(data.message != null){
				  gtcommonDialog(data.message,null);
			  }else{
				  gtcommonDialog("绑定联盟卡失败,请稍后重新绑定",null);  
			  }
		  },error:function(){
			  gtcommonDialog("绑定联盟卡失败,请稍后重新绑定",null);
			  return false;
		  }
		});
	}
}
/**
 * 绑定成功
 */
function bindOk(){
	location.reload(true);
}
function gtcommonDialog(tip,cal){
	if(cal == null){
		gtcom.dialog(tip,"a",cal,"联盟卡绑定提示")
	}else{
		gtcom.dialog(tip,"b",cal,"联盟卡绑定提示")
	}
}
/**
 * 60秒后重新获取验证码
 */
var wait = 60;
function time(btn) {
    if (wait == 0) {
        $('.alliance-code').html("获取验证码");
        wait = 60;
    } else {
        btn.html(wait + "秒后重发");
        wait--;
        setTimeout(function(){
            time(btn);
        },1000)
    }
}

/**
 * 优惠券计算
 * @param divName
 * @param obj
 */
function showLay(divName,obj){
	$(".fade").show();
	if(obj == null){
		$("#"+divName).show();
	}else{
		var sumCoupon = 0;
		$(obj).parents("#couponDiv").find('.mall-item').each(function(index){
			var isCoupons = $(this).find("#isCoupons").val();//能否使用优惠券（1-是 0-否）
			var is_member_discount = $(this).find("#is_member_discount").val();//能否使用会员折扣（1-是 0-否）
			var singlePrice = $(this).find('#singlePrice').html();//会员折扣价
			var primary_price = $(this).find('#primary_price').val();//商品原价
			var singleNum = $(this).find('#singleNum').html();//商品数量
			var groupType = $(this).attr("groupType");
			if(groupType != 7){
				if(isCoupons == 1 && is_member_discount == 1){
					var proPrice = $(this).find('em#proPrice').text();
					if(proPrice != ""){
						singlePrice = proPrice;
					}
					sumCoupon += singlePrice*singleNum;
				}else if(isCoupons == 1 && is_member_discount == 0){
					sumCoupon += primary_price*singleNum;
				}else if(isCoupons == 0 && is_member_discount == 0){
					
				}
			}else if($(this).find(".totalOrderMoneys").length > 0){
				var proPrice = $(this).find(".totalOrderMoneys").val()*1;
				sumCoupon = Math.round((sumCoupon+proPrice)*100)/100;
			}
		});
		/*$(".pay-layer").each(function(){*/
		/*$(".duofenCouponDiv").each(function(){*/
			$(obj).parents("#couponDiv").find(".duofenCouponDiv").each(function(){
				var addUser = $(this).find("#addUser").val();
				if(addUser == 1 && addUser != undefined){
					var cashLeastCost = $(this).find("#cashLeastCost").val();
					if(sumCoupon > cashLeastCost){
						var num = parseInt(sumCoupon/cashLeastCost);
						var couponNum = $(this).find("#couponNum").html();
						//console.log(num+"----"+couponNum)
						if(num >= couponNum){
							$(this).find("#couponNum").html(couponNum);
						}else{
							$(this).find("#couponNum").html(num);
						}
					}else{
						$(this).find("#couponNum").html(1);
					}
				}
			}); 
		/*});*/
		$(obj).parents("#couponDiv").find("#"+divName).show();
	}
}

function payWay(num) {
	if(num == 1){
		$('#orderPayWay').val(1);
		$('#onlinePayment').html("微信支付");
		$("#daodian").show();
	}else if(num == 2){
		$('#orderPayWay').val(2);
		$('#onlinePayment').html("货到付款");
		$("#daodian").hide();
	}else if(num == 3){
		$('#orderPayWay').val(3);
		$('#onlinePayment').html("储值卡支付");
	}else if(num == 4){
		$('#orderPayWay').val(4);
		$('#onlinePayment').html("积分支付");
	}else if(num == 6){
		$('#orderPayWay').val(6);
		$('#onlinePayment').html("到店支付");
	}else if(num == 7){
		$('#orderPayWay').val(7);
		$('#onlinePayment').html("找人代付");
	}else if(num == 8){
		$('#orderPayWay').val(8);
		$('#onlinePayment').html("粉币支付");
	}else if(num == 9){
		$('#orderPayWay').val(9);
		$('#onlinePayment').html("支付宝支付");
	}
	hideLay();
}

function delivery(num) {
	if(num == 1){
		$('#deliveryMethod').val(1);
		$('#delivery').html("快递配送");
		$("#addressDiv").show();//显示发货地址
		$("#delivery_way").hide();	
		$("#daodian").show();
		
		if($("#orderPayWay").val() == 6){
			if($(".wxpay_div").length > 0){
				$('#orderPayWay').val(1);
				$('#onlinePayment').html("微信支付");
			}else if($(".aliPay_div").length > 0){
				$('#orderPayWay').val(9);
				$('#onlinePayment').html("支付宝支付");
			}else if($(".czkpay_div").length > 0){
				$('#orderPayWay').val(3);
				$('#onlinePayment').html("储值卡支付");
			}
		}
		
		$("div.storePayDiv").hide();
		$(".huodao").show();
	}else if(num == 3){//到店购买
		$('#deliveryMethod').val(3);
		$('#delivery').html("到店购买");
		$("#addressDiv").hide();//显示发货地址
		$("#delivery_way").hide();	
		$("#daodian").show();
		if($("#orderPayWay").val() == 6){
			if($(".wxpay_div").length > 0){
				$('#orderPayWay').val(1);
				$('#onlinePayment').html("微信支付");
			}else if($(".aliPay_div").length > 0){
				$('#orderPayWay').val(9);
				$('#onlinePayment').html("支付宝支付");
			}else if($(".czkpay_div").length > 0){
				$('#orderPayWay').val(3);
				$('#onlinePayment').html("储值卡支付");
			}
		}
		$("div.storePayDiv").hide();
		$(".huodao").hide();
	}else{
		$('#deliveryMethod').val(2);
		$('#delivery').html("到店自提");
		$("#addressDiv").hide();//隐藏发货地址
		$("#delivery_way").show();
		
		$(".huodao").hide();
		if($(".isStorePay").length > 0){
			var isStorePay = $(".isStorePay").val();
			if(isStorePay != "" && isStorePay == "1"){
				$("div.storePayDiv").show();
				if($("#orderPayWay").val() == 2){
					$('#orderPayWay').val(1);
					$('#onlinePayment').html("微信支付");
				}
			}
		}
	}
	hideLay();
}

function selectTime(obj) {
	var appointTime = $(obj).find("#times").html();
	var appStartTime = $(obj).find("#startTime").html();
	var appEndTime = $(obj).find("#endTime").html();
	$("#appointTime").val(appointTime);
	$("#appStartTime").val(appStartTime);
	$("#appEndTime").val(appEndTime);
	$("#deliveryTime").html(appointTime+" "+appStartTime+" - "+appEndTime);
	hideLay();
}
function isEmptyObject(obj){
    for(var n in obj){return false} 
    return true; 
}
/**
 * 选择优惠券
 * @param obj
 * @param tag
 */
function coupon(obj,tag) {
	var val = $(obj).find("#cardCode").val() || "";
	var boolen = false;
	$(".useCoupon").each(function(){
		if($(this).attr("dataid") == val && val != "")boolen = true;
	})
	if(boolen){
		alert("该优惠券已被选用");
		return;
	}
	
	$(obj).parents(".couponDivs").find("#useCoupon").attr("dataid",val);
	
	var flag = true;
	if(tag == 0){
		/*$("#sum-money").html($("#sumOldMoney").val());
		$("#orderMoney").val($("#sumOldMoney").val());
		$("#proMoneyAll").val($("#sumOldMoney").val());*/
		$(obj).parent().find(".selected").removeClass("selected");
		var parentObj = $(obj).parents("#couponDiv");
		parentObj.find("#useCoupon").html("");
		parentObj.removeClass("selected");
		
		var coupon= {};
		var shopId = parentObj.find('.shopId').val();
		var cArr = $("#couponList").val();
		if(cArr != null && cArr != ""){
			coupon = JSON.parse(cArr);
		}
		delete coupon[shopId];
		if(isEmptyObject(coupon)){
			$("#couponList").val("");
		}else{
			$("#couponList").val(JSON.stringify(coupon));
		}
	}else{
		var code = $(obj).find("#cardCode").val();
		var type = $(obj).find("#couponType").val();
		var total = 0;
		var fare = 0;
		$('.shopTotal').find("i").each(function(index){
			total += parseFloat($(this).html());//购买总价钱
		});
		$('.shopShipment').find("span").each(function(index){
			//fare += parseFloat($(this).html());//总运费
		});
		var sumMoney = (total+fare).toFixed(2);
		var sumCoupon = 0;
		$(obj).parents("#couponDiv").find('.mall-item').each(function(index){
			var isCoupons = $(this).find("#isCoupons").val();//能否使用优惠券（1-是 0-否）
			var is_member_discount = $(this).find("#is_member_discount").val();//能否使用会员折扣（1-是 0-否）
			var singlePrice = $(this).find('#singlePrice').html();//会员折扣价
			/*var singlePrice = $(this).find('em#proPrice').text();//会员折扣价
*/			var primary_price = $(this).find('#primary_price').val();//商品原价
			var singleNum = $(this).find('#singleNum').html();//商品数量
			var groupType = $(this).attr("groupType");
			if(groupType != 7){
				if(isCoupons == 1 && is_member_discount == 1){
					var proPrice = $(this).find('em#proPrice').text();
					if(proPrice != ""){
						singlePrice = proPrice;
					}
					sumCoupon += singlePrice*singleNum;
				}else if(isCoupons == 1 && is_member_discount == 0){
					sumCoupon += primary_price*singleNum;
				}else if(isCoupons == 0 && is_member_discount == 0){
					
				}
			}else if($(this).find(".totalOrderMoneys").length > 0){
				var proPrice = $(this).find(".totalOrderMoneys").val()*1;
				sumCoupon = Math.round((sumCoupon+proPrice)*100)/100;
			}
		});
		//console.log(sumCoupon)
		sumCoupon = Math.round(sumCoupon*100)/100;
		$("#sumCoupon").val(sumCoupon);//有优惠的商品总额
		var spread = (sumMoney-sumCoupon).toFixed(2);//总价减去有优惠的商品总价
		if(sumCoupon > 0){
			var money = 0;
			var discount = 0;
			if(type == "CASH" || type == 1){//满减券(cash是微信,1是多粉)
				var cashLeastCost = $(obj).find("#cashLeastCost").val()*1;
				money = $(obj).find("#cash").val()*1;
				var couponNum = $(obj).find("#couponNum").html();
				if(type == 1 && couponNum != "" && couponNum != undefined){
					money = money*(couponNum*1);
				}
//				var sum_money = (sumCoupon-money)+spread*1;
				var moneyAll = $("span#money").html()*1;
				var sum_money = moneyAll - money;
				if(sum_money > 0){
					sum_money = (sum_money).toFixed(2);
				}
				if(sumCoupon < cashLeastCost){
					$(obj).parents(".couponDivs").find("#useCoupon").attr("dataid","");
					alert("您购买的商品总额不满足条件。");
					flag = false;
				}else if(sum_money < 0){
					sum_money = 0;
					var explain = $(obj).find("#couponExplain").html();
					$(obj).parents(".couponDivs").find("#useCoupon").html(explain);
				}else{
					var explain = $(obj).find("#couponExplain").html();
					$(obj).parents(".couponDivs").find("#useCoupon").html(explain);
				}
			}else{//折扣券
				var explain = $(obj).find("#couponExplain").html();
				$(obj).parents(".couponDivs").find("#useCoupon").html(explain);
				discount = $(obj).find("#discount").val();

			}
		}else{
//			$("#useCoupon").html("");
			$(obj).parents(".couponDivs").find("#useCoupon").attr("dataid","");
			$(obj).parents(".couponDiv").find("#useCoupon").html("");
			alert("您所购买的商品不能使用优惠券。");
			flag = false;
		}
		/*console.log(flag)*/
		if(flag){
			$(obj).parent().find(".lay-item").removeClass("selected");
			$(obj).addClass("selected");
			$(obj).parents("#couponDiv").addClass("selected");
		}
		/*var shopId = $(obj).parents("#couponDiv").find('.shopId').val();
		var couponObj = {
		 "couponCode":code,"fullCoupon":money,"discountCoupon":discount,"shopId":shopId,"proDisAll":sumCoupon
		};
		var coupon= {};
		
		var cArr = $("#couponList").val();
		if(cArr != null && cArr != ""){
			coupon = JSON.parse(cArr);
		}
		coupon[shopId] = couponObj;
		
		$("#couponList").val(JSON.stringify(coupon));*/
	}
	if(flag){
		//计算各种优惠的方法
		youhui(0,obj);
	}
	//hideLay();
}

function getFreight(province){
	var arr = [];
	$("div.sum-info").each(function(){
		var obj = {
			shop_id:$(this).find(".shopId").val(),
			price_total:$(this).find(".priceTotal").val(),
			proNum:$(this).find(".proNum").val()
		};
		arr[arr.length] = obj;
	});
	var obj = {
		province:province,
		orderArr:JSON.stringify(arr)
	};
	$.ajax({
		  url:"mFreight/79B4DE7C/getFreightByProvinceId.do",
		  type:"POST",
		  data: obj,
		  timeout:300000,
		  dataType:"json",
		  success:function(data){
		  	var data = data.maps;
		  	//console.log(data)
		  	var fPrice = 0;
			if(data != null){
				for(sId in data){
					var price = data[sId];
					if(price != null && price!= ""){
						fPrice += parseFloat(price);
						price = parseFloat(price).toFixed(2);
					}else{
						price = "0.00";
					}
					$(".shopShipment[sId='"+sId+"'] span").html(price);
				}
			}
			var proPrice = parseFloat($("span#money").text());
			$("span#fare").text(fPrice.toFixed(2));
			$("#sum-money").text((proPrice+fPrice).toFixed(2));
		  },error:function(){}
		});
}

function selectJifen(type,obj) {
	youhui(1,obj);
}
var fenbiMsg = $(".fenbi-explans").val();
function selectFenbi(obj) {
	youhui(2,obj);
}
function showHideYouhui(tag){
	if(tag == 2){//粉币
		if(!$(".fenbiyouhui").hasClass("slide_on")){
			$(".fenbiyouhui").addClass("slide_on");
			$("#isFenbi").val(1);
		}else{
			$(".fenbiyouhui").removeClass("slide_on");
			$("#isFenbi").val(0);
		}
	}else if(tag == 1){//积分
		if(!$(".jifenyouhui").hasClass("slide_on")){
			$(".jifenyouhui").addClass("slide_on");
			$("#isJifen").val(1);
		}else{
			$(".jifenyouhui").removeClass("slide_on");
			$("#isJifen").val(0);
		}
	}
}
/**
 * 计算优惠券，粉币抵扣和积分抵扣的优惠、联盟卡折扣（加法减法进行四舍五入，乘法除法进行取余）
 * @param tag 0优惠券  1积分   2粉币  3联盟卡折扣
 * @param obj
 * @returns
 */
function youhui(tag,obj){
	showHideYouhui(tag);
	var jifenSum = 0;//能使用积分兑换的商品总价格
	var integral_money = $("#integral_money").val()*1;//可以积分抵扣的价格
	var jifenNum = $(".jifenNum").val()*1;//可以使用积分抵扣的商品个数
	var jifenProMoney = $(".jifenProMoney").val()*1;//可以使用积分抵扣的商品价格
	var jifens = $("#intergral").val()*1;
	
	var fenbiSum = 0;//能使用粉币兑换的商品总价格
	var fenbi_money = $("#fenbi_money").val()*1;//可以粉币抵扣的数量
	var fenbiNum = $(".fenbiNum").val()*1;//可以使用粉币兑换的商品个数
	var fenbiProMoney = $(".fenbiProMoney").val()*1;//可以使用粉币抵扣的商品价格
	
	//var youhuiPrice = 0;//订单总共优惠了多少钱
	var jifenIndex = 0;
	var fenbiIndex = 0;
	var jifenYouhuiPrice = 0;//积分总共优惠了多少钱
	var fenbiYouhuiPrice = 0;//粉币总共优惠了多少钱
	
	var yhqYouHuiPrice = 0;//优惠券总共优惠了多少钱
	var yhqYouHuiSave = 0;
	var sumCoupon = $("#sumCoupon").val()*100/100;//能够使用优惠券的商品总价
	var countCoupons = 0;
	
	var unionIndex = 0;
	var unionYouHuiSave = 0;//联盟卡总共优惠的价钱
	var unionDiscountVal = "";//联盟卡的折扣
	if($(".unionDiscountVal").length > 0 && $(".unionDiscountVal").val() != ""){
		unionDiscountVal = $(".unionDiscountVal").val()*1;
	}
	var UnionYouhuiList = new Object();
	$(".couponDivs").each(function(e){
		var yhqIndex = 0;
		var yhqCount = $(this).find("#yhqNum").val()*1;
		var parentObj = $(this);
		var countMoney = 0;
		var yhqMoneys = 0;
		var unionMoney = 0;//使用优惠总数
		var unionNum = $(this).find(".unionNum").val()*1;//能使用联盟卡的数量
		var discountObj = $(this).find(".lay-item.selected");
		var type = discountObj.find("#couponType").val();
		if($(this).hasClass("selected")){
			var result = youhuiquan($(this));
			if(result != null){
				yhqYouHuiPrice = result;
			}
		}
		sumCoupon = $(this).find("input#yhqSumMoney").val();
		if(sumCoupon != null && sumCoupon != ""){
			sumCoupon = sumCoupon*1;
		}
		countCoupons = countCoupons*1+sumCoupon*1;
		countCoupons = Math.round(countCoupons*100)/100;
		var countUnion = 0;//统计联盟优惠的金额
		/*console.log("优惠券总共优惠了："+yhqYouHuiPrice+"-------"+countCoupons);*/
		$(this).find(".mall-item").each(function(){
			var isCoupons = $(this).find("#isCoupons").val();//能否使用优惠券（1-是 0-否）
			var is_member_discount = $(this).find("#is_member_discount").val();//能否使用会员折扣（1-是 0-否）
			var integralDeduction = $(this).find("#integralDeduction").val();//能否使用积分抵扣（1-是 0-否）
			var fenbiDeduction = $(this).find("#fenbiDeduction").val();//能否使用粉币抵扣（1-是 0-否）
			var singlePrice = $(this).find('#singlePrice').html();//会员折扣价
			var primary_price = $(this).find('#primary_price').val();//商品原价
			var singleNum = $(this).find('#singleNum').html()*1;//商品数量
			var price = $(this).find('#detailProPrice').val()*singleNum;//商品价格
			price = Math.round(price*100)/100;
			var groupType = $(this).attr("groupType");
			if(groupType == 7 && $(this).find(".totalOrderMoneys").length > 0){
				price = $(this).find(".totalOrderMoneys").val();
			}
			var proPrice = price;
			var youhuiCount = 0;
			//计算联盟卡优惠
			if(unionDiscountVal != ""){
				unionDiscountVal = unionDiscountVal*1;
				price = numSub((proPrice*unionDiscountVal/10),2);
				var youhui = Math.round((proPrice-price)*100)/100;
				
				
				//最后一个能使用优惠券的商品
				/*if(unionIndex+1 == unionNum){
//					youhui = yhqYouHuiPrice - unionMoney;
//					youhui = Math.round(youhui*100)/100;
					youhui = yhqYouHuiPrice - unionMoney;
					price = proPrice-youhui;
					price = Math.round(price*100)/100;
					youhui = Math.round(youhui*100)/100;
				}*/
				proPrice = price;
				console.log("联盟卡优惠了："+youhui+"---"+price+"---"+primary_price+"---"+proPrice)
				unionYouHuiSave = unionYouHuiSave+youhui;
				countUnion = countUnion+youhui;
				//price = price-youhui;
				//price = Math.round(price*100)/100;
				
				unionMoney = unionMoney+youhui;
				++unionIndex;
			}
			//计算优惠券优惠的金额
			if(parentObj.hasClass("selected")){
				if((isCoupons == 1 && is_member_discount == 1 )|| (isCoupons == 1 && is_member_discount == 0)){
//					var youhui = (proPrice/sumCoupon)*yhqYouHuiPrice;
					var youhui = 0;
					var fullCoupon = 0,discount=1;
					if(type == "CASH" || type == 1){//满减券(cash是微信,1是多粉)
						fullCoupon = discountObj.find("#cash").val()*1;
						var num = 1;
						var couponNum = discountObj.find("#couponNum").html();
						if(type == 1 && couponNum != '' && couponNum != undefined){
							num = couponNum*1;//取多粉优惠券使用的数量
							fullCoupon = fullCoupon*num;
						}
					}else{//折扣券
						discount = discountObj.find("#discount").val()*1;//折扣数
					}
					var yhqFlag = false;
					
					if(isCoupons == "1" && is_member_discount == "1"){//能使用优惠券、享受会员折扣
						if(fullCoupon != 0){
							youhui = numSub(((proPrice/sumCoupon)*fullCoupon),2);
							if(proPrice < youhui){
								youhui = proPrice;
							}
							price = Math.round((proPrice-youhui)*100)/100;
							//price =  numSub((proPrice-youhui),2);
							yhqFlag = true;
						}else{
							if(discount > 0){
								price = numSub((proPrice*discount/10),2);
								if(proPrice < youhui){
									youhui = proPrice;
								}
								youhui = Math.round((proPrice-price)*100)/100;
								yhqFlag = true;
							}
						}
					}else if(isCoupons == "1" && is_member_discount == "0"){//能使用优惠券、不享受会员折扣
						if(fullCoupon != 0){
							youhui = numSub(((proPrice/sumCoupon)*fullCoupon),2);
							//price =  numSub((proPrice-youhui),2);
							if(proPrice < youhui){
								youhui = proPrice;
							}
							price = Math.round((proPrice-youhui)*100)/100;
							yhqFlag = true;
						}else{
							if(discount > 0){
								price = numSub((proPrice*discount/10),2);
								if(proPrice < youhui){
									youhui = proPrice;
								}
								youhui = Math.round((proPrice-price)*100)/100;
								yhqFlag = true;
							}
						}
					}
					if(yhqFlag){
						//最后一个能使用优惠券的商品
						if(yhqIndex+1 == yhqCount){
//							youhui = yhqYouHuiPrice - yhqMoneys;
//							youhui = Math.round(youhui*100)/100;
							youhui = yhqYouHuiPrice - yhqMoneys;
							price = proPrice-youhui;
							price = Math.round(price*100)/100;
							youhui = Math.round(youhui*100)/100;
						}
						console.log("优惠券优惠了："+youhui+"---"+price+"---"+primary_price+"---"+proPrice)
						yhqYouHuiSave = yhqYouHuiSave+youhui;
						//price = price-youhui;
						//price = Math.round(price*100)/100;
						
						yhqMoneys = yhqMoneys+youhui;
						++yhqIndex;
					}
				}
			}
			//允许粉币兑换，计算粉币兑换的金额
			if(fenbiDeduction == 1){
				fenbiSum += proPrice;
				var fenbiPrice = price;
				if($(".fenbiyouhui").hasClass("slide_on")){
					//计算粉币抵扣
					var fenbiAvg = (fenbiPrice/fenbiProMoney);
					var youhui = numSub((fenbiAvg*fenbi_money),2);
					if(fenbiIndex+1 == fenbiNum && fenbiYouhuiPrice+youhui != fenbiProMoney){
						youhui = fenbi_money - fenbiYouhuiPrice;
						youhui = Math.round(youhui*100)/100;
//						console.log(fenbi_money+"---"+fenbiYouhuiPrice)
					}
					if(youhui > fenbiPrice){
						youhui = fenbiPrice;
					}
					price = fenbiPrice - youhui;
					price = Math.round(price*100)/100;

					console.log("粉币优惠了："+youhui+"----"+price+"==="+primary_price+"---"+proPrice)
					fenbiYouhuiPrice = Math.round((fenbiYouhuiPrice+youhui)*100)/100;
					++fenbiIndex;
					youhuiCount = numSub(youhuiCount+youhui,2);
				}
			}
			//console.log(price)
			//允许积分兑换，计算积分兑换的金额
			if(integralDeduction == 1){
				jifenSum += proPrice;
				var jifenPrice = price;
				if($(".jifenyouhui").hasClass("slide_on")){
					//计算积分抵扣
					var jifenAvg = (jifenPrice/jifenProMoney);
//					console.log(jifenPrice+"+++++"+jifenProMoney+"+++++"+integral_money+"----"+jifenPrice/jifenProMoney*integral_money)
					var youhui = numSub((jifenPrice/jifenProMoney*integral_money),2);
					
					if(jifenIndex+1 == jifenNum && jifenYouhuiPrice+youhui != integral_money){
						youhui = integral_money - jifenYouhuiPrice;
						youhui = Math.round(youhui*100)/100;
					}
					if(youhui > jifenPrice){
						youhui = jifenPrice;
						youhui = Math.round(youhui*100)/100;
					}
					price = jifenPrice - youhui;
					price = Math.round(price*100)/100;
					
					console.log("积分优惠了："+youhui+"---"+price+"---"+primary_price+"---"+proPrice)
					jifenYouhuiPrice = Math.round((jifenYouhuiPrice+youhui)*100)/100;
					
					++jifenIndex;
					youhuiCount = numSub(youhuiCount+youhui,2);
				}
			}
			if(price < 0){
				price = 0;
			}
			price = numSub(price,2);
			//$(this).find("em#proPrice").html(price);
			$(this).find("input#proPrice").val(price);
			countMoney = countMoney+price;
		});
		var shopIds = $(this).find(".shopId").val();
		UnionYouhuiList[shopIds] = {
			"unionYouhui":countUnion
		};
		
		var couponObj = new Object();
		var cArr = $("#couponList").val();
		if(cArr != null && cArr != ""){
			couponObj = JSON.parse(cArr);
		}
		if(couponObj != null && couponObj != ""){
			var couponDiscountObj = couponObj[shopIds];
			if(couponDiscountObj != null){
				couponDiscountObj["youhui"] = yhqMoneys;
				couponObj[shopIds] = couponDiscountObj;
			}
			$("#couponList").val(JSON.stringify(couponObj));
		}
		
		countMoney = Math.round(countMoney*100)/100;
		$(this).find(".orderCountMoney").val(countMoney);
	});

	if(UnionYouhuiList != null && UnionYouhuiList != ""){
		$(".unionYouhuiList").val(JSON.stringify(UnionYouhuiList));
	}
	
	if(unionYouHuiSave > 0){
		unionYouHuiSave = Math.round(unionYouHuiSave*100)/100;
	}
	if(jifenYouhuiPrice > 0){
		jifenYouhuiPrice = Math.round(jifenYouhuiPrice*100)/100;
	}
	if(fenbiYouhuiPrice > 0){
		fenbiYouhuiPrice =  Math.round(fenbiYouhuiPrice*100)/100;
	}
	if(yhqYouHuiSave > 0){
		yhqYouHuiSave =  Math.round(yhqYouHuiSave*100)/100;
	}
	/*console.log("积分总共优惠了："+jifenYouhuiPrice);
	console.log("粉币总共优惠了："+fenbiYouhuiPrice);
	console.log("优惠券总共优惠了："+yhqYouHuiSave);*/
	$("#yhj").html(yhqYouHuiSave);
	$("#jf").html(jifenYouhuiPrice);
	$("#fb").html(fenbiYouhuiPrice);
	$("#lm").html(unionYouHuiSave);
	$("#countYhq").val(yhqYouHuiSave);
	$("#countJifen").val(jifenYouhuiPrice);
	$("#countFenbi").val(fenbiYouhuiPrice);
	$("#countUnion").val(unionYouHuiSave);
	
	
	//var orderMoneySum = $(".summary #money").html()*1;
	var payMoney  =  $('#money').html()*1;
	/*console.log("优惠前的总价："+payMoney)*/
	if(tag == 0){
		hideLay();
	}
	if(unionYouHuiSave > 0){
		payMoney = payMoney-unionYouHuiSave;
	}
	if(yhqYouHuiSave > 0){
		payMoney = payMoney-yhqYouHuiSave;
	}
	if(jifenYouhuiPrice > 0){
		payMoney = payMoney - jifenYouhuiPrice;
	}
	if(fenbiYouhuiPrice > 0){
		payMoney = payMoney - fenbiYouhuiPrice;
	}
	if(tag == 1){//计算积分
		var starttype = $("#starttype").val()*1;
		/*if(starttype == 0){//积分
			jifen_money = $("#jifenStartMoney").val()*1;
		}else{//订单金额
			jifen_money = $("#orderStartMoney").val()*1;
		}*/
		jifen_money = $("#orderStartMoney").val()*1;
		if(jifenSum < jifen_money){
			alert("您的订单不满足积分抵扣的要求。");
			$(".jifenyouhui").removeClass("slide_on");
			$("#isJifen").val(0);
			return false;
		}
	}
	var freightMoney = $("#orderFreightMoney").val();
	if(freightMoney == null || freightMoney == ""){
		freightMoney = 0;
	}
	if(payMoney < 0){
		payMoney = freightMoney*1;
	}else{
		payMoney = payMoney+freightMoney*1;
	}
	payMoney = Math.round(payMoney*100)/100;
	/*console.log(payMoney)*/
	$("#sum-money").html(payMoney);//使用粉币抵扣计算的实付金额
	$("#sumFenbi").val(fenbiSum);
	$("#sumJifen").val(jifenSum);
	$("#orderMoney").val($("#sum-money").html());
	$("#sumCoupon").val(numSub(countCoupons,2));
	
	var orderMoney = $("#orderMoney").val();
	if(orderMoney == 0){//金额优惠完了
		if(tag == 1){//抵扣了积分，让粉币不可用
			$(".fenbi_noopen_span").show();
			$(".fenbi_open_span").hide();
		}else if(tag == 2){//抵扣了粉币，让积分不可用
			$(".jifen_noopen_span").show();
			$(".jifen_open_span").hide();
		}else{//抵扣了优惠券和微信联盟，让积分粉币不可用
			$(".fenbi_noopen_span").show();
			$(".fenbi_open_span").hide();
			$(".jifen_noopen_span").show();
			$(".jifen_open_span").hide();
		}
	}else{
		if($(".fenbi_noopen_span").is(":hidden")){
			$(".fenbi_noopen_span").hide();
		}
		if(!$(".fenbi_open_span").is(":hidden")){
			$(".fenbi_open_span").show();
		}
		if($(".jifen_noopen_span").is(":hidden")){
			$(".jifen_noopen_span").hide();
		}
		if(!$(".jifen_open_span").is(":hidden")){
			$(".jifen_open_span").show();
		}
	}
	
}
function numSub(num,len){
	var bb = num+"";    
	len = len +1;
	if(bb.indexOf('.') > 0){
		var dian2 = bb.indexOf('.')+len;    
		num = bb.substring(0,dian2)*1;
	}
	return num;
}

function youhuiquan(parentObj){
	var result = null;
	var obj = parentObj.find(".lay-item.selected");
	var code = $(obj).find("#cardCode").val();
	var type = $(obj).find("#couponType").val();
	var total = 0;
	var fare = 0;
	parentObj.find('.shopTotal').find("i").each(function(index){
		total += parseFloat($(this).html());//购买总价钱
	});
	$('.shopShipment').find("span").each(function(index){
		//fare += parseFloat($(this).html());//总运费
	});
	var sumMoney = (total+fare).toFixed(2);
	var sumCoupon = 0;
	var couponNum = 0;
	parentObj.find('.mall-item').each(function(index){
		var isCoupons = $(this).find("#isCoupons").val();//能否使用优惠券（1-是 0-否）
		var is_member_discount = $(this).find("#is_member_discount").val();//能否使用会员折扣（1-是 0-否）
		//var singlePrice = $(this).find('#singlePrice').html();//会员折扣价
		var singlePrice = $(this).find('#detailProPrice').val();//会员折扣价
		var primary_price = $(this).find('#primary_price').val();//商品原价
		var singleNum = $(this).find('#singleNum').html();//商品数量
		var groupType = $(this).attr("groupType");
		if(groupType != 7){
			if(isCoupons == 1 && is_member_discount == 1){
				var proPrice = singlePrice*singleNum;
				sumCoupon = Math.round((sumCoupon+proPrice)*100)/100;
				++couponNum;
			}else if(isCoupons == 1 && is_member_discount == 0){
				var proPrice = primary_price*singleNum;
				sumCoupon = Math.round((sumCoupon+proPrice)*100)/100;
				++couponNum;
			}else if(isCoupons == 0 && is_member_discount == 0){
				
			}
		}else if($(this).find(".totalOrderMoneys").length > 0){
			var proPrice = $(this).find(".totalOrderMoneys").val()*1;
			sumCoupon = Math.round((sumCoupon+proPrice)*100)/100;
			++couponNum;
		}
		
	});
	var yhj = $("span#yhj").html()*1;
	var spread = (sumMoney-sumCoupon).toFixed(2);//总价减去有优惠的商品总价
	if(sumCoupon > 0){
		var money = 0;
		var discount = 0;
		if(type == "CASH" || type == 1){//满减券
			var cashLeastCost = $(obj).find("#cashLeastCost").val()*1;
			money = $(obj).find("#cash").val()*1;
			var num = $(obj).find("#couponNum").html();
			if(type == 1 && num != "" && num != undefined){
				money = money*(num*1);
			}
			var moneyAll = $("span#money").html()*1;
			var sum_money = moneyAll - money;
			if(sum_money > 0){
				sum_money = Math.round(sum_money*100)/100;
			}
			if(sumCoupon < cashLeastCost){
				//alert("您购买的商品总额不满足条件。");
			}else if(sum_money < 0){
				sum_money = 0;
				var explain = $(obj).find("#couponExplain").html();
				
				$('#sum-money').html(sum_money*1);
				$("#proMoneyAll").val(sum_money);
				$("#orderMoney").val($('#sum-money').html());
				result = numSub(money,2);
			}else{
				var explain = $(obj).find("#couponExplain").html();
				$('#sum-money').html(sum_money);
				$("#proMoneyAll").val(sum_money);
				$("#orderMoney").val($('#sum-money').html());
				result = numSub(money,2);
			}
		}else{//折扣券
			var explain = $(obj).find("#couponExplain").html();
			discount = $(obj).find("#discount").val();//折扣数
			var prices = ((sumCoupon*discount)/10);//商品使用折扣券优惠的价钱
			var youhui = (sumCoupon-prices);
			youhui = Math.round(youhui*100)/100;
			var m =numSub((spread*1+youhui),2);
			//parentObj.find("#proPrice").val(((sumCoupon*discount)/10));
			/*console.log("折扣券优惠了："+$('#money').html()*1+"-----"+youhui+"----"+yhj);*/
			var moneyAll = $('#money').html()*1-youhui-yhj;
			moneyAll = Math.round(moneyAll*100)/100;
			$('#sum-money').html(moneyAll);
			$("#proMoneyAll").val(moneyAll);
			$("#orderMoney").val(moneyAll);
			result = Math.round(youhui*100)/100;
//			console.log("haha:"+$("#money").html()*1+"----"+m+"***********"+youhui+"---"+result)
		}
		$(obj).parents(".couponDivs").find("#yhqSumMoney").val(sumCoupon);
		
		var shopId = $(obj).parents("#couponDiv").find('.shopId').val();
		var couponObj = {};
		if(type == "CASH" || type == "DISCOUNT"){
			couponObj = {
				"couponCode":code,"fullCoupon":money,"discountCoupon":discount,"shopId":shopId,"proDisAll":sumCoupon,
				"youhui":result,"couponType":"1"
			};
		}else{
			couponObj = {
				"couponCode":code,"fullCoupon":money,"discountCoupon":discount,"shopId":shopId,"proDisAll":sumCoupon,
				"youhui":result,"addUser":$(obj).find("#addUser").val(),"cId":$(obj).find("#cId").val(),"couponType":"2",
				"gId":$(obj).find("#gId").val(),"num":$(obj).find("#couponNum").html(),"cardType":type
			};
		}
		var coupon= {};
		
		var cArr = $("#couponList").val();
		if(cArr != null && cArr != ""){
			coupon = JSON.parse(cArr);
		}
		coupon[shopId] = couponObj;
		$("#couponList").val(JSON.stringify(coupon));
	}
	/*console.log("--------"+$("#proMoneyAll").val());*/
	return result;
}

function pages(pageId){
	if(pageId != null && pageId != "" && (typeof pageId) != "undefined"){
		window.location.href = "mallPage/"+pageId+"/79B4DE7C/pageIndex.do";
	}
}

function backUrl(){
	var url = $("input.url").val();
	if(url != null && url != "" && (typeof url) != "undefined"){
		window.location.href = url;
	}
}
