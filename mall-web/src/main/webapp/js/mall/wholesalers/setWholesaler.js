$(function(){
	$("#returnBtn").click(function(){
		location.href = "/mallWholesalers/start.do";
	});
	
	$("#condition").click(function(){
		if(!$("input[name='condition']").is(":checked")){
			$("#money").val("");
		}
	});
	
	$("#conditionNum").click(function(){
		if(!$("input[name='conditionNum']").is(":checked")){
			$("#num").val("");
		}
	});
	
	$("#shoupi").click(function(){
		if(!$("input[name='shoupi']").is(":checked")){
			$("#shou").val("");
		}
	});
	
	$("#blue-btn").click(function(){
		var condition = $("input[name='condition']").is(":checked");
		var conditionNum = $("input[name='conditionNum']").is(":checked");
		var shoupi = $("input[name='shoupi']").is(":checked");
		var numTest = /^[0-9]{1,6}$/;
		var isHpMoney = 0;
      	var isHpNum = 0;
      	var isSpHand = 0;
		if(!condition && !conditionNum && !shoupi){
      		promMsg("混批和手批条件中至少选择一种");
      		return false;
      	}
      	if(condition){
      		var money = $("#money").val();
      		if(money <= 0 || money == ""){
      			promMsg("请填写混批条件一次性购买商品金额需要达多少元且大于0");
          		return false;
          	}else if (!numTest.test(money)) {
				data = [];
				alert("请重新输入大于0的六位数字")
				return false;
			}
      		isHpMoney = $("input[name='condition']").val();
      	}
      	if(conditionNum){
      		var num = $("#num").val();
      		if(num < 1 || num == ""){
      			promMsg("请填写混批条件一次性购买数量需要多少件且大于0");
          		return false;
          	}else if (!numTest.test(num)) {
				data = [];
				alert("请重新输入大于0的六位数字")
				return false;
			}
      		isHpNum = $("input[name='conditionNum']").val();
      	}
      	if(shoupi){
      		isSpHand = $("input[name='shoupi']").val();
      		var shou = $("#shou").val();
      		if(shou < 1 || shou == ""){
      			promMsg("请填写手批条件一次性购买商品需要达多少手且大于0");
          		return false;
          	}else if (!numTest.test(shou)) {
				data = [];
				alert("请重新输入大于0的六位数字")
				return false;
			}
      	}
      	/*if(!condition && !conditionNum){
      		promMsg("混批条件中至少选择一种");
      		return false;
      	}
      	if(!shoupi){
      		promMsg("手批条件为必勾选项");
      		return false;
      	}else{
      		isSpHand = $("input[name='shoupi']").val();
      	}
      	if(condition){
      		var money = $("#money").val();
      		if(money <= 0 || money == ""){
      			promMsg("请填写混批条件一次性购买商品金额需要达多少元且大于0");
          		return false;
          	}else if (!numTest.test(money)) {
				data = [];
				alert("请重新输入大于0的六位数字")
				return false;
			}
      		isHpMoney = $("input[name='condition']").val();
      	}
      	if(conditionNum){
      		var num = $("#num").val();
      		if(num < 1 || num == ""){
      			promMsg("请填写混批条件一次性购买数量需要多少件且大于0");
          		return false;
          	}else if (!numTest.test(num)) {
				data = [];
				alert("请重新输入大于0的六位数字")
				return false;
			}
      		isHpNum = $("input[name='conditionNum']").val();
      	}
      	if(shoupi){
      		var shou = $("#shou").val();
      		if(shou < 1 || shou == ""){
      			promMsg("请填写手批条件一次性购买商品需要达多少手且大于0");
          		return false;
          	}else if (!numTest.test(shou)) {
				data = [];
				alert("请重新输入大于0的六位数字")
				return false;
			}
      	}*/
      	
      	/*var params = $(".contentWarp").serializeArray();*/
      	var obj = {
      		"isHpMoney" : isHpMoney,
      		"hpMoney" : $("#money").val(),
      		"hpNum" : $("#num").val(),
      		"isHpNum" : isHpNum,
      		"isSpHand" : isSpHand,
      		"spHand" : $("#shou").val()
      	};
      	$("#blue-btn").attr("disabled",true);//禁止再次点击保存按钮
      	$.ajax({
			url : "/mallWholesalers/updateSetWholesaler.do",
			type: "post",	
			data: {pfSet :JSON.stringify(obj),pfRemark : $("#pfRemark").val(),
				 pfApplyRemark:$("#pfApplyRemark").val()},
	   		dataType : "json",
	   		success : function(data) {
	   			/*console.log(data);*/
				if(data.result == "1" ){
					parent.layer.alert("保存成功" , {
						offset : "30%",
						end: function () {
			               /* location.reload();*///刷新本页面
			                //parent.location.href = "/mallWholesalers/start.do";
							location.href = window.location.href;
			            }
					});
				}else{
					$("#blue-btn").attr("disabled",false);
					promMsg("保存失败");
				}
			}
		});
	});
	
});

function isNum(num) {
	var result = false;
	if(/^\d+$/.test(num)){
		result = true;
	}
	return result;
}

function isCheck(obj){
	if($(obj).is(":checked")){
		$(obj).val("1");
	}else{
		$(obj).val("0");
	}
}

function promMsg(content){
	parent.layer.alert( content , {
		offset : "30%"
	});
};