//JavaScript Document
angular.module('scalescreen', []).directive('scaleDraggable', ['$document',function($document) {
	return {
		/**
		* font-size:14px;margin-left:-250px;margin-top:-280px;width:380px;height:624px;
		*/
		link : function($scope,$element,$attr){
			var winW = $(window).width(),
				winH = $(window).height(),
				elW  = 380,
				elH  = 624,
				elX  = 250,
				elY  = 280,
				data = {};
			if(winH-130<=elH){
				var newH = winH - 130,
					newW = elW*(newH/elH),
					newY = -newH/2+53,
					newX = -newW/2-80;
				$scope.scale = newH/elH;
				$scope.scalestyle = "margin-left:"+newX+"px;margin-top:"+newY+"px;width:"+newW+"px;height:"+newH+"px;";
			}

		}
	}
}])