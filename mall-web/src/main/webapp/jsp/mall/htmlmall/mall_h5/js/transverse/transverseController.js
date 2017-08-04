/**
 * Created by Administrator on 2016/6/7.
 */

angular.module('transverse', []).controller("transverseController",["$scope",function($scope){

    $scope.add_transverse_page = function(){
        $scope.dataTransverse[$scope.NAV_SELECT_index].data.push([]);
        $scope.dataTransverse[$scope.NAV_SELECT_index].bg.push({
            "background":"#fff",
            "slide":"",
        });
    };

    $scope.remove_transverse_page = function(){
        $scope.setNavSelectIndex($scope.NAV_SELECT_index);
        $scope.dataTransverse[$scope.NAV_SELECT_index].data.splice(arguments[0], 1);
        $scope.dataTransverse[$scope.NAV_SELECT_index].bg.splice(arguments[0], 1);
    };
}])
.directive('transverseDraggable', ['$timeout',function($timeout) {

    return {
        link: function($scope,element){
            var len = 0;
            function addswiper(element){
                $timeout(function(){
                    var str = '<div class="swiper-pagination  swiper-pagination-black"></div><div class="swiper-button-next swiper-button-black"></div><div class="swiper-button-prev swiper-button-black"></div>';
                    len = element.find(".swiper-slide").length;
                    if(len <= 0)return false;
                    if(len <= 3){
                        element.find(".swiper-pagination").remove();
                        element.find(".swiper-button-next").remove();
                        element.find(".swiper-button-prev").remove();
                        return false;
                    }
                    element.find(".swiper-container").append(str);
                    var swiper = new Swiper(element.find(".swiper-container"), {
                        pagination: element.find('.swiper-pagination'),
                        nextButton: element.find('.swiper-button-next'),
                        prevButton: element.find('.swiper-button-prev'),
                        slidesPerView: 3,
                        paginationClickable: true,
                        spaceBetween: 0,
                        freeMode: true
                    });
                })
            }

            addswiper(element);

            element.on("click",function(){
                if(len == element.find(".swiper-slide").length)return;
                addswiper(element);
            });

            $timeout(function(){
                $(".tp-add").removeClass("none");
            })
        }
    }
}]);