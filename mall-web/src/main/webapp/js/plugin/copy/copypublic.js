$(function(){
	var clipboard = new Clipboard('.copy_public');
	clipboard.on('success', function(e) {
		var msg = e.trigger.getAttribute('aria-label');
		alert(msg);
	    /*console.info('Action:', e.action);
	    console.info('Text:', e.text);
	    console.info('Trigger:', e.trigger);*/

	   /* e.clearSelection();*/
	});
})