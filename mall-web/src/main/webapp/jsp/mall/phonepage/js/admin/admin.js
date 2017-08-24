/**
 * Created by boo on 2016/3/22.
 */

angular.module('admin',[]).controller('adminController',["$scope","$timeout",function($scope,$timeout){

    $scope.dataJson = dataJson;                 //样式数据
    $scope.picJson  = picJson;                  //图片编号数据
    $scope.message = function(param){
    	messageCli(param);
    }
    /**
     * 对比数据
     */
    $scope.complete=function(param){
    	if(parseInt(param/3) == param/3)return true;
    }
    
    $scope.pfLaye = function(param){
    	if(!param)return;
    	$(".pfj-main").html(param);
    	$(".pfj").show();
    }
    
    
    if(imgIds.length > 1){
		imgIds = imgIds.substr(1,imgIds.length-2);
		var userid = $("input.userid").val();
		$.ajax({
			type : "post",
			url : "/phoneProduct/79B4DE7C/getProductByIds.do",
			data : {
				proIds : imgIds,
				userid : userid
			},
			async : true,
			dataType : "json",
			success : function(data) {
				console.log($scope.picJson)
				if(data.code == -1){
					return;
				}
				picJsonEach(data.data);
				$timeout(function(){
					$scope.picJson  = picJson; 
				})
			}
		});
	}

}])
.directive('admindraggable', ["$timeout",function ($timeout) {
    return {
    	scope: true,
        restrict:"E",
        templateUrl: "js/admin/admin.html"
    }
}]);

