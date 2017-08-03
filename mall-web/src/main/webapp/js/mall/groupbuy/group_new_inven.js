/**
 * 根据规格来创建库存表格
 */
var step = {
	// SKU信息组合
	Creat_Table : function() {
		step.hebingFunction();
		var SKUObj = $(".controls select.spec-contain");
		// var skuCount = SKUObj.length;//
		var arrayTile = new Array();// 标题组数
		var arrayInfor = new Array();// 盛放每组选中的Select值的对象
		var arrayColumn = new Array();// 指定列，用来合并哪些列
		var specPriceObj = new Object();//存放规格的原价以及库存
		
		var bCheck = true;// 是否全选
		var columnIndex = 0;
		$.each(proSpecArr, function(i, item) {
			arrayColumn.push(columnIndex);
			columnIndex++;

			// 选中的Select取值
			var select = new Array();
			
			// 获取规格名称和值
			var specIds = 0;
			var specValue = "";
			if(item != null){
				var priceObj = {
					invPrice : item.invPrice,
					invNum : item.invNum
				};
				
				specPriceObj[item.specificaIds] = priceObj;
				if(item.specList != null){
					var arr = new Array();
					$.each(item.specList,function(j,itemObj){
						if(itemObj != null){
							var obj = {
								id : itemObj.specificaNameId+"",
								value : itemObj.specificaName,
								specificaIds : item.specificaIds+""
							}
							arrayTile = step.saveArray(arrayTile,obj);
							obj = {
								id : itemObj.specificaValueId+"",
								value : itemObj.specificaValue,
								invenIds : item.id+"",
								specificaIds : item.specificaIds+""
							};
							
							select[select.length] = obj;
//							arr[arr.length] = obj;
//							select = step.saveArray(select,obj);
						}
					});
					
				}
			}
			if (select.length > 0) {
				arrayInfor.push(select);
			}
			if (select.join() == "") {
				// bCheck = false;
			}
		});
		console.log(arrayTile)
		console.log(arrayInfor)
		// 开始创建Table表
		if (bCheck == true && arrayInfor != null && arrayInfor.length > 0) {
			var RowsCount = 0;
			var table = $("#createTable table");
			var thead = table.find("thead");
			var trHead = $("<tr></tr>");
			thead.html("");
			trHead.appendTo(thead);
			var itemColumHead = $("<th class=\"text-right\"  width=\"21%\">是否参团</th>");
			itemColumHead.appendTo(trHead);
			// 创建表头
			$.each(arrayTile, function(index, item) {
				var td = $("<th class=\"text-center\" id=\"" + item.id + "\"  width=\"15%\">"
						+ item.value + "</th>");
				td.appendTo(trHead);
			});
			var itemColumHead = $("<th class=\"th-price\" width=\"15%\">原价(元)</th><th class=\"th-stock\" width=\"21%\">拼团价</th><th class=\"th-code\" width=\"10%\">库存</th>");
			itemColumHead.appendTo(trHead);

			var tbody = table.find("tbody");
			tbody.html("");
			tbody.appendTo(table);
			// //生成组合
			//var zuheDate = step.doExchange(arrayInfor);
			var zuheDate = arrayInfor;
			var tdLen = 4 ;
			if (zuheDate.length > 0) {
				// 创建行
				$.each(zuheDate,function(index, item) {
					var tr = $("<tr></tr>");
					tr.appendTo(tbody);

					console.log(item);
					
					tdLen += item.length;
					
					var values ="";
					var ids = "";
					var invenIds = "";
					var specId ="";
					var specTds = "";
					var specValueIds = "";
					$.each(item,function(j,obj){
						specId = obj.specificaIds;
						invenIds = 	obj.invenIds;
						specTds += "<td id=\"" + obj.id+ "\" class='specCla'>" + obj.value+ "</td>";
						if(specValueIds != ""){
							specValueIds += ",";
						}
						specValueIds += obj.id;
					});

					var price = "";
					var num = "";
					var code = "";
					var isGroup = 0;
					var groupPrice = 0;
					var priceId = 0;
					if(specPriceObj != null){
						var obj = specPriceObj[specId];
						if(obj != null){
							price = obj.invPrice;
							num = obj.invNum;
						}
					}
					if(proSpecObj != null){
						var obj = proSpecObj[specValueIds];
						if(obj != null){
							groupPrice = obj.groupPrice;
							isGroup = obj.isJoinGroup;
							priceId = obj.id;
						}
					}
					if(groupPrice <= 0){
						groupPrice = "";
					}
					

					var td0 = $("<td class=\"text-right inp\"><input type=\"checkbox\" id=\""+priceId+"\" specId=\""+specId+"\" invenId=\""+invenIds+"\" class=\"js-default input-small radio\" value=\"1\"><div>不勾选代表此规格不参加团购</div></td>");
					if (isGroup == 1) {
						td0.find("input").attr("checked",
								"checked");
					}
					td0.appendTo(tr);
					
					if(specTds != ""){
						$(specTds).appendTo(tr);
					}
					
					var td1 = $("<td  class='inp'>"+price+"</td>");
					td1.appendTo(tr);
					var td2 = $("<td  class='inp'><input data-stock-id=\"0\" type=\"text\" class=\"js-price input-mini\" value=\""
							+ groupPrice
							+ "\" maxlength=\"8\"><div class=\"red\"></div></td>");
					td2.appendTo(tr);
					
					var td3 = $("<td  class='inp'>"+num+"</td>");
					td3.appendTo(tr);
				});
			}

			$("input#proStockTotal").attr("disabled", "disabled");

			var isDefault = $(".js-default").is(":checked");
			if (!isDefault) {
				$(".js-default").attr("checked", "checked");
			}
			// 结束创建Table表
			arrayColumn.pop();// 删除数组中最后一项
			// 合并单元格
//			$(table).mergeCell({
//				// 目前只有cols这么一个配置项, 用数组表示列的索引,从0开始
//				cols : arrayColumn
//			});
			$("#createTable").show();
			$("#createTable tfoot td").attr("colspan",tdLen);

			/**
			 * 鼠标选中事件 验证输入内容
			 */
			$("input.js-price").focus(function() {
				valiTable($(this));

			});
			/**
			 * 鼠标失去焦点 验证输入内容
			 */
			$("input.js-price").blur(function() {
				valiTable($(this));
			});
//			$("input.js-default").change(function() {
//				step.saveInp($(this));
//				if($(this).is(":checked")){
//					var price =  $(this).parents("tr").find(".js-price").val();
//					//预览
//					var iframe = document.getElementById("oneIframe").contentWindow;
//					var pPrice = iframe.document.getElementById("pPrice");
//					$(pPrice).html(price);
//				}
//			});
//			if(specIdObj != null){
//				step.saveInp(null);
//			}
			$("#createTable table").show();
		} else {
			// 未全选中,清除表格
			// document.getElementById('createTable').innerHTML="";
			// $("#createTable").html("");
			$("#createTable").hide();
			$("#createTable tbody").html("");
		}
		loadWindow();
	},// 合并行
	hebingFunction : function() {
		$.fn.mergeCell = function(options) {
			return this.each(function() {
				var cols = options.cols;
				for (var i = cols.length - 1; cols[i] != undefined; i--) {
					// fixbug console调试
					// console.debug(cols[i]);
					mergeCell($(this), cols[i]);
				}
				dispose($(this));
			});
		};
		function mergeCell($table, colIndex) {
			$table.data('col-content', ''); // 存放单元格内容
			$table.data('col-rowspan', 1); // 存放计算的rowspan值 默认为1
			$table.data('col-td', $()); // 存放发现的第一个与前一行比较结果不同td(jQuery封装过的),
			// 默认一个"空"的jquery对象
			$table.data('trNum', $('tbody tr', $table).length); // 要处理表格的总行数,
			// 用于最后一行做特殊处理时进行判断之用
			// 进行"扫面"处理 关键是定位col-td, 和其对应的rowspan
			$('tbody tr', $table).each(
					function(index) {
						// td:eq中的colIndex即列索引
						var $td = $('td:eq(' + colIndex + ')', this);
						// 取出单元格的当前内容
						var currentContent = $td.html();
						// 第一次时走此分支
						if ($table.data('col-content') == '') {
							$table.data('col-content', currentContent);
							$table.data('col-td', $td);
						} else {
							// 上一行与当前行内容相同
							if ($table.data('col-content') == currentContent) {
								// 上一行与当前行内容相同则col-rowspan累加, 保存新值
								var rowspan = $table.data('col-rowspan') + 1;
								$table.data('col-rowspan', rowspan);
								// 值得注意的是 如果用了$td.remove()就会对其他列的处理造成影响
								$td.hide();
								// 最后一行的情况比较特殊一点
								// 比如最后2行 td中的内容是一样的,
								// 那么到最后一行就应该把此时的col-td里保存的td设置rowspan
								if (++index == $table.data('trNum'))
									$table.data('col-td').attr('rowspan',
											$table.data('col-rowspan'));
							} else { // 上一行与当前行内容不同
								// col-rowspan默认为1, 如果统计出的col-rowspan没有变化, 不处理
								if ($table.data('col-rowspan') != 1) {
									$table.data('col-td').attr('rowspan',
											$table.data('col-rowspan'));
								}
								// 保存第一次出现不同内容的td, 和其内容, 重置col-rowspan
								$table.data('col-td', $td);
								$table.data('col-content', $td.html());
								$table.data('col-rowspan', 1);
							}
						}
					});
		}
		// 同样是个private函数 清理内存之用
		function dispose($table) {
			$table.removeData();
		}
	},
	// 组合数组
	doExchange : function(doubleArrays) {
		var len = doubleArrays.length;
		if (len >= 2) {
			var arr1 = doubleArrays[0];
			var arr2 = doubleArrays[1];
			var len1 = doubleArrays[0].length;
			var len2 = doubleArrays[1].length;
			var newlen = len1 * len2;
			var temp = new Array(newlen);
			var index = 0;
			for (var i = 0; i < len1; i++) {
				for (var j = 0; j < len2; j++) {
					var obj = new Object();
					obj.value = arr1[i].value + "," + arr2[j].value;
					obj.id = arr1[i].id + "," + arr2[j].id;
					obj.invenIds = arr1[i].invenIds + "," + arr2[j].invenIds;
					temp[index] = obj;
					index++;
				}
			}
			var newArray = new Array(len - 1);
			newArray[0] = temp;
			if (len > 2) {
				var _count = 1;
				for (var i = 2; i < len; i++) {
					newArray[_count] = doubleArrays[i];
					_count++;
				}
			}
			return step.doExchange(newArray);
		} else {
			return doubleArrays[0];
		}
	},
	// 保存输入内容
	saveInp : function(obj) {
		if(obj == null || obj == ""){
			$(".table-sku-stock tbody td.inp input").each(function() {
				step.doSaveVal($(this));
			});
		}else{
			step.doSaveVal(obj);
		}
		

	},doSaveVal:function(tObj){
		var obj = {};
		var price = num = code = isDefault = "";
		var key = "";
		tObj.parents("tr").find(".specCla").each(function() {
			if (key != "") {
				key += ",";
			}
			key += $(this).text();
		});
		if(specInvenInpObject!=null && specInvenInpObject[key] != null){
			obj = specInvenInpObject[key];
		}
		if (tObj.hasClass("js-price")) {// 价格
			price = tObj.val();
			obj.price = price;
		} else if (tObj.hasClass("js-stock-num")) {// 库存
			num = tObj.val();
			obj.num = num;
		} else if (tObj.hasClass("js-code")) {// 商家编码
			code = tObj.val();
			obj.code = code;
		}else if(tObj.hasClass("js-default")){
			if(tObj.is(':checked')){
				isDefault = 1;
			}else{
				isDefault = 0;
			}
			obj.isDefault=isDefault;
		}
		
		specInvenInpObject[key] = obj;
	},saveArray:function(arr,obj){
		var bol = true;
		if(arr != null && arr.length > 0){
			$.each(arr,function(k,tObj){
				if(tObj.id == obj.id){
					bol = false;
					return;
				}
			});
		}
		if(bol){
			arr[arr.length] = obj;
		}
		return arr;
	}
}
