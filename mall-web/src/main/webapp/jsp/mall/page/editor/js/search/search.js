/**
 * Created by boo on 2016/3/22.
 */

angular.module('search',[]).controller("searchController",["$scope",function($scope){

}])
.directive('searchDraggable', ["$document",function ($document) {
    return {
        templateUrl: "js/search/searchDirective.html",
        link: function(scope, element, attr){
            $('#picker01').farbtastic('#bgcolor');
            $('#picker02').farbtastic('#borderColor');
            $('#picker03').farbtastic('#color');
            if(scope.dataedit == null || scope.dataedit == "")return;
            var w = 250;
            var m1 = 100;
            var m2 = 320;
            var startX = 0,has=false;
            element.find(".ui-slider-handle").on('mousedown', function(event) {
                event.preventDefault();
                var _this = $(this);
                if(_this.hasClass("ui-slider-handle01")){
                    has = false;
                    startX = event.pageX - scope.dataedit.attr.width*w/m2;
                }else{
                    has = true;
                    startX = event.pageX - scope.dataedit.attr.height*w/m1;
                }

                $document.on('mousemove', mousemove);
                $document.on('mouseup', mouseup);
                $(this).addClass("ui-slider-handle-active");
            });

            function mousemove(event) {
                x = event.pageX - startX;

                if(x < 0)x = 0;else if(x > w)x = w;

                if(has){
                    element.find(".ui-slider-handle02").css({
                        left:  x + 'px'
                    });
                }else{
                    element.find(".ui-slider-handle01").css({
                        left:  x + 'px'
                    });
                }

                scope.$apply(function(){
                    if(has){
                        scope.dataedit.attr.height = parseInt(x/w * m1);
                    }else {
                        scope.dataedit.attr.width = parseInt(x/w * m2);
                    }
                })
            }

            function mouseup() {
                $document.unbind('mousemove', mousemove);
                $document.unbind('mouseup', mouseup);
                element.find(".ui-slider-handle-active").removeClass("ui-slider-handle-active");
            }

            $(".u-colorpicker").on("click",function(){
                $(".color-picker").hide();
                $(this).parents(".control-group").find(".color-picker").show();
                event.stopPropagation();
            });

            $("body,html").click(function(){
                $(".color-picker").hide();
            });
            $(".color-picker").click(function(){
                event.stopPropagation();
            })

        }
    }
}]);
