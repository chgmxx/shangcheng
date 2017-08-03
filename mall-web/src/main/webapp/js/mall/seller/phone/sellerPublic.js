if($(".loading").length > 0){
	$(".loading").hide();
}

var deviceWidth = document.documentElement.clientWidth;
if(deviceWidth > 750) deviceWidth = 750;
document.documentElement.style.fontSize = deviceWidth /7.5 + 'px';














/** 自定义一个序列化表单的方法* */
$.fn.serializeObject = function()    
{    
   var o = {};    
   $(this).find("input").each(function(index){
	   if($(this).attr("name") != undefined){
		   if($(this).attr("type")=="text" || $(this).attr("type")=="password" || $(this).attr("type")=="hidden"){
			   o[$(this).attr("name")] = $(this).val();
		   }
		   
		   if($(this).attr("type")=="checkbox" || $(this).attr("type")=="radio"){
			  if($(this).is(":checked")){
				  o[$(this).attr("name")] = 1;
			  }else{
				  o[$(this).attr("name")] = 0;
			  }
		   }
	   }
   });
   
   $(this).find("select").each(function(index){
	   if($(this).attr("name") != undefined){
		   o[$(this).attr("name")] = $(this).val(); 
	   }
   });
   
   $(this).find("textarea").each(function(index){
	   if($(this).attr("name") != undefined){
		   o[$(this).attr("name")] = $(this).val(); 
	   }
   });
   return o;    
};