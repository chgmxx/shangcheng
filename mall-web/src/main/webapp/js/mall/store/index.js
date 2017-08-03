


$(function(){
	$("#go").click(function(){
		$("#queryForm").submit();
	});
});


/**
 * 删除
 * @param obj
 */
function del(ids){
	//event.stopPropagation();
	parent.layer.confirm('确认要删除此数据吗?', {icon: 3, title:'提示',offset: "40%"}, function(index){
		parent.layer.close(index);
		var params = {
			ids : ids
		};
		var index2 = parent.layer.load(3, {
			offset : '40%',
			shade: [0.4,'#8E8E8E'] 
		});
		$.post("store/delete.do",params,function(data){
			parent.alertMsg(data.message);
			if(data.result){
				parent.layer.close(index2);
				location.reload();
			}
		},"json");
			
	});
}

/**
 * 删除多个
 */
function batchdel(){
	var ids = "";
	$("input[name='genre']").each(function(index){
		if($(this).is(":checked")){
			ids += $(this).val()+",";
		}
	});
	ids = ids.substring(0,ids.lastIndexOf(","));
	if(ids != "" && ids != undefined){
		del(ids);
	}else{
		parent.alertMsg("请至少选择一条数据！");
	}
}

/**
 * 查看二维码
 * @param id
 */
function viewQR(id){
	parent.openIframeNoScoll("店铺二维码","400px","300px","store/viewQR.do?id="+id);
}