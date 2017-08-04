// JavaScript Document

angular.module('colorTo',[]).factory('colorToService',function(){
	function rgb2hex(rgb) {
	  rgb = rgb.match(/^rgb\((\d+),\s*(\d+),\s*(\d+)\)$/);
	  function hex(x) {
		  console.log(rgb);
		return ("0" + parseInt(x).toString(16)).slice(-2);
	  }
	  return "#" + hex(rgb[1]) + hex(rgb[2]) + hex(rgb[3]);
	}
	function xtorgb(x){
		var x = x.match(/\d|[a-z]/g);
		function rgb(r){
			return parseInt(r,16).toString(10);
		}
		if(x.length <= 3){
			return rgb(x[0]) + "," + rgb(x[1]) + "," + rgb(x[2]);
		}else{
			return rgb(x[0]+x[1]) + "," + rgb(x[2]+x[3]) + "," + rgb(x[4]+x[5]);
		}
	}
	return xtorgb;
});