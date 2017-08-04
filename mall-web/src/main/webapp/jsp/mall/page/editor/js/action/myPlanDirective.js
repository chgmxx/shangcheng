// JavaScript Document

angular.module('dragPlan', []).directive('myPlan',['$document',function($document) {
    return {
        link:function(scope, element) {
            var startX = 0, startY = 0, x = 0, y = 0, h = 0, cons=30 , index=0,_index=0, array_y=[], array_h=[], box = $(".app-preview-box"), _box = $(".app-preview-box"), box_page=[], dom = "";
            
            function getArray(){
        		box = $(".app-preview-box");
        		
                for(var i=0; i<box.length; i++){
                	array_y[i] = box.eq(i).position().top;
                	array_h[i] = box.eq(i).height();
                }
            }
            
            element.on('mousedown', function(event) {
                // 组织所选对象的默认拖曳操作
                event.preventDefault();
                getArray();
                for(var i=0; i<box.length;i++){
                	box_page[i] = i;
                }
                _box = $(".app-preview-box");
                index = $(element).parent().index();
                _index = index;
                
                y = $(element).position().top;
                x = $(element).position().left;
                
                h = $(element).height();
                
                dom = "<div class='dragplan app-preview-box' style='height:"+h+"px'></div>";
                
                element.addClass("module-active").parent().removeClass("app-preview-box").after(dom);
                
                startY = event.pageY - y;
                startX = event.pageX - x;
                $document.on('mousemove', mousemove);
                $document.on('mouseup', mouseup);
            });

            function mousemove(event) {
                y = event.pageY - startY;
                x = event.pageX - startX;
                
                for(var i=1;i<array_y.length;i++){
                	var pageNum = index + i;
                	var pageNumsub = index - i;
	                if((array_y[pageNum]+array_h[pageNum]-cons < y+h && y+h < array_y[pageNum]+array_h[pageNum]) || (array_y[pageNum] < y && y < array_y[pageNum]+cons)){
	                	
	                	box_page.changeAt(index,pageNum);
	                	
	                	$(".dragplan").remove();
	                	
	                	_box.eq(box_page[pageNum-1]).after(dom);
	                	
	                	getArray();
	            		index = pageNum;
	            		
	                }else if((array_y[pageNumsub]+array_h[pageNumsub] > y+h && y+h > array_y[pageNumsub]+array_h[pageNumsub]-cons) || (array_y[pageNumsub] > y && y > array_y[pageNumsub]-cons)){
	                	
	                	box_page.changeAt(index,pageNumsub);
	                	
	                	$(".dragplan").remove();
	                	
	                	_box.eq(box_page[pageNumsub+1]).before(dom);
	                	getArray();
	            		index = pageNumsub;
	                } 
                }
                
                                                
                element.css({
					top:  y + 'px',
					left: x + 'px'
				});
            }

            function mouseup() {
            	
                $document.unbind('mousemove', mousemove);
                $document.unbind('mouseup', mouseup);
                scope.$apply(function(){
                	dataJson.changeAt(_index,index);
            		picJson.changeAt(_index,index); 
            		setTimeout(function(){
            			element.click();
            		},150)
            	})
            	
            	element.removeClass("module-active").attr("style","").parent().addClass("app-preview-box");
            	
            	$(".dragplan").remove();   
            }
        }
    }
}]);