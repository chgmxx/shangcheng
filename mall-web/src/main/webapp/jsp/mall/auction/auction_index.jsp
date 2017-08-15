<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">
<head>
    <title>拍卖管理</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <%
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    %>
    <base href="<%=basePath%>"/>
    <link rel="stylesheet" type="text/css" href="/css/common.css?<%=System.currentTimeMillis()%>"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/auction/aucMargin.css?<%= System.currentTimeMillis()%>"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/manageList.css"/>
    <script src="/js/plugin/jquery-1.8.3.min.js"></script>
    <script src="/js/plugin/layer/layer.js"></script>
    <script type="text/javascript" src="/js/public.js"></script>
    <script type="text/javascript" src="/js/util.js"></script>
    <script type="text/javascript" src="/js/common/copy.js"></script>
    <script type="text/javascript" src="/js/mall/mall_public.js"></script>
    <script type="text/javascript" src="/js/plugin/copy/clipboard.min.js"></script>
    <script type="text/javascript" src="/js/plugin/copy/copypublic.js"></script>
    <script type="text/javascript">
        $(function () {
            /* $(".copy").zclip({
             path: '/js/zclip/ZeroClipboard.swf',
             copy: function(){//复制内容
             return $(this).parent().next().val();
             },
             afterCopy: function(){//复制成功
             parent.layer.alert("复制成功！",{
             icon:	6,
             offset : "30%",
             closeBtn: 0
             });
             }
             }); */


            /**
             * 查看二维码
             * @param id
             */
            $(".qrcode").click(function () {
                parentOpenIframe("拍卖预览","208px","251px","<img src ='/mAuction/79B4DE7C/getTwoCode.do?code=" + $(this).attr("tit") + "' style='width:200px;height:200px;'/>");
//                parent.layer.open({
//                    type: 1,
//                    title: "拍卖预览",
//                    skin: 'layui-layer-rim', //加上边框
//                    area: ['208px', '251px'], //宽高
//                    offset: "30%",
//                    content: "<img src ='/mAuction/79B4DE7C/getTwoCode.do?code=" + $(this).attr("tit") + "' style='width:200px;height:200px;'/>"
//                });
            });
        });
    </script>
</head>
<body>
<jsp:include page="/jsp/common/headerCommon.jsp"/>
<div class="con_div">
    <c:if test="${empty isNoAdminFlag }">
        <div id="con-box">
            <c:if test="${!empty shoplist }">
                <div class="con-head">
                    <a class="navColor" href="/mAuction/index.do">拍卖管理</a>
                    <a class="" href="/mAuction/margin.do">保证金管理</a>
                </div>
                <div class="con-box1">
                    <div class="txt-btn" style="margin: 0px;">
                        <div class="blue-btn fl box1Btn">
                            <a id="layShow" href="/mAuction/to_edit.do">新建拍卖</a>
                        </div>
                        <c:if test="${!empty videourl }">
                            <div class="blue-btn fl right-15">
                                <a href='${videourl}' class="btn" target='_blank' style="color: white;">教学视频</a>
                            </div>
                        </c:if>
                        <div class="fl box1Btn" style="height: 28px;">
                            <select onchange="findGroup(this);" style="font-size: 16px; height: 28px;width:100px;" class="groupType">
                                <option value="">全部</option>
                                <option value="1" <c:if test="${!empty type && type eq 1 }">selected='selected'</c:if>>进行中</option>
                                <option value="-1" <c:if test="${!empty type && type eq -1 }">selected='selected'</c:if>>未开始</option>
                                <option value="2" <c:if test="${!empty type && type eq 2 }">selected='selected'</c:if>>已结束</option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="msg-list">
                    <ul id="list group_ul">
                        <li class="txt-tle">
                            <span class="f2 fl" style="">拍卖商品</span>
                            <span class="f2 fl" style="">所属店铺</span>
                            <span class="f9 fl" style="">起拍价(元)</span>
                            <span class="f9 fl" style="">结束价(元)</span>
                            <span class="f8 fl" style="text-align:center;margin-left:10px;width:18%;">有效时间</span>
                            <span class="f1 fl">活动状态</span>
                            <span class="f2 fl">创建时间</span>
                            <span class="f2 fl">操作</span>
                        </li>

                        <c:if test="${!empty page.subList}">
                            <c:forEach var="auction" items="${page.subList }">
                                <li class="txt-tle" style="min-height:50px;height:auto;">
                                    <span class="f2 fl" style="line-height:20px;padding-top:10px;"><c:if test="${!empty auction.proName}">${auction.proName }</c:if></span>
                                    <span class="f2 fl" style="line-height:20px;padding-top:10px;">${auction.shopName }</span>
                                    <span class="f9 fl" style="line-height:20px;padding-top:10px;">${auction.aucStartPrice }</span>
                                    <span class="f9 fl" style="line-height:20px;padding-top:10px;">${auction.aucLowerPrice }</span>
                                    <span class="f8 fl" style="text-align:left;margin-left:10px;line-height:20px;padding-top:10px;width:18%;">
							${auction.aucStartTime } 至 ${auction.aucEndTime }
						</span>
                                    <span class="f1 fl">
							<c:if test="${!empty auction.status}"><div>
								<c:if test="${auction.status == 0}">未开始</c:if>
								<c:if test="${auction.status == 1}">进行中</c:if>
								<c:if test="${auction.status == -1}">已结束</c:if>
								<c:if test="${auction.status == -2}">已失效</c:if>
							</div>
                            </c:if>
						</span>
                                    <span class="f2 fl" style="line-height:20px;padding-top:10px;"><fmt:formatDate pattern="yyyy-MM-dd hh:mm:ss"
                                                                                                                   value="${auction.createTime }"/></span>
                                    <span class="f2 fl span_a" style="width: 130px;position:relative;display:inline-block;">
							<c:if test="${auction.status == 0 || (auction.joinId == 0 && auction.status == 1)}">
                                <a href="/mAuction/to_edit.do?id=${auction.id }" class="editGroup" title="编辑"></a>
                            </c:if>
							<c:if test="${auction.status == 0 || auction.status == -1 || auction.status == -2}">
                                <a href="javascript:void(0);" id="${auction.id}" class="deleteGroup" onclick="deleteGroup(this,-1);" title="删除"></a>
                            </c:if>
							<c:if test="${auction.status == 0 || (auction.joinId == 0 && auction.status == 1)}">
                                <a href="javascript:void(0);" id="${auction.id}" class="shiGroup" onclick="deleteGroup(this,-2);" title="使失效"></a>
                            </c:if>
							<c:if test="${auction.status == 1}">
                                <a href="javascript:;" class="bj-a qrcode" title="预览" tit="${auction.productId}/${auction.shopId}/${auction.id }"></a>
                                <a href="javascript:;" class="bj-a copy copy_public" title="复制链接"
                                   data-clipboard-text="${path }/mAuction/${auction.productId}/${auction.shopId}/${auction.id }/79B4DE7C/auctiondetail.do" aria-label="复制成功！"></a>
                            </c:if>
						</span>
                                    <input type="hidden" class="link" value="${path }/mAuction/${auction.productId}/${auction.shopId}/${auction.id }/79B4DE7C/auctiondetail.do"/>
                                    <div class="cb"></div>
                                </li>
                            </c:forEach>
                        </c:if>
                    </ul>
                    <c:if test="${! empty page.subList }">
                        <input type="hidden" id="taskId" value="0"/>
                        <jsp:include page="../../common/page/pageTwo.jsp"></jsp:include>
                    </c:if>
                </div>
            </c:if>
            <c:if test="${empty shoplist }">
                <h1 class="groupH1"><strong>您还没有店铺，请先去店铺管理创建店铺</strong></h1>
            </c:if>
            <!--内容编辑行end-->
        </div>
    </c:if>
    <c:if test="${!empty isNoAdminFlag }">
        <h1 class="groupH2" style="margin: auto;"><strong>您还不是管理员，不能管理商城</strong></h1>
    </c:if>
</div>
<!--中间信息结束-->
<!--container  End-->
<!--footer  End-->
<script src="/js/plugin/laydate/laydate.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript">
    var shopId = $(".shopId").attr("id");
    $(".shopId").find("option[value=" + shopId + "]").attr("selected", true);

</script>
<script type="text/javascript" src="/js/mall/auction/auction.js"></script>
</body>
</html>