<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    /* String path=application.getRealPath(request.getRequestURI());   */
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" +
            request.getServerPort() +
            request.getContextPath() +
            request.getServletPath().substring( 0, request.getServletPath().lastIndexOf( "/" ) + 1 );
/* String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/"; */
%>

<!DOCTYPE html>
<html ng-app="app">
<style>
    .black_overlay {
        display: none;
        position: absolute;
        width: 100%;
        height: 100%;
        background-color: black;
        z-index: 1001;
        -moz-opacity: 0.4;
        opacity: .40;
        filter: alpha(opacity=40);
        Opacity: 0.4;
        left: 0px;
        top: 0px;
    }

</style>
<head>
    <base href="<%=basePath%>">
    <meta charset="UTF-8">
    <title></title>
    <link rel="stylesheet" href="css/base.css?<%=System.currentTimeMillis()%>"/>
    <script src="http://libs.baidu.com/jquery/1.9.0/jquery.js?"></script>
    <script src="js/angular.min.js"></script>
    <script src="js/app.js?<%=System.currentTimeMillis()%>"></script>

    <link rel="stylesheet" href="js/admin/admin.css?<%=System.currentTimeMillis()%>"/>
    <script src="js/admin/admin.js?<%=System.currentTimeMillis()%>"></script>

    <!--商品-->
    <link rel="stylesheet" href="js/commodity/commodity.css?<%=System.currentTimeMillis()%>"/>
    <script src="js/commodity/commodity.js?<%=System.currentTimeMillis()%>"></script>

    <!--轮播图-->
    <link rel="stylesheet" href="js/swiper/swiper.css"/>
    <script src="js/swiper/swiper.js"></script>

    <!--分类-->
    <link rel="stylesheet" href="js/classify/classify.css?<%=System.currentTimeMillis()%>"/>
    <script src="js/classify/classify.js?<%=System.currentTimeMillis()%>"></script>

    <!--间隔-->
    <link rel="stylesheet" href="js/interval/interval.css?<%=System.currentTimeMillis()%>"/>
    <script src="js/interval/interval.js?<%=System.currentTimeMillis()%>"></script>

    <!--头部-->
    <link rel="stylesheet" href="js/header/header.css?<%=System.currentTimeMillis()%>"/>
    <script src="js/header/header.js?<%=System.currentTimeMillis()%>"></script>

    <!--弹框-->
    <link rel="stylesheet" href="js/tank/commodity_tank.css?<%=System.currentTimeMillis()%>"/>

    <script type="text/javascript" src="js/action/myPlanDirective.js?<%=System.currentTimeMillis()%>"></script>

    <!--layer-->
    <script type="text/javascript" src="/js/plugin/layer/layer.js"></script>

    <script type="text/javascript" src="js/action/Sortable.js"></script>

    <!--搜索框-->
    <link rel="stylesheet" href="js/search/search.css?<%=System.currentTimeMillis()%>"/>
    <link rel="stylesheet" href="js/action/farbtastic.css?<%=System.currentTimeMillis()%>"/>

    <!--预售-->
    <link rel="stylesheet" href="js/reservation/reservation.css?<%=System.currentTimeMillis()%>"/>
    <script src="js/reservation/reservation.js"></script>

    <script src="js/action/farbtastic.js?<%=System.currentTimeMillis()%>"></script>
    <script src="js/search/search.js?<%=System.currentTimeMillis()%>"></script>

    <script src="js/action/countdown.js?<%=System.currentTimeMillis()%>"></script>

</head>
<body style="overflow-y:scroll;">
<jsp:include page="/jsp/common/headerCommon.jsp"/>
<admindraggable></admindraggable>
<!-- 商品隐藏id -->
<input type="hidden" id="stoId" value="${stoId}"/>
<input type="hidden" class="userid" value="${userid }"/>
<div style="height:100px"></div>
<div style="width:100%;margin:0 auto;padding-top:3px;position:fixed;text-align:center;bottom:0;background-color:#ffc;padding:10px;z-index:10001;">
    <a href="javascirpt:void(0)" onclick="save()"
       style="background-color: #1aa1e7; border-radius: 3px; color: #fff; display: inline-block; font-size: 14px; height: 15px; line-height: 15px; text-align: center; width: 70px; cursor: pointer; padding: 8px; margin-right: 20px">保存</a>
    <a href="javascirpt:void(0)" onclick="yl()"
       style="background-color: #1aa1e7; border-radius: 3px; color: #fff; display: inline-block; font-size: 14px; height: 15px; line-height: 15px; text-align: center; width: 70px; cursor: pointer; padding: 8px; margin-right: 20px">预览</a>
    <a href="javascirpt:void(0)" onclick="window.close()"
       style="background-color: #1aa1e7; border-radius: 3px; color: #fff; display: inline-block; font-size: 14px; height: 15px; line-height: 15px; text-align: center; width: 70px; cursor: pointer; padding: 8px">关闭</a>
</div>
<!--修改网站名称弹出层结束-->
<div id="fade" class="black_overlay"></div>
<div id="moveGroupLaye" style="display: none; z-index: 1002; width: 200px; height: 200px; position: fixed; left:50%; top:50%;margin-left:-100px; margin-top:-100px">
    <img src="/images/loading.gif">
</div>
<script>
    //type 1-商品，2-轮播，3-分类
    var dataJson =
    ${dataJson}||
    []; //样式数据
    picJson =
    ${picJson} ||
    [];//图片id数据
    imgList = []; //每次填充图片数据修改此数据
    array = [[], [], [], []],
        lbMap = '${lbMap}';
    stoName = "${stoName}" || "";//店铺名称
    stoPicture = "${stoPicture}" || "";//店铺图片
    countproduct = "${countproduct}" || 0;//全部商品数量
    headImg = "${headImg}" || "";

    var verson = 0;
    if (dataJson.length > 0) {
        dataJson.forEach(function (e, i) {
            if (e.type == 7) {
                verson = 1;
                picJson.splice(i, 0, {type: 7, stoName: stoName, stoPicture: stoPicture, countproduct: countproduct, headImg: headImg})
            }
        })
    }
    if (verson == 0) {
        dataJson.unshift({type: 7, radio: true})
        picJson.unshift({type: 7, stoName: stoName, stoPicture: stoPicture, countproduct: countproduct, headImg: headImg})
    }

    var imgIdList = [];
    var imgIds = ",";
    picJson.forEach(function (e) {
        if (e.type == 1) {
            e.imgID.forEach(function (e) {
                if (imgIds.indexOf("," + e.id + ",") < 0) {
                    imgIds += e.id + ",";
                }
            })
        }
    })
    function picJsonEach(data) {
        picJson.forEach(function (e) {
            if (e.type == 1) {
                e.imgID.forEach(function (e) {
                    data.forEach(function (data) {
                        if (e.id == data.id) {
                            e.price = data.price;
                            e.src = data.src;
                            e.title = data.title;
                            if (data.url != null && data.url != "") {
                                e.url = data.url;
                            }
                        }
                    })
                })
            }
        })
    }


</script>


<script type="text/javascript">
    //弹出遮罩层
    function showFade() {
        document.getElementById('fade').style.display = 'block';
        var bgdiv = document.getElementById('fade');
        bgdiv.style.width = document.body.scrollWidth;
        $('#fade').height($("body").height());
    }
    //弹出弹出层
    function showAll() {
        showFade();
        $("#moveGroupLaye").show();
    }
    //去掉遮蔽层和加载页面
    function closeWindow() {
        document.getElementById('moveGroupLaye').style.display = 'none';
        document.getElementById('fade').style.display = 'none';
    }

    function center(obj) {
        var screenWidth = $(window).width(), screenHeight = window.screen.height; //当前浏览器窗口的 宽和电脑屏幕的高度高
        var scrolltop = $(document).scrollTop();//获取当前窗口距离页面顶部高度
        var objLeft = (screenWidth - obj.width()) / 2;
        var objTop = (screenHeight - obj.height()) / 2 + scrolltop;
        obj.css({
            left: objLeft + 'px',
            top: objTop + 'px',
            'display': 'block'
        });
    }
    /**
     *选择商品
     */
    function choosePro(type) {
        if (type == 2) {//预售商品
            openIframe("选择预售商品", "600px", "480px", "/mallPage/choosePresalePro.do?stoId=" + '${shopid}' + "&check=0");//check==0代表多选，check==1代表单选
        } else {
            openIframe("选择商品", "600px", "480px", "/mallPage/choosePro.do?stoId=" + '${shopid}' + "&check=0");//check==0代表多选，check==1代表单选
        }
        /* var json = {
         "id":1,
         "title":"大范甘迪",
         "src":"fsdgfds",
         "href":"fsdfsd",
         "price" : "232",
         "desc" : "gdgdgdfg5436456"
         };
         imgList.push(json); */
    };


    /**打开素材库**/
    var pic_type = 0;  //0表示重新选择图片,  1表示第一次添加图片,  2表示添加分类图片
    function materiallayer(param) {
        pic_type = param;

        openIframe('素材库','820px','500px',crossDomainUrl+'/common/material.do?retUrl=' + window.location.href);


    }

    /**素材库里面返回信息**/
    function image(id,url){
        //alert(url);
        imgList = url;
        if (pic_type == 0) {
            $("#addswiperpic").click();
        } else if (pic_type == 1) {
            $("#addswiperpic_last").click();
        } else if (pic_type == 2) {
            $(".not-empty.current").css("background-color", "#ccc").find("img").attr("src", imgList);
            $(".not-empty.current span").remove();
            $("#saveclassify").click();
            $(".not-empty.current").click();
        }

        layer.closeAll();
    }

    /**轮播模块下拉类型点击事件**/
    var image_num = 0;
    var matter_one_type = 0;  //1代表轮播图里面的触发，2表示在分类页的触发触发
    function lbTypeClick(obj, param) {
        image_num = $(obj).parents("li").index();
        if (param) {
            matter_one_type = param;
        }
        var type = $(obj).attr("type");//type 为1 代表商品
        if (type == 1) {
            openIframe("选择商品", "600px", "480px", "/mallPage/choosePro.do?stoId=" + '${shopid}' + "&check=1");//check==0代表多选，check==1代表单选
        }
        //type 为2 代表分类
        if (type == 2) {
            openIframe("选择分类页", "600px", "480px", "/mallPage/branchPage.do?stoId=" + '${shopid}');//check==0代表多选，check==1代表单选
        }
    }
    //分类页面，选择返回相对于的数据
    function returnBranch(obj, style) {
        if (matter_one_type == 1) {
            imgList = obj;
            $("#addcommoditypicone").click();
        } else if (matter_one_type == 2) {
            $(".not-empty.current").css("background-color", "#ccc").find("a").attr({"href": obj[0].url, "data-name": obj[0].title});
            $(".not-empty.current span").remove();
            $("#saveclassify").click();
            $(".not-empty.current").click();
        }

        matter_one_type = 0;
    }
    /**
     *选择商品 回调 check==0 daibiao
     */
    function returnProVal(obj, type, check) {
        if (type == 1) {
            imgList = obj;

            if (matter_one_type == 2) {
                $(".not-empty.current").css("background-color", "#ccc").find("a").attr({"href": obj[0].url, "data-name": obj[0].title});
                $(".not-empty.current span").remove();
                $("#saveclassify").click();
                $(".not-empty.current").click();
                matter_one_type = 0;
                return false;
            }
            if (check == 0) {
                $("#addcommoditypic").click();
            } else {
                $("#addcommoditypicone").click();
            }

        } else if (type == 6) {

            imgList = obj;

            if (matter_one_type == 2) {
                $(".not-empty.current").css("background-color", "#ccc").find("a").attr({"href": obj[0].url, "data-name": obj[0].title});
                $(".not-empty.current span").remove();
                $("#saveclassify").click();
                $(".not-empty.current").click();
                matter_one_type = 0;
                return false;
            }
            if (check == 0) {
                $("#addcommoditypic").click();
            } else {
                $("#addcommoditypicone").click();
            }
        }
    }


    /**打开一个IFRAME窗口**/
    function openIframe(title, width, height, content) {
        layer.open({
            type: 2,
            title: title,
            shadeClose: true,
            shade: 0.3,
            offset: "10%",
            shadeClose: false,
            area: [width, height],
            content: content
        });
    }
    //数据保存或修改

    function save() {
        var id = $("#stoId").val();
        var des = angular.toJson(dataJson);
        var pic = angular.toJson(picJson);
        showAll();
        $.ajax({
            type: "post",
            url: "/mallPage/savepage.do",
            data: {
                pagCss: des,
                pagData: pic,
                id: id
            },
            async: true,
            dataType: "json",
            success: function (data) {
                closeWindow();
                var error = data.error;
                alert(data.message);

            }
        });
    }

    //素材库回调取消方法
    function go_back() {
        layer.closeAll();
    }

    function yl() {
        var id = $("#stoId").val();
        var des = angular.toJson(dataJson);
        var pic = angular.toJson(picJson);
        showAll();
        $.ajax({
            type: "post",
            url: "/mallPage/savepage.do",
            data: {
                pagCss: des,
                pagData: pic,
                id: id
            },
            async: true,
            dataType: "json",
            success: function (data) {
                closeWindow();
                var error = data.error;
                if (error == 0) {
                    layer.open({
                        type: 2,
                        title: '微商城预览',
                        shadeClose: true,
                        shade: 0.2,
                        area: ['400px', '400px'],
                        offset: "10px",
                        content: '/mallPage/ylpage.do?id=' + id,
                    })
                } else {
                    alert(data.message);
                }
            }
        });

    }

</script>
</body>
</html>
