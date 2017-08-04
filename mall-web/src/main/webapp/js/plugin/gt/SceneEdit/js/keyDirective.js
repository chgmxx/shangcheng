// JavaScript Document
angular.module('keydown', []).directive('keyDraggable', ['$document','$timeout',function($document,$timeout) {
	return {
		link: function($scope,element){
			var copyPage = 0;
			console.log(element);
			$timeout(function(){
				var start_index = 0;
				new Sortable(element.find(".page")[0],{
					group: "name",
					store: null, // @see Store
					draggable: ".c-ct",   // 指定那些选项需要排序
					opacity: 0.6 ,
					onStart: function (/**Event*/evt) { // 拖拽
						start_index = $(evt.item).index();
					},
					onEnd: function (/**Event*/evt) { // 拖拽
						$scope.$apply(function(){
							dataJson.changeAt(start_index,$(evt.item).index());
							dataBg.changeAt(start_index,$(evt.item).index());
						});
						$timeout(function(){
							$(".page>div").eq($(evt.item).index()).click();
						})
					}
				});
			});

			$document.on("keydown",function(){
				if($scope.showBg && !event.ctrlKey)return;
				var key = event.which || event.keyCode;
				switch (key)
				{
					//删除
					case 46:
						break;
					//↑
					case 38:
						$scope.$apply(function(){
							$scope.MAIN_SELECT.attrs.top --;
						});
						break;
					//←
					case 37:
						$scope.$apply(function(){
							$scope.MAIN_SELECT.attrs.left --;
						});
						break;
					//↓
					case 40:
						$scope.$apply(function(){
							$scope.MAIN_SELECT.attrs.top ++;
						});
						break;
					//→
					case 39:
						$scope.$apply(function(){
							$scope.MAIN_SELECT.attrs.left ++;
						});
						break;
					//复制
					case 67:
						if(!$scope.showBg && event.ctrlKey && !$("textarea,input").is(":focus")){
							$scope.$apply(function(){
								$scope.copy = JSON.stringify($scope.MAIN_SELECT);
								layer.msg('复制成功',{offset: ["70px","50%"]});
							});
							copyPage = 1;
						}
						break;
					//粘贴
					case 86:
						if(event.ctrlKey){
							$scope.$apply(function() {
								if($scope.copy && !$("textarea,input").is(":focus")){
									var index = $scope.NAV_SELECT_index;
									var len = $scope.NAV_SELECT_length = $scope.dataJson[index].length + 1;
									var num = len * 100 + 10000;
									$scope.dataJson[index][len - 1] = eval('(' + $scope.copy + ')');
									var data = $scope.dataJson[index][len - 1].attrs;
									data.zIndex = num;
									data.left += 10*copyPage;
									data.top += 10*copyPage;
									delete $scope.dataJson[index][len - 1].$$hashKey;
									copyPage++;
									layer.msg('粘贴完成',{offset: ["70px","50%"]});
								}
							});

						}
						break;
				}
			})
			
		}
	}
}])