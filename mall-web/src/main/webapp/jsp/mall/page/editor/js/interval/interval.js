/**
 * Created by boo on 2016/3/22.
 */

angular.module('interval',[]).controller("intervalController",["$scope",function($scope){
	
    $scope.setpage = function(param){
        if($scope.dataedit.css>1 && param!=1){
            $scope.dataedit.css = 0;
        }
    };

    //显示添加商品弹框
    $scope.addCommodity = function(){
        //$scope.shade.commodity_shade = true;
        //$scope.shade.shade = true;
    	
    	$scope.addcommoditypic();
    };

    $scope.removeshop = function(param){
        jsonService._delete($scope.picJson[$scope.pageEdit].imgID,param);
    }

}])
.directive('intervalDraggable', ["$document",function ($document) {
    return {
        templateUrl: "js/interval/intervalDirective.html",
        link: function(scope, element, attr){
        	if(scope.dataedit == null || scope.dataedit == "")return;
        	var startX = 0, startY = 0, x = scope.dataedit.height * 2.52, y = 0;
        	element.find(".ui-slider-handle").on('mousedown', function(event) {
	            // 组织所选对象的默认拖曳操作
	            event.preventDefault();
	            startX = event.pageX - x;
	            $document.on('mousemove', mousemove);
	            $document.on('mouseup', mouseup);
	            $(this).addClass("ui-slider-handle-active");
	          });

              function mousemove(event) {
				x = event.pageX - startX;
				
				if(x < 0){
					x = 0;
				}else if(x > 252){
					x = 252;
				}
				
				element.find(".ui-slider-handle").css({
					left:  x + 'px'
				});
				
				scope.$apply(function(){
					scope.dataedit.height = parseInt(x/252 * 100);
				})
              }

              function mouseup() {
            	  $document.unbind('mousemove', mousemove);
            	  $document.unbind('mouseup', mouseup);
            	  element.find(".ui-slider-handle-active").removeClass("ui-slider-handle-active");
              }
        }
    }
}]);
