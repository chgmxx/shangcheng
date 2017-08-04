// JavaScript Document
angular.module('json',[]).factory('jsonService',function(){
	return {
		/**
		 * 删除数据
		 * @param json 操作对象
		 * @param num 删除第几个，0是第一个
		 * @returns {*}
         * @private
         */
		_delete : function(json,num){
					delete json[num];
					var len = json.length, k=0;
					for(var i=0; i<len; i++){
						if(!json[i]){
							k++;
							continue;
						}
						json[i-k] = json[i];
					}
					json.length--;
					return json;
				},

		/**
		 * 添加数据
		 * @param json 操作对象
		 * @param num 加到第几个的后面
		 * @param isobj 是插入数组还是对象
		 * @param val 插入什么
         * @returns {*}
         * @private
         */
		_add    : function(json, num, isobj, val){
					var len = json.length;
					for(var i=len; i>=num; i--){
						if(i == num){
							json[i] = [];
							if(isobj)json[i] = {};
							if(val)json[i] = val;
							break;
						}
						json[i] = json[i-1];
					}
					return json;
				},
	}
})