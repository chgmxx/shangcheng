var editor;
	    KindEditor.ready(function(K) {
		editor = K.create('textarea[name="content"]',
			{
			width : '100%',
			height : '100%',
			minWidth : '150px',
			resizeType : 1,
			items : [
			'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
			'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
			'insertunorderedlist', '|', 'emoticons', 'image', 'link'],allowPreviewEmoticons : true,
			allowImageUpload : true,
			allowFileManager : true,
			allowFileUpload : true,
			uploadJson : '../linkurl/uploadJson',
			fileManagerJson : '../linkurl/fileManagerJson',
			formatUploadUrl : false,
			wellFormatMode : true,
			afterBlur : function() {
				var bodyHtml = $(
				   document.getElementsByTagName("iframe")[0].contentWindow.document.body)
						.html();
				if (bodyHtml.length !== 0) {
					$("#url_address").val("");
					$("#url_address").attr("readonly",
						"readonly");
				} else {
					$("#url_address").removeAttr(
						"readonly");
				}
			}
		});
	});