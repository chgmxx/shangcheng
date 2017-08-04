angular.module('htmljs', []).controller("htmljsController",["$scope",function($scope){

//保存数据,保存issave是1，预览是2，
$scope.save = function(){
	var datajson = angular.toJson($scope.dataJson);
	var databg = angular.toJson($scope.dataModuleBg);
	var datatransverse = angular.toJson($scope.dataTransverse);
	var musicurl = $("#htmlmusicurl").val();;
	var musicname = $("#htmlmusicname").val();
	var addres = $("#addres").val();
	var playerStyle = $("#player_style").val();
	var id = $("#id").val();
	$.ajax({   
		type:"post",
		url:"/mallhtml/htmlSave.do",
		data:{datajson:datajson,databg:databg,datatransverse:datatransverse,playerStyle:playerStyle,addres:addres,musicname:musicname,musicurl:musicurl,id:id},
		async:false,
		dataType:"json",
		success:function(data){
			var a = data.error;
			if(a=="0"||a==0){
				layer.alert("保存成功",{
					offset : "30%",
					closeBtn: 0
				});
			}else{
				layer.alert("保存失败",{
					offset : "30%",
					closeBtn: 0
				});
			}
		}
     });

}
$scope.preview = function(){
	var datajson = angular.toJson($scope.dataJson);
	var databg = angular.toJson($scope.dataModuleBg);
	var datatransverse = angular.toJson($scope.dataTransverse);
	var musicurl = $("#htmlmusicurl").val();;
	var musicname = $("#htmlmusicname").val();
	var addres = $("#addres").val();
	var playerStyle = $("#player_style").val();
	var id = $("#id").val();
	$.ajax({   
		type:"post",
		url:"/mallhtml/htmlSave.do",
		data:{datajson:datajson,databg:databg,datatransverse:datatransverse,playerStyle:playerStyle,addres:addres,musicname:musicname,musicurl:musicurl,id:id},
		async:false,
		dataType:"json",
		success:function(data){
			var a = data.error;
			if(a=="0"||a==0){
				 layer.open({
	                   type: 2,
	                   title: 'h5商城预览',
	                   shadeClose: true,
	                    shade: 0.2,
	                     area: ['400px', '400px'],
	                    offset : "10%",
	                    content: '/mallhtml/ylcodeurl.do?url='+$("#codeurl").val(), //iframe的url
	              }); 
			}else{
				layer.alert("保存失败",{
					offset : "30%",
					closeBtn: 0
				});
			}
		}
     });

  }
}])
