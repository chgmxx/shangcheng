$(function () {
    //初始化省市区
    var pid = $("#visitProvinceId").val() == 0 ? "-1" : $("#visitProvinceId").val();
    areaChange(pid, 1, 1);
    $("#visitCityId").val($("#city").val());
    areaChange($("#city").val(), 2, 1);
    $("#visitAreaId").val($("#area").val());


    onLoad();

    $(".vali").blur(function () {
        valiInp($(this));
    });
    $(".vali").focus(function () {
        valiInp($(this));
    });
    $(".vali").change(function () {
        valiInp($(this));
    });

    if ($(".timeDiv").length > 0) {
        $(".timeDiv").each(function () {
            var index = $(this).index("div");
            var days = $(this).find("input.days").val();
            if (days != null && days != "") {
                var dayArr = days.split(",");
                if (dayArr != null && dayArr.length > 0) {
                    for (var i = 0; i < dayArr.length; i++) {
                        var objs = $(this).find(".js-week-item[did='" + dayArr[i] + "']");
                        var day = objs.text();
                        daysObjs[day] = index;
                        objs.addClass("selected");

                        $(".js-week-item[did='" + dayArr[i] + "']").addClass("disabled");
                        if (daysObjs[day] == index) {
                            objs.removeClass("disabled");
                        }

                    }
                }
                var dayVal = "";
                $(this).find(".js-week-item.selected").each(function () {
                    if (dayVal != null && dayVal != "") {
                        dayVal += ",";
                    }
                    dayVal += $(this).text();
                });
                $(this).find(".js-weeks-selector").hide();
                $(this).find(".times_span").html(dayVal);
                $(this).find(".ok_span").hide();
            }
        });
    }
//	console.log(daysObjs)

    parent.loadWindow();
});
/**
 * 验证输入框
 * @param obj
 * @returns {Boolean}
 */
function valiInp(obj) {
    var bol = true;
    var datatype = obj.attr("datatype");// 正则表达式
    var errormsg = obj.attr("errormsg");// 错误提示
    if (errormsg == "") {
        errormsg = "不能为空";
    }
    if (obj.attr("isnotnull") == "1") {
        if ($.trim(obj.val()) == "") {
            obj.parents("td").next().find("span").html(errormsg);
            bol = false;
        }
    }

    if (datatype != null) {
        datatype = new RegExp(datatype);
        if (!datatype.test($.trim(obj.val()))) {
            obj.parents("td").next().find("span").html(errormsg);
            bol = false;
        } else {
            obj.parents("td").next().find("span").html("");
        }
    } else {
        var maxlength = obj.attr("maxlength");
        if (obj.val().length > maxlength) {
            obj.parents("td").next().find("span").html(errormsg + "。并且不能超过" + maxlength + "字");
            bol = false;
        } else {
            obj.parents("td").next().find("span").html("");
        }
    }
    return bol;
}
/**
 * 验证接待时间
 * @param obj
 */
function valiTime(obj) {
    var bol = true;
    var startTime = obj.find("#visitStartTime").val();
    var endTime = obj.find("#visitEndTime").val();
    if (startTime != "" && endTime != "") {
        var startTimes = parseFloat(startTime.replace(":", "."));
        var endTimes = parseFloat(endTime.replace(":", "."));
        if (startTimes >= endTimes) {
            obj.parents("td").next().find("span").html("接待开始时间要大于接待结束时间");
            bol = false;
        } else {
            obj.parents("td").next().find("span").html("");
        }
    } else {
        if (startTime == "") {
            obj.parents("td").next().find("span").html("接待开始时间不能为空");
        } else if (endTimes == "") {
            obj.parents("td").next().find("span").html("接待结束时间不能为空");
        }
        bol = false;
    }
    return bol;
}


/**选择图片**/
function choosePicture() {
        fhmater(1);
    // parent.layer.open({
    //     type: 2,
    //     title: '素材库',
    //     shadeClose: true,
    //     shade: 0.2,
    //     area: ['820px', '500px'],
    //     offset: "10px",
    //     content: "/common/material.do?selectType=checked",
    // });
}
/**选择图片回调**/
function image(imageArray, url) {
    var imgHttp = $(".imgHttp").val();
    var html = "";
    if (imageArray != null && imageArray.length > 0) {
        for (var i = 0; i < imageArray.length; i++) {
            var imageUrl = imageArray[i].url;
            var url = imageUrl.split("/upload/")[1];


            html += "<li class='sort delParent'>";
            html += "<img id='img1' aId='' src='" + (imgHttp + url) + "' width='50'/>";
            html += "<a class='js-delete-picture close-modal small hide' onclick='delImg(this)'>×</a>";
            html += "<input type='hidden' class='imageInp' value='"
                + (imgHttp + url)
                + "' /><input type='hidden' class='imageId' value='' />";
            html += "</li>";

        }
        $(".ztImageUrl").append(html);
    }
    loadImage();

}
function loadImage() {
    var imgs = "";
    $(".ztImageUrl img").each(function () {
        var url = $(this).attr("src");
        if (imgs != "") {
            imgs += ",";
        }
        imgs += url.split("/upload/")[1];
    });
    $("#imagesUrl").val(imgs);
}
function delImg(obj) {
    $(obj).parent().remove();
    loadImage();
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
                    var html = "";
                    if (isquery == 1) {
                        html = "<option value='0'>请选择省份</option>";
                    } else if (isquery == 2) {
                        html = "<option value='0'>请选择市</option>";
                    } else if (isquery == 3) {
                        html = "<option value='0'>请选择区</option>";
                    }
                    for (var i = 0; i < data.length; i++) {
                        html += "<option value='" + data[i].id + "'>" + data[i].city_name + "</option>";
                    }
                    if (isquery == 1) {
                        $("#visitCityId option").remove();
                        $("#visitCityId").append(html);
                        $("#visitAreaId").val("");
                    } else if (isquery == 2) {
                        $("#visitAreaId option").remove();
                        $("#visitAreaId").append(html);
                    }
                }
            }
        };
        $.ajax(options);
    }
    if (o != 1) {
        $("#visitAddress").val("");
    }
}

/**打开地图**/
function openMap() {
    var data = {};
    var address = "";
    if ($("#visitAddress").val()) {
        address += $("#visitAddress").val();
    } else {
        if ($("#visitProvinceId").val() != "0" && $("#visitProvinceId").val() != undefined) {
            address = $("#visitProvinceId option:selected").text();
        } else {
            layer.msg("请选择省份!", {
                shade:[0.1,"#fff"],
                offset: "10%"
            });
            return;
        }
        if ($("#visitCityId").val() != "0" && $("#visitCityId").val() != undefined) {
            address += $("#visitCityId option:selected").text();
        } else {
            layer.msg("请选择城市!", {
                shade:[0.1,"#fff"],
                offset: "10%"
            });
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
            url: "http://apis.map.qq.com/ws/geocoder/v1",
            jsonpCallback: "QQmap",
            success: function (json) {
                console.info(json);
                var longitude = json.result.location.lng;
                var latitude = json.result.location.lat;
                $("#longitude").val(longitude);
                $("#latitude").val(latitude);
                var url = "http://apis.map.qq.com/tools/locpicker?search=1&type=1&key=2VBBZ-A3C3O-E7XW7-S6RWA-Q676Z-O6FGU&referer=test";
                if (latitude != "" && longitude != "") {
                    url += "&coordtype=5&coord=" + latitude + "," + longitude;
                }

                layer.open({
                    area: ['800px', '600px'],
                    title: [
                        '消息',
                        'background-color:#5FBFE7; color:#fff;'
                    ],
                    offset: "5%",
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
    $("#visitAddress").val(loc.poiname);
    $("#longitude").val(loc.latlng.lng);
    $("#latitude").val(loc.latlng.lat);
}
function eachImage() {
    var imageList = new Array();
    var delimageList = new Array();
    var defaultObj = new Object();
    if (imagDefaultObj != null) {
        defaultObj = imagDefaultObj;
    }
    if ($(".ztImageUrl img").length > 0) {
        $(".ztImageUrl img").each(function (i) {
            var url = $(this).attr("src");
            var imgs = url.split("/upload/")[1];
            var id = $(this).attr("aId");
            var imgObj = {
                imageUrl: imgs,
                assType: 3,
                assSort: i
            }
            if (i == 0) {
                imgObj.isMainImages = 1;
                $("#imagesUrl").val(imgs);
            }
            if (id != "") {
                imgObj.id = id;

                delete defaultObj[id];

                delimageList[delimageList.length] = imgObj;
            } else {
                imageList[imageList.length] = imgObj;
            }
        });
    }
    if (defaultObj != null) {
        for (var str in defaultObj) {
            var obj = new Object();
            obj.id = str;
            obj.isDelete = 1;
            delimageList[delimageList.length] = obj;
        }
    }
    var imageObj = {
        imageList: imageList,
        delimageList: delimageList
    };
    return imageObj;
}

function eachTimes() {
    var days = "";
    var timeList = new Array();
    var deltimeList = new Array();
    var defaultObj = new Object();
    if (daysDefaultObj != null) {
        defaultObj = daysDefaultObj;
    }
    if ($(".timeDiv").length > 0) {
        $(".timeDiv").each(function () {
            var days = "";
            if (valiTime($(this))) {
                $(this).find(".js-week-item.selected").each(function () {
                    if (days != "") {
                        days += ",";
                    }
                    days += $(this).attr("did");
                });
                if (days != null && days != "") {
                    var id = $(this).attr("id");
                    var startTime = $(this).find(".visitStartTime").val();
                    var endTime = $(this).find(".visitEndTime").val();

                    var dayObj = {
                        startTime: startTime,
                        endTime: endTime,
                        visitDays: days
                    };
                    if (id != "" && id != null) {
                        dayObj.id = id;

                        delete defaultObj[id];

                        deltimeList[deltimeList.length] = dayObj;
                    } else {
                        timeList[timeList.length] = dayObj;
                    }
                } else {
                    $(this).parents("td").next().find("span").html("请至少选择一天为接待日期");
                    timeList = null;
                    deltimeList = null;
                    return;
                }

            } else {
                timeList = null;
                deltimeList = null;
                return;
            }


        });
    } else {
        $(".add-times").parents("td").next().find("span").html("请至少选择一天为接待日期");
        timeList = null;
        deltimeList = null;
        return;
    }

    if (defaultObj != null) {
        for (var str in defaultObj) {
            var obj = new Object();
            obj.id = str;
            obj.isDelete = 1;
            deltimeList[deltimeList.length] = obj;
        }
    }
    var timesObj = {
        timeList: timeList,
        deltimeList: deltimeList
    };
    return timesObj;

}

/**保存**/
function save() {
    /*loadImage();*/

    var imageObj = eachImage();


    var imagesUrl = $("#imagesUrl").val();
    if (!requiredValidate()) {
        alert("请输入正确的自提点信息");
        return false;
    } else if (imagesUrl == null || imagesUrl == "") {
        alert("请上传自提点图片");
        return false;
    } else {
        var flag = true;
        //验证输入框信息
        $(".vali").each(function () {
            flag = valiInp($(this));
            if (!flag) {
                return false;
            }
        });
        if (!flag) {
            alert("请输入正确的自提点信息");
            return false;
        }
        /*flag = valiTime();
         if(!flag){
         alert("请输入正确的接待时间");
         return false;
         }*/

        var obj = $("#tab").serializeObject();
        if (obj.id == null || obj.id == "") {
            delete obj["id"];
        }
        var address = $("#visitProvinceId option:selected").text() + " " + $("#visitCityId option:selected").text() + " " + $("#visitAreaId option:selected").text() + " " + $("#visitAddress").val();
        obj.visitAddressDetail = address;

        var imageList = imageObj.imageList;

        var params = {
            obj: JSON.stringify(obj),
            imageList: JSON.stringify(imageList)
        };
        if (imageObj.delimageList != null) {
            params.delimageList = JSON.stringify(imageObj.delimageList);
        }

        var timeObj = eachTimes();
        if (timeObj.timeList == null && timeObj.deltimeList == null) {
            alert("请编辑接待时间");
            return false;
        } else {
            if (timeObj.timeList != null) {
                params.timeList = JSON.stringify(timeObj.timeList);
            }
            if (timeObj.deltimeList != null) {
                params.deltimeList = JSON.stringify(timeObj.deltimeList);
            }
        }

        var index = layer.load(3, {
            offset: '10%',
            shade: [0.4, '#fff']
        });
        $.ajax({
            url: "/mFreight/editTake.do",
            data: params,
            dataType: "json",
            type: "post",
            success: function (data) {
                layer.close(index);
                if (data.flag) {
                    location.href = "/mFreight/takeindex.do";
                } else {
                    layer.msg("编辑自提点失败，请稍后提交", {
                        shade:[0.1,"#fff"],
                        offset: "10%"
                    });
                }
            }
        });
    }
}
/**
 * 新增时间段
 */
$(".add-times").click(function () {
    var objHtml = $(".timeDivHide");
    /*obj.find(".visitStartTime").val("");
     obj.find(".visitEndTime").val("");
     obj.find(".js-week-item").removeClass("selected");
     obj.find(".ok_span").show();
     obj.find(".edit_span").hide();
     obj.find(".days").val("");
     obj.find(".timeId").val("");*/

    objHtml.find(".js-week-item").removeClass("selected");

    $(this).before("<div class='timeDiv'>" + objHtml.html() + "</div>");

    onLoad();

    $(".add-times").hide();


    loadTimes($(".timeDiv:last"));

});


function onLoad() {
    $(".js-week-item").unbind();
    $(".js-week-item").bind("click", function () {
        if (!$(this).hasClass("disabled")) {
            if ($(this).hasClass("selected")) {
                $(this).removeClass("selected");
            } else {
                $(this).addClass("selected");
            }
        }

    });
    $(".visitStartTime").unbind();
    //初始化接待开始时间
    $(".visitStartTime").bind("click", function (e) {
        var obj = $(this);
        e.stopPropagation();
        $(this).lqdatetimepicker({
            css: 'datetime-hour',
            selectback: function () {
                valiTime(obj.parents(".timeDiv"));
            }
        });
    });
    $(".visitEndTime").unbind();
    // 初始化接待结束时间
    $(".visitEndTime").bind("click", function (e) {
        var obj = $(this);
        e.stopPropagation();
        $(this).lqdatetimepicker({
            css: 'datetime-hour',
            selectback: function () {
                valiTime(obj.parents(".timeDiv"));
            }
        });
    });
    $("a.cancel,a.dele").unbind();
    /**
     * 取消
     */
    $("a.cancel,a.dele").bind("click", function (e) {
        var obj = $(this).parents(".timeDiv");
        obj.find(".js-week-item.selected").each(function () {
            delete daysObjs[$(this).text()];
        });

        obj.remove();
        $(this).parents("td").next().find("span").html("");
        var bol = true;
        $(".timeDiv").each(function () {
            var disabled = $(this).find(".visitStartTime").attr("disabled");
            if (disabled == "disabled") {
                bol = true;
            } else {
                bol = false;
                return;
            }
        });
        if (bol && getJsonLength(daysObjs) < 7) {
            $(".add-times").show();
        }


    });
    $("a.ok").unbind();
    /**
     * 确认时间段
     */
    $("a.ok").bind("click", function (e) {
        okTime($(this));
    });
    $("a.edit").unbind();
    /**
     * 编辑时间段
     */
    $("a.edit").bind("click", function (e) {
        if ($(".timeDiv .ok_span:visible").length == 0) {
            var obj = $(this).parents(".timeDiv");
            $(".add-times").hide();
            obj.find(".ok_span").show();
            obj.find(".edit_span").hide();
            obj.find(".js-weeks-selector").show();
            obj.find(".times_span").hide();
            obj.find(".times_span").html("");
            obj.find(".visitStartTime").removeAttr("disabled");
            obj.find(".visitEndTime").removeAttr("disabled");

            loadTimes(obj);
        } else {
            layer.alert("请先确认接待时间再编辑");
        }

    });
}
function loadTimes(obj) {
    var index = obj.index("div");
    obj.find(".js-week-item").removeClass("disabled");
    obj.find(".js-week-item").each(function () {
        var bol = true;
        var val = $(this).text();
        if (daysObjs != null) {
            if (daysObjs[val] != null && daysObjs[val] != index) {
                bol = false;
            }
        }
        if (!bol) {
            $(this).addClass("disabled");
        }
    });

}

function okTime(obj) {
    var days = "";
    var bol = false;
    obj = obj.parents(".timeDiv");
    var start = obj.find(".visitStartTime").val();
    var end = obj.find(".visitEndTime").val();
    if (start == null || start == "") {
        bol = false;
        obj.parents("td").next().find("span").html("请输入接待开始时间");
    } else if (end == null || end == "") {
        bol = false;
        obj.parents("td").next().find("span").html("请输入接待结束时间");
    } else {
        if (valiTime(obj)) {
            var index = obj.index("div");
            obj.find(".js-week-item.selected").each(function () {
                var val = $(this).text();
                var resBol = true;
                if (days != "") {
                    days += ",";
                }
                if (daysObjs != null) {
                    if (daysObjs[val] != null && daysObjs[val] != index) {
                        resBol = false;
                    }
                }
                if (resBol) {
                    daysObjs[val] = index;
                    days += val;
                }

            });
            if (days == null || days == "") {
                bol = false;
                obj.parents("td").next().find("span").html("请至少选择一天为接待日期");
            } else {
                bol = true;
            }
        }
    }
    var daysSeleObj = new Object();
    $(".timeDiv").each(function () {
        var index = $(this).index("div");
        $(this).find(".js-week-item.selected").each(function () {
            daysSeleObj[$(this).text()] = index;
        });
    });
    if (daysSeleObj != null) {
        daysObjs = daysSeleObj;
    }
    if (bol) {
        if (getJsonLength(daysObjs) < 7) {
            $(".add-times").show();
        }
        obj.find(".ok_span").hide();
        obj.find(".edit_span").show();
        obj.find(".js-weeks-selector").hide();
        obj.find(".times_span").show();
        obj.find(".times_span").html(days);
        obj.find(".visitStartTime").attr("disabled", "disabled");
        obj.find(".visitEndTime").attr("disabled", "disabled");
    }
}

function getJsonLength(jsonData) {
    var jsonLength = 0;
    for (var item in jsonData) {
        jsonLength++;
    }
    return jsonLength;
}