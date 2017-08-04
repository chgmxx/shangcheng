/**
 * Created by Administrator on 2016/1/20
 */
angular.module('form', ['data','json']).controller("formController",["$scope","Data","jsonService",function($scope,Data,jsonService){
    $scope.data = $scope.MAIN_SELECT;
    $scope.listpage = Data.form_name_num;
    $scope.list = Data.form_name;
    $scope.formbgBoard = false;
    $scope.formborBoard = false;
    $scope.submitbgBoard = false;
    $scope.submitborBoard = false;
    $scope.alert01 = false;
    $scope.alert02 = false;
    $scope.init_state = false;

    $scope.init = function(){
        $scope.formbgBoard = false;
        $scope.formborBoard = false;
        $scope.submitbgBoard = false;
        $scope.submitborBoard = false;
        $scope.alert01 = false;
        $scope.alert02 = false;
    };
    var init_board = function(){
        $scope.formbgBoard = false;
        $scope.formborBoard = false;
        $scope.submitbgBoard = false;
        $scope.submitborBoard = false;
    };
    $scope.getinit_state = function(){
        $scope.init_state = true;
    };
    $scope.setformbgBoard = function(){
        $('.formBoard01').farbtastic('.forminput01');
        var state = !$scope.formbgBoard;
        init_board();
        $scope.formbgBoard = state;
    };
    $scope.setformborBoard = function(){
        $('.formBoard02').farbtastic('.forminput02');
        var state = !$scope.formborBoard;
        init_board();
        $scope.formborBoard = state;
    };
    $scope.setsubmitbgBoard = function(){
        $('.formBoard03').farbtastic('.forminput03');
        var state = !$scope.submitbgBoard;
        init_board();
        $scope.submitbgBoard = state;
    };
    $scope.setsubmitborBoard = function(){
        $('.formBoard04').farbtastic('.forminput04');
        var state = !$scope.submitborBoard;
        init_board();
        $scope.submitborBoard = state;
    };
    $scope.setalert01 = function(){
        var state = !$scope.alert01;
        $scope.init();
        $scope.alert01 = state;
        $scope.init_state = true;
    };
    $scope.setalert02 = function(){
        var state = !$scope.alert02;
        $scope.init();
        $scope.alert02 = state;
        $scope.init_state = true;
    };
    $scope.setformname = function(param){
        param[1] = $scope.list[param[0]];
    };
    $scope.addform = function(){
        var len1 = $scope.data.dataform.input.data.length;
        var len2 = $scope.list.length - 1;
        var val = [len1,$scope.list[len1]];
        if(len1 >= len2)val = [len2,$scope.list[len2]];
        if(len1 > 6)return;
        jsonService._add($scope.data.dataform.input.data,len1,false,val);
        $scope.init_state = true;
    };
    $scope.removeform = function(param){
        jsonService._delete($scope.data.dataform.input.data,param);
    };

    $scope.plusborder = function(){
        if($scope.data.dataform.input.border<10)$scope.data.dataform.input.border++;
    };
    $scope.minusborder = function(){
        if($scope.data.dataform.input.border>0)$scope.data.dataform.input.border--;
    }
}])
.directive('formDirective', function ($document) {
    return{
        templateUrl: "js/form/form.html",
        link:function(scope){
            $document.on("click",".g-config",function(){
                if(scope.init_state){
                    scope.init_state = false;
                    return;
                }
                scope.$apply(function(){
                    scope.init();
                });
            })
        }
    }
});