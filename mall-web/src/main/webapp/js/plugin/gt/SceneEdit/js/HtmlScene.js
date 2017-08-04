function music(){
	 layer.ready(function(){ 
		     layer.open({
		        type: 2,
		        title: '背景音乐',
		        fix: false,
		        shade : 0.2,
		        shadeClose: false,
		        closeBtn : 0,
		        shift : 1,
		        maxmin: false,
		        area: ['500px', '420px'],//定义宽、高
		        content: '/scene/musicUrl.do?id='+scene_detail_id,
		        yes : function(index){
		            layer.close(index);
		        },
		        end : function(){
		        	reload();
		        }
		    });
		});
}
function layerwindow(music_id,musci_name,index){
	if(music_id==0){
   		   $(".music").attr("class","music no");
   		   $(".music-name").text("没有背景");
   	 }else{
   		   var music_name1 = ""+musci_name+"";
   		   $(".music").attr("class","music");
   		   $(".music-name").text(music_name1);
   	 }
	layer.close(index);
	
}

//保存音乐素材库的
function fathervallue(index,musicid,musicname,musicurl,musicscene){
	layer.close(index);
	if(musicscene==0||musicscene=='0'){
		$(".layui-layer-content").find("iframe")[0].contentWindow.fhmat(musicid,musicname,musicurl);
	}else{
	$("#musicsrc").val(musicurl).trigger("change");
	$("#musicname").val(musicname).trigger("change");
	}
}
//关闭音乐素材库
function closelayertwo(index){
	layer.close(index);
}