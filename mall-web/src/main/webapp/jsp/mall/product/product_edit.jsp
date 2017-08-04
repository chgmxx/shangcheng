<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">
<head>
    <title>商品管理-编辑商品</title>
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
    <link rel="stylesheet" type="text/css" href="/css/mall/editMall.css"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/editMallNext.css"/>
    <link rel="stylesheet" type="text/css" href="/css/plugin/chosen/chosen.css"/>
    <link rel="stylesheet" type="text/css" href="/css/plugin/jquery-ui.css"/>
    <script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="/js/plugin/layer/layer.js"></script>
    <script type="text/javascript" src="/js/public.js"></script>
    <script type="text/javascript" src="/js/plugin/chosen/chosen.jquery.js" charset="utf-8"></script>
    <script type="text/javascript" src="/js/plugin/jquery-ui.min.js" charset="utf-8"></script>
    <script type="text/javascript" src="/js/plugin/bootstrap.min.js" charset="utf-8"></script>
    <script type="text/javascript" src="/js/plugin/kindeditor/kindeditor.js" charset="utf-8"></script>
    <script type="text/javascript" src="/js/plugin/kindeditor/lang/zh_CN.js" charset="utf-8"></script>
    <script type="text/javascript" src="/js/mall/mall_public.js"></script>
    <script type="text/javascript">
        if (top == self) {
            window.location.href = "/mPro/product_start.do";
        }
        var error = '${error}';
        if (error != undefined && error != "") {
            parent.layer.alert("参数错误，将调回前一个页面");
            window.history.back(-1);
        }
        var imgResource = "${imgUrl}";
        var groups = {};
        var groupDefaults = new Array();
        var delimageList = new Array();
        var updGroupList = new Array();
        var updGroupObj = new Object();
        var inveSelectObj = new Object();

        var inveDefaultObj = new Object();//默认库存数据
        var specDefaultObj = new Object();//默认规格数据
        var imagDefaultObj = new Object();//默认图片数据
        var paramDefaultObj = new Object();//默认参数数据

        var specIdObj = new Object();
        var specValObj = new Object();

        var specInvenInpObject = new Object();

        var groupSelectObj = new Object();

        var editor;

        KindEditor.ready(function (K) {
            editor = K.create('textarea.productDetail', {
                width: '98%',
                height: '650px',
                minWidth: '150px',
                margin: '0 auto',
                resizeType: 0,
                filterMode: false,
                items: ['fontname', 'fontsize', '|', 'forecolor', 'hilitecolor',
                    'bold', 'italic', 'underline', 'removeformat', '|',
                    'justifyleft', 'justifycenter', 'justifyright',
                    'insertorderedlist', 'insertunorderedlist', '|',
                    'emoticons', 'image', 'link', 'media'],
                allowPreviewEmoticons: true,
                allowImageUpload: true,
                allowFileManager: true,
                allowFileUpload: true,
                uploadJson: '/upload/upload.do',
                fileManagerJson: '../linkurl/fileManagerJson',
                formatUploadUrl: false,
                wellFormatMode: true,
                afterBlur: function () {
                    this.sync();
                },
                afterChange: function () {

                    if (editor != null) {
                        var iframe = document.getElementById("twoIframe").contentWindow;
                        var detail = iframe.document.getElementById("detail-info");
                        $(detail).html(editor.html());
                    }
                }
            });
            editor.sync();
        });
    </script>
</head>
<body>
<div class="editWarp">
    <div class="con-head">
        <a class="navColor" href="/mPro/index.do">商品管理</a>
        <a class="" href="/mPro/group/group_index.do">分组管理</a>
        <a class="" href="/mPro/group/label_index.do">搜索推荐管理</a>
    </div>
    <div class="title">
        <span class="i-con"></span>
        <p class="title-p">新建商品</p>
    </div>

    <div class="stepflex" id="#sflex03">
        <dl class="first done">
            <dt class="s-num">1</dt>
            <dd class="s-text">
                1.新建商品<s></s><b></b>
            </dd>
        </dl>
        <dl class="normal doing twoDl">
            <dt class="s-num">2</dt>
            <dd class="s-text">
                2.编辑商品信息<s></s><b></b>
            </dd>
        </dl>
        <dl class="normal last threeDl">
            <dt class="s-num">3</dt>
            <dd class="s-text">
                3.编辑商品详情<s></s><b></b>
            </dd>
        </dl>
        <dl class="normal last">
            <dt class="s-num">4</dt>
            <dd class="s-text">
                4.完成<s></s><b></b>
            </dd>
        </dl>
    </div>
    <c:set var="proTypeId" value="0"></c:set>
    <c:set var="isPresell" value="0"></c:set>
    <c:set var="memberType" value="0"></c:set>
    <c:set var="cardType" value="0"></c:set>
    <c:set var="flowId" value="0"></c:set>
    <c:if test="${!empty invenList }">
        <c:forEach items="${invenList}" var="inven">
            <script type="text/javascript">
                var obj = {
                    invPrice: "${inven.invPrice}",
                    invNum: "${inven.invNum}",
                    invCode: "${inven.invCode}",
                    invSaleNum: "${inven.invSaleNum}",
                    isDefault: "${inven.isDefault}"
                }
                inveSelectObj["${inven.specificaIds}"] = obj;
                inveDefaultObj["${inven.specificaIds}"] = "${inven.id}";
            </script>
        </c:forEach>
    </c:if>
    <c:if test="${!empty pro }">
        <c:set var="proTypeId" value="${pro.proTypeId }"></c:set>
        <c:set var="isPresell" value="${pro.isPresell }"></c:set>
        <c:set var="memberType" value="${pro.memberType }"></c:set>
        <c:set var="cardType" value="${pro.cardType }"></c:set>
        <c:set var="flowId" value="${pro.flowId }"></c:set>
    </c:if>
    <c:if test="${!empty proGroupList }">
        <c:set var="i" value="0"></c:set>
        <c:forEach var="groups" items="${proGroupList }">
            <script type="text/javascript">
                var obj = new Object();
                obj.shopId = "${groups.shopId}";
                obj.groupId = "${groups.groupId}";

                updGroupObj[obj.groupId] = obj;

                obj.id = "${groups.id}";
                groupDefaults["${i}"] = obj;

                groupSelectObj[obj.groupId] = obj.shopId;

            </script>
            <c:set var="i" value="${i+1 }"></c:set>
        </c:forEach>
    </c:if>
    <div class="conten-box">
        <form id="productForm">
            <div class="onePro">
                <input type="hidden" name="id" class="proId" id="id"
                       value="<c:if test="${!empty pro}">${pro.id }</c:if>"/>
                <input type="hidden" name="twoCodePath" value="<c:if test="${!empty pro}">${pro.twoCodePath }</c:if>"/>
                <div class="left-box">
                    <!--基本信息-->
                    <div class="group-inner">
                        <h2 class="box-title">基本信息：</h2>
                        <div class="control-group">
                            <label for="" class="control-label"><em class="required">*</em>所属店铺：</label>
                            <div class="controls">
                                <!--<input type="text" name="" id="" value="" />-->
                                <select class="chosen-container shop-contain" name="shopId"
                                        data-selected-id="" data-placeholder="选择所属店铺"
                                        style="width: 250px !important; border-radius: 5px;"
                                        <c:if test="${pro.shopId != null && pro.shopId != '' }">disabled="disabled"</c:if>>
                                    <c:if test="${!empty shoplist }">
                                        <c:forEach var="shop" items="${shoplist }">
                                            <option value="${shop.id }"
                                                    <c:if test="${shop.id == pro.shopId }">selected='selected'</c:if>>${shop.sto_name }</option>
                                        </c:forEach>
                                    </c:if>
                                </select>

                            </div>
                        </div>
                        <div class="control-group">
                            <label for="" class="control-label text-top"><em class="required">*</em>商品分组：</label>
                            <div class="controls">
                                <!--<input type="text" name="" id="" value="" />-->
                                <%-- <select class="chosen-container chosen-contain group-contain"
                                    multiple="" data-selected-id="" data-placeholder="选择商品分组"
                                    style="width: 250px !important; border-radius: 5px;">
                                    <c:if test="${!empty groupList }">
                                        <c:forEach var="group" items="${groupList }">
                                            <option value="${group.id}" shopStr="${group.shopId }">${group.groupName }</option>
                                            <script type="text/javascript">
                                                groups["${group.id}"] = "${group.shopId }";
                                            </script>
                                        </c:forEach>
                                    </c:if>
                                </select> --%>
                                <div class="groupDiv">
                                    <c:set var="groups" value=""/>
                                    <c:if test="${!empty proGroupList }">
                                        <c:forEach var="proGroup" items="${proGroupList }" varStatus="i">
                                            <c:if test="${groups != '' }">
                                                <c:set var="groups" value="${groups}、${proGroup.groupName }"/>
                                            </c:if>
                                            <c:if test="${groups == '' }">
                                                <c:set var="groups" value="${proGroup.groupName }"/>
                                            </c:if>

                                        </c:forEach>
                                    </c:if>
                                    <textarea class="groups" cols="30" rows="5" placeholder="请选择分组信息">${groups }</textarea>


                                    <c:if test="${!empty groupList }">
                                        <c:forEach var="group" items="${groupList }">
                                            <script type="text/javascript">
                                                groups["${group.id}"] = "${group.shopId }";
                                            </script>
                                        </c:forEach>
                                    </c:if>
                                </div>

                                <p class="help-inline">
                                    <!-- <a class="refresh-tag" href="javascript:;">刷新</a> <span>|</span> -->
                                    <a class="new-window" target="_blank" href="/mPro/group/start.do">新建分组</a>
                                    <!--  <span>|</span>
                                <a class="new-window" target="_blank" href="">帮助</a> -->
                                </p>
                            </div>
                        </div>
                        <c:if test="${(cardList == null || cardList.size() == 0) && proTypeId == 2 }">
                            <c:set var="proTypeId" value="0"></c:set>
                        </c:if>
                        <c:if test="${noShowSt == 1 }">
                            <c:set var="proTypeId" value="${empty pro.proTypeId ? 1: pro.proTypeId }"></c:set>
                        </c:if>
                        <c:if test="${proTypeId != 0 }">
                            <c:set var="noUpSpec" value="0"></c:set>
                        </c:if>
                        <c:set var="isOrder" value="0"></c:set>
                        <div class="control-group">
                            <label for="" class="control-label text-top">商品类型：</label>
                            <input type="hidden" value="${empty noUpInvNum ? 0: noUpInvNum }" class="noUpInvNum"/>
                            <input type="hidden" value="${empty noUpSpec ? 0 : noUpSpec}" class="noUpSpec"/>
                            <div class="controls">
                                <c:if test="${empty noChangeFlow && noShowSt != 1}">
                                    <label class="radio inline">
                                        <input type="radio" name="proTypeId" class="proTypeId"
                                               <c:if test="${isOrder == 1 }">disabled="disabled"</c:if>
                                               <c:if test="${proTypeId==0 }">checked="true"</c:if> value="0">实物商品
                                    </label>
                                    <br/>
                                </c:if>
                                <c:if test="${empty noChangeFlow }">
                                    <label class="radio inline">
                                        <input type="radio" name="proTypeId" class="proTypeId"
                                               <c:if test="${isOrder == 1 }">disabled="disabled"</c:if>
                                               <c:if test="${proTypeId==1 }">checked="true"</c:if> value="1">虚拟商品(非会员卡，无需物流)
                                    </label>
                                    <br/>
                                </c:if>
                                <c:if test="${cardList != null && cardList.size() > 0 && empty noChangeFlow}">
                                    <label class="radio inline">
                                        <input type="radio" name="proTypeId" class="proTypeId"
                                               <c:if test="${isOrder == 1 }">disabled="disabled"</c:if>
                                               <c:if test="${proTypeId==2 }">checked="true"</c:if> value="2">虚拟商品(会员卡，无需物流)
                                    </label>
                                    <br/>
                                </c:if>
                                <c:if test="${cardReceiveList != null && cardReceiveList.size() > 0 && empty noChangeFlow}">
                                    <label class="radio inline">
                                        <input type="radio" name="proTypeId" class="proTypeId"
                                               <c:if test="${isOrder == 1 }">disabled="disabled"</c:if>
                                               <c:if test="${proTypeId==3 }">checked="true"</c:if> value="3">虚拟商品(卡券包，无需物流)
                                    </label>
                                    <br/>
                                </c:if>
                                <c:if test="${!empty newFlowList }">
                                    <label class="radio inline">
                                        <input type="radio" name="proTypeId" class="proTypeId"
                                               <c:if test="${isOrder == 1 }">disabled="disabled"</c:if>
                                               <c:if test="${proTypeId==4 }">checked="true"</c:if> value="4">虚拟商品(流量包，无需物流)
                                    </label>
                                </c:if>
                                <c:if test="${isOrder == 1 }">
                                    <p class="help-desc">商品已经被购买，不能修改商品类型</p>
                                </c:if>
                            </div>
                        </div>
                        <c:if test="${cardList != null && cardList.size() > 0 && empty noChangeFlow}">
                            <div class="control-group proTypeDiv memberTypeDiv" style="<c:if test="${proTypeId!=2 }">display:none</c:if>">
                                <label for="" class="control-label text-top">会员卡类型：</label>
                                <div class="controls">
                                    <c:forEach var="card" items="${cardList }" varStatus="i">
                                        <label class="radio inline">
                                            <input type="radio" name="memberType" class="memberType"
                                                   <c:if test="${isOrder == 1 }">disabled="disabled"</c:if>
                                                   <c:if test="${memberType==card.ctId || (memberType == 0 && i.index==0)}">checked="true"</c:if> value="${card.ctId }"
                                                   jq="${card.buyMoney }">${card.ctName }
                                        </label>
                                    </c:forEach>
                                </div>
                            </div>
                        </c:if>
                        <script type="text/javascript">
                            var cardMessage = new Object();
                        </script>
                        <c:set var="cardMessage" value=""></c:set>
                        <c:if test="${cardReceiveList != null && cardReceiveList.size() > 0 && empty noChangeFlow}">
                            <div class="control-group proTypeDiv cardTypeDiv" style="<c:if test="${proTypeId!=3 }">display:none</c:if>">
                                <label for="" class="control-label text-top">卡券包：</label>
                                <div class="controls">
                                    <c:forEach var="card" items="${cardReceiveList }" varStatus="i">
                                        <c:if test="${cardType==card.id || (cardType == 0 && i.index==0)}">
                                            <c:set var="cardMessage" value="${card.messageList }"></c:set>
                                        </c:if>
                                        <label class="radio inline">
                                            <input type="radio" name="cardType" class="cardType"
                                                   <c:if test="${isOrder == 1 }">disabled="disabled"</c:if>
                                                   <c:if test="${cardType==card.id || (cardType == 0 && i.index==0)}">checked="true"</c:if> value="${card.id }"
                                                   jq="${card.buyMoney }">${card.cardsName }
                                        </label>
                                        <br/>
                                        <script type="text/javascript">
                                            cardMessage["${card.id}"] = ${card.messageList };
                                        </script>
                                    </c:forEach>
                                    <table class="cardTab">
                                        <thead>
                                        <tr>
                                            <th class="text-center specCla" id="14">卡券名</th>
                                            <th style="width: auto;">卡券数量</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <c:if test="${!empty cardMessage }">
                                            <c:forEach var="card" items="${cardMessage }">
                                                <tr>
                                                    <td>${card.cardName }</td>
                                                    <td>${card.num }</td>
                                                </tr>
                                            </c:forEach>
                                        </c:if>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </c:if>
                        <c:if test="${!empty newFlowList }">
                            <div class="control-group proTypeDiv FlowDiv" style="<c:if test="${proTypeId!=4 }">display:none</c:if>">
                                <label for="" class="control-label text-top">流量包：</label>
                                <div class="controls">
                                    <input type="hidden" name="flowRecordId" value="${pro.flowRecordId }"/>
                                    <table class="cardTab">
                                        <thead>
                                        <tr>
                                            <th class="text-center" width="10">&nbsp;&nbsp;</th>
                                            <th class="text-center">流量包名</th>
                                            <th class="text-center" style="width: auto;">流量包数量</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <c:if test="${!empty newFlowList}">
                                            <c:forEach var="flow" items="${newFlowList }" varStatus="i">
                                                <tr>
                                                    <td width="10"><input type="radio" name="flowId" class="flowId"
                                                                          <c:if test="${isOrder == 1 }">disabled="disabled"</c:if>
                                                                          <c:if test="${flowId==flow.id || (flowId == 0 && i.index==0)}">checked="true"</c:if> value="${flow.id }"
                                                                          count="${flow.count }"/></td>
                                                    <td>${flow.type }M流量包</td>
                                                    <td>${flow.count }</td>
                                                </tr>
                                            </c:forEach>
                                        </c:if>
                                        <c:if test="${!empty busFlow }">
                                            <tr>
                                                <td width="10"><input type="radio" name="flowId" class="flowId"
                                                                      <c:if test="${isOrder == 1 }">disabled="disabled"</c:if>
                                                                      <c:if test="${flowId==busFlow.id || (flowId == 0 && i.index==0)}">checked="true"</c:if> value="${busFlow.id }"
                                                                      count="${busFlow.count }"/></td>
                                                <td>${busFlow.type }M流量包</td>
                                                <td>${busFlow.count }</td>
                                            </tr>
                                        </c:if>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </c:if>
                        <div id="" class="pre-sale-warp" style="display:none;"><%-- <c:if test='${proTypeId!=0 }'>display:none;</c:if> --%>
                            <div class="control-group">
                                <label for="" class="control-label">预售设置：</label>
                                <div class="controls">
                                    <label class="checkbox inline"> <input type="checkbox"
                                                                           name="isPresell" class="isPresell" value="1"
                                                                           <c:if test="${isPresell==1 }">checked="true"</c:if>>预售商品
                                    </label>
                                </div>
                            </div>
                            <div class="control-group pre-sale-item"
                                 <c:if test="${isPresell==1 }">style="display:block"</c:if>>
                                <label for="" class="control-label text-top">预售结束时间</label>
                                <div class="controls controls2">
                                    <input type="text" name="proPresellEnd"
                                           class="proPresellEnd date1" id="pre-end-time"
                                           value="<c:if test="${!empty pro}">${pro.proPresellEnd }</c:if>"/>
                                    <p class="yushoulVali red"></p>
                                </div>

                            </div>
                            <div class="control-group pre-sale-item"
                                 <c:if test="${isPresell==1 }">style="display:block"</c:if>>
                                <label for="" class="control-label text-top">预计发货时间</label>
                                <div class="controls controls2">
                                    <input type="text" name="proDeliveryStart"
                                           class="proDeliveryStart date2" id="sale-start-time"
                                           value="<c:if test="${!empty pro}">${pro.proDeliveryStart }</c:if>"/>
                                    至 <input type="text" name="proDeliveryEnd"
                                             class="proDeliveryEnd date2" id="sale-end-time"
                                             value="<c:if test="${!empty pro}">${pro.proDeliveryEnd }</c:if>"/>
                                    <p class="fahuoVali red"></p>
                                </div>
                            </div>
                            <div class="control-group pre-sale-item help-desc">
                                注意：目前预售设置仅仅用于商品详情展示，不会关联到订单的处理流程，预售结束，商品也不会自动下架，请务必按照约定时间发货以免引起客户投诉。
                            </div>
                        </div>
                    </div>
                    <!--库存/规格-->
                    <div class="group-inner">
                        <h2 class="box-title">库存/规格：</h2>
                        <div class="control-group">
                            <label for="" class="control-label text-top">商品规格：</label>
                            <input type="hidden" class="isDelSpec" value='<c:if test="${!empty isDelSpec }">${isDelSpec }</c:if>'/>
                            <input type="hidden" class="isType" value='<c:if test="${!empty isType }">${isType }</c:if>'>
                            <div class="fade-box" style="position: relative;  display: inline-block; width: 460px;">
                                <div class="controls specification">
                                    <c:set var="isImage" value="false"></c:set>
                                    <c:if test="${!empty specList }">
                                        <c:forEach var="spec" items="${specList }" varStatus="i">
                                            <div class="group-list" index="${i.index }">
                                                <div class="sku-sub-group ">
                                                    <div class="sku-group-title delParent">
                                                        <select name="select2-container" class="spec-contain"
                                                                nameId="${spec.specNameId}" index="${i.index }"
                                                                id="s2id_autogen1" data-placeholder="选规格"
                                                                style="width: 100px; display: block;" <c:if
                                                                test="${(!empty isDelSpec && isDelSpec == 0) || noUpSpec == 1 }"> disabled="disabled"</c:if>>
                                                        </select>
                                                        <c:if test="${i.index == 0 }">
                                                            <c:if test="${empty isDelSpec || isDelSpec == 1 }">
                                                                <label for=""><input type="checkbox"
                                                                                     name="addPicture" id="addPicture" value="" <c:if
                                                                        test="${(!empty isDelSpec && isDelSpec == 0 ) || noUpSpec == 1}"> disabled="disabled"</c:if>>添加规格图片</label>
                                                            </c:if>
                                                        </c:if>
                                                        <c:if test="${(empty isDelSpec || isDelSpec == 1) && noUpSpec != 1}">
                                                            <a class="remove-sku-group" onclick="del(this)">×</a>
                                                        </c:if>
                                                    </div>
                                                    <div class="sku-group-cont">
                                                        <div style="display: block;">
                                                            <div class="js-sku-atom-list sku-atom-list"
                                                                 style="display: inline-block;">
                                                                <c:if test="${!empty spec.specValues }">
                                                                    <c:forEach var="sValue" items="${spec.specValues }">
                                                                        <div
                                                                                class="sku-atom delParent <c:if test="${i.index == 0 }">active</c:if>">
                                                                            <span class="groupName" id="${sValue.specValueId }">${sValue.specValue }</span>
                                                                            <c:if test="${(empty isDelSpec || isDelSpec == 1 ) && noUpSpec != 1}">
                                                                                <div
                                                                                        class="atom-close close-modal small js-remove-sku-atom"
                                                                                        onclick="delSpec(this)">×
                                                                                </div>
                                                                            </c:if>
                                                                            <c:if test="${i.index == 0 }">
                                                                                <div class="upload-img-wrap "
                                                                                     style="display: block;">
                                                                                    <div class="arrow"></div>

                                                                                    <div class="js-upload-container"
                                                                                         style="position: relative;">
                                                                                        <div class="add-image js-btn-add"
                                                                                             <c:if test="${(empty isDelSpec || isDelSpec == 1) && noUpSpec != 1}">onclick="materiallayer(1,this)"</c:if>>
                                                                                            <c:if test="${sValue.specImage == null || sValue.specImage==''}">
                                                                                                +
                                                                                            </c:if>
                                                                                            <c:if test="${sValue.specImage != null && sValue.specImage!=''}">
                                                                                                <c:set var="isImage" value="true"></c:set>
                                                                                                <img src="${imgUrl}${sValue.specImage }"/>
                                                                                                <input type='hidden' class='imageInp'
                                                                                                       value="${sValue.specImage }"/>
                                                                                            </c:if>
                                                                                        </div>
                                                                                    </div>
                                                                                </div>
                                                                            </c:if>
                                                                        </div>
                                                                        <script type="text/javascript">
                                                                            var defaultKey = "${spec.specNameId}"
                                                                                + "_"
                                                                                + "${sValue.specValueId}";
                                                                            specIdObj["${sValue.specValueId}"] = "${sValue.id}";
                                                                            specDefaultObj[defaultKey] = "${sValue.id}";
                                                                        </script>
                                                                    </c:forEach>
                                                                </c:if>
                                                            </div>
                                                            <c:if test="${(empty isDelSpec || isDelSpec == 1 ) && noUpSpec != 1}">
                                                                <a href="javascript:;" tabindex="0" id="popover"
                                                                   class="js-add-sku-atom add-sku" data-toggle="popover"
                                                                   data-placement="bottom" data-original-title="" title="">+添加</a>
                                                            </c:if>
                                                        </div>
                                                    </div>
                                                    <c:if test="${i.index == 0 }">
                                                        <div class="sku-group-warning">
                                                            <p class="help-desc">目前只支持为第一个规格设置不同的规格尺寸</p>
                                                            <p class="help-desc">设置后，用户选择不同规格会显示不同图片</p>
                                                            <!-- <p class="help-desc">建议图片比例：1.4</p> -->
                                                            <p class="help-desc">建议图片尺寸：870px X 716px</p>
                                                        </div>
                                                    </c:if>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </c:if>
                                    <script type="text/javascript">
                                        var isImage = "${isImage}";
                                        if (isImage == "true") {
                                            $("#addPicture").attr("checked",
                                                true);
                                        } else {
                                            $(".upload-img-wrap").hide();
                                            $(".upload-img-wrap").parent()
                                                .removeClass("active");
                                        }
                                    </script>
                                    <c:if test="${(empty isDelSpec || isDelSpec == 1) && noUpSpec != 1}">
                                        <div class="add-project-spe"
                                             style="<c:if test="${!empty specList && specList.size() >= 3 }">display:none</c:if>">
                                            <input type="button" name="add-spe-btn" id="add-spe-btn"
                                                   class="spe-btn" value="添加项目规格"/>
                                        </div>
                                    </c:if>
                                </div>
                                <c:if test="${(!empty isDelSpec && isDelSpec == 0 ) || noUpSpec == 1}">
                                    <input type="hidden" class="isDelSpec" value="${isDelSpec }"/>
                                    <div class="control-fade"
                                         style="color:#fff;  width:495px; height:100%; z-index:999;text-align:center; background-color: rgba(0,0,0,0.4);position:absolute; top:0; left:0; ">
                                        <p style="position: absolute; width: 100%; height: 30px; text-align: center; top:50%; margin-top: -15px; line-height: 30px">
                                            <c:if test="${isType == 1 }">该商品已经加入团购。不能修改商品规格</c:if>
                                            <c:if test="${isType == 2 }">该商品已经加入秒杀。不能修改商品规格和商品库存</c:if>
                                            <c:if test="${isType == 3 }">该商品已经加入拍卖。不能修改商品规格和商品库存</c:if>
                                            <c:if test="${isType == 4 }">该商品已经加入预售。不能修改商品规格和商品库存</c:if>
                                            <c:if test="${isType == 5 }">该商品已经加入批发。不能修改商品规格</c:if>
                                            <c:if test="${noUpSpec == 1 }">商城不能修改商品规格和库存，请去进销存修改</c:if>
                                        </p>
                                    </div>
                                </c:if>
                            </div>
                        </div>
                        <div class="control-group" style="display: none;">
                            <label for="" class="control-label ">商品库存：</label>
                            <!-- <div style="position: relative;  display: inline-block; width: 460px;"> -->
                            <div class="controls text-top sku-stock" id="createTable">
                                <table border="0" cellspacing="0" cellpadding="0" width="100%"
                                       class="table-sku-stock">
                                    <thead>
                                    <tr>
                                        <th class="text-center">尺寸</th>
                                        <th class="text-center">颜色</th>
                                        <th class="th-price">价格(元)</th>
                                        <th class="th-stock">库存</th>
                                        <th class="th-code">商家编码</th>
                                        <th class="text-right">销量</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    </tbody>
                                    <tfoot>
                                    <tr height="44px">
                                        <td colspan="5">
                                            <div class="batch-opts">
                                                批量设置 <span class="js-batch-type" style="display: inline;">
																<a class="js-batch-price" href="javascript:;"
                                                                   errormsg="价格最多只能输入6位小数">价格</a> &nbsp;&nbsp; <a
                                                    class="js-batch-stock" href="javascript:;"
                                                    errormsg="库存最多只能输入6位数字">库存</a>
															</span> <span class="js-batch-form"> <input type="text"
                                                                                                        class="js-batch-txt input-mini" placeholder=""
                                                                                                        maxlength="10"> <a href="javascript:;"
                                                                                                                           class="js-batch-save">保存</a> <a href="javascript:;"
                                                                                                                                                           class="js-batch-cancel">取消</a>
															</span>
                                            </div>
                                        </td>
                                    </tr>
                                    </tfoot>
                                </table>
                            </div>
                            <%-- <c:if test="${!empty isDelSpec && isDelSpec == 0 }">
                            <div class="control-fade" style="color:#fff;  width:495px; height:100%; z-index:999;text-align:center; background-color: rgba(0,0,0,0.4);position:absolute; top:0; left:0; ">
                                  <p style="position: absolute; width: 100%; height: 30px; text-align: center; top:50%; margin-top: -15px; line-height: 30px">
                                  <c:if test="${isType == 1 }">该商品已经加入团购。不能修改商品规格</c:if>
                                  <c:if test="${isType == 2 }">该商品已经加入秒杀。不能修改商品规格</c:if>
                                  </p>
                            </div>
                            </c:if>
                        </div> --%>
                        </div>
                        <c:set var="isShowStock" value="false"></c:set>
                        <c:if test="${!empty pro}">
                            <c:if test="${pro.isShowStock==1}">
                                <c:set var="isShowStock" value="true"></c:set>
                            </c:if>
                        </c:if>
                        <div class="control-group param-parent">
                            <label for="" class="control-label text-top">商品参数：</label>
                            <div class="controls ">
                                <c:if test="${!empty paramList }">
                                    <c:forEach var="params" items="${paramList }" varStatus="i">
                                        <div class="sku-param-title" index="${i.index }" <c:if test="${i.index>0 }">style="margin-top:10px;"</c:if>>
                                            <select class="chosen-container chosen-contain param-contain"
                                                    nameId="${params.paramsNameId}" nameVal="${params.paramsName}"
                                                    data-selected-id="" data-placeholder="${params.paramsName}"
                                                    style="width: 150px !important; border-radius: 5px;">
                                            </select>：
                                            <select class="chosen-container chosen-contain param-value-contain"
                                                    nameId="${params.paramsValueId}" nameVal="${params.paramsValue}"
                                                    data-selected-id="" data-placeholder="${params.paramsValue}" disabled="disabled"
                                                    style="width: 150px !important; border-radius: 5px;">
                                            </select>
                                            <span>
											<a href="javascript:void(0);" class="add" onclick="addParam(this);" <c:if test="${i.index>0 }">style="display:none;"</c:if>>新增</a>
											<a href="javascript:void(0);" class="del" onclick="delParam(this);" <c:if test="${i.index==0 }">style="display:none;"</c:if>>删除</a>
										</span>
                                        </div>
                                        <script type="text/javascript">
                                            var defaultKey = "${params.paramsNameId}"
                                                + "_"
                                                + "${params.paramsValueId}";
                                            specIdObj["${params.paramsValueId}"] = "${params.id}";
                                            paramDefaultObj[defaultKey] = "${params.id}";
                                        </script>
                                    </c:forEach>
                                </c:if>
                                <span class="proCodeVali red"></span>
                            </div>
                        </div>

                        <div class="control-group">
                            <label for="" class="control-label "><em
                                    class="required">*</em>总库存：</label>
                            <div class="controls text-top">
                                <c:if test="${noUpInvNum == 0 || empty noUpInvNum}">
                                <input type="text" name="proStockTotal" id="proStockTotal"
                                       value="<c:if test="${!empty pro}">${pro.proStockTotal }</c:if>"
                                       class="input-small" datatype="^\d{1,6}$"
                                       <c:if test="${!empty isDelSpec && isDelSpec == 0 && isType != 5}">disabled="disabled"</c:if>
                                       errormsg="库存必须是1~6位数字"/> <label class="checkbox inline">
                                <input type="checkbox" name="isShowStock" class="isShowStock"
                                       value="0"
                                       <c:if test="${isShowStock==true}">checked="checked"</c:if>>页面不显示商品库存
                                </c:if>
                                <c:if test="${noUpInvNum == 1 }"><c:if test="${!empty pro}">${pro.proStockTotal }</c:if></c:if>
                            </label>
                                <p class="help-desc">总库存为 0 时，会上架到『已售罄的商品』列表里</p>
                                <p class="help-desc">发布后商品同步更新，以库存数字为准</p>
                                <p class="flow_help red" style="display: none;color:#f00">商品总库存，不能超过<em></em></p>
                                <span class="proStockTotalVali red" style="color:#f00"></span>
                            </div>
                        </div>
                        <div class="control-group">
                            <label for="" class="control-label ">商家编码：</label>
                            <div class="controls text-top">
                                <input type="text" name="proCode"
                                       value="<c:if test="${!empty pro}">${pro.proCode }</c:if>"
                                       class="input-small proCode"
                                       datatype="^[\u4E00-\u9FA5\uf900-\ufa2d\w\.\s]{0,15}$"
                                       errormsg="商家编码最多输入15位字符串"/> <span class="proCodeVali red"></span>
                            </div>
                        </div>
                    </div>
                    <!--商品信息-->
                    <div class="group-inner">
                        <h2 class="box-title">商品信息：</h2>
                        <div class="control-group">
                            <label for="" class="control-label text-top"><em
                                    class="required">*</em>商品名称：</label>
                            <div class="controls">
                                <input type="text" class="input-xxlarge proName" name="proName"
                                       maxlength="100" not-null="1"
                                       errormsg="商品名称最多输入100位字符串"
                                       value="<c:if test="${!empty pro}">${pro.proName }</c:if>"/>
                                <div class="proNameVali red"></div>
                            </div>
                        </div>
                        <div class="control-group">
                            <label for="" class="control-label text-top"><em
                                    class="required">*</em>价格：</label>
                            <div class="controls">
                                <div class="input-prepend" style="vertical-align: top;">
                                    <span class="add-on">￥</span> <input data-stock-id="total"
                                                                         type="text" maxlength="9" name="proPrice"
                                                                         value="<c:if test="${!empty pro}">${pro.proPrice }</c:if>"
                                                                         class="control-price input-small proPrice price"
                                                                         datatype="^[0-9]{1}\d{0,5}(\.\d{1,2})?$"
                                                                         errormsg="价格最多只能输入六位小数，如：30.00">
                                </div>
                                <input type="text" class="input-small text-top proCostPrice price"
                                       placeholder="原价：¥199.99" name="proCostPrice"
                                       value="<c:if test="${!empty pro}">${pro.proCostPrice }</c:if>"
                                       datatype="^([0-9]{0,1}\d{0,5})(\.\d{1,2})?$"
                                       errormsg="原价最多只能输入六位小数，如：30.00">
                                <div class="proPriceVali red"></div>
                                <div class="proCostPriceVali red"></div>
                            </div>
                        </div>
                        <div class="control-group">
                            <label for="" class="control-label text-top"><em
                                    class="required">*</em>商品图片：</label>
                            <div class="picture-list controls text-top">
                                <ul class="picture-list app-image-list clearfix"
                                    id="ui-sortable">
                                    <c:if test="${!empty imageList}">
                                        <c:forEach var="image" items="${imageList }">
                                            <li class="sort delParent"><img
                                                    src="${imgUrl}${image.imageUrl }"/> <a
                                                    class="js-delete-picture close-modal small hide"
                                                    onclick="delImg(this)">×</a> <input type='hidden'
                                                                                        class='imageInp' value="${image.imageUrl }"/> <input
                                                    type='hidden' class='imageId' value="${image.id }"/></li>
                                            <script type="text/javascript">
                                                imagDefaultObj["${image.id }"] = "${image.id }";
                                            </script>
                                        </c:forEach>
                                    </c:if>
                                    <li class="sort delParent">
                                        <!-- <img src="/images/addPic.jpg"/> --> <a
                                            href="javascript:;" class="add-goods js-add-picture"
                                            onclick="materiallayer(0,this)">+加图</a>
                                    </li>
                                </ul>
                                <div class="imageVali red"></div>
                            </div>
                            <!-- <p class="help-desc align">建议图片比例：1.4
                                像素；您可以拖拽图片调整图片顺序。第一张图为主图</p> -->
                            <p class="help-desc align">建议图片尺寸：870px X 716px；您可以拖拽图片调整图片顺序。第一张图为主图</p>
                        </div>
                        <div class="control-group">
                            <label class="control-label text-top">商品标签：</label>
                            <div class="controls">
                                <input type="text" class="input-small proLabel" name="proLabel"
                                       maxlength="2" value="<c:if test="${!empty pro }">${pro.proLabel }</c:if>"
                                       errormsg="商品标签最多输入2个字符串"/>
                                <div class="proLabelVali red"></div>
                            </div>
                            <p class="help-desc align">商品标签最多输入2个字符串</p>
                        </div>
                        <div class="control-group">
                            <label class="control-label text-top">商品重量：</label>
                            <div class="controls">
                                <input type="text" class="input-small proWeight" name="proWeight" placeholder="请输入商品重量"
                                       maxlength="9" value="<c:if test="${!empty pro && pro.proWeight > 0}">${pro.proWeight }</c:if>"
                                       datatype="^([0-9]{0,1}\d{0,5})(\.\d{1,2})?$"
                                       errormsg="商品重量最多输入2个字符串"/>g
                                <div class="proLabelVali red"></div>
                            </div>
                            <p class="help-desc align">商品重量最多只能输入大于0的六位小数，如：30.00</p>
                        </div>
                        <div class="control-group">
                            <label for="" class="control-label">销量基数：</label>
                            <div class="controls">
                                <label class="checkbox inline">
                                    <input type="text" name="salesBase"
                                           value="<c:if test="${!empty pro && pro.salesBase > 0}">${pro.salesBase }</c:if>"
                                           class="input-small salesBase" datatype="^[1-9]{0,1}\d{0,7}$" maxlength="8" placeholder="请输入销量基数"
                                           errormsg="最多只能输入8位数字"/>
                                </label>
                            </div>
                            <p class="help-desc align">最多只能输入9位数字</p>
                        </div>
                    </div>
                    <!--物流/其他-->
                    <div class="group-inner">
                        <h2 class="box-title">其他</h2>
                        <div class="control-group">
                            <label for="" class="control-label">每人限购：</label>
                            <div class="controls">
                                <input type="text" name="proRestrictionNum"
                                       value="<c:if test="${!empty pro}">${pro.proRestrictionNum }</c:if>"
                                       class="input-small proRestrictionNum" datatype="^\d{0,6}$"
                                       errormsg="只能输入6位数字"/> <span class="grey help-desc">0代表不限购</span>
                            </div>
                            <div class="proRestrictionNumVali red"></div>
                        </div>
                        <div class="control-group">
                            <label for="" class="control-label text-top">完成订单后允许退款天数：</label>
                            <div class="controls">
                                <input type="text" name="returnDay"
                                       value="<c:if test="${!empty pro && pro.returnDay != ''}">${pro.returnDay }</c:if><c:if test="${(empty pro || pro.returnDay == '')&&pro.returnDay!=0}">7</c:if><c:if test="${pro.returnDay==0 }">0</c:if>"
                                       class="input-small returnDay" datatype="^\d{0,6}$" maxlength="6"
                                       errormsg="只能输入6位数字"/>天
                            </div>
                            <p class="help-desc align">完成订单后在有效期内允许退款，默认7天,不填写或填写0则在订单完成后不允许退款</p>
                            <div class="returnDayVali red align"></div>
                        </div>
                        <c:set var="isMember" value="1"/>
                        <c:set var="isCoupons" value="1"/>
                        <c:set var="isInvoice" value="0"/>
                        <c:set var="isWarranty" value="0"/>
                        <c:set var="isIntegralChangePro" value="0"></c:set>
                        <c:set var="isShowViews" value="0"></c:set>
                        <c:set var="isReturn" value="1"></c:set>
                        <c:set var="isIntegralDeduction" value="1"></c:set>
                        <c:set var="isFenbiDeduction" value="1"></c:set>
                        <c:set var="isFenbiChangePro" value="0"></c:set>
                        <c:if test="${!empty pro}">
                            <c:set var="isMember" value="${pro.isMemberDiscount }"/>
                            <c:set var="isCoupons" value="${pro.isCoupons }"/>
                            <c:set var="isInvoice" value="${pro.isInvoice }"/>
                            <c:set var="isWarranty" value="${pro.isWarranty }"/>
                            <c:set var="isIntegralChangePro" value="${pro.isIntegralChangePro }"></c:set>
                            <c:set var="isShowViews" value="${pro.isShowViews }"></c:set>
                            <c:set var="isReturn" value="${pro.isReturn }"></c:set>
                            <c:set var="isIntegralDeduction" value="${pro.isIntegralDeduction }"></c:set>
                            <c:set var="isFenbiDeduction" value="${pro.isFenbiDeduction }"></c:set>
                            <c:set var="isFenbiChangePro" value="${pro.isFenbiChangePro }"></c:set>
                        </c:if>
                        <div class="control-group">
                            <label for="" class="control-label">会员折扣：</label>
                            <div class="controls">
                                <label class="checkbox inline"> <input type="checkbox"
                                                                       name="isMemberDiscount" value="1"
                                                                       <c:if test="${isMember==1 }">checked="checked"</c:if>>参加会员折扣
                                </label>
                            </div>
                        </div>
                        <div class="control-group">
                            <label for="" class="control-label">优惠券：</label>
                            <div class="controls">
                                <label class="checkbox inline"> <input type="checkbox"
                                                                       name="isCoupons" value="1" class="isCoupons"
                                                                       <c:if test="${isCoupons==1 }">checked="checked"</c:if>>使用优惠券
                                </label>
                            </div>
                        </div>
                        <div class="control-group">
                            <label for="" class="control-label">是否允许积分抵扣：</label>
                            <div class="controls">
                                <label class="checkbox inline"> <input type="checkbox"
                                                                       name="isIntegralDeduction" value="1" class="isIntegralDeduction"
                                                                       <c:if test="${!empty pro && isIntegralDeduction==1 }">checked="checked"</c:if>>允许积分抵扣
                                </label>
                            </div>
                        </div>
                        <div class="control-group">
                            <label for="" class="control-label">是否允许粉币抵扣：</label>
                            <div class="controls">
                                <label class="checkbox inline"> <input type="checkbox"
                                                                       name="isFenbiDeduction" value="1" class="isFenbiDeduction"
                                                                       <c:if test="${!empty pro && isFenbiDeduction==1 }">checked="checked"</c:if>>允许粉币抵扣
                                </label>
                            </div>
                        </div>
                        <div style="display: none;">
                            <div class="control-group">
                                <label for="" class="control-label">积分兑换商品：</label>
                                <div class="controls">
                                    <label class="checkbox inline"> <input type="checkbox"
                                                                           name="isIntegralChangePro" value="1" class="isIntegralChangePro"
                                                                           <c:if test="${!empty pro && isIntegralChangePro==1 }">checked="checked"</c:if>>允许积分兑换商品
                                    </label>
                                </div>
                            </div>
                            <div class="control-group integralDiv" <c:if test="${empty pro || isIntegralChangePro!=1 }">style="display:none;"</c:if>>
                                <label for="" class="control-label">兑换积分值：</label>
                                <div class="controls">
                                    <label class="checkbox inline">
                                        <input type="text" name="changeIntegral"
                                               value="<c:if test="${!empty pro && pro.changeIntegral != ''}">${pro.changeIntegral }</c:if>"
                                               class="input-small changeIntegral price" datatype="^[1-9]{0,1}\d{0,5}$" maxlength="6"
                                               errormsg="只能输入大于0的6位数字"/>
                                    </label>
                                </div>
                                <p class="help-desc align">选中允许积分兑换商品，兑换积分值才生效</p>
                                <div class="changeIntegralVali red align"></div>
                            </div>
                        </div>
                        <div class="control-group">
                            <label for="" class="control-label">粉币兑换商品：</label>
                            <div class="controls">
                                <label class="checkbox inline"> <input type="checkbox"
                                                                       name="isFenbiChangePro" value="1" class="isFenbiChangePro"
                                                                       <c:if test="${!empty pro && isFenbiChangePro==1 }">checked="checked"</c:if>>允许粉币兑换商品
                                </label>
                            </div>
                        </div>
                        <div class="control-group fenbiDiv" <c:if test="${empty pro || isFenbiChangePro!=1 }">style="display:none;"</c:if>>
                            <label for="" class="control-label">兑换粉币值：</label>
                            <div class="controls">
                                <label class="checkbox inline">
                                    <input type="text" name="changeFenbi"
                                           value="<c:if test="${!empty pro && pro.changeFenbi != '' && pro.changeFenbi > 0}">${pro.changeFenbi }</c:if>"
                                           class="input-small changeFenbi price" datatype="^([0-9]{0,1}\d{0,5})(\.\d{1,2})?$" maxlength="8"
                                           errormsg="只能输入大于0的6位小数"/>
                                </label>
                            </div>
                            <p class="help-desc align">选中允许粉币兑换商品，兑换粉币值才生效</p>
                            <div class="changeFenbiVali red align"></div>
                        </div>
                        <div class="control-group">
                            <label for="" class="control-label">是否显示浏览量：</label>
                            <div class="controls">
                                <label class="checkbox inline"> <input type="checkbox"
                                                                       name="isShowViews" value="1" class="isShowViews"
                                                                       <c:if test="${!empty pro && isShowViews==1 }">checked="checked"</c:if>>显示浏览量
                                </label>
                            </div>
                        </div>
                        <div class="control-group viewDiv" <c:if test="${empty pro || isShowViews!=1 }">style="display:none;"</c:if>>
                            <label for="" class="control-label">浏览量：</label>
                            <div class="controls">
                                <label class="checkbox inline">
                                    <input type="text" name="viewsNum" id=""
                                           value="<c:if test="${!empty pro}">${pro.viewsNum }</c:if>"
                                           class="input-small viewsNum" datatype="^[1-9]{0,1}\d{0,8}$" maxlength="9"
                                           errormsg="最多只能输入9位数字"/>
                                </label>
                            </div>
                            <p class="help-desc align">最多只能输入9位数字</p>
                            <div class="viewsNumVali red align"></div>
                        </div>
                        <div class="control-group">
                            <label for="" class="control-label">是否允许退款：</label>
                            <div class="controls">
                                <label class="checkbox inline"> <input type="checkbox"
                                                                       name="isReturn" value="1" class="isReturn"
                                                                       <c:if test="${!empty pro && isReturn==1 }">checked="checked"</c:if>>允许商品退款
                                </label>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label">发票：</label>
                            <div class="controls">
                                <label class="radio inline"> <input type="radio"
                                                                    name="isInvoice" value="0"
                                                                    <c:if test="${isInvoice==0 }">checked="checked"</c:if>>无
                                </label> <label class="radio inline"> <input type="radio"
                                                                             name="isInvoice" value="1"
                                                                             <c:if test="${isInvoice==1 }">checked="checked"</c:if>>有
                            </label>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label">保修：</label>
                            <div class="controls">
                                <label class="radio inline"> <input type="radio"
                                                                    name="isWarranty" value="0"
                                                                    <c:if test="${isWarranty==0 }">checked="checked"</c:if>>无
                                </label> <label class="radio inline"> <input type="radio"
                                                                             name="isWarranty" value="1"
                                                                             <c:if test="${isWarranty==1 }">checked="checked"</c:if>>有
                            </label>
                            </div>
                        </div>
                    </div>
                    <div class="bottom">
                        <input type="button" class="btn subBtn"   value="下一步"/>
                    </div>
                </div>
                <div class="right-box">
                    <div class="phone">
                        <iframe name="oneIframe" id="oneIframe"
                                src="/jsp/mall/product/iframe/product_edit_one.jsp"
                                id="oneIframe" width="234" height="420" style="border: 0;"></iframe>
                    </div>
                </div>
            </div>
            <div class="twoPro" style="display:none;">
                <div class="editWarp">
                    <div class="contentWarp">
                        <div class="left-box">
                            <div class="editBox">
                                <div class="control-group">
                                    <label class="control-label">商品信息：</label>
                                    <div class="controls">
										<textarea class="productMessage" cols="60" rows="5"><c:if
                                                test="${!empty proDetail }">${proDetail.productMessage }</c:if></textarea>
                                    </div>
                                </div>
                                <div class="control-group">
                                    <label class="control-label">商品简介：</label>
                                    <div class="controls">
										<textarea class="productIntrodu" cols="60" rows="5"><c:if
                                                test="${!empty proDetail }">${proDetail.productIntrodu }</c:if></textarea>
                                    </div>
                                </div>

                                <div class="control-group" style="margin-top:10px;">
                                    <label class="control-label">商品详情：</label>
                                    <div class="controls">
										<textarea class="productDetail"><c:if
                                                test="${!empty proDetail }">${proDetail.productDetail }</c:if></textarea>
                                        <p class="help-desc">建议图片尺寸：870px X 716px</p>
                                    </div>
                                </div>


                                <input type="hidden" class="detailId" value="<c:if test="${!empty proDetail }">${proDetail.id }</c:if>"/>
                                <div class="arrow"></div>

                            </div>
                        </div>
                        <div class="right-box">

                            <div class="phone">

                                <iframe name="twoIframe" id="twoIframe"
                                        src="/jsp/mall/product/iframe/product_edit_two.jsp"
                                        width="234" height="420" style="border: 0;"></iframe>
                            </div>

                        </div>
                        <div class="bottom">
                            <input type="button" name="prePage" id="prePage" class="prePage page" value="上一步"/>
                            <input type="button" name="nextPage" id="savePage2" class="savePage page" value="保存"/>
                            <input type="button" name="nextPage" id="savePage" class="saveStatusPage page" value="保存并送审"/>
                            <!-- <input type="button" name="preview" id="preview" class="preview page" value="预览" /> -->
                        </div>

                    </div>
                </div>
            </div>
        </form>
    </div>
</div>
<div class="firstGroup" style="display: none;">
    <div class="group-list" index="0">
        <div class="sku-sub-group ">
            <div class="sku-group-title delParent">
                <select name="select2-container" class="spec-contain" index="0"
                        id="s2id_autogen1" data-placeholder="选规格"
                        style="width: 100px; display: none;">
                </select> <label for=""><input type="checkbox" name="addPicture"
                                               id="addPicture" value="">添加规格图片</label> <a
                    class="remove-sku-group" onclick="del(this)">×</a>
            </div>
            <div class="sku-group-cont">
                <div style="display: none;">
                    <div class="js-sku-atom-list sku-atom-list" style="display: none;">
                    </div>
                    <a href="javascript:;" tabindex="0"
                       class="js-add-sku-atom add-sku" data-toggle="popover"
                       data-placement="bottom" data-original-title="" title="">+添加</a>
                </div>
            </div>
            <div class="sku-group-warning">
                <p class="help-desc">目前只支持为第一个规格设置不同的规格尺寸</p>
                <p class="help-desc">设置后，用户选择不同规格会显示不同图片</p>
                <p class="help-desc">建议尺寸：640 x 640像素</p>
            </div>
        </div>
    </div>
</div>
<div class="nextGroup" style="display: none;">
    <div class="group-list" index="1">
        <div class="sku-sub-group ">
            <div class="sku-group-title delParent">
                <select name="select2-container" class="spec-contain" index="0"
                        id="s2id_autogen1" data-placeholder="选规格"
                        style="width: 100px; display: none;">
                </select> <a class="remove-sku-group" onclick="del(this)">×</a>
            </div>
            <div class="sku-group-cont">
                <div style="display: none;">
                    <div class="js-sku-atom-list sku-atom-list" style="display: none;">
                    </div>
                    <a href="javascript:;" tabindex="0" id="popover"
                       class="js-add-sku-atom add-sku" data-toggle="popover"
                       data-placement="bottom" data-original-title="" title="">+添加</a>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="addGroupDivFirst" style="display: none">
    <div class="sku-atom delParent">
        <span class="groupName"  ></span>
        <div class="atom-close close-modal small js-remove-sku-atom"
             onclick="delSpec(this)">×
        </div>
        <div class="upload-img-wrap " style="display: none;">
            <div class="arrow"></div>
            <div class="js-upload-container" style="position: relative;">
                <div class="add-image js-btn-add" onclick="materiallayer(1,this)">
                    +
                </div>
            </div>
        </div>
    </div>
</div>
<div class="addGroupDivNext" style="display: none">
    <div class="sku-atom delParent">
        <span class="groupName" id=""></span>
        <div class="atom-close close-modal small js-remove-sku-atom"
             onclick="delSpec(this)">×
        </div>
    </div>
</div>
<select class="specifaceSelects" id="s2id_autogen1"
        data-placeholder="选规格名" style="width: 100px; display: none;">
    <option value=""></option>
    <c:if test="${!empty specDictMap}">
        <c:forEach items="${specDictMap}" var="dict">
            <option value="${dict.key}">${dict.value}</option>
        </c:forEach>
    </c:if>
</select>
<div class="paramsDivs" style="display: none;">
    <div class="sku-param-title" style="margin-top:10px;">
        <select class="chosen-container chosen-contain param-contain"
                nameId="" nameVal=""
                data-selected-id="" data-placeholder="选择商品参数名"
                style="width: 150px !important; border-radius: 5px;">
        </select>：
        <select class="chosen-container chosen-contain param-value-contain"
                nameId="" nameVal=""
                data-selected-id="" data-placeholder="选择商品参数值" disabled="disabled"
                style="width: 150px !important; border-radius: 5px;">
        </select>
        <span>
				<a href="javascript:void(0);" class="add" onclick="addParam(this);">新增</a>
				<a href="javascript:void(0);" class="del" onclick="delParam(this);" style="display:none;">删除</a>
			</span>
    </div>
</div>
<input type="hidden" class="urls" value="${urls }"/>
<input type="hidden" class="isJxc" value="${empty isJxc ? 1 : isJxc }"/>

<script type="text/javascript" src="/js/util.js"></script>
<script src="/js/plugin/laydate/laydate.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript" src="/js/mall/product/pro_load.js"></script>
<script type="text/javascript" src="/js/mall/product/pro_new_load.js"></script>
<script type="text/javascript" src="/js/mall/product/pro_new_inven.js"></script>
<script type="text/javascript" src="/js/mall/product/pro_edit.js"></script>
<script type="text/javascript">
    var specNameList = "${specDictMap}";

    var groupShopObj = new Object();
    /**
     * 初始化选中分组
     */
    function loadGroup() {
        if (groupDefaults != null) {
            //console.log(groupDefaults)
            var chosenObj = $(".chosen-contain");
            var groupArr = new Array();
            for (var i = 0; i < groupDefaults.length; i++) {
                var group = groupDefaults[i];
                //console.log(group)
                groupArr[i] = group.groupId;

                var val = group.groupId + "_" + group.shopId;
                groupShopObj["" + val + ""] = group.id;
            }
            //console.log(groupShopObj)

            chosenObj.val(groupArr);// 给分组下拉框设置选种值
            chosenObj.trigger("chosen:updated");
        }
    }
    loadGroup();

    var costPrice = $(".proCostPrice").val();
    var isNum = false;
    if (costPrice != null && costPrice != "") {
        costPrice = parseFloat(costPrice);
        if (costPrice > 0) {
            $(".proCostPrice").val(costPrice);
            isNum = true;
        }
    }
    if (!isNum) {
        $(".proCostPrice").val("");
    }
</script>
</body>
</html>