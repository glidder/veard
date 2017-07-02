/**
 * core.js script that loads the header and footer in every page it is included.
 * 30/07/2017
 * Copyright (c) Luis J. Quintana B.
 */
$(document).ready(function(){
		//Loading header & footer
		$.get('main_menu.html', function(data){
        	$('#header').replaceWith($(data));
        	if (typeof updateContextButton == 'function') { updateContextButton(); }
    	});
		$.get('footer.html', function(data){
        	$('#footer').replaceWith($(data)); 
    	});
});

$.urlParam = function(name){
    var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
    if (results==null){
       return null;
    }
    else{
       return results[1] || 0;
    }
}