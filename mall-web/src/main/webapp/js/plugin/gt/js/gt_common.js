var gt_common = null;
var gtcom = null;
$(function(){

     gt_common = function(){

         this.screenWidth = $(window).width();

         this.screenHeight = $(window).height();

     };

     gt_common.prototype.toast_html = function(type)
     { 
     	var str = "",_html = "";
     	switch(type)
     	{
     		case "load":
				   str = '<i><img src="./images/loading.jpg"/></i><section class="gt_txt">正在加载</section>';
				_html = _html + '<section class="gt_toast loadimg">';
				_html = _html + '<section class="gt_bg"></section>';
				_html = _html + '<section class="gt_box">';
				_html = _html + '<section class="gt_zoom">';
				_html = _html + str;
				_html = _html + '</section>';
				_html = _html + '</section>';
				_html = _html + '</section>';
     		break;
     		case "verify":
				  str = '<i class="gt_icon_success_no_circle"></i><section class="gt_txt">验证码已发送</section>';
				_html = _html + '<section class="gt_toast">';
				_html = _html + '<section class="gt_bg"></section>';
				_html = _html + '<section class="gt_box">';
				_html = _html + '<section class="gt_zoom">';
				_html = _html + str;
				_html = _html + '</section>';
				_html = _html + '</section>';
				_html = _html + '</section>';
     		break;
     	}
		return _html;
     }

     gt_common.prototype.toast= function(type,inter)
     {
     	if(localStorage.getItem("gt_toast") == null)
     	{
     		if(inter == "" || inter == null || inter == undefined) inter = 2;
     		$("body").append(this.toast_html(type));
	     	setTimeout(function(){
	     		localStorage.removeItem("gt_toast")
	     		$(".gt_toast").remove();
	     	},inter*1000);
     	}
     }

      gt_common.prototype.toastRemove = function()
      {
      	   $(".gt_toast").remove();
      }

     gt_common.prototype.dialog_html = function(title,_html_gt_html,_html_gt_btn)
     { 
     	var _html = "";
		_html = _html + '<section class="gt_dialog">';
		_html = _html + '<section class="gt_bg"></section>';
		_html = _html + '<section class="gt_box">';
		_html = _html + '<section class="st_label">'+title+'</section>';
		_html = _html + '<section class="st_html">';
		_html = _html + _html_gt_html;
		_html = _html + '</section>';
		_html = _html + '<section class="st_btn gt_flex">';
		_html = _html + _html_gt_btn;
		_html = _html + '</section>';
		_html = _html + '</section>';
		_html = _html + '</section>';	
		return _html;
     }

    gt_common.prototype.dialog = function(gt_html,gt_btn_type,callback,title,inter,abtn_txt)
    { 
     	if(localStorage.getItem("gt_dialog") == null)
     	{
	     	var _html_gt_btn = "",_html_gt_html = "";

	     	if(title == "" || title == null || title == undefined) title = "提示";
	     	//if(inter == "" || inter == null || inter == undefined) inter = 2;

	     	if(!/<\w+\s+(.*)?>/gi.test(gt_html))
	     	{
	            _html_gt_html = '<section class="st_tips_txt"><p>'+gt_html+'</p></section>';
	     	}
	     	else
	     	{
	     		_html_gt_html = gt_html;
	     	}

	        switch(gt_btn_type)
	        {
	            case "a":
	                _html_gt_btn = '<a href="javascript:;" class="gt_close">我知道了</a>';
                    if(inter != undefined)
                    {
	                    setTimeout(function(){
				     		localStorage.removeItem("gt_dialog");
				     		$(".gt_dialog").remove();
				     	},inter*1000);
                    }
	            break;
	            case "b":
	                _html_gt_btn = '<a href="javascript:;" class="gt_close">取 消</a>';
	                if(callback == "" || callback == null || callback == undefined)
	                {
	                	_html_gt_btn = _html_gt_btn + '<a href="javascript:;" class="gt_close">确 定</a>';
	                }
	                else
	                {
	                    _html_gt_btn = _html_gt_btn + '<a href="javascript:;" class="gt_diglog_ok">确 定</a>';
	                }
	            break;
	            case "c":
	                _html_gt_btn = '<a href="javascript:;" class="gt_diglog_ok">'+abtn_txt+'</a>';
                    if(inter != undefined)
                    {
	                    setTimeout(function(){
				     		localStorage.removeItem("gt_dialog");
				     		$(".gt_dialog").remove();
				     	},inter*1000);
                    }
	            break;
	            case "d":
	            	
	                _html_gt_btn = '<a href="javascript:;" class="gt_close">关闭</a>';
	                if(callback == "" || callback == null || callback == undefined)
	                {
	                	_html_gt_btn = _html_gt_btn + '<a href="javascript:;" class="gt_close">关闭</a>';
	                }
	                else
	                {
	                    _html_gt_btn = _html_gt_btn + '<a href="javascript:;" class="gt_diglog_ok">查看</a>';
	                }
	            break;
	        }
     		$("body").append(this.dialog_html(title,_html_gt_html,_html_gt_btn));
	     	this.dialog_handle(callback);
     	}
    }

    gt_common.prototype.gt_dialog_close = function()
 	{
 		$(".gt_dialog").remove();
 	}

    gt_common.prototype.dialogImages = function(img){
        var _html = "";
        var imgObj = new Image();
        imgObj.src = img;
        imgObj.onload  = function(){
            var _H = imgObj.height * 80 / 100;
            var _T = (gtcom.screenHeight - _H)/2;
        	_html = _html + '<section class="gt_dialog">';
			_html = _html + '<section class="gt_bg gt_imgbg" onclick="gtcom.gt_dialog_close();"></section>';
			_html = _html + '<section class="gt_box" style="width: 60%;left:20%;top:'+_T+'px">';
			_html = _html + '<section class="st_html st_imgbox"><img src="';
			_html = _html + img;
			_html = _html + '" style="width:100%;display:block;"/></section>';
			_html = _html + '</section>';
			_html = _html + '</section>';	
			$("body").append(_html);
        }
    }

    gt_common.prototype.dialog_handle = function(callback)
 	{
 		$(".gt_dialog").on("click",".gt_close",function(){
            localStorage.removeItem("gt_dialog");
	     	$(".gt_dialog").remove();
        });

        $(".gt_dialog").on("click",".gt_diglog_ok",function(){
            localStorage.removeItem("gt_dialog");
	     	$(".gt_dialog").remove();
	     	eval(callback + "()");
        });
 	}

    gt_common.prototype.tab = function(){
         $(".gt_tab_handle").find(".gt_hb").find("li").click(function(){
              $(this).addClass('selected').siblings().removeClass('selected');
         });
 	}

 	gt_common.prototype.fixedboxhtml = function(img,position,isflag){
 		var _html = "",open_flashing = "";
 		if(isflag) open_flashing = "open_flashing";
		_html = _html + '<section class="gt_flex gt_fixedbox gt_flex_box gt_'+position+'_fixedbox ' + open_flashing + '">';
		_html = _html + '<section class="gt_cell_primary clearfix">';
		_html = _html + '<img src="' + img + '"/><span class="flashtxt">关注</span>我们';
		_html = _html + '</section>';
		_html = _html + '<section class="gt_cell_primary clearfix">';
		_html = _html + '<a href="javascript:;" data-img = "' + img + '" class="gt_btn gtb_green_a flashbtn focusBtn">按 钮</a>';
		_html = _html + '</section>';
		_html = _html + '<a href="javascript:;" class="gt_icon_clear gt_close"></a>';
		_html = _html + '</section>';
		$("body").append(_html);
 	}

 	gt_common.prototype.fixedbox = function(){
 		$("body").on("click",".gt_flex_box a.gt_close",function(){
             $(this).parents(".gt_flex_box").remove();
 		});
 		$("body").on("click",".gt_flex_box a.focusBtn",function(){
             gtcom.dialogImages($(this).attr("data-img"));
 		});
 	}

    gtcom = new gt_common();
    gtcom.tab();
    gtcom.fixedbox();
});
