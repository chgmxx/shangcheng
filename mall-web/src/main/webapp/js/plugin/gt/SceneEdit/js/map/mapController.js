/**
 * Created by Administrator on 2016/2/23.
 */

angular.module('map', ['data']).controller("mapController",["$scope",'Data',function($scope,Data){
    $scope.data = $scope.MAIN_SELECT;
}])
.directive('mapDirective', function () {
    return{
        templateUrl: "/js/SceneEdit/js/map/map.html",
        link: function(scope, element){
            var ip = scope.data.dataMap.latlng.split(",");
            var center = new qq.maps.LatLng(ip[0],ip[1]);
            var latlng = null;
            var map = new qq.maps.Map(document.getElementById('map'),{
                center: center,
                zoom: 13,
                disableDefaultUI: true    //禁止所有控件
            });
            var Interval = setInterval(function(){
                var doms = $("#map>div>div");
                if(!doms)return;
                doms.hide();
                if(doms.length == 3){
                    doms.eq(0).show();
                    clearInterval(Interval);
                }
            },10);
            var geocoder = new qq.maps.Geocoder({
                complete : function(result){
                    map.setCenter(result.detail.location);
                    //填充经纬度
                    $(".map_latlng").val(latlng.lat+","+latlng.lng).trigger("change");
                }
            });
            qq.maps.event.addListener(map, 'center_changed', function() {
                latlng = map.getCenter();
            });
            qq.maps.event.addListener(map,'zoom_changed',function() {
                ip = scope.data.dataMap.latlng.split(",");
                center = new qq.maps.LatLng(ip[0],ip[1]);
                map.setCenter(center);
            });

            $("#map").on("mouseup",function(){
                geocoder.getAddress(latlng);
            });
            $(".icon_find").on("click",function(){
                geocoder.getLocation($(".map_address").val());
            });

        }
    }
});