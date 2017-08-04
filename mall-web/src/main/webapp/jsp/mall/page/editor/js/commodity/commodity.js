/**
 * Created by boo on 2016/3/22.
 */

angular.module('commodity',[]).controller("commodityController",["$scope",function($scope){
	
	
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
        $scope.picJson[$scope.pageEdit].imgID.removeAt(param);
    }

}])
.directive('commodityDraggable', ["$timeout",function ($timeout) {
    return {
        templateUrl: "js/commodity/commodityDirective.html",
        link: function(scope,element,attr){
        	$timeout(function(){
        		var start_index = 0;
        		new Sortable(element.find("ul")[0],{
        		    group: "name",
        		    store: null, // @see Store
        		    draggable: ".sort",   // 指定那些选项需要排序
        		    opacity: 0.6 ,
        		    onStart: function (/**Event*/evt) { // 拖拽
        		    	start_index = $(evt.item).index()-1;
        		    },
        		    onEnd: function (/**Event*/evt) { // 拖拽
        		        var itemEl = evt.item;
        		        scope.$apply(function(){
        		        	picJson[scope.pageEdit].imgID.changeAt(start_index,$(evt.item).index()-1);
        		        })
        		    }
        		});
        	})
        	
        }
    }
}]);
