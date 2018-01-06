function tanchu(){
	//弹出素材库
		 layer.open({
			    type: 2,
			    title: '素材库',
			    shadeClose: true,
			    shade: 0.2,
			    area: ['820px', '500px'],
			    offset : "10px",
			    content: '/common/material.do?selectType=1',
			 }); 

}
//关闭素材库弹窗
function go_back(){
	layer.closeAll();
}
//素材库回调方法
function image(id,url){
	layer.closeAll();
	// debugger;
	if(url!=null&&url!=""&&url!=undefined){
		$("#imageurlst").val(url);
		
		$(".confirm-btn").click();
	}else{
      var ids = JSON.stringify(id);
//      ids = eval(ids);
//      for(var i=0;i<ids.length;i++)
//      {
//    	alert(" value:"+ids[i].url );
//      }
      var swiper = $(".swiper-btn-tip");
      if(swiper.length>0){
    	  swiper.val(ids).click();
      }else{
    	  debugger;
    	  $(".swiper-bg-btn-tip").val(ids).click();
      }
      
	}
}


//弹出音乐层
function music(){
	var musicurl = $("#htmlmusicurl").val();;
	var musicname = $("#htmlmusicname").val();
	var addres = $("#addres").val();
	var player_style = $("#player_style").val();
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
		        content: '/mallhtml/E9lM9uM4ct/musicUrl.do?musicurl='+musicurl+"&musicname="+musicname+"&addres="+addres+"&player_style="+player_style,
		        yes : function(index){
		            layer.close(index);
		        },
		        end : function(){
		        	reload();
		        }
		    });
		});
}
function layerwindow(musicurl2,musicname2,player_style2,addres2,index){
	 $("#addres").val(addres2);
	 $("#player_style").val(player_style2);
	if(musicurl2==undefined||musicurl2==null){
  		   $(".music").attr("class","music no");
  		   $(".music-name").text("没有背景");
  	 }else{
  		$("#htmlmusicurl").val(musicurl2);;
  		 $("#htmlmusicname").val(musicname2);
  		   var music_name1 = ""+musicname2+"";
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


