angular.module('scene', []).controller("sceneController",["$scope",function($scope){
$scope.httpurl = function(type,colour){
	$.ajax({   
		type:"post",
		url:"/scene/materialUrl.do",
		data:{type:type,colour:colour},
		async:false,
		dataType:"json",
		success:function(data){
			$(".upload-pic-nav li").attr("class","");
			$("#type"+type+"").attr("class","on");
			$(".upload_pic-li li").attr("class","");
			/*$("#colour1").attr("class","on");*/
			$("#mater").val(type);
			if(type==0){
			$(".upload_pic-li").css("display","none");
			$(".divuploadc").css("display","block");
			}else{
				$(".divuploadc").css("display","none");
				$(".upload_pic-li").css("display","none");
				var cp = $(".upload_pic-li-option"+type+"");
				cp.css("display","block");
				cp.find("#colour"+data.onezid+"").addClass("on");
			}
			var li="";
			$.each(data.imageList,function(index,value){
				    li+="<li class='imgPreview' onclick='imgclick(this)'>";
				    li+="<div class='dz-details'>";
				    li+="<div class='previewImg' style='background-image:url("+value.src+")' srcoption='"+value.src+"'></div>";
				    li+="<div class='delbtn'></div>";
				    li+="</div>";
					li+="  <input type='hidden' class='imagesids' value='"+value.id+"'>";
				    if(type==0){
				    	li+="<div class='close' onclick='closeclick(this)'></div>";
				    }
				    li+="</li>"
				    
				});
			  $("#imageurlst").val(0);
			$('.imagePreviewContent li').remove();
			$(".imagePreviewContent ul").append(li);
			/*$scope.dataPic =data.imageList;*/
		}
 });
	
}

$scope.httptype = function(colour){
	var type = $("#mater").val();
	$.ajax({   
		type:"post",
		url:"/scene/materialUrl.do",
		data:{type:type,colour:colour},
		async:false,
		dataType:"json",
		success:function(data){
			$(".upload_pic-li li").attr("class","");
			$("#colour"+colour+"").attr("class","on");
			var li="";
			$.each(data.imageList,function(index,value){
				  li+="<li class='imgPreview' onclick='imgclick(this)'>";
				    li+="<div class='dz-details'>";
				    li+="<div class='previewImg' style='background-image:url("+value.src+")' srcoption='"+value.src+"'></div>";
				    li+="<div class='delbtn'></div>";
				    li+="</div>";
				    li+="  <input type='hidden' class='imagesids' value='"+value.id+"'>";
				    if(type==0){
				    	li+="<div class='close' onclick='closeclick(this)'></div>";
				    }
				    li+="</li>"
				    
				});
			  $("#imageurlst").val(0);
			$('.imagePreviewContent li').remove();
			$(".imagePreviewContent ul").append(li);
			
		}
 });
	
}
//保存数据,保存issave是1，预览是2，
$scope.save = function(){
	var scene_data = angular.toJson($scope.dataJson);
	var scene_dataModuleBg = angular.toJson($scope.dataModuleBg);
	
	$.ajax({   
		type:"post",
		url:"/scene/Scenesave.do",
		data:{sceneData:scene_data,sceneDatamodulebg:scene_dataModuleBg,scene_data_id:scene_data_id},
		async:false,
		dataType:"json",
		success:function(data){
			var a = data.reTurn;
			if(a=="1"||a==1){
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
	var scene_data = angular.toJson($scope.dataJson);
	var scene_dataModuleBg = angular.toJson($scope.dataModuleBg);
	$.ajax({   
		type:"post",
		url:"/scene/Scenesave.do",
		data:{sceneData:scene_data,sceneDatamodulebg:scene_dataModuleBg,scene_data_id:scene_data_id},
		async:false,
		dataType:"json",
		success:function(data){
			var a = data.reTurn;
			if(a=="1"||a==1){
           	  var id = ids
              layer.open({
                   type: 2,
                   title: '微场景预览',
                   shadeClose: true,
                    shade: 0.2,
                     area: ['820px', '500px'],
                    offset : "10px",
                    content: '/scene/webSceneyl.do?pid='+id+"&type=2" //iframe的url
                 }); 
              }
		else{
			layer.alert("保存失败",{
				offset : "30%",
				closeBtn: 0
			});
			}
		}
   });

}
}])
