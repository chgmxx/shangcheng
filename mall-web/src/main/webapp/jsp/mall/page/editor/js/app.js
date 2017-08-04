/**
 * Created by Administrator on 2016/3/22.
 */

var app = angular.module('app',['ngSanitize',"farbtastic",'admin','commodity','swiper',"classify","interval","dragPlan","search","reservation","time","header"]);

Array.prototype.changeAt = function(num1,num2){
	if(isNaN(num1)||num1>this.length||isNaN(num2)||num2>this.length||num1==num2){return false;}
	if(num1>num2){
		var obj = this[num1];
		for(var i=parseInt(num1),n=i-1;i>num2;i--){
			this[i] = this[n--];
		}
		this[num2] = obj;
	}else{
		var obj = this[num1];
		for(var i=parseInt(num1),n=i+1;i<num2;i++){
			this[i] = this[n++];
		}
		this[num2] = obj;
	}
}
Array.prototype.removeAt=function(Index){
    if(isNaN(Index)||Index>this.length){return false;}
    for(var n=parseInt(Index),i=(n+1);i<this.length;i++)
    {
        this[n++]=this[i]
    }
    this.length--;
}
Array.prototype.addAt = function(Index,val){
	if(isNaN(Index)||Index>this.length){return false;}
	if(Index == this.length){
		this[Index] = val;
		return false;
	}
	for(var i=this.length,n=i;i>Index;i--)
    {
		this[i] = this[--n];
    }
	this[Index] = val;
}
