
$(document).ready(function(){
	//console.log("LOLOLOL"+$.urlParam('proc'))
	$.get( "./rest/dao/projects/"+$.urlParam('proc'), function( data ) {
		$("#theviewer").load("editor_view.html",function () {  
            setTimeout(function(){
				loadCodeandRun(data);	
			}, 500);// cough cough
			
	    });
	});
	
	
	//var iframeView = document.querySelector('iframe.viewFrame')
	//iframeView.contentWindow.run(code);
});