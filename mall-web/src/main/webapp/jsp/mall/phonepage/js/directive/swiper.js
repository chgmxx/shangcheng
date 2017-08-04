/**
 * Created by boo on 2016/3/22.
 */

angular.module('swiper',[]).directive('swiperDraggable', ["$timeout",function ($timeout) {
    return {
        link: function(scope,element){
        	$timeout(function(){
        		if(element.find('.swiper-slide').length<=1)return;
        		var swiper = new Swiper(element.find('.swiper-container'), {
        	        pagination: element.find(".swiper-pagination"),
        	        paginationClickable: true,
        	        spaceBetween: 30,
        	        autoplay: 4500,
        	        autoplayDisableOnInteraction: false,
        	        lazyLoading: true,
        	        lazyLoadingInPrevNextAmount:2
        	    });
        	})
        }
    }
}]);

