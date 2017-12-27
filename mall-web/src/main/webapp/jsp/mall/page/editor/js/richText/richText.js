/**
 * Created by boo on 2016/3/22.
 */

angular.module('richText',[]).controller("richTextController",["$scope",function($scope){

}])
.directive('richTextDraggable', ["$timeout","$sce",function ($timeout,$sce) {
    return {
        templateUrl: "js/richText/richTextDirective.html",
        link: function(scope,element){
        	
        	if(scope.dataedit.dom)element.find("#editor-trigger").html(scope.dataedit.dom.$$unwrapTrustedValue());
        	
        	$timeout(function(){
        		var editor = new wangEditor('editor-trigger');
        		
        		editor.config.pasteFilter = false;
        		
        		editor.config.menus = [
                   'source',
                   'bold',
                   'underline',
                   'italic',
                   'strikethrough',
                   'eraser',
                   'forecolor',
                   'bgcolor',
                   'quote',
                   'fontfamily',
                   'fontsize',
                   'head',
                   'unorderlist',
                   'orderlist',
                   'alignleft',
                   'aligncenter',
                   'alignright',
                   'link',
                   'unlink',
                   'table',
                   'emotion',
                   'img',
                   'video',
                   'location',
                   'insertcode',
                   'undo',
                   'redo',
                   'fullscreen'
               ];
        		
        		
        		editor.create();
        		
//        		$("#editor-trigger").on("keyup",function(){
//        			scope.$apply(function(){
//        				scope.dataedit.dom = $sce.trustAsHtml(editor.$txt.html());
//	                })
//        		})
        	},100)
        }
    }
}]);
