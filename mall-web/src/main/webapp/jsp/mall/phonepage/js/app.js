/**
 * Created by Administrator on 2016/3/22.
 */

var app = angular.module('app',['ngSanitize','admin','swiper','time']);

app.directive('onLoad', function ($timeout) {
    return {
        restrict: 'A',
        link: function (scope, element, attr) {
        	var dom = element;
            if (scope.$last === true) {
                $timeout(function () {
                	var img = $(dom).parent().find(".img_class");
                    if(img.length > 0){
                    	setTimeout((function(img){
                    		return function(){
                    			img.lazyload({
                    				effect: "fadeIn",
                    				threshold : 200,
                    				skip_invisible : true
                    			});
                    		}
                    	})(img),200);
                	}
                });
            }
        }
    }
});

$(window).load(function() {
  var winW = $(window).width(), winH = $(window).height(), page1 = 320, page2 = 624, meta = $("#meta");
  meta.attr("content", "width="+page1+",initial-scale=" + winW / page1 + ", minimum-scale=" + winW / page1 + ", maximum-scale=" + winW / page1 + ", user-scalable=no");
    window.parent.postMessage('{"width":320,"type":1}', '*');
});