<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">
<head>
    <title>商品管理-商品列表</title>
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
    <link rel="stylesheet" type="text/css" href="/css/common.css?<%=System.currentTimeMillis()%>"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/reset.css"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/manageList.css?<%=System.currentTimeMillis()%>"/>

    <script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="/js/public.js"></script>
    <script type="text/javascript" src="/js/plugin/layer/layer.js"></script>
    <script type="text/javascript" src="/js/common/copy.js"></script>

    <script type="text/javascript" src="/js/util.js"></script>
    <script type="text/javascript" src="/js/mall/mall_public.js"></script>
    <script type="text/javascript" src="/js/plugin/copy/clipboard.min.js"></script>
    <script type="text/javascript" src="/js/plugin/copy/copypublic.js"></script>

</head>

<div class="contentWarp">
    <c:if test="${empty isNoAdminFlag }">
        <div class="con-head">
            <a class="navColor" href="/mPro/index.do">商品管理</a>
            <a class="" href="/mPro/group/group_index.do">分组管理</a>
            <a class="" href="/mPro/group/label_index.do">搜索推荐管理</a>
        </div>
        <c:if test="${!empty page}">
            <div class="con-box">
                <select name="mallChange" class="change proChange">
                    <option value="0">全部商品</option>
                    <option value="1" <c:if test="${proType == 1 }">selected='selected'</c:if>>出售中的商品</option>
                    <option value="2" <c:if test="${proType == 2 }">selected='selected'</c:if>>已售罄的商品</option>
                    <option value="3" <c:if test="${proType == 3 }">selected='selected'</c:if>>仓库中的商品</option>
                    <option value="4" <c:if test="${proType == 4 }">selected='selected'</c:if>>未送审的商品</option>
                    <option value="5" <c:if test="${proType == 5 }">selected='selected'</c:if>>审核中的商品</option>
                </select>
                <c:if test="${!empty videourl }">
                    <a href='${videourl}' class="btn" target='_blank' style="display: inline-block;">教学视频</a>
                </c:if>
                <c:if test="${proMaxNum-page.rowCount > 0}">
                    <input type="button" name="mallAdd" id="mallAdd" class="btn"
                           value="新增" onclick="javascript:location.href='/mPro/to_edit.do'"/>
                </c:if>
                <c:if test="${page.rowCount > 0}">
                    <input type="button" name="mallAdd" id="mallAdd" class="btn"
                           value="一键同步" onclick="syncProductByShop();"/>
                </c:if>
                <span>您只能拥有${proMaxNum }件商品，还可以新增<c:if test="${proMaxNum-page.rowCount > 0}">${proMaxNum-page.rowCount}</c:if><c:if test="${proMaxNum-page.rowCount <= 0}">0</c:if>件商品</span>

                <div class="srh">
                    <div class="sbt" id="go"></div>
                    <input type="text" placeholder="请输入关键字" id="search" class="srh"
                           name="keyword" value="<c:if test="${!empty proName }">${proName}</c:if>">
                </div>
            </div>
            <div class="ui-box">
                <table border="0" cellspacing="0" cellpadding="0" width="100%"
                       class="ui-table">
                    <tr>
                        <th class="cell-3"><input type="checkbox" name="check"
                                                  id="checkAll" class="allCheck" value="" onclick="showMore(this)"/></th>
                        <th class="cell-10">商品</th>
                        <th class="cell-8"></th>
                        <th class="cell-8">所属店铺</th>
                        <th class="cell-8">单价(元)</th>
                        <!--  <th class="cell-10">访问量</th> -->
                        <th class="cell-8">库存</th>
                        <th class="cell-8">总销量</th>
                        <th class="cell-7">浏览量</th>
                        <!-- <th class="cell-10">创建时间</th> -->
                        <th class="cell-7">核状态</th>
                        <th class="cell-8">上架状态</th>
                        <th class="cell-20 text-center">操作</th>
                    </tr>
                    <c:if test="${!empty page.subList}">
                        <c:forEach var="pro" items="${page.subList }">
                            <tr class="list-item">
                                <td class="check-box"><input type="checkbox" name="check"
                                                             id="${pro.id }" class="checkPro" value="" status="${pro.checkStatus }"
                                                             publish="${pro.isPublish }"/></td>
                                <td class="goods-image-td">
                                    <div class="goods-image js-goods-image ">
                                        <img
                                                src="<c:if test="${!empty pro.imageUrl}">${imgUrl}${pro.imageUrl }</c:if>"
                                                alt="商品图片"/>
                                    </div>
                                </td>
                                <td class="goods-meta">
                                    <p class="goods-title" style="max-height:auto;">
                                            <%-- <c:if test="${pro.checkStatus ==1}">
                                           <a href="mallPage/${pro.id }/${pro.shopId }/79B4DE7C/phoneProduct.do" target="_blank" class="new-window"
                                               title="<c:if test="${!empty pro.proName}">${pro.proName }</c:if>"><c:if
                                                   test="${!empty pro.proName}">${pro.proName }</c:if></a>
                                           </c:if> --%>
                                            ${pro.proName }
                                    </p>
                                </td>
                                <td><c:if test="${!empty pro.shopName}">${pro.shopName }</c:if></td>
                                <td>￥<c:if test="${!empty pro.proPrice}">${pro.proPrice }</c:if></td>
                                <!--  <td>
                            <div>UV:23</div>
                            <div>PV:166</div>
                        </td> -->
                                <td><c:if test="${!empty pro.stockTotal}">${pro.stockTotal }</c:if></td>
                                <c:set var="sale" value="0"></c:set>
                                <c:if test="${!empty pro.saleTotal}">
                                    <c:set var="sale" value="${sale+pro.saleTotal }"></c:set>
                                </c:if>
                                <c:if test="${!empty pro.sales_base}">
                                    <c:set var="sale" value="${sale+pro.sales_base }"></c:set>
                                </c:if>
                                <td title="实际销量：${pro.saleTotal};销量基数：${pro.sales_base}">${sale }</td>
                                <td><c:if test="${!empty pro.saleTotal}">${pro.viewsNum }</c:if></td>
                                    <%-- <td class="text-center"><c:if test="${!empty pro.createTime}">
                                            <fmt:formatDate pattern="yyyy-MM-dd hh:mm"
                                                value="${pro.createTime }" />
                                        </c:if></td> --%>
                                <td>
                                    <c:if test="${pro.checkStatus ==0}">审核中</c:if>
                                    <c:if test="${pro.checkStatus ==1}">审核成功</c:if>
                                    <c:if test="${pro.checkStatus ==-1}">审核失败
                                        <c:if test="${pro.checkReason!=null }">原因：${pro.checkReason }</c:if>
                                    </c:if>
                                    <c:if test="${pro.checkStatus ==-2}">未送审</c:if>
                                </td>
                                <td>
                                    <c:if test="${pro.isPublish ==0}">未上架</c:if>
                                    <c:if test="${pro.isPublish ==1}">已上架</c:if>
                                    <c:if test="${pro.isPublish ==-1}">已下架</c:if>
                                </td>
                                <td>
                                    <div class="operate" style="position:relative;display:inline-block;">
                                        <c:if test="${pro.checkStatus !=0 && pro.isSeckill != 1}">
                                            <a href="/mPro/to_edit.do?id=${pro.id }" class="edit" title="编辑商品"></a>
                                        </c:if>
                                        <c:if test="${pro.isPublish !=1 && pro.isGroup != 1 && pro.isSeckill != 1}">
                                            <a href="javascript:;" class="delete" title="删除商品" id="${pro.id }"></a>
                                        </c:if>
                                        <c:if test="${pro.checkStatus == -2 || pro.checkStatus == -1}">
                                            <a href="javascript:;" class="send_valid" id="${pro.id }" title="送审商品"></a>
                                        </c:if>
                                        <c:if test="${pro.checkStatus == 1 && pro.isPublish != 1 && pro.isGroup == 0 && pro.isSeckill == 0}">
                                            <a href="javascript:;" class="sj" id="${pro.id }" title="上架商品"></a>
                                        </c:if>
                                        <c:if test="${pro.checkStatus == 1 && pro.isPublish == 1 && pro.isGroup == 0 && pro.isSeckill == 0}">
                                            <a href="javascript:;" class="xj" id="${pro.id }" title="下架商品"></a>
                                        </c:if>
                                        <a href="javascript:;" class="viewNum" id="${pro.id }" num="${pro.viewsNum }" isView="${pro.isShowView }" title="修改浏览量"></a>
                                        <a href="javascript:;" class="sales" id="${pro.id }" salesBase="${pro.sales_base }" title="修改销量基数"></a>
                                        <a href="javascript:;" class="sync" title="同步商品" id="${pro.id }"></a>
                                        <c:if test="${pro.checkStatus == 1 && pro.isPublish == 1 }">
                                            <c:if test="${pro.twoCodePath != null && pro.twoCodePath != ''}">
                                                <a href="javascript:;" class="qrcode review" title="预览"
                                                   url="/mallPage/${pro.id}/${pro.shopId }/79B4DE7C/phoneProduct.do?toshop=0"></a>
                                            </c:if>
                                            <a href="javascript:;" class="copy copy_public" title="复制链接"
                                               data-clipboard-text="${path }/mallPage/${pro.id}/${pro.shopId }/79B4DE7C/phoneProduct.do?toshop=0" aria-label="复制成功！"></a>
                                            <a href="javascript:;" class="toShops shiGroup" title="到店购买" id="${pro.id }" sId="${pro.shopId }"></a>
                                        </c:if>
                                    </div>
                                    <input type="hidden" class="link" value="${path }/mallPage/${pro.id}/${pro.shopId }/79B4DE7C/phoneProduct.do?toshop=0"/>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:if>
                </table>
                <div class="list-footer-region ui-box">
                    <c:if test="${!empty page.subList }">
                        <input type="hidden" id="taskId" value="0"/>
                        <jsp:include page="pageProduct.jsp"></jsp:include>
                    </c:if>
                    <!--与page.jsp类似 -->
                    <!-- <div class="widget-list-footer">
                        <div class="pull-left">
                            <input type="checkbox" name="" id="" value="" onclick="showMore(this)"/>
                            <a href="javascript:;" class="ui-btn js-batch-checkAll">全选</a>
                            <a href="javascript:;" class="ui-btn js-batch-tag" >改分组</a>
                            <a href="javascript:;" class="ui-btn js-batch-unload">下架</a>
                            <a href="javascript:;" class="ui-btn js-batch-delete">删除</a>
                        </div>
                        <div class="pagenavi">
                            <ul>
                                <li>首页</li>
                                <li>上一页</li>
                                <li>第一页</li>
                                <li>下一页</li>
                                <li>尾页</li>
                                <li>共一页</li>
                            </ul>
                        </div>
                    </div> -->
                </div>
            </div>
        </c:if>
        <c:if test="${empty page }">
            <h1 class="groupH1"><strong>您还没有店铺，请先去店铺管理创建店铺</strong></h1>
        </c:if>
    </c:if>
    <c:if test="${!empty isNoAdminFlag }">
        <h1 class="groupH1"><strong>您还不是管理员，不能管理商城</strong></h1>
    </c:if>
    <div class="viewNum" style="display:none;">
        <div style="width:400px;margin-top:20px;">
            <div style="width:300px;margin: 0px auto;">
                <span style="width:100px;display: inline-block;text-align: right;">是否显示浏览量：</span>
                <input type="checkbox" name="isShowViews" value="1" class="isShowViews">显示浏览量
            </div>
            <div class="viewDiv" style="width:300px;margin: 10px auto;display:none;">
                <span style="width:100px;display: inline-block;text-align: right;">浏览量：</span>
                <input type="text" class="vNums" value="" maxlength="9" style="height:25px;line-height:25px;" placeholder="请输入大于0的9位数字"/>
                <div style="margin-left:100px;color:red;">
                    请输入大于0的9位数字
                </div>
            </div>
        </div>
    </div>
    <div class="saleBase" style="display:none;">
        <div style="width:400px;margin-top:20px;">
            <div class="viewDiv" style="width:300px;margin: 10px auto;">
                <span style="width:100px;display: inline-block;text-align: right;">销量基数：</span>
                <input type="text" class="saleBase" value="" maxlength="8" style="height:25px;line-height:25px;" placeholder="请输入大于0的8位数字"/>
                <div style="margin-left:100px;color:red;">
                    请输入大于0的8位数字
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/jsp/common/headerCommon.jsp"/>
<script type="text/javascript" src="/js/mall/product/pro_index.js?<%= System.currentTimeMillis()%>"></script>
<script type="text/javascript">
    /*全选 反选*/
    var _array = $("input[name='check']");
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
    $(".checkPro").click(function () {
        if (!$(this).is(":checked")) {
            $(".allCheck").removeAttr("checked");
        } else {
            var flag = true;
            $(".checkPro").each(function () {
                if (!$(this).is(":checked")) {
                    flag = false;
                }
            });
            if (flag) {
                $(".allCheck").attr("checked", "checked");
            }
        }
    });
    //全选
    $(".js-batch-checkAll").click(function () {
        $(".allCheck").attr("checked", "checked");
        showMore($(".allCheck"));
    });
</script>

</body>
</html>