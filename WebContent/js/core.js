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