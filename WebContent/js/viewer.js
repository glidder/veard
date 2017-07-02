/**
 * viewer.js script that injects the code retrieved from the REST API into the viewer.
 * 30/07/2017
 * Copyright (c) Luis J. Quintana B.
 */
$(document).ready(function(){
	$.get( "./rest/dao/projects/"+$.urlParam('proc'), function( data ) {
		$("#theviewer").load("editor_view.html",function () {  
            setTimeout(function(){
				loadCodeandRun(data);	
			}, 1000);// cough cough
            setTimeout(function(){
				loadCodeandRun(data);	
			}, 5000);
			
	    });
	});
});