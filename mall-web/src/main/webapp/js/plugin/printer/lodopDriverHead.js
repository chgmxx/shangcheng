function lodop_getLodop(oOBJECT,oEMBED){
    var strCLodopInstall="<br><font color='#FF00FF'>CLodop云打印服务(localhost本地)未安装启动!点击这里<a href='CLodopPrint_Setup_for_Win32NT.exe' target='_self'>执行安装</a>,安装后请刷新页面。</font>";
    var strCLodopUpdate="<br><font color='#FF00FF'>CLodop云打印服务需升级!点击这里<a href='CLodopPrint_Setup_for_Win32NT.exe' target='_self'>执行升级</a>,升级后请刷新页面。</font>";
    var LODOP;
    try{
        var isIE = (navigator.userAgent.indexOf('MSIE')>=0) || (navigator.userAgent.indexOf('Trident')>=0);
        try{ LODOP=getCLodop();} catch(err) {};
        if (!LODOP && document.readyState!=="complete") {alert("你还没有安装打印机驱动，请下载安装！"); return;};
        if (!LODOP) {
		 if (isIE) document.write(strCLodopInstall); else
		 document.documentElement.innerHTML=strCLodopInstall+document.documentElement.innerHTML;
                 return;
        } else {

	         if (CLODOP.CVERSION<"2.0.6.8") { 
			if (isIE) document.write(strCLodopUpdate); else
			document.documentElement.innerHTML=strCLodopUpdate+document.documentElement.innerHTML;
		 };
		 if (oEMBED && oEMBED.parentNode) oEMBED.parentNode.removeChild(oEMBED);
		 if (oOBJECT && oOBJECT.parentNode) oOBJECT.parentNode.removeChild(oOBJECT);	
	};
	LODOP.SET_LICENSES("广东谷通科技有限公司","13601BD5AB0D5B544607D05CDFBE1565","","");
    return LODOP;
    } catch(err) {alert("getLodop出错:"+err);};
};
function lodop_checkIsInstall() {	
	try{ 
//	    var LODOP = getLodop(); 
		if (LODOP.VERSION) {
			 if (LODOP.CVERSION){}
			 return true;
		}else{
			alert("你还没有安装打印机驱动，请下载安装！");
			return false;
		}
	}catch(err){ 
		alert("你还没有安装打印机驱动，请下载安装！");
		return false;
	} 
};
function lodop_print(url, params){
	if(lodop_checkIsInstall()){
		params = "paramJson=" + params;
		var lodop_Obj = lodop_printerAjax(url, params);
		if(lodop_Obj == null){
			return;
		}
		lodop_printOrPrview(false, lodop_Obj.results, lodop_Obj.printerConfiguration.printererVal, lodop_Obj.printerConfiguration.paperVal);
	}
};
function lodop_prview(url, params){
	if(lodop_checkIsInstall()){
		params = "paramJson=" + params;
		var lodop_Obj = lodop_printerAjax(url, params);
		if(lodop_Obj == null){
			return;
		}
		lodop_printOrPrview(true, lodop_Obj.results, lodop_Obj.printerConfiguration.printererVal, lodop_Obj.printerConfiguration.paperVal);
	}
};
function lodop_printOrPrview(toPrview, lodop_html, indexValue, pageSizeValue){
	LODOP.SET_LICENSES("广东谷通科技有限公司","13601BD5AB0D5B544607D05CDFBE1565","","");
    LODOP.SET_PRINTER_INDEXA(indexValue);
    LODOP.SET_PRINT_PAGESIZE(0,0,0,pageSizeValue);
    LODOP.ADD_PRINT_HTM(0,0,"100%","100%",lodop_html);
    if (toPrview)
        LODOP.PREVIEW();
    else
        LODOP.PRINT();
};
function lodop_printerAjax(url, params){
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
	// console.log(params);
	xmlHttpRequest.send(params);
	var res = null;
	if(xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200){
		res = xmlHttpRequest.responseText;
	}else{
		alert("获取数据失败，错误代号为："+xmlHttpRequest.status+"错误信息为："+xmlHttpRequest.statusText);
	}
	var resObj = eval('(' + res + ')');
	if(!resObj.success){
		alert(resObj.errors);
		return null;
	}
	var rest = eval('(' + resObj.message + ')');
	return rest;
}