console.log(666)
var Print = function(){};
Print.prototype.open =function(t){
	$(document).find('#gtFormBox').show();
	//window.parent.printPopup(true);
	document.getElementById("gtForm").reset()
};
Print.prototype.close =function(t){
	$(document).find('#gtFormBox').hide();
	$(document).find('#printLargeImage').hide();
	//window.parent.printPopup();
};
Print.prototype.enlarge = function(){
	$(document).find('#printLargeImage').show();
};
Print.prototype.pageNum = 1;
Print.prototype.lastPageNum = null;
Print.prototype.pageBox = function(lastPageNum){ 
	this.lastPageNum = lastPageNum;
	lastPageNum = lastPageNum || 'N';	
	var html = '<div class="gt-page-box gt-fr" style="margin-right: 30px;">'+
	'          <a href="javascript:;" onclick="print.firstPage()" title="首页" class="iconfont">&#xe676;</a>'+
	'	      <a href="javascript:;" onclick="print.beforePage()" title="上一页" class="iconfont">&#xe67b;</a>'+
	'	      <a href="javascript:;" title="当前页" class="gt-page-now">'+this.pageNum+' </a>'+
	'	      <a href="javascript:;" onclick="print.nextPage()" title="下一页" class="iconfont">&#xe679;</a>'+
	'	      <a href="javascript:;" onclick="print.lastPage()" title="最后一页" class="iconfont">&#xe677;</a>'+
	'         <a href="javascript:;" class="gt-page-list">共'+lastPageNum+'页</a>'+
	'    </div>';
	$('#pageBox').html('');
	return $('#pageBox').append(html);
};
Print.prototype.onloadData = function(){
	var that = this;
	var options = {
        url: 'http://rap.duofee.com/mockjsdata/30/printer/conf/templateList.do',
        type: 'post',
        data: {pageNum: that.pageNum},
        dataType: 'json',
        success: function(data) {
            if (data.success) {
                var jsonData = data.data;
                console.log(jsonData)
                if (jsonData != undefined) {
                    var result = "";
                    jsonData.forEach(function(item){
                    	result+='<div class="template-item">'+
                    	'	<div class="title">'+item.templateName+'</div>'+
                    	'	<div class="image"><img src="'+item.imgUrl+'" /></div>'+
                    	'	<div class="bottom">'+
                    	'		<div class="btns">'+
                    	'			<a class="preview" href="javascript:;">打印预览</a>'+
                    	'			<a class="enlarge" onclick="print.enlarge()"  href="javascript:;">图片放大</a>'+
                    	'			<a onclick="print.open()" class="sure" href="javascript:;">选择模版</a>'+
                    	'		</div>			'+
                    	'	</div>'+
                    	'</div>';
                    });
                    $('#content').html('');
                    $("#content").append(result);
                    
                    if(jsonData.length >= 6){
                    	that.pageBox(6);
                    }
                } else {
                    parent.layer.msg(data.msg, {offset: '200px'}, {icon: 5});
                }
            } else {
                parent.layer.msg(data.msg, {offset: '200px'}, {icon: 5});
            }
        }
    };
    $.ajax(options)
};
Print.prototype.firstPage = function(){ //第一页
	this.pageNum = 1;
	this.onloadData();
};
Print.prototype.beforePage = function(){ //上一页
	if(this.pageNum <= 1){
		parent.layer.msg('第一页', {offset: '200px'}, {icon: 5});
		return;
	}else{
		this.pageNum -= 1;
	}
	this.onloadData();
};
Print.prototype.nextPage = function(){ //下一页
	if(this.pageNum >=  this.lastPageNum){
		parent.layer.msg('最后一页', {offset: '200px'}, {icon: 5});
		return;
	}else{
		this.pageNum += 1;
	}
	this.onloadData();
};
Print.prototype.lastPage = function(){ //最后一页
	this.pageNum = this.lastPageNum;
	this.onloadData();
};
var print = new Print();