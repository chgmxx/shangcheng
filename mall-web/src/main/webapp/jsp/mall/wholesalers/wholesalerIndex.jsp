<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">
<head>
    <title>批发管理</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <%
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://"
                + request.getServerName() + ":" + request.getServerPort()
                + path + "/";
    %>
    <base href="<%=basePath%>"/>
    <link rel="stylesheet" type="text/css" href="/css/common.css"/>
    <link rel="stylesheet" type="text/css" href="/css/common/Fan-index.css"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/manageList.css"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/group.css"/>
    <script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="/js/plugin/layer/layer.js"></script>
    <script type="text/javascript" src="/js/public.js"></script>
    <script type="text/javascript" src="/js/util.js"></script>
    <script type="text/javascript" src="/js/common/copy.js"></script>
    <script type="text/javascript" src="/js/mall/mall_public.js"></script>
    <script type="text/javascript" src="/js/plugin/copy/clipboard.min.js"></script>
    <script type="text/javascript" src="/js/plugin/copy/copypublic.js"></script>
    <script type="text/javascript">
        $(function () {
            /* $(".copy").zclip({
             path: '/js/plugin/zclip/ZeroClipboard.swf',
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
                parent.layer.open({
                    type: 1,
                    title: "批发预览",
                    skin: 'layui-layer-rim', //加上边框
                    area: ['200px', '240px'], //宽高
                    offset: "30%",
                    content: "<img src ='/store/79B4DE7C/getTwoCode.do?url=" + $(this).attr("url") + "'/>"
                });
            });
        });
    </script>
</head>
<body>
<div class="con_div">
    <c:if test="${empty isNoAdminFlag }">
        <div class="con-head" style="margin-bottom: 0px;">
            <a class="navColor" href="/mallWholesalers/index.do">批发管理</a>
            <a class="" href="/mallWholesalers/wholesaleList.do">批发商管理</a>
            <a class="" href="/mallWholesalers/toSetWholesale.do">批发设置</a>
        </div>
        <div id="con-box">
            <c:if test="${!empty shoplist }">
                <div class="con-box1">
                    <div class="txt-btn">
                        <div class="fl box1Btn" style="height: 28px;">
                            <select onchange="findGroup(this);" style="font-size: 16px; height: 28px;" class="groupType">
                                <option value="">全部</option>
                                <option value="1" <c:if test="${!empty type && type eq 1 }">selected='selected'</c:if>>进行中</option>
                                <option value="-1" <c:if test="${!empty type && type eq -1 }">selected='selected'</c:if>>未开始</option>
                                <option value="2" <c:if test="${!empty type && type eq 2 }">selected='selected'</c:if>>已结束</option>
                            </select>
                        </div>
                        <div class="blue-btn fl box1Btn">
                            <a id="layShow" href="/mallWholesalers/to_edit.do">新建批发</a>
                        </div>
                        <c:if test="${!empty videourl }">
                            <div class="blue-btn fl right-15">
                                <a href='${videourl}' class="btn" target='_blank' style="color: white;">教学视频</a>
                            </div>
                        </c:if>
                        <c:if test="${empty isOpenPifa }">
                            <div class=" fl box1Btn" style="line-height:30px;">
                                <a href="/store/setindex.do" style="color: red;">您还没有开启商品批发，请前往商城设置里面开启</a>
                            </div>
                        </c:if>
                    </div>
                </div>
                <div class="msg-list">
                    <ul id="list group_ul">
                        <li class="txt-tle">
                            <span class="f2 fl" style="width:17%;">商品名称</span>
                            <span class="f2 fl" style="width:17%;">所属店铺</span>
                            <span class="f2 fl" style="width:20%;text-align:center;margin-left:10px;">有效时间</span>
                            <span class="f2 fl">活动状态</span>
                            <span class="f2 fl">创建时间</span>
                            <span class="f2 fl">操作</span>
                        </li>

                        <c:if test="${!empty page.subList}">
                            <c:forEach var="pifa" items="${page.subList }">
                                <li class="txt-tle" style="min-height:50px;height:auto;">
                                    <span class="f2 fl" style="line-height:20px;padding-top:10px;width:17%;">${pifa.proName }</span>
                                    <span class="f2 fl" style="line-height:20px;padding-top:10px;width:17%;">${pifa.shopName }</span>
                                    <span class="f2 fl" style="width:20%;text-align:left;margin-left:10px;line-height:20px;padding-top:10px;">
							${pifa.pfStartTime } 至 ${pifa.pfEndTime }
						</span>
                                    <span class="f2 fl">
							<c:if test="${!empty pifa.status}"><div>
								<c:if test="${pifa.status == 0}">未开始</c:if>
								<c:if test="${pifa.status == 1}">进行中</c:if>
								<c:if test="${pifa.status == -1}">已结束</c:if>
								<c:if test="${pifa.status == -2}">已失效</c:if>
							</div>
                            </c:if>
						</span>
                                    <span class="f2 fl" style="line-height:20px;padding-top:10px;"><fmt:formatDate pattern="yyyy-MM-dd hh:mm:ss"
                                                                                                                   value="${pifa.createTime }"/></span>
                                    <span class="f2 fl span_a" style="width: 130px;position:relative;display:inline-block;">
							<c:if test="${pifa.status == 0 || (pifa.joinId == 0 && pifa.status == 1)}">
                                <a href="/mallWholesalers/to_edit.do?id=${pifa.id }" class="editGroup" title="编辑"></a>
                            </c:if>
							<c:if test="${pifa.status == 0 || pifa.status == -1 || pifa.status == -2}">
                                <a href="javascript:void(0);" id="${pifa.id}" class="deleteGroup" onclick="deleteGroup(this,-1);" title="删除"></a>
                            </c:if>
							<c:if test="${pifa.status == 0 || (pifa.joinId == 0 && pifa.status == 1)}">
                                <a href="javascript:void(0);" id="${pifa.id}" class="shiGroup" onclick="deleteGroup(this,-2);" title="使失效"></a>
                            </c:if>
							<c:if test="${pifa.status == 1}">
                                <c:if test="${pifa.twoCodePath != null && pifa.twoCodePath != '' }">
                                    <a href="javascript:;" class="bj-a qrcode" title="预览" url="/mallPage/${pifa.productId}/${pifa.shopId }/79B4DE7C/phoneProduct.do"></a>
                                </c:if>
                                <a href="javascript:;" class="bj-a copy copy_public" title="复制链接"
                                   data-clipboard-text="${path }/mallPage/${pifa.productId}/${pifa.shopId }/79B4DE7C/phoneProduct.do" aria-label="复制成功！"></a>
                            </c:if>
						</span>
                                    <input type="hidden" class="link" value="${path }/mallPage/${pifa.productId}/${pifa.shopId }/79B4DE7C/phoneProduct.do"/>
                                    <div class="cb"></div>
                                </li>
                            </c:forEach>
                        </c:if>
                    </ul>
                    <c:if test="${! empty page.subList }">
                        <input type="hidden" id="taskId" value="0"/>
                        <jsp:include page="/jsp/common/page/pageTwo.jsp"></jsp:include>
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
<script type="text/javascript" src="/js/mall/pifa/pifa.js"></script>
</body>
</html>