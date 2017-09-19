$(function () {
    //$("#chooseTr").hide();
    /*var pid = $("#stoProvince").val()==0?"-1":$("#stoProvince").val();
     areaChange(pid,1,1);
     $("#stoCity").val($("#city_").val());
     areaChange($("#city_").val(),2,1);
     $("#stoArea").val($("#area_").val());*/
    if ($("#stoBranchId").val() != 0 && $("#stoBranchId").val() != "") {
        $(".abc.text1").attr("disabled", "disabled");
        $("#stoPid").find("option").eq(0).remove();
    }
    var isWxShopId = $(".isWxShopId").val();
    if (isWxShopId == null || isWxShopId == "" || isWxShopId == 0) {
        $("#wxShopId").removeAttr("disabled");
    }

    $("#stoIsSms").change(function () {
        if ($(this).is(":checked")) {
            $(".tdSMSTel").show();
        } else {
            $(".tdSMSTel").hide();
        }
        loadWindow();
    });

    $(".addTel").click(function () {
        if ($(".inpDiv").length < 5) {
            var div = $(this).parent().clone();

            div.insertAfter($(".telDiv .inpDiv:last"));

            var obj = $(".telDiv .inpDiv:last");
            var iObj = obj.find("i");
            iObj.removeClass("addTel").addClass("delTel");
            iObj.html("删除");
            iObj.unbind("click");
            iObj.bind("click", function () {
                deleteTel(this);
            });
            obj.find("input").val("");
        } else {
            alert("推送手机号码最多能添加5个");
        }
        loadWindow();
    });
    loadWindow();
});
function deleteTel(obj) {
    $(obj).parent().remove();
    loadWindow();
}

/**
 * 上级店铺改变事件
 * @param obj
 */
function branchChange(obj) {
    if ($("#id").val() == "") {
        if ($(obj).val() != 0) {
            $("#chooseTr").show();
            $(".abc").attr("disabled", "disabled");
        } else {
            $("#chooseTr").hide();
            $(".abc").removeAttr("disabled");
        }
    }
}


/**
 * 选择分店
 */
function chooseFd() {
    // parent.openIframeNoScoll("分店选择", "500px", "400px", "/store/chooseFd.do");
    parentOpenIframe("分店选择", "500px", "400px", "/store/chooseFd.do");
}
/**
 * 选择门店
 * @param obj
 */
function shopChange(t) {
    var shopId = $(t).find("option:selected").val();
    if (shopId != null && shopId != "" && shopId != "-1") {
        var obj = new Object();
        $("span.shopSpan#" + shopId + " input").each(function () {
            obj[$(this).attr("ids")] = $(this).val();
        });
        if (obj != null) {
            $("#wxShopId").val(obj.id);
            $("#stoName").val(obj.name);
            $("#stoProvince").val(obj.province);
//			areaChange(obj.province,1,1);
            $("#stoCity").val(obj.city);
            if (obj.city != null && obj.city != "") {
//				areaChange(obj.city,2,1);
                $("#stoArea").val(obj.district);
            } else {
                $("#stoArea").val("请选择");
            }

            $("#longitude").val(obj.longitude);
            $("#latitude").val(obj.latitude);

            $("#stoAddress").val(obj.address);
            $("#stoPhone").val(obj.telephone);
            $("#stoHouseMember").val(obj.detail);
            $("#img1").attr("src", obj.imageUrl);
            var url = obj.imageUrl.split("/upload/")[1];
            if (url != null && url != "") {
                $(".stoPicture").show();
                $(".table-td3 .abc.imgURL").hide();
            } else {
                $(".stoPicture").hide();
                $(".table-td3 .abc.imgURL").show();
            }
            $("#stoPicture").val(url);

            $("#stoName").attr("disabled", "disabled");
            $("#stoProvince").attr("disabled", "disabled");
            $("#stoCity").attr("disabled", "disabled");
            $("#stoArea").attr("disabled", "disabled");
            $("#stoAddress").attr("disabled", "disabled");
            if (obj.telephone != null && obj.telephone != "")
                $("#stoPhone").attr("disabled", "disabled");
            if (obj.detail != null && obj.detail != "")
                $("#stoHouseMember").attr("disabled", "disabled");
        }
    }
}

/**
 * 分店选择回调
 * @param obj
 */
function returnVal(obj) {
    $("#wxShopId").val(obj.id);
    $("#stoName").val(obj.name);
    $("#stoProvince").val(obj.province);
    areaChange(obj.province, 1, 1);
    $("#stoCity").val(obj.city);
    if (obj.city != null && obj.city != "") {
        areaChange(obj.city, 2, 1);
        $("#stoArea").val(obj.district);
    } else {
        $("#stoArea").val("请选择");
    }

    $("#longitude").val(obj.longitude);
    $("#latitude").val(obj.latitude);

    $("#stoAddress").val(obj.adder);
    $("#stoPhone").val(obj.telephone);
    $("#stoHouseMember").val(obj.detail);

    $("#stoName").attr("disabled", "disabled");
    $("#stoProvince").attr("disabled", "disabled");
    $("#stoCity").attr("disabled", "disabled");
    $("#stoArea").attr("disabled", "disabled");
    $("#stoAddress").attr("disabled", "disabled");
    if (obj.telephone != null && obj.telephone != "")
        $("#stoPhone").attr("disabled", "disabled");
    if (obj.detail != null && obj.detail != "")
        $("#stoHouseMember").attr("disabled", "disabled");
}

var claObj = "";
/**选择图片**/
function choosePicture(classess) {
    claObj = classess;
    if ($("#stoPid").val() != -1) {
        // parent.materiallayer();
        fhmater(0);
    } else {
        alert("请选择店铺");
    }
}

/**选择图片回调**/
function image(id, url) {
    $("." + claObj).attr("src", url);
    $("." + claObj).attr("width", 50);
    var url = url.split("/upload/")[1];
    $("#" + claObj).val(url);
    $("." + claObj).show();
}

/**城市联动**/
function areaChange(pid, isquery, o) {
    if (isquery == 1 || isquery == 2) {
        var options = {
            url: "restaurant/getArea.do",
            dataType: "json",
            data: {pid: pid},
            async: false,
            type: "post",
            success: function (data) {
                if (data != "" && data.length > 0) {
                    var html = "<option value='0'>请选择</option>";
                    for (var i = 0; i < data.length; i++) {
                        html += "<option value='" + data[i].id + "'>" + data[i].city_name + "</option>";
                    }
                    if (isquery == 1) {
                        $("#stoCity option").remove();
                        $("#stoCity").append(html);
                    } else if (isquery == 2) {
                        $("#stoArea option").remove();
                        $("#stoArea").append(html);
                    }
                }
            }
        };
        $.ajax(options);
    }
    if (o != 1) {
        $("#stoAddress").val("");
    }
}


/**打开地图**/
function openMap() {
    var data = {};
    var address = "";
    if ($("#stoAddress").val()) {
        address += $("#stoAddress").val();
    } else {
        if ($("#stoProvince").val() != "0" && $("#stoProvince").val() != undefined) {
            address = $("#stoProvince option:selected").text();
        } else {
            layer.msg("请选择省份!");
            return;
        }
        if ($("#stoCity").val() != "0" && $("#stoCity").val() != undefined) {
            address += $("#stoCity option:selected").text();
        } else {
            layer.msg("请选择城市!");
            return;
        }
    }
    data.output = "jsonp";
    data.address = address;
    data.key = "2VBBZ-A3C3O-E7XW7-S6RWA-Q676Z-O6FGU";
    $.ajax({
            type: "get",
            dataType: 'jsonp',
            data: data,
            async: false,
            jsonp: "callback",
            url: "https://apis.map.qq.com/ws/geocoder/v1",
            jsonpCallback: "QQmap",
            success: function (json) {
                console.info(json);
                var longitude = json.result.location.lng;
                var latitude = json.result.location.lat;
                $("#longitude").val(longitude);
                $("#latitude").val(latitude);
                var url = "https://apis.map.qq.com/tools/locpicker?search=1&type=1&key=2VBBZ-A3C3O-E7XW7-S6RWA-Q676Z-O6FGU&referer=test";
                if (latitude != "" && longitude != "") {
                    url += "&coordtype=5&coord=" + latitude + "," + longitude;
                }

                layer.open({
                    area: ['800px', '600px'],
                    title: [
                        '消息',
                        'background-color:#5FBFE7; color:#fff;'
                    ],
                    offset: "10%",
                    type: 2,
                    btn: ["确定", "取消"],
                    content: [url, "no"],
                    yes: function (index) {
                        layer.close(index);
                    }
                });
            }
        }
    );
}


/**
 * 地图选择回调
 * @param loc
 */
function setAddress(loc) {
    $("#stoAddress").val(loc.poiname);
    $("#longitude").val(loc.latlng.lng);
    $("#latitude").val(loc.latlng.lat);
}

var phoneTest = /^((14[0-9])|(13[0-9])|(15[0-9])|(18[0-9]))\d{8}$/;

/**保存**/
function save() {
    var wxShopId = $("#wxShopId option:selected").val();
    var stoPicture = $("#stoPicture").val();
    var stoIsSms = $("#stoIsSms").is(":checked");
    var stoSmsTelephone = $("#stoSmsTelephone").val();
    var stoQqCustomer = $("#stoQqCustomer").val();
    var stoHeadImg = $("#stoHeadImg").val();
    var qqTest = /^\d{0,20}$/;
    if (wxShopId == null || wxShopId == "" || wxShopId == "-1") {
        alert("请选择门店");
        return false;
    } else if (stoPicture == null || stoPicture == "") {
        alert("请前往门店上传门店图片或上传店铺图片");
        return false;
    } else if (stoHeadImg == null || stoHeadImg == "") {
        alert("请上传店铺头像");
        return false;
    } else if (stoIsSms && !phoneTest.test(stoSmsTelephone)) {
        alert("请填写正确的推送手机号");
        $("#stoSmsTelephone").focus();
        return false;
    } else if (requiredValidate()) {
        var tel = "";
        var msg = "";
        if (stoIsSms) {
            $(".stoSmsTelephone").each(function () {
                if (tel != "") {
                    tel += ",";
                }
                if ($(this).val() == null || $(this).val() == "") {
                    msg = "请输入推送手机号";
                } else if (!phoneTest.test($(this).val())) {
                    msg = "请填写正确的推送手机号";
                }
                if (msg != "") {
                    $(this).focus();
                    return false;
                } else {
                    tel += $(this).val();
                }
            });
        }
        if (msg != "") {
            alert(msg);
            return false;
        }
        if (stoQqCustomer != null && stoQqCustomer != "") {
            if (!qqTest.test(stoQqCustomer)) {
                alert("QQ号码必须是小于0-20位的数字");
                $("#stoQqCustomer").focus();
                return false;
            }
        }
        if (!stoIsSms) {
            $("#stoSmsTelephone").val("");
        }
        var obj = $("#tab").serializeObject();
        obj.stoSmsTelephone = tel;
        var params = {
            obj: JSON.stringify(obj)
        };
        /*var stoPid = $("#stoPid").val();
         if(stoPid=="-1"){
         alert("请选择店铺");
         return false;
         }*/
        var index = layer.load(3, {
            offset: '10%',
            shade: [0.4, '#fff']
        });
        $.ajax({
            url: "/store/saveOrUpdate.do",
            data: params,
            dataType: "json",
            type: "post",
            success: function (data) {
                layer.close(index);
                if (data.code === 1) {
                    layer.alert("保存店铺成功", {
                        offset: "10%",
                        shade: [0.1, "#fff"],
                        closeBtn: 0
                    }, function (index) {
                        location.href = "/store/index.do";
                        layer.closeAll();
                    });
                } else {
                    alert("保存店铺失败，请稍后重试");
                }

            }
        });
    }
}
