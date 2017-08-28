function toLogin(data, keys) {
    var userid = $("input.userid").val();
    /*location.href = "/phoneLoginController/"+userid+"/79B4DE7C/phonelogin.do?returnKey="+returnKey;*/
    var urls = window.location.href;
    urls = urls.substr(urls.indexOf(window.location.pathname) + 1, urls.length);
    if (urls.indexOf("/") > 0) {
        urls = "/" + urls;
    }
    var datas = {
        uId: userid,
        urls: urls
    };
    if (datas != null && datas != "" && typeof(datas) != "undefined") {
        datas["datas"] = data;
    }
    if (keys != null && keys != "" && typeof(keys) != "undefined") {
        datas["keys"] = keys;
    }
    $.ajax({
        url: "/mMember/79B4DE7C/isLogin.do",
        type: "POST",
        data: datas,
        async: false,
        timeout: 300000,
        dataType: "json",
        success: function (data) {
            if (data.returnUrl != null) {
                //location.href = "/phoneLoginController/" + userid + "/79B4DE7C/phonelogin.do?returnKey=" + data.returnUrl;
                location.href = data.returnUrl;
            } else if (data.isWx != null) {
                location.href = window.location.href;
            }
        }, error: function () {
        }
    });
}

function toLoginOnOrder() {
    var userid = $("input.userid").val();
    location.href = "/phoneLoginController/" + userid + "/79B4DE7C/phonelogin.do?returnKey=mall:uc_tologin";
}


var saleMemberId = $("input.footerSaleMemberId").val();
var memberId = $("input.memberId").val();
var userid = $("input.userid").val();
var shopid = $("input.shopid").val();
//跳转到首页
function pageclick(obj) {
    if (saleMemberId == null || saleMemberId == "") {
        if (obj == '' || obj == null || obj == undefined || obj == 0 || obj == '0') {
            alert("店面未设置微商城主页面或者微商城主页面已删除");
        } else {
            window.location.href = "/mallPage/" + obj + "/79B4DE7C/pageIndex.do";
        }
    } else {
        window.location.href = "/phoneSellers/" + saleMemberId + "/79B4DE7C/mallIndex.do";
    }
}
//分类页面跳转
function shoppingall() {
    if (saleMemberId == null || saleMemberId == "") {
        window.location.href = "/mallPage/" + shopid + "/79B4DE7C/shoppingall.do?uId=" + userid;
    } else {
        window.location.href = "/phoneSellers/" + saleMemberId + "/79B4DE7C/shoppingall.do?uId=" + userid;
    }
}
//购物车跳转
function shopCart() {
//	if(memberId != null && memberId != ""){
    if (saleMemberId == null || saleMemberId == "") {
        window.location.href = "/mallPage/79B4DE7C/shoppingcare.do?member_id=" + memberId + "&uId=" + userid;
    } else {
        window.location.href = "/mallPage/79B4DE7C/shoppingcare.do?member_id=" + memberId + "&saleMemberId=" + saleMemberId + "&uId=" + userid;
    }
//	}else{
//		toLogin();
//	}
}
//进入我的页面
function toUsers() {
    var memberId = $("input.memberId").val();
    if (userid != null && userid != "") {
        location.href = "/mMember/79B4DE7C/toUser.do?uId=" + userid;
        /* var url = window.location.href;
         url = url.substr(url.indexOf("/mallPage"),url.length);
         location.href = "/phoneLoginController/"+userid+"/79B4DE7C/phonelogin.do?returnUrl="+url; */
    } else {
        location.href = "/mMember/79B4DE7C/toUser.do?member_id=" + memberId + "&uId=" + userid;
    }
}