
/** 自定义一个序列化表单的方法* */
$.fn.serializeObject = function()    
{    
   var o = {};    
   $(this).find("input").each(function(index){
	   if($(this).attr("name") != undefined){
		   if($(this).attr("type")=="text" || $(this).attr("type")=="password" || $(this).attr("type")=="hidden"){
			   o[$(this).attr("name")] = $(this).val();
		   }
		   
		   if($(this).attr("type")=="checkbox"){
			  if($(this).is(":checked")){
				  o[$(this).attr("name")] = 1;
			  }else{
				  o[$(this).attr("name")] = 0;
			  }
		   }
		   if($(this).attr("type")=="radio"){
				  if($(this).is(":checked")){
					  o[$(this).attr("name")] = $(this).val();
				  }else{
					  o[$(this).attr("name")] = $(this).val();
				  }
			   }
	   }
   });
   
   $(this).find("select").each(function(index){
	   if($(this).attr("name") != undefined){
		   o[$(this).attr("name")] = $(this).val(); 
	   }
   });
   
   $(this).find("textarea").each(function(index){
	   if($(this).attr("name") != undefined){
		   o[$(this).attr("name")] = $(this).val(); 
	   }
   });
   return o;    
};

/* ie10以下不支持placeHolder  password属性不支持placeHolder */
;(function(window, document, $) {

		var isOperaMini = Object.prototype.toString.call(window.operamini) == '[object OperaMini]';
		var isInputSupported = 'placeholder' in document.createElement('input') && !isOperaMini;
		var isTextareaSupported = 'placeholder' in document.createElement('textarea') && !isOperaMini;
		var prototype = $.fn;
		var valHooks = $.valHooks;
		var propHooks = $.propHooks;
		var hooks;
		var placeholder;

		if (isInputSupported && isTextareaSupported) {

			placeholder = prototype.placeholder = function() {
				return this;
			};

			placeholder.input = placeholder.textarea = true;

		} else {

			placeholder = prototype.placeholder = function() {
				var $this = this;
				$this
					.filter((isInputSupported ? 'textarea' : ':input') + '[placeholder]')
					.not('.placeholder')
					.bind({
						'focus.placeholder': clearPlaceholder,
						'blur.placeholder': setPlaceholder
					})
					.data('placeholder-enabled', true)
					.trigger('blur.placeholder');
				return $this;
			};

			placeholder.input = isInputSupported;
			placeholder.textarea = isTextareaSupported;

			hooks = {
				'get': function(element) {
					var $element = $(element);

					var $passwordInput = $element.data('placeholder-password');
					if ($passwordInput) {
						return $passwordInput[0].value;
					}

					return $element.data('placeholder-enabled') && $element.hasClass('placeholder') ? '' : element.value;
				},
				'set': function(element, value) {
					var $element = $(element);

					var $passwordInput = $element.data('placeholder-password');
					if ($passwordInput) {
						return $passwordInput[0].value = value;
					}

					if (!$element.data('placeholder-enabled')) {
						return element.value = value;
					}
					if (value == '') {
						element.value = value;
						// Issue #56: Setting the placeholder causes problems if
						// the element continues to have focus.
						if (element != safeActiveElement()) {
							// We can't use `triggerHandler` here because of
							// dummy text/password inputs :(
							setPlaceholder.call(element);
						}
					} else if ($element.hasClass('placeholder')) {
						clearPlaceholder.call(element, true, value) || (element.value = value);
					} else {
						element.value = value;
					}
					// `set` can not return `undefined`; see
					// http://jsapi.info/jquery/1.7.1/val#L2363
					return $element;
				}
			};

			if (!isInputSupported) {
				valHooks.input = hooks;
				propHooks.value = hooks;
			}
			if (!isTextareaSupported) {
				valHooks.textarea = hooks;
				propHooks.value = hooks;
			}

			$(function() {
				// Look for forms
				$(document).delegate('form', 'submit.placeholder', function() {
					// Clear the placeholder values so they don't get submitted
					var $inputs = $('.placeholder', this).each(clearPlaceholder);
					setTimeout(function() {
						$inputs.each(setPlaceholder);
					}, 10);
				});
			});

			// Clear placeholder values upon page reload
			$(window).bind('beforeunload.placeholder', function() {
				$('.placeholder').each(function() {
					this.value = '';
				});
			});

		}

		function args(elem) {
			// Return an object of element attributes
			var newAttrs = {};
			var rinlinejQuery = /^jQuery\d+$/;
			$.each(elem.attributes, function(i, attr) {
				if (attr.specified && !rinlinejQuery.test(attr.name)) {
					newAttrs[attr.name] = attr.value;
				}
			});
			return newAttrs;
		}

		function clearPlaceholder(event, value) {
			var input = this;
			var $input = $(input);
			if (input.value == $input.attr('placeholder') && $input.hasClass('placeholder')) {
				if ($input.data('placeholder-password')) {
					$input = $input.hide().next().show().attr('id', $input.removeAttr('id').data('placeholder-id'));
					// If `clearPlaceholder` was called from
					// `$.valHooks.input.set`
					if (event === true) {
						return $input[0].value = value;
					}
					$input.focus();
				} else {
					input.value = '';
					$input.removeClass('placeholder');
					input == safeActiveElement() && input.select();
				}
			}
		}

		function setPlaceholder() {
			var $replacement;
			var input = this;
			var $input = $(input);
			var id = this.id;
			if (input.value == '') {
				if (input.type == 'password') {
					if (!$input.data('placeholder-textinput')) {
						try {
							$replacement = $input.clone().attr({ 'type': 'text' });
						} catch(e) {
							$replacement = $('<input>').attr($.extend(args(this), { 'type': 'text' }));
						}
						$replacement
							.removeAttr('name')
							.data({
								'placeholder-password': $input,
								'placeholder-id': id
							})
							.bind('focus.placeholder', clearPlaceholder);
						$input
							.data({
								'placeholder-textinput': $replacement,
								'placeholder-id': id
							})
							.before($replacement);
					}
					$input = $input.removeAttr('id').hide().prev().attr('id', id).show();
					// Note: `$input[0] != input` now!
				}
				$input.addClass('placeholder');
				$input[0].value = $input.attr('placeholder');
			} else {
				$input.removeClass('placeholder');
			}
		}

		function safeActiveElement() {
			// Avoid IE9 `document.activeElement` of death
			// https://github.com/mathiasbynens/jquery-placeholder/pull/99
			try {
				return document.activeElement;
			} catch (err) {}
		}

	}(this, document, jQuery));



$(function(){
//	alert($(window).parent().find(".dao-hang").height());
	//$(window.parent.document.getElementById('ifr')).height($(window.parent.document).height());
	$(function() {
		$('input').placeholder();
	});
	// 初始化必填文本项
	$("input[type='text']").each(function(){
		$(this).blur(function(){
			var span = $(this).parent().siblings().find("span");
			if($(this).attr("required") == "required"){
				if($(this).val() == ""){
					span.text("不能为空！");
					return;
				}else{
					span.text("");
				}
			}
			if($(this).attr("validation") == "integer"){
				if(!IsInteger($(this).val())){
					span.text("请输入一个正整数！");
				}else{
					span.text("");
				}
			}else if($(this).attr("validation") == "number"){
				if(!IsNum($(this).val())){
					span.text("请输入一个数字！");
				}else{
					if($(this).attr("decimal") != ""){
						var decimal = $(this).val().split(".")[1];
						if(decimal != undefined && decimal != ""){
							if(decimal.length > 2){
								span.text("最多两位小数！");
								$(this).val("");
							}else{
								span.text("");
							}
						}else{
							span.text("");
						}
					}else{
						span.text("");
					}
				}
			}else if($(this).attr("validation") == "hour"){
				if($(this).val().indexOf(":") == -1 && $(this).val().indexOf("：") == -1){
					span.text("格式错误！");
				}else{
					var timestr = $(this).val().split(":") || $(this).val().split("：");
					if(timestr[0] == "" || timestr[1] == "" || parseInt(timestr[0]) > 24 || parseInt(timestr[0]) < 0 || parseInt(timestr[1]) >= 60 || parseInt(timestr[1]) < 0){
						span.text("格式错误！");
					}
				}
			}else if($(this).attr("validation") == "linkWay"){
				var isPhone = /^([0-9]{3,4})?[0-9]{7,8}$/;
				var isMob = /^1[3|4|5|7|8][0-9]{9}$/;
				if (!isMob.test($(this).val()) && !isPhone.test($(this).val())){
					span.text("请输入正确的联系方式！");
				}else{
					span.text("");	
				}
			}else if($(this).attr("validation") == "phone"){
				var isMob = /^1[3|4|5|7|8][0-9]{9}$/;
				if($(this).val() == ""){
					span.text("不能为空！");
				}
				else if (!isMob.test($(this).val())){
					span.text("请输入正确的手机号码！");
				}else{
					span.text("");	
				}
			}
			else{
				span.text("");
			}
		});
	});
	
});

/** 判断必填项是否填写完整* */
function requiredValidate(){
	var flag = true;
	$("input[required='required']").each(function(){
		if($(this).val() == ""){
			$(this).parent().siblings().find("span").text("不能为空！");
			flag = false;
		}
	});
	return flag;
}

/** 是否为数字* */
function IsNum(s)
{	
    if (s!=null && s!="")
    {
        return !isNaN(s);
    }
    return false;
}

/** 是否为正整数* */
function IsInteger(s)
{
    if(s!=null){
        var r,re;
        re = /\d*/i; // \d表示数字,*表示匹配多个数字
        r = s.match(re);
        if(r <= 0) return false;
        return (r==s)?true:false;
    }
    return false;
}
/****
 * 手机号码验证
 * @returns {Boolean}
 */
function isMob(phone){
	var isMob = /^1[3|4|5|7|8][0-9]{9}$/;
	if (!isMob.test(phone)){
		return false;
	}else{
		return true;
	}
}

/**选项卡切换**/
function changeTop(obj){
	location.href = "/"+$(obj).attr("href");
}
