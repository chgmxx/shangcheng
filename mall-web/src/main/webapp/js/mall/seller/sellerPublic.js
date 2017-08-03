/**
 * 搜索
 */
function searchUrl(){
	var keyWord = $(".keyWord").val();
	var url = window.location.pathname;
	if(keyWord != null && keyWord != ""){
		if(url.split("?").length == 1){
			url += "?";
		}else{
			url += "&";
		}
		url += "keyWord="+keyWord;
	}
	if($("#startTime").length > 0){
		var startTime = $("#startTime").val();
		if(startTime != null && startTime != ""){
			if(url.split("?").length == 1){
				url += "?";
			}else{
				url += "&";
			}
			url += "startTime="+startTime;
		}
	}
	if($("#endTime").length > 0){
		var endTime = $("#endTime").val();
		if(endTime != null && endTime != ""){
			if(url.split("?").length == 1){
				url += "?";
			}else{
				url += "&";
			}
			url += "endTime="+endTime;
		}
	}
	location.href = url;
}
function pressSearch(e){
	 var e=e||window.event;
     if (e.keyCode == 13) {
    	 searchUrl();
     } 
}

/**
 * 审核销售员
 * sellerId  销售员id
 * status  1通过  -1不通过
 */
function checkSeller(sellerId,status){
	var msg = "审核通过";
	if(status == -1){
		 msg = "审核不通过";
	}
	parent.layer.confirm("您是否要"+msg+"此销售员", {
		  btn: ['确定','取消'], //按钮
		  offset : "30%"
	}, function(){
		var data = {
			"id":sellerId,
			"checkStatus" : status
		}
		data = {seller : JSON.stringify(data)};
		editSeller(data,msg);
	});
}
/**
 * 启用禁用销售员
 * @param sellerId
 * @param status
 */
function sellerStart(sellerId,status){
	var msg = "启用";
	if(status == -1){
		 msg = "暂停";
	}
	parent.layer.confirm("您是否要"+msg+"此销售员", {
		  btn: ['确定','取消'], //按钮
		  offset : "30%"
	}, function(){
		var data = {
			"id":sellerId,
			"isStartUse" : status
		}
		data = {seller : JSON.stringify(data)};
		editSeller(data,msg);
	});
}

/**
 * 批量审核销售员
 */
function batchCheck(status){
	var id = [];
	var flag = false
	var msg = "审核通过";
	if(status == -1){
		 msg = "审核不通过";
	}
	$(".check").each(function() {
		var state = $(this).attr("status");
		if ($(this).is(":checked")) {
			flag = true;
		}
		if ($(this).is(":checked") && (state == "0")) {
			id.push($(this).val());
		}
	});
	if (id.length == 0 && !flag) {
		parent.layer.alert("请选择需要"+msg+"的销售员", {
			offset : "30%",
			closeBtn : 0
		}, function(index) {
			parent.layer.closeAll();
		});
	}else{
		parent.layer.confirm("您是否要"+msg+"选中的销售员", {
			btn : [ '确定', '取消' ],
			offset : '100px'
		}, function() {
			parent.layer.closeAll();
			var data = {
				"ids" : JSON.stringify(id),
				"checkStatus" : status
			};
			editSeller(data, msg);
		});

	}
}

function editSeller(data,msg){
	// loading层
	var layerLoad = parent.layer.load(1, {
		offset : "30%",
		shade : [ 0.1, '#fff' ]
	// 0.1透明度的白色背景
	});
	$.ajax({
		type : "post",
		url : "mallSellers/checkSeller.do",
		data : data,
		dataType : "json",
		success : function(data) {
			parent.layer.closeAll();
			if (data.flag ) {
				var tip = parent.layer.alert(msg+"成功", {
					offset : "30%",
					closeBtn : 0
				}, function(index) {
					parent.layer.close(tip);
					location.href = window.location.href;
				});
			} else {// 编辑失败
				parent.layer.alert(msg+"失败，请稍后重试", {
					offset : "30%"
				});
			}

		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			parent.layer.closeAll();
			parent.layer.alert(msg+"失败，请稍后重试", {
				offset : "30%"
			});
			return;
		}
	});
}



/*全选 反选*/
var _array = $("input.check");
function showMore(obj) {
	if ($(obj).is(":checked")) {
		for (var i = 0; i < _array.length; i++) {
			_array[i].checked = true;
		}
	} else {
		for (var i = 0; i < _array.length; i++) {
			_array[i].checked = false;
		}
	}
}
$(".check").click(function(){
	if(!$(this).is(":checked")){
		$(".checkall").removeAttr("checked");
	}else{
		var flag = true;
		$(".check").each(function(){
			if(!$(this).is(":checked")){
				flag = false;
			}
		});
		if(flag){
			$(".checkall").attr("checked","checked");
		}
	}
});
//全选
$(".js-batch-checkAll").click(function(){
	$(".checkall").attr("checked","checked");
	showMore($(".checkall"));
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