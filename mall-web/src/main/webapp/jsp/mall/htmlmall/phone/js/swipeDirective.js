// JavaScript Document
var swiperInterval = null;
angular.module('dragModule', []).directive('myDraggable', ["$document","$timeout",function ($document, $timeout) {
    return {
        link: function ($scope, $element) {
            var touchPoint = 0,
                lengthMax = $scope.datajson.length,
                animation = false,  //控制翻页动画完成后才能翻页
                circulation = false,//循环一次
                indexpage = 0,       //当前翻到第几页
                indexpageold = 0,    //从第几页翻到当前页
                time = null,
                page = [],
                delaypage = [],
                enddelaypage = [],
                iconUp = $(".iconUp"),
                animate_page = 0,
                animateturnThePpage = [
                    ["a_slideZoom_bottomIn 0.8s","a_slideZoom_topIn 0.8s"],["a_slide_bottomIn 0.8s","a_slide_topIn 0.8s"],
                    ["a_fadeIn_bottomIn 0.8s","a_fadeIn_topIn 0.8s"],["a_rotate_bottomIn 0.8s","a_rotate_topIn 0.8s"],
                    ["cutCard-bottom-upward 0.8s ease","cutCard-top-upward 0.8s ease"],["a_reversal_bottomIn 0.8s linear","a_reversal_topIn 0.8s linear"]
                ],
                swipt_i = 0, swipt_doms = null,swipt_z=999;

            !function(){
                $(window).load(function(){
                    //停止所有音频
                    var audios = $("#pages .page").eq(0).find("audio");
                    if('${msg.resource_url}'==''){
                        $(".music-logo").css("background","");//移除音乐图标
                    }else{
                       if(audios.length==0){
                        	 musicplay(0);
                         
                        }else{
                            for(var i=0; i<audios.length; i++){
                                if(audios.eq(i).attr("data-page") == 1){
                                	 musicplay(1);
 		                        	var s = audios.eq(i)[0];
 		                        	 s.play();
 		                        	 document.addEventListener("WeixinJSBridgeReady", function () {
 		                 		        WeixinJSBridge.invoke('getNetworkType', {}, function (e) {
 		                            s.play();
 		                 		       });
 		                 		    }, false);
                                }else{
                                	 musicplay(0);
                                }
                            }
                        }
                    }

                    var g,h,i,a=[],shipei=true,b=$(window).width(),c=$(window).height(),d=380,e=624,f=$("#meta");_init();for(c*d/b>=e?f.attr("content","width=380,initial-scale="+b/d+", minimum-scale="+b/d+", maximum-scale="+b/d+", user-scalable=no"):f.attr("content","width="+b/c*e+",initial-scale="+c/e+", minimum-scale="+c/e+", maximum-scale="+c/e+", user-scalable=no"),g=0;g<$(".page").length;g++)a[g]=$(".page").eq(g);for(a[0].addClass("z-move z-index"),setTimeout(function(){a[0].removeClass("z-move")},30),h=[],i=a[0].find(".delay"),g=0;g<i.length;g++)h[g]=i.eq(g),setTimeout(function(a){return function(){a.show()}}(h[g]),1e3*h[g].attr("data-delay")+25);$(".loading").remove();
                    
                    if(enddelaypage.length > indexpage){
	                    if(enddelaypage[indexpage].length>0){
	                        for(var i=0; i<enddelaypage[indexpage].length; i++){
	                            var dom = enddelaypage[indexpage].eq(i);
	                            if(dom.hasClass("delay"))continue;
	                            dom.show();
	                        }
	                    }
                    }

                    if(!cloud.dataModuleBg[0].bgswiper)return;
                    swipt_doms = page[0].find(".bgswiper>div");
                    swipt_i = 0;
                    swipt_z = 999;
                    swiper_interval();
                    swiperInterval = setInterval(swiper_interval,2400);
                })
            }();

            $timeout(function () {
                pagedom = $(".page");
                for (var i = 0; i < lengthMax; i++) {
                    page[i] = pagedom.eq(i);
                    delaypage[i] = page[i].find(".delay");
                    enddelaypage[i] = page[i].find(".enddelay");
                }
                pagedom.delegate("","webkitAnimationEnd animationEnd", delayAnimate);

                if(cloud.dataModuleBg[indexpage].bgdata == undefined)iconUp.show();
                else if(!cloud.dataModuleBg[indexpage].bgdata.lockslide)iconUp.show();

                $(".tip-off").on("click",function(){
                    $("#tip-off-wrap").show();
                });
                $("#tip-off-wrap").on("click","li",function(){
                    $("#tip-off-wrap .active").removeClass("active");
                    $(this).addClass("active");
                }).on("click",".tip-off-con",function(){
                    if($("#tip-off-wrap .active").length<=0){
                        alert("请选择举报原因");
                    }else{
                        alert("举报成功");
                        $("#tip-off-wrap").hide();
                        var htmlid= $("#htmlid").val();
                        var style = $(".ui-radio:checked").val();
                        $.ajax({   
                    		type:"post",
                    		url:"/mallhtml/79B4DE7C/htmlReport.do",
                    		data:{htmlid:htmlid,style:style},
                    		async:false,
                    		dataType:"json",
                    		success:function(data){
                    			
                    		}
                        })
                    }
                }).on("click",".tip-off-can",function(){
                    $("#tip-off-wrap").hide();
                })
                
                var swiper_box = new Swiper($(".page").eq(0).find(".swiper-container-box"), {
                    paginationClickable: true
                });
                
            });

            $document.on("click",".insidepage",function(){
                var num = $(this).attr("data-page") - 1;
                if(indexpage > num)fullpage("down",num);
                else if(indexpage < num)fullpage("up",num);
            });

            $document.on('touchstart mousedown', function (event) {
                if($(".loading").length>0)return;
                if(!cloud.dataModuleBg[indexpage].bgdata)cloud.dataModuleBg[indexpage].bgdata = {};
                if (animation || cloud.dataModuleBg[indexpage].bgdata.lockslide == true) {
                    iconUp.hide();
                    $document.on('touchmove mousemove', touch_move2);
                    $document.on('touchend mouseup', touch_end2);
                    return
                }
                try{
                    touchPoint = event.originalEvent.changedTouches[0].clientY;
                }catch(e){
                    touchPoint = event.pageY;
                }
                $document.on('touchmove mousemove', touch_move);
                $document.on('touchend mouseup', touch_end);
            });

            /**
             * 判断滑动方向
             * @param event
             */
            function touch_move(event) {
                var touchUp = touchPoint - 70;
                var touchDown = touchPoint + 70;
                try{
                    var y = event.originalEvent.changedTouches[0].clientY;
                    var x = event.originalEvent.changedTouches[0].clientX;
                }catch(e){
                    var y = event.pageY;
                    var x = event.pageX;
                }
                if (touchUp > y) {
                    touch_end();
                    fullpage('up');
                } else if (touchDown < y) {
                    touch_end();
                    fullpage('down');
                }
                event.preventDefault();
            }

            /**
             * 滑动中
             * @param param
             * @param num
             */
            function fullpage(param,num) {
                if (!circulation && indexpage == 0 && param == "down")return;
                animation = true;
                indexpageold = indexpage;
                page[indexpage].removeClass("z-index").addClass("z-move");
                iconUp.hide();
                //手指向上滑动
                if (param == 'up') {
                    indexpage++;
                    if(num>0)indexpage = num;
                    if (indexpage >= lengthMax) {
                        indexpage = 0;
                        circulation = true;
                    }
                    try {
                        animate_page = cloud.dataModuleBg[indexpage].bgdata.slide || 0;
                    }catch (err){
                        animate_page = 0;
                    }
                    if(animate_page == 4){
                        if(indexpage <= 0){
                            page[lengthMax-1].css({"animation":"cutCard-top-downward 0.8s ease","WebkitAnimation":"cutCard-top-downward 0.8s ease"});
                        }else{
                            page[indexpage-1].css({"animation":"cutCard-top-downward 0.8s ease","WebkitAnimation":"cutCard-top-downward 0.8s ease"});
                        }
                    }else if(animate_page == 5){
                        if(indexpage <= 0){
                            page[lengthMax-1].css({"animation":"a_reversal_bottomOut 0.8s linear","WebkitAnimation":"a_reversal_bottomOut 0.8s linear"});
                        }else{
                            page[indexpage-1].css({"animation":"a_reversal_bottomOut 0.8s linear","WebkitAnimation":"a_reversal_bottomOut 0.8s linear"});
                        }
                    }

                    page[indexpage].css({"animation":animateturnThePpage[animate_page][0],"WebkitAnimation":animateturnThePpage[animate_page][0]});
                }
                //手指向下滑动
                else {
                    indexpage--;
                    if(num>0)indexpage = num;
                    if (indexpage < 0) {
                        if (circulation) {
                           indexpage = lengthMax - 1;
                        } else {
                           indexpage = 0;
                        }
                    }
                    try {
                        animate_page = cloud.dataModuleBg[indexpage].bgdata.slide || 0;
                    }catch (err){
                        animate_page = 0;
                    }
                    if(animate_page == 4){
                        if(indexpage+1 >= lengthMax){
                            page[0].css({"animation":"cutCard-bottom-downward 0.8s ease","WebkitAnimation":"cutCard-bottom-downward 0.8s ease"});
                        }else {
                            page[indexpage+1].css({"animation":"cutCard-bottom-downward 0.8s ease","WebkitAnimation":"cutCard-bottom-downward 0.8s ease"});
                        }
                    }else if(animate_page == 5){
                        if(indexpage+1 >= lengthMax){
                            page[0].css({"animation":"a_reversal_topOut 0.8s linear","WebkitAnimation":"a_reversal_topOut 0.8s linear"});
                        }else {
                            page[indexpage+1].css({"animation":"a_reversal_topOut 0.8s linear","WebkitAnimation":"a_reversal_topOut 0.8s linear"});
                        }
                    }
                    page[indexpage].css({"animation":animateturnThePpage[animate_page][1],"WebkitAnimation":animateturnThePpage[animate_page][1]});
                }
                page[indexpage].addClass("z-index z-move");
                time = new Date();

                if(enddelaypage[indexpage].length>0){
                    for(var i=0; i<enddelaypage[indexpage].length; i++){
                        var dom = enddelaypage[indexpage].eq(i);
                        if(dom.hasClass("delay"))continue;
                        dom.show();
                    }
                }

                //停止所有音频
                var audios = $("#pages audio");
                var music = document.getElementById("bgMusic");
                for(var i=0; i<audios.length; i++){
                    audios.eq(i)[0].pause();
                    if(bgmusic){
                    	musicplay(0);
                    }else{
                    	musicplay(1);
                    }
                }
                //播放自动音频
                var _audios = $(".z-index audio");
                for(var i=0; i<_audios.length; i++){
                    if(_audios.eq(i).attr("data-page") == 1){
                        _audios.eq(i)[0].play();
                        musicplay(1);
                    }
                }
            }

            /**
             * 滑动后
             * @param e
             */
            function delayAnimate(e){
                if(e.target != e.currentTarget)return;
                if(swiperInterval){
                    page[indexpageold].find(".bgswiper>div").css("z-index",0).eq(0).css("z-index",1);
                    clearInterval(swiperInterval);
                }
                $(".page").css({"animation":"none","WebkitAnimation":"none"});
                animation = false;
                delaypage[indexpageold].hide();
                enddelaypage[indexpageold].hide();
                pagedom.removeClass("z-move");
                if(!cloud.dataModuleBg[indexpage].bgdata)cloud.dataModuleBg[indexpage].bgdata = {};
                if(!cloud.dataModuleBg[indexpage].bgdata.lockslide){
                    iconUp.show();
                }
                if(delaypage[indexpage].length>0){
                    var array = [];
                    var delay = 0;
                    for(var i=0; i<delaypage[indexpage].length; i++){
                        array[i] = delaypage[indexpage].eq(i);
                        delay = array[i].attr("data-delay")*1000;
                        setTimeout((function(dom,delay){
                            return function (){
                                var _time = new Date();
                                if(_time - time >= delay){
                                    dom.show();
                                }
                            }
                        })(array[i],delay),delay);
                    }
                }
                
                var swiper_box = new Swiper(page[indexpage].find(".swiper-container-box"), {
                    paginationClickable: true
                });
                

                if(!cloud.dataModuleBg[indexpage].bgswiper)return;
                swipt_doms = page[indexpage].find(".bgswiper>div");
                swipt_i = 0;
                swipt_z=999;
                swiper_interval();
                swiperInterval = setInterval(swiper_interval,2400);

            }

            function swiper_interval(){
                swipt_doms.eq(swipt_i).addClass("animate-pic").css("z-index",swipt_z);

                setTimeout((function(dom){
                    return function(){
                        dom.removeClass("animate-pic").css("z-index","");
                    }
                })(swipt_doms.eq(swipt_i)),3000);
                swipt_i++;
                swipt_z--;
                if(swipt_i>=swipt_doms.length)swipt_i=0;
                if(swipt_z<=0)swipt_z=999;
            }

            function touch_end() {
                $document.unbind('touchmove mousemove', touch_move);
                $document.unbind('touchend mouseup', touch_end);
            }

            function touch_move2(event) {
                event.preventDefault();
            }

            function touch_end2() {
                $document.unbind('touchmove mousemove', touch_move2);
            }

            /**
             * 摇一摇
             */
            var SHAKE_THRESHOLD = 250;
            var last_update = 0;
            var interval = new Date();
            var x = y = z = last_x = last_y = last_z = 0;
            function _init() {
                if (window.DeviceMotionEvent) {
                    window.addEventListener('devicemotion', deviceMotionHandler, false);
                    last_update = new Date();
                } else {
                    alert('not support mobile event');
                }
            }
            function deviceMotionHandler(eventData) {
                var acceleration = eventData.accelerationIncludingGravity;
                var curTime = new Date();
                if ((curTime - last_update) > 100) {
                    var diffTime = curTime - last_update;
                    last_update = curTime;
                    x = acceleration.x;
                    y = acceleration.y;
                    z = acceleration.z;
                    var speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 1000;
                    if (speed > SHAKE_THRESHOLD && curTime - interval > 800) {
                        interval = new Date();
                        var _this = $(".z-index");
                        var audios = _this.find("audio");
                        var audio = null;
                        for(var i=0; i<audios.length; i++){
                            if(audios.eq(i).attr("data-page") == 3){
                                audio = audios[i];
                            }
                        }
                        if(!audio.paused){
                            audio.pause();
                            musicplay(0);
                        }else {
                            audio.play();
                            musicplay(1);
                        }
                    }
                    last_x = x;
                    last_y = y;
                    last_z = z;
                }
            }

        }
    }
}])