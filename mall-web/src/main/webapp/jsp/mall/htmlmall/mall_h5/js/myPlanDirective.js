// JavaScript Document

angular.module('dragPlan', []).directive('myPlan',['$document',function($document) {
    return {
        scope:{
            datamodel:"=",
            datarate:"@",
            dataint:"@"
        },
        link:function(scope, element) {
            var startX = 0, startY = 0, x = 0, y = 0;

            element.on('mousedown', function(event) {
                // 组织所选对象的默认拖曳操作
                event.preventDefault();
                x = element[0].offsetLeft;
                startX = event.pageX - x - 10;
                $document.on('mousemove', mousemove);
                $document.on('mouseup', mouseup);
            });

            function mousemove(event) {
                x = event.pageX - startX;
                if(x<=0)x = 0;
                if(x>=130)x = 130;
                scope.$apply(function () {
                    if(scope.dataint){
                        scope.datamodel = parseInt(x/1.3/scope.datarate);
                    }else{
                        scope.datamodel = parseFloat(x/1.3/scope.datarate).toFixed(2);
                    }

                });
            }

            function mouseup() {
                $document.unbind('mousemove', mousemove);
                $document.unbind('mouseup', mouseup);
            }
        }
    }
}]);