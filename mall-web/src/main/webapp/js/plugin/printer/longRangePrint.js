;function longRangePrint(url, params){
	params = "paramJson=" + params;
	return longRange_printerAjax(url, params);
};
function longRange_printerAjax(url, params){
	var xmlHttpRequest = null;
	if(window.ActiveXObject) {
		xmlHttpRequest = new ActiveXObject("Microsoft.XMLHTTP");
	}else {
		xmlHttpRequest = new XMLHttpRequest();
	}
	if(xmlHttpRequest == null){
		alert("你的浏览器不支持该插件！");
		return;
	}
	xmlHttpRequest.open("post", url, false);
	xmlHttpRequest.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	xmlHttpRequest.send(params);
	var res = null;
	if(xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200){
		res = xmlHttpRequest.responseText;
	}else{
		alert("获取数据失败，错误代号为："+xmlHttpRequest.status+"错误信息为："+xmlHttpRequest.statusText);
	}
	var resObj = eval('(' + res + ')');
	return resObj;
};