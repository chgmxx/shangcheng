
function newhref(url,meaus_id,logo){
	$(".ahover").css("color","#fff");
	$(".amenushover").attr("src",logo);

	window.location.href = "/"+url;
}
//微信公众号还未绑定，跳转页面
function a(){
	layer.alert("还未绑定微信公众号，请先绑定微信公众号",{
		offset: "30%",
	    closeBtn: 0
	},function(){
		parent.layer.alert("绑定公众前，请先确认公众号没有授权给其他第三方平台，否则会影响正常的业务处理",{
			offset: "30%",
		    closeBtn: 0
		},function(index){
			parent.layer.close(index);
			window.location.href = "/wx/api.do";
		});
	});
}


$(function() {
	//鼠标悬浮在li上的效果(菜单栏的效果，后面改图片，需要改这里)
	$('.candan').mouseover(function(){
		var menus_id = $(this).find("input").val();
		var a_href=$(this).find("a");
		a_href.css("color","#1aa1e7");
		var src = $(this).find("img");
		var menus_logo = $(this).find(".menus_describe").val();
		
		src.attr("src",menus_logo);
	
	});
	//鼠标离开在li上的效果
	$('.candan').mouseout(function(){
		var menus_id = $(this).find(".menus_id").val();
		var a_href=$(this).find("a");
		a_href.css("color","#fff");
		var src = $(this).find("img");
		var menus_logo = $(this).find(".menus_logo").val();
		src.attr("src",menus_logo);
	});
});


$(function(){
	$('.full-radio').click(function(){
		$(".ke-edit-iframe").css('minHeight','400px');//隐藏编辑框下拉div
	});	
	$("#loginout").click(function(){
		window.location.href = "/user/loginOut.do";//安全退出
	});
	
});


//获取评估打分的分数
function straAssess(){
	var success = $(".authenticationSuccess").length;
	var defeat = $(".authenticationDefeat").length;
	var rate = parseInt(success/(success + defeat)*100);
	window.frames['arcPlan'].arcPlan('myCanvas',rate);
	var success_info = $(".authenticationSuccess_info").length;
	var fail_info = $(".authenticationDefeat_info").length;
	var rate_info = parseInt(success_info/(success_info + fail_info)*100);
	window.frames['arcPlan1'].arcPlan('myCanvas',rate_info);
}


//超过一定高度时， 显示返回顶部按钮
$(window).scroll(function() {
    var scrollTop =  document.body.scrollTop || document.documentElement.scrollTop
    if (scrollTop > 200) {
    	$('#gotop').show();
    }
    else {
    	$('#gotop').hide();
    }
});

//不许超出长度
function getByteLen(obj,maxNum) {
	var len = 0;
	var val = obj.value;
	var str = ""
	for (var i = 0; i < val.length; i++) {
		var a = val.charAt(i);
		if (a.match(/[^\x00-\xff]/ig) != null)len += 2;
		else len += 1;
		str += a;
		if(len>=maxNum)break;
	}
	obj.value = str;
}

//右侧功能按钮
$(function(){	
   //getHeight();
	$('#gotop').hide();
	//返回顶部按钮
	$('#gotop a').click(function(){
		$(document).scrollTop(0);
        document.documentElement.scrollTop = 0;
	});
	//二维码显示消失功能
	$('#code1').hover(function(){
		$('.code').css('background','url(../../../images/icons.jpg) no-repeat center center');
		$('.code').show();
	},function(){
		$('.code').hide();
	});
	//联系我们
	$('#code2').hover(function(){
		$('.code2').show();
	    },function(){
		$('.code2').hide();
	});
	});

$(function(){
$("#main",window.parent.document).load(function(){
		$(this).height($("body")[0].scrollHeight+50);
   });
});
	

/**
 * 点群发按钮跳转至推广群发消息
 */
function bulk() {
	top.location.href="/mass/indexstart.do";
}	

/**
 * 修改iframe src
 * @param url
 */	
function setIframeSrc(url){
	$(window.parent.document).find("#main").attr("src",url);
	
}

function windowAdjust(){
	var winH = $(window).height();
	var conH = $("#container").height();
	if(winH<conH){
		$(window).scrollTop(conH-winH);
	}
}

/**选择行业**/
function industry(){
	var industryid = $("#industry").val();
	if(industryid == "" || industryid ==0){
		alert("请选择所属行业！");
	}else{
		var r = confirm("所属行业一旦选定将不允许更改，您确认要保存吗？");
		if(r){
			$.post("/user/saveIndustry.do",{industryid : industryid},function(data){
				if(data.result){
					alert("保存成功！请重新登录系统。");
					location.href="/user/loginOut.do";
				}else{
					alert(data.message);
				}
			},"json");
		}
	}
}