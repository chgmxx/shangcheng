/**
 * Created by Administrator on 2016/2/26.
 */

angular.module('mapModule', []).directive('mapDraggable', [function () {
    return {
        link:function(scope,element){
            var center = new qq.maps.LatLng(39.916527,116.397128);
            var map = new qq.maps.Map(element,{
                center: center,
                zoom: 13
            });
            var infoWin = new qq.maps.InfoWindow({
                map: map
            });
            infoWin.open();
            //tips  自定义内容
            infoWin.setContent('<div style="box-sizing:content-box;width:256px;height:90px;overflow:hidden;"><div class="BMap_bubble_title" style="overflow:hidden;height:auto;line-height:24px;white-space:nowrap;width:auto;">云来总部</div><div class="BMap_bubble_content" style="width:auto;height:auto;">电话：<a href="tel:4000-168-906">4000-168-906</a><br>地址：广东省深圳市南山区朗山一路</div><div class="BMap_bubble_max_content" style="display:none;position:relative"></div></div>');
            infoWin.setPosition(center);
        }

    }
}]);
