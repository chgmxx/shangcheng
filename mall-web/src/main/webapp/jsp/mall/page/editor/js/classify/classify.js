/**
 * Created by boo on 2016/3/30.
 */

angular.module('classify',[]).controller("classifyController",["$scope",function($scope){

}])
.directive('classifyDraggable', ["$timeout",function ($timeout) {
    return {
        templateUrl: "js/classify/classifyDirective.html",
        scope:{
        	dataedit:"=",
        },
        link: function(scope,element){
            var data_y = element.find("tr"),
                data_td = element.find("td"),
                max_y = 3, max_x = 3, this_x,this_y,
                colorname = ["index-0","index-1","index-2","index-3","index-4","index-5","index-6","index-7","index-8","index-9","index-10","index-11","index-12","index-13","index-14","index-15"],
                addcolor = "",
                layout = element.find(".layout_tank");
            
            element.css("opacity",0);
            $timeout(function(){
	            element.find("table").html(scope.dataedit.dom);
	            data_td = element.find("td");
	            data_y = element.find("tr");
	            for(var i=0; i<data_td.length; i++){
	                array[data_td.eq(i).attr("data-y")][data_td.eq(i).attr("data-x")] = data_td.eq(i);
	            }
	            element.css("opacity",1);
            },100)
            element.on("click","td",function(){
                var _this = $(this);
                if(_this.hasClass(".not-empty"))return;
                this_x = parseInt(_this.attr("data-x"));
                this_y = parseInt(_this.attr("data-y"));
                max_y = 3;
                var len = 0;
                var str = "";
                var dom = "";

                for(var i=this_x; i<=max_x; i++){
                    if(!array[this_y][i])continue;
                    if(array[this_y][i].hasClass("empty")){
                        len++;
                    }else{
                        break;
                    }
                }

                var num_x = 0,num_y=0;
                for(var k=0; k<len; k++){
                    num_x++;
                    for(var j=this_y; j<=max_y; j++){
                        if(!array[j][this_x+k]){
                            max_y = j-1;
                            dom += "<ul>"+str+"</ul>";
                            str = "";
                            num_y = 0;
                            break;
                        }
                        num_y++;
                        if(array[j][this_x+k].hasClass("empty")){
                            str += "<li data-x='"+num_x+"' data-y='"+num_y+"'></li>";
                            if(max_y == j){
                                dom += "<ul>"+str+"</ul>";
                                str = "";
                                num_y = 0;
                            }
                        }else {
                            max_y = j-1;
                            dom += "<ul>"+str+"</ul>";
                            str = "";
                            num_y = 0;
                            break;
                        }
                    }
                }

                layout.find(".layout_tank_body>.inb").html(dom);

                for(var u=0; u<colorname.length; u++){
                    if(!data_td.hasClass(colorname[u])){
                        addcolor = colorname[u];
                        break;
                    }
                }

                if(layout.find("li").length<=1){
                    layout.find("li").click();
                }else{
                    layout.show();
                }

            }).on("click","td.not-empty",function(){
                var _this = $(this);
                var x = parseInt(_this.attr("data-x")),
                    y = parseInt(_this.attr("data-y")),
                    col = parseInt(_this.attr("colspan")),  // 横着
                    row = parseInt(_this.attr("rowspan"));  // 竖着

                $(".current").removeClass("current");
                _this.addClass("current");

                element.find(".pix").text(col*160+" x "+row*160);

                var dom_a = _this.find("a");
                var dom_imp = _this.find("img");
                if(dom_a.attr("href")){
                    element.find(".notLine").css("display","none");;
                    element.find(".hsaLine span").text(dom_a.attr("data-name"));
                    element.find(".hsaLine").css("display","inline-block");
                }else{
                    element.find(".notLine").css("display","inline-block");
                    element.find(".hsaLine").css("display","none");
                }
                if(dom_imp.attr("src")){
                    element.find(".choice_pic img").attr("src",dom_imp.attr("src"));
                    element.find(".choice_pic").show();
                    element.find(".choice_pic").prev().hide();
                }else{
                    element.find(".choice_pic").hide();
                    element.find(".choice_pic").prev().show();
                }
                
                element.find(".choice-content").show();

            }).on("click",".remove_btn.layout_module",function(){
                delectlayout($(".current"));
                savedom();
                element.find(".choice-content").hide();
            }).on("click",".remove_btn.layout_href",function(){
            	$(".current a").attr({"href":"","data-name":""});
            	$(".current").click();
            	savedom();
            }).on("click","a",function(){
            	event.preventDefault();
            })

            layout.on("mouseenter","li",function(){
                var max_x = $(this).attr("data-x"),
                    max_y = $(this).attr("data-y"),
                    alldom = $(".layout_tank_body li");

                for(var i in alldom){
                    var dom = alldom.eq(i);
                    if(dom.attr("data-x")<=max_x && dom.attr("data-y")<=max_y){
                        dom.addClass("selected");
                    }else{
                        dom.removeClass("selected");
                    }
                }


            }).on("click","li",function(){
                choicelayout($(this));
                savedom();
                layout.hide();
                element.find(".choice-content").show();
            }).on("click",".close",function(){
                layout.hide();
            });

            function choicelayout(param){
                var y = param.attr("data-y"),
                    x = param.attr("data-x");
                for(var i=0; i<x; i++){
                    for(var k=0; k<y; k++){
                        if(i == 0 && k==0)continue;
                        array[this_y+k][this_x+i].remove();
                    }
                }
                data_td = element.find("td");
                array = [[],[],[],[]];
                for(var i=0; i<data_td.length; i++){
                    array[data_td.eq(i).attr("data-y")][data_td.eq(i).attr("data-x")] = data_td.eq(i);
                }
                var height = "rows-" + y;
                $(".current").removeClass("current");
                array[this_y][this_x].attr({"colspan":x,"rowspan":y}).removeClass("empty").addClass(addcolor+" not-empty current "+height).html("<a><img src></a><span>"+ x*160 + "x" + y*160 + "</span>");
                element.find(".pix").text(x*160+" x "+y*160);
                $(".current").click();
            }
            
            function savedom(){
            	scope.$apply(function(){
            		scope.dataedit.dom = element.find("table").html();
            	});
            }

            function delectlayout(param){
                var x = parseInt(param.attr("data-x")),
                    y = parseInt(param.attr("data-y")),
                    col = parseInt(param.attr("colspan")),  // 横着
                    row = parseInt(param.attr("rowspan"));  // 竖着

                var str1 = "",str2 = "";
                for(var i=0; i<row; i++){
                    for(var k=0; k<col; k++){
                        array[y+i][x+k] = '<td class="empty" data-x="'+(x+k)+'" data-y="'+(y+i)+'"></td>';
                    }
                }

                for(var i=0; i<array.length; i++){
                    for(var j=0; j<array[i].length; j++){
                        if(!array[i][j])continue;
                        if(array[i][j].length == 1){
                            str1 += array[i][j][0].outerHTML;
                        }else{
                            str1 += array[i][j];
                        }
                    }
                    str2 += "<tr>"+str1+"</tr>";
                    str1 = "";
                }

                element.find("table").html(str2);

                data_td = element.find("td");
                array = [[],[],[],[]];
                for(var i=0; i<data_td.length; i++){
                    array[data_td.eq(i).attr("data-y")][data_td.eq(i).attr("data-x")] = data_td.eq(i);
                }

            }
        }
    }
}]);
