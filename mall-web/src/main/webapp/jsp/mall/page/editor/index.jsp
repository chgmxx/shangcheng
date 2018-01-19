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
    <link rel="stylesheet" href="css/model.css?<%=System.currentTimeMillis()%>"/>
    <link rel="stylesheet" href="css/wangEditor.min.css?<%=System.currentTimeMillis()%>"/>

    <script src="https://libs.baidu.com/jquery/1.9.0/jquery.js"></script>

    <link rel="stylesheet" type="text/css" href="css/jquery-ui.css"/>
    <script type="text/javascript" src="js/jquery-ui-1.10.4.custom.min.js"></script>
    <script type="text/javascript" src="js/jquery.ui.datepicker-zh-CN.js"></script>
    <script type="text/javascript" src="js/jquery-ui-timepicker-addon.js"></script>
    <script type="text/javascript" src="js/jquery-ui-timepicker-zh-CN.js"></script>

    <script src="js/angular.min.js"></script>
    <script src="js/app.js?<%=System.currentTimeMillis()%>"></script>

    <link rel="stylesheet" href="js/admin/admin.css?<%=System.currentTimeMillis()%>"/>
    <script src="js/admin/admin.js?<%=System.currentTimeMillis()%>"></script>

    <!--商品-->
    <link rel="stylesheet" href="js/commodity/commodity.css?<%=System.currentTimeMillis()%>"/>
    <script src="js/commodity/commodity.js?<%=System.currentTimeMillis()%>"></script>

    <!--轮播图-->
    <link rel="stylesheet" href="js/swiper/swiper.css?<%=System.currentTimeMillis()%>"/>
    <script src="js/swiper/swiper.js?<%=System.currentTimeMillis()%>"></script>

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
    <script type="text/javascript" src="/js/plugin/layer/layer.js?<%=System.currentTimeMillis()%>"></script>

    <script type="text/javascript" src="js/action/Sortable.js?<%=System.currentTimeMillis()%>"></script>

    <!--搜索框-->
    <link rel="stylesheet" href="js/search/search.css?<%=System.currentTimeMillis()%>"/>
    <link rel="stylesheet" href="js/action/farbtastic.css?<%=System.currentTimeMillis()%>"/>

    <!--预售-->
    <link rel="stylesheet" href="js/reservation/reservation.css?<%=System.currentTimeMillis()%>"/>
    <script src="js/reservation/reservation.js"></script>

    <!--标题-->
    <link rel="stylesheet" href="js/title/title.css?<%=System.currentTimeMillis()%>"/>
    <script src="js/title/title.js"></script>

    <!--文本导航-->
    <link rel="stylesheet" href="js/textNav/textNav.css?<%=System.currentTimeMillis()%>"/>
    <script src="js/textNav/textNav.js"></script>

    <!--图片导航-->
    <link rel="stylesheet" href="js/picNav/picNav.css?<%=System.currentTimeMillis()%>"/>
    <script src="js/picNav/picNav.js"></script>

    <!--橱窗-->
    <link rel="stylesheet" href="js/window/window.css?<%=System.currentTimeMillis()%>"/>
    <script src="js/window/window.js"></script>

    <!--进入店铺-->
    <link rel="stylesheet" href="js/goShop/goShop.css?<%=System.currentTimeMillis()%>"/>
    <script src="js/goShop/goShop.js"></script>

    <!--公告-->
    <link rel="stylesheet" href="js/notice/notice.css?<%=System.currentTimeMillis()%>"/>
    <script src="js/notice/notice.js"></script>

    <!--辅助线-->
    <link rel="stylesheet" href="js/guide/guide.css?<%=System.currentTimeMillis()%>"/>
    <script src="js/guide/guide.js"></script>

    <!--优惠券-->
    <link rel="stylesheet" href="js/coupon/coupon.css?<%=System.currentTimeMillis()%>"/>
    <script src="js/coupon/coupon.js"></script>

    <!--商品分组-->
    <link rel="stylesheet" href="js/grouping/grouping.css?<%=System.currentTimeMillis()%>"/>
    <script src="js/grouping/grouping.js"></script>

    <!--商品列表-->
    <link rel="stylesheet" href="js/commodityList/commodityList.css?<%=System.currentTimeMillis()%>"/>
    <script src="js/commodityList/commodityList.js"></script>

    <!--富文本-->
    <link rel="stylesheet" href="js/richText/richText.css?<%=System.currentTimeMillis()%>"/>
    <script src="js/richText/richText.js"></script>

    <script src="js/action/farbtastic.js?<%=System.currentTimeMillis()%>"></script>
    <script src="js/search/search.js?<%=System.currentTimeMillis()%>"></script>

    <script src="js/action/countdown.js?<%=System.currentTimeMillis()%>"></script>


</head>
<body style="overflow-y:scroll;">
<jsp:include page="element.jsp"></jsp:include>
<admindraggable></admindraggable>
<!-- 商品隐藏id -->
<input type="hidden" id="stoId" value="${stoId}"/>

<%--<div style="width:100%;margin:0 auto;padding-top:3px;position:fixed;text-align:center;bottom:0;background-color:#ffc;padding:10px;z-index:10001;">
    <a href="javascirpt:void(0)" onclick="save()"
       style="background-color: #1aa1e7; border-radius: 3px; color: #fff; display: inline-block; font-size: 14px; height: 15px; line-height: 15px; text-align: center; width: 70px; cursor: pointer; padding: 8px; margin-right: 20px">保存</a>
    <a href="javascirpt:void(0)" onclick="yl()"
       style="background-color: #1aa1e7; border-radius: 3px; color: #fff; display: inline-block; font-size: 14px; height: 15px; line-height: 15px; text-align: center; width: 70px; cursor: pointer; padding: 8px; margin-right: 20px">预览</a>
    <a href="javascirpt:void(0)" onclick="window.close()"
       style="background-color: #1aa1e7; border-radius: 3px; color: #fff; display: inline-block; font-size: 14px; height: 15px; line-height: 15px; text-align: center; width: 70px; cursor: pointer; padding: 8px">关闭</a>
</div>--%>
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
    console.log(dataJson, "-----------", picJson)

    var verson = 0;
    if (dataJson.length > 0) {
        dataJson.forEach(function (e, i) {
            if (e.type == 7) {
                verson = 1;
                //picJson.splice(i, 0, {type: 7, stoName: stoName, stoPicture: stoPicture, countproduct: countproduct, headImg: headImg})
            }
        })
    }
    if (verson == 0) {
        dataJson.unshift({type: 7, radio: true})
        picJson.unshift({type: 7, stoName: stoName, stoPicture: stoPicture, countproduct: countproduct, headImg: headImg})
    }


    $("body").click(function () {
        $(".color-picker").hide();
    });
    $("body").on("click", ".color-picker", function () {
        event.stopPropagation();
    })
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
    /**打开素材库**/
    var pic_type = 0;  //0表示重新选择图片,  1表示第一次添加图片,  2表示添加分类图片
    var paramList = "";
    function materiallayer(param, e, editor) {
        pic_type = param;
        paramList = arguments;
        openLayer('素材库', '820px', '500px', 'https://suc.deeptel.com.cn/common/material.do?retUrl=' + window.location.href);
    }
    function openLayer(title, width, height, content) {
        this.openParentShade();
        layer.open({
            type: 2,
            title: title,
            shade: 0.5,
            offset: "10%",
            shadeClose: false,
            area: [width, height],
            content: content,
            cancel: function () {
                CloseParentShade();
            }
        });
    }


    /**打开一个IFRAME窗口**/
    function openIframe(data) {
        this.openParentShade();
        vm.openDialog(data);
    }
    function openParentShade() {
        parent.parent.window.postMessage("openMask()", "*");
        parent.shadeShow();
    }
    function CloseParentShade() {
        layer.closeAll();
        parent.shadeHide();
        parent.parent.window.postMessage("closeMask()", "*");
    }
    /**素材库里面返回信息**/
    window.addEventListener("message", function (e) {
        if (!e.data)return;
        eval(e.data)
    });
    //素材库回调取消方法
    function go_back() {
        this.CloseParentShade();
    }
    function image(id, url) {
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
        } else if (typeof pic_type == 'function') {
            pic_type(imgList, paramList[2], paramList[1])
        }

        this.CloseParentShade();
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
            openIframe({
                title: "选择商品",
                isCheck: 1, //isCheck==0代表多选，isCheck==1代表单选
                shopId: ${shopid},
                type: 1
            });
            <%--openIframe("选择商品", "600px", "480px", "/mallPage/choosePro.do?stoId=" + '${shopid}' + "&isCheck=1");//isCheck==0代表多选，isCheck==1代表单选--%>
        }
        //type 为2 代表分类
        if (type == 2) {
            openIframe({
                title: "选择分类页",
                isCheck: 1, //isCheck==0代表多选，isCheck==1代表单选
                shopId: ${shopid},
                type: 2
            });
            <%--openIframe("选择分类页", "600px", "480px", "/mallPage/branchPage.do?stoId=" + '${shopid}');//isCheck==0代表多选，isCheck==1代表单选--%>
        }
    }

    /**
     *选择商品
     */
    function choosePro(type) {
        if (type == 2) {//预售商品
            openIframe({
                title: "选择预售商品",
                isCheck: 0, //isCheck==0代表多选，isCheck==1代表单选
                shopId: ${shopid},
                type: 3
            });
            <%--openIframe("选择预售商品", "600px", "480px", "/mallPage/choosePresalePro.do?stoId=" + '${shopid}' + "&isCheck=0");//isCheck==0代表多选，isCheck==1代表单选--%>
        } else {
            openIframe({
                title: "选择商品",
                isCheck: 0, //isCheck==0代表多选，isCheck==1代表单选
                shopId: ${shopid},
                type: 1
            });
            <%--openIframe("选择商品", "600px", "480px", "/mallPage/choosePro.do?stoId=" + '${shopid}' + "&isCheck=0");//isCheck==0代表多选，isCheck==1代表单选--%>
        }
    };

    /**
     *选择分组
     */
    var group_type = 0;
    function chooseGroup(type) {
        group_type = type;
        if (type == 2) {
            openIframe({
                title: "商品分组",
                isCheck: 1, //isCheck==0代表多选，isCheck==1代表单选
                shopId: ${shopid},
                type: 4
            });
            <%--openIframe("商品分组", "600px", "480px", "/mallPage/chooseGroup.do?stoId=" + '${shopid}' + "&isCheck=1");//isCheck==0代表多选，isCheck==1代表单选*/--%>
        } else {
            openIframe({
                title: "商品分组",
                isCheck: 0, //isCheck==0代表多选，isCheck==1代表单选
                shopId: ${shopid},
                type: 4
            });
            <%--openIframe("商品分组", "600px", "480px", "/mallPage/chooseGroup.do?stoId=" + '${shopid}' + "&isCheck=0");//isCheck==0代表多选，isCheck==1代表单选--%>
        }
    };
    /**
     * 分组返回数据
     */
    function returnGroupArr(jsonArry) {
        imgList = jsonArry;

        if (group_type == 2) {
            $("#editchooseGroup").click();
        } else {
            $("#addchooseGroup").click();
        }
        console.log(jsonArry, jsonArry);
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
     *选择商品 回调 isCheck==0 daibiao
     */
    function returnProVal(obj, type, check) {
        console.log(obj, "obj");
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

        } else if (type == 3) {
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


    //数据保存或修改

    function save() {
        var id = $("#stoId").val();
        var des = angular.toJson(dataJson);
        var pic = angular.toJson(picJson);
        console.log(des, "----", pic)
//        showAll();
        $.ajax({
            type: "post",
            url: "/mallPage/savepage.do",
            data: {
                pagCss: des,
                pagData: pic,
                id: id
            },
            async: true,
//            dataType: "json",
            success: function (data) {
                closeWindow();
                console.log(data, "=====")
//                var error = data.error;
//                alert(data.message);

                alert("保存成功");

            }, error: function (data) {
                console.log(data, "dataerror")
            }
        });
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
