/**
 * Created by Administrator on 2016/6/22.
 */
var aniamte = null;
angular.module('bgswiper', []).directive('bgswiperDraggable',['$document','$timeout',function($document,$timeout) {
    return {
        link: function($scope, element){
            var i = 0, doms = null,z=999;
            $timeout(function(){
                if(aniamte)clearInterval(aniamte);
                if(element.children().length <=1)return;
                interval();
                aniamte = setInterval(interval,2400);
            });

            function interval(){
                doms = element.children();
                doms.eq(i).addClass("animate-pic").css("z-index",z);
                console.log(1);
                setTimeout((function(dom){
                    return function(){
                        dom.removeClass("animate-pic").css("z-index",0);
                    }
                })(doms.eq(i)),3000);
                i++;
                z--;
                if(i>=doms.length)i=0;
                if(z<=0)z=999;
            }


        }
    }
}]);