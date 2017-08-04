<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>


<!--右侧qq联系我们-->
<div class="right-float clearfix ">
    <ul>
        <li class="item item1" id="gotop"><a href="javascript:void(0)"></a></li>
        <li class="item item2" id="code1"><a href="javascript:void(0)"></a></li>
        <li class="item item3">
            <iframe name="tencent" style="display:none"></iframe>
            <a href="tencent://message/?Menu=yes&amp;uin=938188163&amp;Service=58&amp;SigT=A7F6FEA02730C98848AA17E7EA0C4541D1AB6B0588A8659BD6EA5ECD767F2949B15FBDFBF7A981BA743090690B5F5F0C3D49F30760535DA6956B8B2F3251EE2A660F99EAF040D30AC05EBFE2B53FD07C41AB564653CFA16E1B10051BDBF4450AF7353F25DEFFEF1B0696B7DA942E97408762B9381F06B602&amp;SigU=30E5D5233A443AB250CC3E8399B705436C072BEB88755A87351A9D17A0D3FE71C9345B22CA2136C780C54D119D3CB86ACBA3C6D2D4C77239B56A1B71B64D87FAE5DDC6E4274947BE"
               target="tencent"></a>
            <!--  <a href="tencent://message/?Menu=yes&amp;uin=4008894522&amp;Service=58&amp;target="_blank"></a> -->
            <!-- <a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=4008894522&site=qq&menu=yes"><img border="0" src="http://wpa.qq.com/pa?p=2:4008894522:51" alt="点击这里给我发消息" title="点击这里给我发消息"/></a> -->
            <!-- WPA Button Begin -->
            <!-- <script charset="utf-8" type="text/javascript" src="http://wpa.b.qq.com/cgi/wpa.php?key=XzkzODE4ODE2M18zNDIwNTZfNDAwODg5NDUyMl8"></script> -->
            <!-- WPA Button End -->
        </li>
        <li class="item item4" id="code2"><a href="javascript:void(0)"></a></li>
        <li class="code"></li>
        <li class="code2">
            <p class="code2Text">24h服务热线</p>
            <p class="code2Text">400 889 4522</p>
        </li>
    </ul>
</div>
<!--右侧qq联系我们 End-->

<footer>
    <div class="footer">
        <div class="foot-con1">
            <a>首页</a>
            <span>|</span>
            <a>产品服务</a>
            <span>|</span>
            <a>合作召集</a>
            <span>|</span>
            <a>案例中心</a>
            <span>|</span>
            <a>帮助中心</a>
        </div>
        <p class="footText">
            <label>服务热线：</label>
            <label>400</label>
            <label>889</label>
            <label>4522</label>
            <label class="statistics">已有<span class="staSum">${busCount.total }</span>商家入驻，今日入驻<span class="staToday">${busCount.daysCount}</span>位</label>
        </p>
        <div class="foot-con2">
            <span>Copyright © 2015 ${domain}. All Rights Reserved.  </span>
            <span>粤ICP备15016183号-3. 经营许可证编号：粤B2-20150533</span>
        </div>
        <p class="foot-con3">广东谷通科技有限公司</p>
    </div>
</footer>
<script>

    $(document).ready(function (e) {
        var iframe = document.getElementById("main");
        if (iframe != undefined) {
            if (iframe.attachEvent) {
                iframe.attachEvent("onload", function () {
                    setTimeout("setIframeHeight()", 200);
                });
            } else {
                iframe.onload = function () {
                    setTimeout("setIframeHeight()", 200);
                };
            }
        }
    });
    function setIframeHeight() {
        var iframe = document.getElementById("main");
        var body = $(iframe).contents().find("body").height();
        var ah = $("#nav #dao-hang0 a li").height()
        var candan = $("#nav #dao-hang0 a").size() * ah + 100;
        if (body < candan) {
            $(iframe).height(candan);
            $(iframe).css("min-height", candan + 'px');
            $(iframe).contents().find("body").height(candan);
            $(iframe).contents().find("body").css("min-height", candan + 'px');
        }
    }
</script>