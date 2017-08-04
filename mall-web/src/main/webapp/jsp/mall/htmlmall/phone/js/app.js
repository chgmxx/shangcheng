// JavaScript Document


angular.module('app',['dragModule','animation',"colorTo","mapModule","bgswiper"]).controller('appController',["$scope","$sce","colorToService",function($scope,$sce,colorToService){
	$scope.datajson     = cloud.datajson;
	$scope.dataModuleBg = cloud.dataModuleBg;
	$scope.dataTransverse = cloud.dataTransverse;
	//当前所在屏的大小
	$scope.direction    = 0;
	$scope.time         = 1000;
	$scope.animates     = [];
	$scope.formName = "ssss";
	$scope.sce = $sce.trustAsResourceUrl;
	//储存字体
	$scope.font_name = [
		["SimHei","黑体"],
		["微软雅黑","微软雅黑"]
	];
	//判断有没有src
	$scope.haveSrc = function(src){
		if(!src)return false;return true;
	};

	$scope.viewshow = function(index){
		if(index != $scope.indexpage)return false;return true;
	};
		
	$scope.hasanimate = function(param1){
		if( param1.length > 5)return true;
		else return false;
	};
	
	$scope.unitchange = function(param){
		if(param<100){
			return param+"px";
		}else if(param>=100){
			return "100%";
		}
	};

	$scope.delayanimate = function(param){
		if(param == "" || param == null || param == 0) {
			return "block";
		}else{
			return "none"
		}
	};

	$scope.hasdelay = function(param){
		if(param>0)return true;
		else return false;
	};

	$scope.changeColor = function(param){
		if(param.bgopacity == 0 || param.bgopacity == null || param.bgopacity == "" || param.bgcolor == "" || param.bgcolor == null)return;
		return "background-color:rgba("+colorToService(param.bgcolor)+ "," + param.bgopacity/100 +")";
	};

	$scope.showbgcolor = function(param1,param2,param3){
		if(param1 == "" || param1 == "none" || param1 == null || param2 == null)return "";
		else return param3 + "rgba(" + colorToService(param1) + "," + param2/100 + ");"
	};

	$scope.hashref = function(param){
		if(param.btnType == 1){
			return param.url;
		}else if(param.btnType == 2){
			return "tel:"+ param.phone;
		}else if(param.btnType == 3){
			return "javascript:void(0)";
		}else if(param.btnType == 4){
			return "http://wpa.qq.com/msgrd?v=3&uin="+param.QQ+"&site=qq&menu=yes"
		}
		return;
	};

	$scope.layer = function(param){
		if(param.btnType != 5)return;
		event.preventDefault();

		var str = "";
		for(var i=0; i<param.swiper.length; i++){
			str += '<div class="swiper-slide" style="background-image: url('+param.swiper[i]+')"></div>';
		}
		swiper.destroy(false);
		$(".swiper-wrapper").html(str);
		$(".swiper-container").show().animate({opacity:1},800);

		swiper = new Swiper('.swiper-container', {
			nextButton: '.swiper-button-next',
	        prevButton: '.swiper-button-prev',
		});


	};

	/**
	 * 隐藏音乐
	 */
	$scope.hidemusic = function(param){
		if(param){
			return "display:none;";
		}else{
			return "display:block;";
		}
	};

	/**
	 * 判断按钮是否内页跳转
	 */
	$scope.hasinsidepage = function(param){
		if(param == 3)return true;
	};

	/**
	 * 切图对应显示范围
	 */
	$scope.setcutpicw = function (param1, param2, param3) {
		if (param2 == "" || param2 == "100%" || !param1 || param1 == "" || param1 == null) {
			return param3;
		} else {
			return param1 * param3 / param2;
		}
	};
	$scope.setcutpicxy = function (param1, param2, param3) {
		if (param2 == "" || param2 == "100%") {
			return param1;
		} else {
			return param1 * param3 / param2;
		}
	};

	/**
	 * 经纬度
	 */
	$scope.getlatlng = function (param1,param2){
		return param1.split(",")[param2]
	};

	/**
	 * 扫描二维码
	 */
	$scope.showDiscern = function(param){
		if(param){
			return true;
		}else{
			return false;
		}
	};

	/**
	 * 设置横向翻页样式
	 */
	$scope.dataTransverse_style = function(param){
		return "width:"+param.attr.width+"px;height:"+param.attr.height+"px;left:"+param.attr.left+"px;top:"+param.attr.top+"px";
	}
	
	$scope.datatransverse_bg = function(param1,param2){
		return "background:" + param1.bg[param2].background;
	}
	
}]);
