


$(function(){
	
});


/**
 * 保存
 */
function save(){
	var pagStoId = $("#pagStoId").val();
	if(pagStoId == "" || pagStoId <= 0){
		$("#pagStoId").parent().next().find("span").text("不能为空");
		return;
	}
	if(requiredValidate()){
		var index = parent.layer.load(3, {
			offset : '40%',
			shade: [0.4,'#8E8E8E'] 
		});
		var obj = $("#tab").serializeObject();
		var params = {
			obj : JSON.stringify(obj)
		};
		$.ajax({
			 type: "post",	
			url : "/mallPage/saveOrUpdate.do",
			data : params,
			dataType : "json",
			success : function(data){
				parent.layer.close(index);
				parent.alertMsg(data.message);
				if(data.result){
					location.href=$(".urls").val();
				}
			}
		});
	}
}