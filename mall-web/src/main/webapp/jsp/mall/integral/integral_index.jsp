<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">
<head>
    <title>积分商品管理</title>
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
    <link rel="stylesheet" type="text/css" href="/css/mall/auction/aucMargin.css"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/manageList.css"/>
    <script src="/js/plugin/jquery-1.8.0.min.js"></script>
    <script src="/js/plugin/layer/layer.js"></script>
    <script type="text/javascript" src="/js/public.js"></script>
    <script type="text/javascript" src="/js/common/util.js"></script>
    <%--<script type="text/javascript" src="/js/zclip/zclip.js"></script>--%>
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
             });
             $(".copy_mall").zclip({
             path: '/js/zclip/ZeroClipboard.swf',
             copy: function(){//复制内容
             return $(".shopUrl").val();
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
                 layer.open({
                    type: 1,
                    shade:[0.1,"#fff"],
                    title: "积分商品预览",
                    skin: 'layui-layer-rim', //加上边框
                    area: ['208px', '251px'], //宽高
                    offset: "10%",
                    content: "<img src ='/mallIntegral/getTwoCode.do?" + $(this).attr("tit") + "' style='width:200px;height:200px;'/>"
                });
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
                    <a class="navColor" href="/mallIntegral/index.do">积分商品管理</a>
                    <a class="" href="/mallIntegral/image_index.do">积分商城图片设置</a>
                </div>
                <div class="con-box1">
                    <div class="txt-btn" style="margin: 0px;">
                        <div class="blue-btn fl box1Btn">
                            <a id="layShow" href="/mallIntegral/to_edit.do">新建积分商品</a>
                        </div>
                        <c:if test="${!empty videourl }">
                            <div class="blue-btn fl right-15">
                                <a href='${videourl}' class="btn" target='_blank' style="color: white;">教学视频</a>
                            </div>
                        </c:if>
                        <c:if test="${!empty shop_id && shop_id > 0 }">
                            <div class="blue-btn fl box1Btn">
                                <a id="layShow" class="copy_mall copy_public" data-clipboard-text="${path }/phoneIntegral/${shop_id }/79B4DE7C/toIndex.do?uId=${userId}"
                                   aria-label="复制成功！" href="javascript:void(0);">复制积分商城链接</a>
                                <input type="hidden" class="shopUrl" value="${path }/phoneIntegral/${shop_id }/79B4DE7C/toIndex.do?uId=${userId}"/>
                            </div>
                        </c:if>
                        <c:if test="${!empty shoplist }">
                            <div class="fl box1Btn" style="height: 28px;">
                                <select onchange="findShop(this);" style="font-size: 16px; height: 28px;" class="shopType">
                                    <option value="-1">全部店铺</option>
                                    <c:forEach var="shop" items="${shoplist }">
                                        <option value="${shop.id }" <c:if test="${!empty shop_id && shop.id == shop_id }">selected='selected'</c:if>>${shop.sto_name }</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="fl box1Btn" style="height: 28px;">
                                <select onchange="findInteger(this);" style="font-size: 16px; height: 28px;width:100px;" class="groupType">
                                    <option value="">全部</option>
                                    <option value="1" <c:if test="${!empty type && type eq 1 }">selected='selected'</c:if>>进行中</option>
                                    <option value="-1" <c:if test="${!empty type && type eq -1 }">selected='selected'</c:if>>未开始</option>
                                    <option value="2" <c:if test="${!empty type && type eq 2 }">selected='selected'</c:if>>已结束</option>
                                    <option value="3" <c:if test="${!empty type && type eq 2 }">selected='selected'</c:if>>已失效</option>
                                </select>
                            </div>
                        </c:if>
                    </div>
                </div>
                <div class="msg-list">
                    <ul id="list group_ul">
                        <li class="txt-tle">
                            <span class="f2 fl" style="">积分商品</span>
                            <span class="f2 fl" style="">所属店铺</span>
                            <span class="f8 fl" style="text-align:center;margin-left:10px;width: 18%;">有效时间</span>
                            <span class="f1 fl">活动状态</span>
                            <span class="f8 fl">创建时间</span>
                            <span class="f2 fl">操作</span>
                        </li>

                        <c:if test="${!empty page.subList}">
                            <c:forEach var="integral" items="${page.subList }">
                                <li class="txt-tle" style="min-height:50px;height:auto;">
                                    <span class="f2 fl" style="line-height:20px;padding-top:10px;"><c:if test="${!empty integral.proName}">${integral.proName }</c:if></span>
                                    <span class="f2 fl" style="line-height:20px;padding-top:10px;">${integral.shopName }</span>
                                    <span class="f8 fl" style="text-align:left;margin-left:10px;line-height:20px;padding-top:10px;width: 18%;">
							${integral.startTime } 至 ${integral.endTime }
						</span>
                                    <span class="f1 fl">
							<c:if test="${!empty integral.status}"><div>
								<c:if test="${integral.status == 0}">未开始</c:if>
								<c:if test="${integral.status == 1}">进行中</c:if>
								<c:if test="${integral.status == -1}">已结束</c:if>
								<c:if test="${integral.status == -2}">已失效</c:if>
							</div>
                            </c:if>
						</span>
                                    <span class="f8 fl" style="line-height:20px;padding-top:10px;">
                                        <fmt:formatDate pattern="yyyy-MM-dd hh:mm:ss" value="${integral.createTime }"/>
                                    </span>
                                    <span class="f2 fl span_a" style="width: 130px;position:relative;display:inline-block;">
							<a href="/mallIntegral/to_edit.do?id=${integral.id }" class="editGroup" title="编辑"></a>
							<a href="javascript:void(0);" id="${integral.id}" class="deleteGroup" onclick="deleteGroup(this,-1);" title="删除"></a>
							<c:if test="${integral.isUse == 1}">
                                <a href="javascript:void(0);" id="${integral.id}" class="shiGroup" onclick="deleteGroup(this,-2);" title="使失效"></a>
                            </c:if>
							<c:if test="${integral.isUse == -1}">
                                <a href="javascript:void(0);" id="${integral.id}" class="shiGroup" onclick="deleteGroup(this,1);" title="启用"></a>
                            </c:if>
							<c:if test="${integral.status == 1}">
                                <a href="javascript:;" class="bj-a qrcode" title="预览" tit="id=${integral.productId}&uId=${integral.user_id }&shopId=${integral.shop_id}"></a>
                                <a href="javascript:;" class="bj-a copy copy_public" title="复制链接"
                                   data-clipboard-text="${path }/phoneIntegral/79B4DE7C/integralProduct.do?id=${integral.productId}&uId=${integral.user_id }&shopId=${integral.shop_id}"
                                   aria-label="复制成功！"></a>
                            </c:if>
						</span>
                                    <input type="hidden" class="link"
                                           value="${path }/phoneIntegral/79B4DE7C/integralProduct.do?id=${integral.productId}&uId=${integral.user_id }&shopId=${integral.shop_id}"/>
                                    <div class="cb"></div>
                                </li>
                            </c:forEach>
                        </c:if>
                    </ul>
                    <c:if test="${! empty page.subList }">
                        <input type="hidden" id="taskId" value="0"/>
                        <jsp:include page="/jsp/mall/pageView.jsp"></jsp:include>
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
<script type="text/javascript" src="/js/mall/integral/integral.js?<%=System.currentTimeMillis()%>"></script>
</body>
</html>