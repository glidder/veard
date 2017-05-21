
$(document).ready(function(){
	//console.log("LOLOLOL"+$.urlParam('proc'))
	$.get( "./rest/viewer/project/"+$.urlParam('proc'), function( data ) {
		$("#theviewer").load("editor_view.html",function () {  
			$(this).contentWindow.run(data);
	    });
	});
	
	
	//var iframeView = document.querySelector('iframe.viewFrame')
	//iframeView.contentWindow.run(code);
});