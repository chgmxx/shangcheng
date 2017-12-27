/**
 * Created by boo on 2016/3/22.
 */

angular.module('title',[]).controller("titleController",["$scope",function($scope){

	$scope.removeswiperpic = function(param){
        $scope.dataedit.navName = "";
        $scope.picJson[$scope.pageEdit].imgID.removeAt(param);
    }
    $scope.removeswipername = function(param){
        $scope.picJson[$scope.pageEdit].imgID[param]="";
    }
    
    $scope.swiperuploadpic = function(param){
    	$scope.pic_index = param;
    }
    

}])
.directive('titleDraggable', ["$timeout",function ($timeout) {
    return {
        templateUrl: "js/title/titleDirective.html",
        link: function(){
        	var input;
        	$timeout(function(){
        		input = $( "input.dateInput" );
        		input.datetimepicker();
        		$("input.dateInput02").datetimepicker();
        	})
        	$( ".titleDirective" ).on("blur","input.dateInput",function(){
        		input.hide();
        	})
        	$( ".titleDirective" ).on("click",".dateBtn",function(){
        		input.show();
        		input.focus();
        	})
        }
    }
}]);
