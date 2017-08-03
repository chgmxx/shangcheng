/*var $container = $(window);
if($("input.curPage").length > 0 && $("input.isLoading").length > 0){
	$container.scroll(function () {
		var curPage = $("input.curPage").val();//当前页
		var pageCount = $("input.pageCount").val();//总共的页数
		var isLoading = $("input.isLoading").val();//是否继续加载
		if(isLoading == 0 || curPage+1 > pageCount){
			$container.unbind('scroll');
			return false;
		}
		var totalHeight = $("body").prop('scrollHeight');
		var scrollTop = $container.scrollTop(); 
		var height = $container.height();
		var TRIGGER_SCROLL_SIZE =  $(".media-item:eq(0)").height()*2;
		if(totalHeight - (height + scrollTop) <= TRIGGER_SCROLL_SIZE && isLoading == 1){
			loadMore();
	    }
	  });
}*/

/**
 * 加载更多
 */
function loadMore(){
	var curPage = $("input.curPage").val();
    if(curPage == null || curPage == ''){
    	return false;
    }
    var datas = {
       	curPage:curPage*1+1,
       	shopId:$(".shopId").val()
    };
	$("input.isLoading").val(-1);
	$(".load-more").hide();
	$.ajax({
		type : "post",
		url : "/phoneIntegral/79B4DE7C/integerProductPage.do",
		data : datas,
		dataType : "json",
		success : function(data) {
			var imageHttp = $("input.imageHttp").val();
			var html = "";
			if(data != null){
				if(data.productList != null && data.productList.length > 0){
					for(var i = 0;i < data.productList.length; i++){
						var product = data.productList[i];
						html += "<div class='media-item'  onclick='toProduct("+product.id+");'>" +
								"<div class='media-imgWrap'><img src='"+product.product_image+"' class='item-img'></div>" +
								"<div class='item-bd'>" +
								"<h4 class='bd-tt'>"+product.pro_name+"</h4>" +
								"<div class='bd-txt'>会员积分兑换</div>" +
								"<div class='bd-txt integral-list-price '>"+product.money+"积分</div>" +
								"</div>" +
								"</div>";
					}
				}
				if( data.curPage*1 <= data.pageCount*1){
					$("input.isLoading").val(0);
				}else{
					$(".load-more").show();
				}
				$("input.curPage").val(data.curPage);
			}
			if(html == ""){
				$("input.isLoading").val(0);
				return false;
			}else{
				$(".product_list").append(html);
				$("input.isLoading").val(1);
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			$("input.isLoading").val(1);
		}
	});
}
/**
 * 进入兑换记录页面
 */
function toRecord(){
	var userid = $(".userid").val();
	var shopId = $(".shopId").val();
	location.href = "/phoneIntegral/79B4DE7C/recordList.do?uId="+userid+"&shopId="+shopId;
}
/**
 * 进入积分明细页面
 */
function integralDetail(){
	var userid = $(".userid").val();
	var shopId = $(".shopId").val();
	location.href = "/phoneIntegral/79B4DE7C/integralDetail.do?uId="+userid+"&shopId="+shopId;
}
function toProduct(proId){
	var userid = $(".userid").val();
	var shopId = $(".shopId").val();
	location.href = "/phoneIntegral/79B4DE7C/integralProduct.do?id="+proId+"&uId="+userid+"&shopId="+shopId;
}
function returnUrls(type,obj){
	if(type == 1){
		var return_url = $(obj).find(".return_url").val();
		if(return_url != null && return_url != "" && typeof(return_url) != "undefined"){
			location.href = return_url;
		}
	}
}