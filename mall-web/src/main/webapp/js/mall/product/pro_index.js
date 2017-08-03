var parentLayer = window.parent.layer;
/**
 * 删除
 */
$(".delete").click(function() {
	var id = [];
	id.push($(this).attr("id"));
	var data = {
		ids : JSON.stringify(id),
		isDelete : 1
	};
	// 询问框
	parent.layer.confirm('您确定要删除此产品？', {
		btn : [ '确定', '取消' ],
		offset : '100px'
	}, function() {
		parentLayer.closeAll();
		deletePro(data, "删除",1);
	});
});
$(".js-batch-viewNum").click(function(){
	var id = [];
	var flag = false
	$(".checkPro").each(function() {
		var state = $(this).attr("status");
		if ($(this).is(":checked")) {
			flag = true;
			id.push($(this).attr("id"));
		}
		/*if ($(this).is(":checked") && (state == "1")) {
			id.push($(this).attr("id"));
		}*/
	});
	if (id.length == 0 && !flag) {
		parentLayer.alert("请选择需要修改浏览量的商品", {
			offset : "30%",
			closeBtn : 0
		}, function(index) {
			parentLayer.closeAll();
		});
	}else{
		var data = {
			ids : JSON.stringify(id)
		};
		upViewsNum(0,0,data);
	}
});
/**
 * 销量基数
 */
$(".js-batch-sale").click(function(){
	var id = [];
	var flag = false
	$(".checkPro").each(function() {
		var state = $(this).attr("status");
		if ($(this).is(":checked")) {
			flag = true;
			id.push($(this).attr("id"));
		}
		/*if ($(this).is(":checked") && (state == "1")) {
			id.push($(this).attr("id"));
		}*/
	});
	if (id.length == 0 && !flag) {
		parentLayer.alert("请选择需要修改销量基数的商品", {
			offset : "30%",
			closeBtn : 0
		}, function(index) {
			parentLayer.closeAll();
		});
	}else{
		var data = {
			ids : JSON.stringify(id)
		};
		upSaleBase(0,data);
	}
});
/**
 * 单独修改浏览量
 */
$(".viewNum").click(function(){
	var id = [];
	id.push($(this).attr("id"));
	var data = {
		ids : JSON.stringify(id),
	};
	var nums = $(this).attr("num");
	var isView = $(this).attr("isView");
	upViewsNum(nums,isView,data);
});
function upViewsNum(nums,isView,data){
	parentLayer.open({
		area :["400px","200px"],
		offset : '100px',
		title : "修改浏览量",
		type: 1,
		content: $("div.viewNum").html(),
		btn : [ '确定', '取消' ],
		success : function(layero,index){
			if(nums*1 > 0){
				layero.find(".vNums").val(nums);
			}
			if(isView == 1){
				layero.find(".isShowViews").attr("checked","checked");
				layero.find(".viewDiv").show();
			}
			layero.find(".isShowViews").change(function(){
				var checked = $(this).is(":checked");
				if(checked){
					layero.find(".viewDiv").show();
				}else{
					layero.find(".viewDiv").hide();
				}
			});
			
		},
		yes : function(index, layero) {
			var viewsNum = layero.find(".vNums").val();
			var checked = layero.find(".isShowViews").is(":checked");
			var test = /^\d{0,9}$/;
			if(checked){
				if(!test.test(viewsNum)){
					alert("请输入大于0的9位数字");
					return;
				}
				isShowViews = 1;
			}else{
				viewsNum = 0;
				isShowViews = 0;
			}
			data.isShowViews = isShowViews;
			data.viewsNum = viewsNum;
			parentLayer.closeAll();
			deletePro(data, "修改浏览量",0);
		}
	});
}


/**
 * 单独修改浏览量
 */
$(".sales").click(function(){
	var id = [];
	id.push($(this).attr("id"));
	var data = {
		ids : JSON.stringify(id),
	};
	var saleBase = $(this).attr("salesBase");
	upSaleBase(saleBase,data);
});
function upSaleBase(saleBase,data){
	parentLayer.open({
		area :["400px","200px"],
		offset : '100px',
		title : "修改销量基数",
		type: 1,
		content: $("div.saleBase").html(),
		btn : [ '确定', '取消' ],
		success : function(layero,index){
			if(saleBase*1 > 0){
				layero.find("input.saleBase").val(saleBase);
			}
		},
		yes : function(index, layero) {
			var saleBase = layero.find("input.saleBase").val();
			var test = /^\d{0,9}$/;
			if(!test.test(saleBase)){
				alert("请输入大于0的9位数字");
				return;
			}
			data.salesBase = saleBase;
			parentLayer.closeAll();
			deletePro(data, "修改销量基数",0);
		}
	});
}
/**
 * 单独送审
 */
$(".send_valid").click(function(){
	var id = [];
	id.push($(this).attr("id"));
	parentLayer.confirm('送审后商品不允许修改，您确定要送审选中的产品？', {
		btn : [ '确定', '取消' ],
		offset : '100px'
	}, function() {
		parentLayer.closeAll();
		var data = {
			ids : JSON.stringify(id),
			checkStatus : 0
		};
		deletePro(data, "送审",0);
	});
});
/**
 * 批量送审
 */
$(".js-batch-valid").click(function() {
	var id = [];
	var flag = false
	$(".checkPro").each(function() {
		var state = $(this).attr("status");
		if ($(this).is(":checked")) {
			flag = true;
		}
		if ($(this).is(":checked") && (state == "-2" || state == "-1")) {
			id.push($(this).attr("id"));
		}
	});
	if (id.length == 0 && !flag) {
		parentLayer.alert("请选择需要送审的商品", {
			offset : "30%",
			closeBtn : 0
		}, function(index) {
			parentLayer.closeAll();
		});
	}else{
		if (id.length == 0 && flag) {
			parentLayer.alert("未送审的商品才能进行送审操作", {
				offset : "30%",
				closeBtn : 0
			}, function(index) {
				parentLayer.closeAll();
			});
		} else {
			parentLayer.confirm('送审后商品不允许修改，您确定要送审选中的产品？', {
				btn : [ '确定', '取消' ],
				offset : '100px'
			}, function() {
				parentLayer.closeAll();
				var data = {
					ids : JSON.stringify(id),
					checkStatus : 0
				};
				deletePro(data, "批量送审",0);
			});

		}
	}
});
/**
 * 单独上架
 */
$("a.sj").click(function(){
	var id = [];
	id.push($(this).attr("id"));
	parentLayer.confirm('只能上架审核通过的商品，您确定要上架选中的商品？', {
		btn : [ '确定', '取消' ],
		offset : '100px'
	}, function() {
		parentLayer.closeAll();
		var data = {
			ids : JSON.stringify(id),
			isPublish : 1
		};
		deletePro(data, "上架",0);
	});
});
/**
 * 批量上架
 */
$(".js-batch-load").click(function() {
	var id = [];
	var flag = false
	$(".checkPro").each(function() {
		var publish = $(this).attr("publish");
		var status = $(this).attr("status");
		if ($(this).is(":checked")) {
			flag = true;
		}
		if ($(this).is(":checked")&& status == 1) {
			id.push($(this).attr("id"));
		}
	});
	if (id.length == 0 && !flag) {
		parentLayer.alert("请选择需要上架的商品", {
			offset : "30%",
			closeBtn : 0
		}, function(index) {
			parentLayer.closeAll();
		});
	}else{
		if (id.length == 0 && flag) {
			parentLayer.alert("审核通过后的商品才能上架商品", {
				offset : "30%",
				closeBtn : 0
			}, function(index) {
				parentLayer.closeAll();
			});
		} else {
			parentLayer.confirm('只能上架审核通过的商品，您确定要上架选中的商品？', {
				btn : [ '确定', '取消' ],
				offset : '100px'
			}, function() {
				parentLayer.closeAll();
				var data = {
					ids : JSON.stringify(id),
					isPublish : 1
				};
				deletePro(data, "上架",0);
			});

		}
	}

});
/**
 * 单独下架
 */
$("a.xj").click(function(){
	var id = [];
	id.push($(this).attr("id"));
	parentLayer.confirm('只能下架审核通过的商品，您确定要下架选中的产品？', {
		btn : [ '确定', '取消' ],
		offset : '100px'
	}, function() {
		parentLayer.closeAll();
		var data = {
			ids : JSON.stringify(id),
			isPublish : -1
		};
		deletePro(data, "下架",0);
	});
});
/**
 * 批量下架
 */
$(".js-batch-unload").click(function() {
	var id = [];
	var flag = false
	$(".checkPro").each(function() {
		var publish = $(this).attr("publish");
		var status = $(this).attr("status");
		if ($(this).is(":checked")) {
			flag = true;
		}
		if ($(this).is(":checked") && status == 1) {
		
			id.push($(this).attr("id"));
		}
	});
	if (id.length == 0 && !flag) {
		parentLayer.alert("请选择需要下架的商品", {
			offset : "30%",
			closeBtn : 0
		}, function(index) {
			parentLayer.closeAll();
		});
	}else{
		if (id.length == 0 && flag) {
			parentLayer.alert("审核通过后的商品才能下架商品", {
				offset : "30%",
				closeBtn : 0
			}, function(index) {
				parentLayer.closeAll();
			});
		} else {
			parentLayer.confirm('只能下架审核通过的商品，您确定要下架选中的产品？', {
				btn : [ '确定', '取消' ],
				offset : '100px'
			}, function() {
				parentLayer.closeAll();
				var data = {
					ids : JSON.stringify(id),
					isPublish : -1
				};
				deletePro(data, "下架",0);
			});

		}
	}

});

/**
 * 批量删除
 */
$(".js-batch-delete").click(function() {
	var id = [];
	var flag = false
	$(".checkPro").each(function() {
		var publish = $(this).attr("publish");
		if ($(this).is(":checked")) {
			flag = true;
		}
		if ($(this).is(":checked") && (publish != "1")) {
			id.push($(this).attr("id"));
		}
	});
	if (id.length == 0 && !flag) {
		parentLayer.alert("请选择需要删除的商品", {
			offset : "30%",
			closeBtn : 0
		}, function(index) {
			parentLayer.closeAll();
		});
	}else{
		if (id.length == 0 && flag) {
			parentLayer.alert("未上架的商品才能进行删除操作", {
				offset : "30%",
				closeBtn : 0
			}, function(index) {
				parentLayer.closeAll();
			});
		} else {
			parentLayer.confirm('您确定要批量删除选中的产品？', {
				btn : [ '确定', '取消' ],
				offset : '100px'
			}, function() {
				parentLayer.closeAll();
				var data = {
					ids : JSON.stringify(id),
					isDelete : 1
				};
				deletePro(data, "批量删除",1);
			});

		}
	}
	

});

function deletePro(data, tip,index) {
	var layerLoad = parentLayer.load(1, {
		shade : [ 0.3, '#000' ]
	});
	$.ajax({
		type : "post",
		data : data,
		url : "mPro/batchProduct.do",
		dataType : "json",
		success : function(data) {
			layer.close(layerLoad);
			parent.layer.closeAll();
			if (data.code == 0) {// 重新登录
				parent.layer.alert("操作失败，长时间没操作，跳转到登录页面", {
					offset : "30%",
					closeBtn : 0
				}, function(index) {
					location.href = "/user/tologin.do";
				});
			} else if (data.code == 1) {
				var tipLayer = parent.layer.alert(tip + "成功", {
					offset : "30%",
					closeBtn : 0
				}, function(index) {
					parent.layer.close(tipLayer);
					if(index == 1){
						location.href = "/mPro/index.do";
					}else{
						location.href = window.location.href;
					}
				});
			} else {// 编辑失败
				parent.layer.alert(tip + "失败", {
					offset : "30%"
				});
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			parent.layer.closeAll();
			parent.layer.alert(tip + "失败", {
				offset : "30%"
			});
			return;
		}
	});
}
/**
 * 切换要查看的商品类型
 */
$(".proChange").change(function() {
	var type = $(this).val();
	getProduct(type, null);
});
/**
 * 根据名称搜索商品
 */
$(".sbt").click(function() {
	var proName = $("input.srh").val();
	var type = $(".proChange option:selected").val();
	getProduct(type, proName);
});
/**
 * 回车提交
 */
$("input.srh").keyup(function(event) {
	if (event.keyCode == 13) {
		var proName = $("input.srh").val();
		var type = $(".proChange option:selected").val();
		getProduct(type, proName);
	}
});

function getProduct(type, proName) {
	var html = "/mPro/index.do";
	if (type == 1) {// 出售中的商品
		html += "?isPublish=1&checkStatus=1&proType=" + type;
	} else if (type == 2) {// 已售罄商品
		html += "?type=1&proType=" + type;
	} else if (type == 3) {// 仓库中的商品
		html += "?isPublish=-1&proType=" + type;
	} else if (type == 4) {// 待审核的商品
		html += "?checkStatus=-2&proType=" + type;
	} else if (type == 5) {// 审核中的商品
		html += "?checkStatus=0&proType=" + type;
	}
	if (proName != null && proName != "") {
		if (type == 0) {
			html += "?";
		} else {
			html += "&";
		}
		html += "proName=" + proName;
	}
	location.href = html;
}
/**
 * 查看二维码
 * @param id
 */
$(".qrcode").click(function(){
	parent.layer.open({
		  type: 1,
		  title :"商品预览",
		  skin: 'layui-layer-rim', //加上边框
		  area: ['200px', '240px'], //宽高
		  offset: "30%",
		  content: "<img src ='/store/79B4DE7C/getTwoCode.do?url="+$(this).attr("url")+"'/>"
		});
});

$(".toShops").click(function(){
	var id = $(this).attr("id");
	var shopId = $(this).attr("sId");
	parent.layer.open({
		  type: 2,
		  title :"到店购买",
		  skin: 'layui-layer-rim', //加上边框
		  area: ['300px', '300px'], //宽高
		  offset: "30%",
		  bth:["下载二维码"],
		  content: "/mPro/79B4DE7C/codeIframs.do?id="+id+"&shopId="+shopId+""
	});
});
function viewQR(id){
	parent.openIframeNoScoll("店铺二维码","400px","300px","store/viewQR.do?id="+id);
}
/**
 * 同步
 */
$(".sync").click(function(){
	var proId = $(this).attr("id");
	parent.openIframe("同步商品","500px","530px","/mPro/group/getGroups.do?proId="+proId+"&type=1");
});
/**
 * 一键同步
 */
function syncProductByShop(){
	parent.openIframe("一键同步商品","500px","200px","/mPro/group/syncProduct.do");
}

