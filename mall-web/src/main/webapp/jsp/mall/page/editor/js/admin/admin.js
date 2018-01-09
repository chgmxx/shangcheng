/**
 * Created by boo on 2016/3/22.
 */

angular.module('admin',[]).controller('adminController',["$scope","$timeout","$compile","$sce",function($scope,$timeout,$compile,$sce){
	
    $scope.dataJson = dataJson;                 //样式数据
    $scope.picJson  = picJson;                  //图片编号数据
    
    $scope.edit = null;         				//当前需要显示的编辑框
    $scope.pageEdit = 0;        				//当前编辑的第几个模块
    $scope.dataedit = null;    					//当前编辑的数据
    $scope.positionTop = 0;    					//编辑器卷上去高度
    
    $scope.pic_index = 0; 						//判断选择第几张图
    
    $scope.dataJson.forEach(function(e){
    	if(e.type == 18){e.dom = $sce.trustAsHtml(e.html)}
    })
    
    //遮罩
    $scope.shade = {
        "shade":false,
        "commodity_shade":false
    };
    
    //关闭所有遮罩
    $scope.colseShade = function(){
        for(var i in $scope.shade){
            $scope.shade[i] = false;
        }
    };
    //关闭商品遮罩
    $scope.closeTank = function(){
        $scope.shade.commodity_shade = false;
        $scope.shade.shade = false;
    };

    //修改需要显示的编辑器和当前显示的第几个模块的值
    $scope.setShowEdit = function(param1,param2,$event){
    	
    	window.wangEditor="";
    	
        $scope.positionTop = $($event.currentTarget).position().top + 50;
        $scope.edit = 0;
        //$scope.edit = param1.type;
        $scope.pageEdit = param2;
        $scope.dataedit = param1;
        $timeout(function(){
        	$scope.edit = param1.type;
        })
        
        $(".actions-active").removeClass("actions-active");
        $($event.target).addClass("actions-active");
    };

    /*****************************************************************************/
    
    /**
     * 添加数据
     * type 1：商品 2：轮播图 3：分类
     */
    var addpublic = function(param){
    	$scope.picJson.addAt(param,{
            "type":$scope.edit,
            "imgID":[],
        });
        $timeout(function(){
            $(".module").eq(param).find(".actions").click();
        },50)
    }
    
    //商品
    $scope.addcommodity = function(param){
    	if(isNaN(param))param = $scope.dataJson.length;
        $scope.dataJson.addAt(param,{
            "type":1,
            "page": 2,
            "css":0,
            "btn": true,
            "btnpage": 0,
            "name": false,
            "intro":false,
            "price": true,
        });
        addpublic(param);
    };
    //轮播图
    $scope.addswiper = function(param){
    	if(isNaN(param))param = $scope.dataJson.length;
        $scope.dataJson.addAt(param,{
            "type":2,
            "pic":[]
        });
        addpublic(param);
    };
    //分类
    $scope.addclassify = function(param){
    	if(isNaN(param))param = $scope.dataJson.length;
        $scope.dataJson.addAt(param,{
            "type":3,
            "dom":'<tr><td class="empty" data-x="0" data-y="0"></td><td class="empty" data-x="1" data-y="0"></td><td class="empty" data-x="2" data-y="0"></td><td class="empty" data-x="3" data-y="0"></td></tr><tr><td class="empty" data-x="0" data-y="1"></td><td class="empty" data-x="1" data-y="1"></td><td class="empty" data-x="2" data-y="1"></td><td class="empty" data-x="3" data-y="1"></td></tr><tr><td class="empty" data-x="0" data-y="2"></td><td class="empty" data-x="1" data-y="2"></td><td class="empty" data-x="2" data-y="2"></td><td class="empty" data-x="3" data-y="2"></td></tr><tr><td class="empty" data-x="0" data-y="3"></td><td class="empty" data-x="1" data-y="3"></td><td class="empty" data-x="2" data-y="3"></td><td class="empty" data-x="3" data-y="3"></td></tr>',
        });
        addpublic(param);
    };
    
    //间隔
    $scope.addinterval = function(param){
    	if(isNaN(param))param = $scope.dataJson.length;
    	$scope.dataJson.addAt(param,{
            "type":4,
            "height":20,
        });
    	addpublic(param);
    }
    
    //搜索
    $scope.addsearch = function(param){
        if(isNaN(param))param = $scope.dataJson.length;
        var on = false;
        $scope.dataJson.forEach(function(e){
            if(e.type==5)on = true;
        });
        if(on){
            alert("已存在一个搜索");
            return;
        }
        $scope.dataJson.addAt(param,{
            "type":5,
            "attr":{
                "height":27,
                "width":294,
                "border":1,
                "borderColor":"#cacaca",
                "color":"#999",
                "bgColor":"#fff",
            },
        });
        addpublic(param);
    }
    
    //预售
    $scope.addreservation = function(param){
    	if(isNaN(param))param = $scope.dataJson.length;
    	$scope.dataJson.addAt(param,{
            "type":6,
        });
        addpublic(param);
    }
    
    //标题
    $scope.addtitle = function(param){
    	if(isNaN(param))param = $scope.dataJson.length;
    	$scope.dataJson.addAt(param,{
    		"title":"",
            "titleWay":"0",
            "viceTitle":"",
            "showWay":"left",
            "background":"#ffffff",
            "navName":"",
            
            "time":"",
            "author":"",
            "linkName":"",
            
            "type":8,
        });
        addpublic(param);
    }
    
    //文本导航
    $scope.addTextNav = function(param){
    	if(isNaN(param))param = $scope.dataJson.length;
    	$scope.dataJson.addAt(param,{
            "list":[{"title":""}],
            "type":9,
        });
        addpublic(param);
    }
    
    //图片导航
    $scope.addPicNav = function(param){
    	if(isNaN(param))param = $scope.dataJson.length;
    	$scope.dataJson.addAt(param,{
            "pic":[{"src":"","title":""},{"src":"","title":""},{"src":"","title":""},{"src":"","title":""}],
            "type":10,
        });
        addpublic(param);
    }
    
    //橱窗
    $scope.addWindow = function(param){
    	if(isNaN(param))param = $scope.dataJson.length;
    	$scope.dataJson.addAt(param,{
    		"title":"",
    		"showWay":"0",
    		"picGap":"0",
    		"contentTitle":"",
    		"contentExplain":"",
            "pic":[
                   {"src":""},
                   {"src":""},
                   {"src":""}
               ],
            "type":11,
        });
        addpublic(param);
    }
    
    //进入店铺
    $scope.addGoShop = function(param){
    	if(isNaN(param))param = $scope.dataJson.length;
    	$scope.dataJson.addAt(param,{
            "type":12,
            "stoName": stoName,
            "name": '进入店铺'
        });
        addpublic(param);
    }
    
    //公告
	$scope.addNotice = function(param){
		if(isNaN(param))param = $scope.dataJson.length;
    	$scope.dataJson.addAt(param,{
    		"text":"",
            "type":13,
        });
        addpublic(param);
    }
	
	//辅助线
	$scope.addGuide = function(param){
		if(isNaN(param))param = $scope.dataJson.length;
    	$scope.dataJson.addAt(param,{
    		"color":"#e5e5e5",
    		"leftSide":false,
    		"style":0,
            "type":14,
        });
        addpublic(param);
	}
	
	//优惠券
	$scope.addCoupon = function(param){
		if(isNaN(param))param = $scope.dataJson.length;
    	$scope.dataJson.addAt(param,{
            "type":15,
        });
        addpublic(param);
	}
	
	//商品分组
	$scope.addGrouping = function(param){
		if(isNaN(param))param = $scope.dataJson.length;
    	$scope.dataJson.addAt(param,{
    		"nav": 0,
            "page": 2,
            "css":0,
            "btn": true,
            "btnpage": 0,
            "name": false,
            "intro":false,
            "price": true,
            "type":16,
        });
        addpublic(param);
	}
	
	//商品列表
	$scope.addcommodityList = function(param){
		if(isNaN(param))param = $scope.dataJson.length;
    	$scope.dataJson.addAt(param,{
    		"number": "6",
            "page": 2,
            "css":0,
            "btn": true,
            "btnpage": 0,
            "name": false,
            "intro":false,
            "price": true,
            "type":17,
        });
        addpublic(param);
	}
	
	//富文本
	$scope.addRichText = function(param){
		if(isNaN(param))param = $scope.dataJson.length;
    	$scope.dataJson.addAt(param,{
    		"dom":"",
            "type":18,
        });
        addpublic(param);
	}
	
    
    /************************************/
    
    $scope.addmoduleAny = function(param,$event){
    	var str ='<div class="app-add-field">' +
					'<h4>添加内容</h4>'+
					'<ul>'+
					'	 <li ng-click="addcommodity('+param+')"><a>商品</a></li>'+
	                '    <li ng-click="addswiper('+param+')"><a>轮播</a></li>'+
	                '    <li ng-click="addclassify('+param+')"><a>魔方</a></li>'+
	                '    <li ng-click="addinterval('+param+')"><a>辅助<br>空白</a></li>'+
	                '    <li ng-click="addsearch('+param+')"><a>商品<br>搜索</a></li>'+
	                '    <li ng-click="addreservation('+param+')"><a>预售</a></li>'+
	                '    <li ng-click="addtitle('+param+')"><a>标题</a></li>'+
	                '    <li ng-click="addTextNav('+param+')"><a>文本<br>导航</a></li>'+
	                '    <li ng-click="addPicNav('+param+')"><a>图片<br>导航</a></li>'+
	                '    <li ng-click="addWindow('+param+')"><a>橱窗</a></li>'+
	                '    <li ng-click="addGoShop('+param+')"><a>进入<br>店铺</a></li>'+
	                '    <li ng-click="addNotice('+param+')"><a>公告</a></li>'+
	                '    <li ng-click="addGuide('+param+')"><a>辅助线</a></li>'+
	                //'    <li ng-click="addCoupon('+param+')"><a>优惠券</a></li>'+
	                '    <li ng-click="addGrouping('+param+')"><a>商品<br>分组</a></li>'+
	                '    <li ng-click="addcommodityList('+param+')"><a>商品<br>列表</a></li>'+
	                '    <li ng-click="addRichText('+param+')"><a>富文本</a></li>'+
					'</ul>'+
				'</div>';
		
    	$(".app-sidebar-inner>div").html($compile(str)($scope));
    	$event.stopPropagation();
    }

    /***************************************************************************************/

    /**
     * 添加图片
     */
    //商品图片
    $scope.addcommoditypic = function(){
    	for(var i=0; i<imgList.length; i++){
    		$scope.picJson[$scope.pageEdit].imgID.push(imgList[i]);
    	}
    };
    $scope.addcommoditypicone = function(){
        $scope.picJson[$scope.pageEdit].imgID[image_num] = imgList[0];        
    };
    $scope.addchooseGroup = function () {
        $scope.picJson[$scope.pageEdit].imgID = $scope.picJson[$scope.pageEdit].imgID.concat(imgList);
    };
    $scope.editchooseGroup = function () {
        $scope.picJson[$scope.pageEdit].imgID = imgList;
    };
    
    //轮播图片
    $scope.addswiperpic = function(){
        $scope.dataedit.pic[$scope.pic_index].src = imgList;
    };
    $scope.addswiperpic_last = function(){
        $scope.dataedit.pic.push({
        	"src":imgList,
            "title":"",
        });
        $scope.picJson[$scope.pageEdit].imgID.push({})
    };
    

    /****************************************************************************************/

    /**
     * 删除数据
     */
    $scope.removemodule = function(param){
    	$scope.dataJson.splice(param,1);
    	$scope.picJson.splice(param,1);
        $timeout(function(){
            $(".module:first").click();
        },100)
    }
    
    /**
     * 对比数据
     */
    $scope.complete=function(param){
    	if(parseInt(param/3) == param/3)return true;
    }
    
    /*****************************************************************************************/
    
    /**
     * 保存数据
     */
    
    //保存分类
    $scope.saveclassify = function(){
    	$scope.dataedit.dom = $(".classifyDirective table").html().replace(/\"/g, "\'");
    }
    
    /************************************************************************/
    
    /*
     * 保持编辑器数据
     */
    $scope.saverichText = function(){
    	var html = $("#editor-trigger").html();
    	if(html == "<p><br></p>"){
    		$scope.dataedit.html = "";
    		$scope.dataedit.dom = "";
    	}else{
    		$scope.dataedit.html = html;
    		$scope.dataedit.dom = $sce.trustAsHtml(html);
    	}
    }

}])
.directive('admindraggable', ["$timeout",function ($timeout) {
    return {
        restrict:"E",
        templateUrl: "js/admin/admin.html",
        link: function(){
        	$timeout(function(){
        		$(".app-preview-box>.module:eq(0) .actions").click();
        	},100)
        }
    }
}]);

