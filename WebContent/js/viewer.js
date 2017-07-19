/**
 * viewer.js script that injects the code retrieved from the REST API into the viewer.
 * 30/07/2017
 * Copyright (c) Luis J. Quintana B.
 */
$(document).ready(function(){
     var project = $.urlParam('proc');
     if (typeof(project) === 'string') {
         $.get( "./rest/dao/projects/"+project, function( data ) {
             $("#theviewer").load("editor_view.html",function () {
                 setTimeout(function(){
                     loadCodeandRun(data);
                 }, 1000);// cough cough
              });
         });
     }
});
//$(document).ready(function(){
//	var code;
	//var iframeView = $( "#theviewer" );//document.querySelector('iframe.viewFrame');
//	$.get( "https://46.101.38.23:8443/veard/rest/dao/projects/"+$.urlParam('proc'), function( data ) {
		//var iframeView		= document.querySelector('iframe.viewFrame');        
		 //iframeView.contentWindow.init(); // TEMPORAL???? 		
		//$('#theviewer')[0].contentWindow.loadCodeandRun(data);
//		$("#theviewer").load("editor_view.html",function () {  
//            setTimeout(function(){
//				onLoad();
//				loadCodeandRun(data);	
//			}, 1000);// cough cough
//            setTimeout(function(){
//				loadCodeandRun(data);	
//			}, 5000);
//			
//	    });
//		code = data;
//	});
//	setTimeout(function(){
//		loadCodeandRun(code);
//	}, 5000);
//});
