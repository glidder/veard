
function updateContextButton() {
	$('#contextButton').html("<button id='popover' data-toggle='popover' data-container='body' data-placement='left' class='btn btn-default navbar-btn'>Save</button>");
	//Instead of a popover, it could be a modal
	//Also, the form content should be saved and/or loaded if the projects was loaded or previously saved.
	$('#popover').popover({
		  html: true,
		  title: "Save Form",
		  content: "<form id='saveForm' role='form' method='POST' enctype='application/x-www-form-urlencoded' action='./rest/dao/upload/project'>"+
			"<div class='form-group''>"+
			"	<label for='text'>Name:</label>"+
			"	<input type='text' class='form-control' id='name' name='name'>"+
			"</div>"+
			"<div class='form-group'>"+
			"	<label for='text'>Description:</label>"+
			"	<input type='text' class='form-control' id='desc name='desc'>"+
			"</div>"+
			"<button id='saveButton' type='button' class='btn btn-default' onclick='saveWorkspace();closePopover();'>Submit</button>"+
		"</form>"+
		"<script>"+
		"	$('#saveButton').on('click', function(event){"+
		//"		console.log('LOLOLOLOL');"+
		"		saveWorkspace();"+
		"		$('#popover').popover('hide');"+
		"	});"+
		"</script>"//,
		  //trigger: 'manual',
		  //delay: {'show':1000, 'hide':250},
	});
	//$('#popover').on({
	//	  click:function(){
	//	    $('#popover').not(this).popover('hide');
	//	  }
	//});
	//document.querySelector('#saveButton').addEventListener('click', function(event){
//	$('#saveButton').on('click', function(event){
//		console.log("LOLOLOLOL");
//		saveWorkspace();
//		$('#popover').popover('hide');
//	});
}
	
$(document).ready(function(){
	$("#myTabContent").css('height', '100%').css('height', '-='+($("#myTab").height()*2)+'px');
});

$(window).resize(function(){
	$("#myTabContent").css('height', '100%').css('height', '-='+($("#myTab").height()*2)+'px');
});

window.addEventListener("beforeunload", function (e) {
	  //saveFormData();
	console.log($('#modifiedStatus').text());
	console.log($('#contextButton').html());
	if($('#modifiedStatus').text() != 'saved' && Blockly.JavaScript.workspaceToCode() != ''){
		(e || window.event).returnValue = null;
		return ("There's unsaved work! Are you sure you want to leave?");
	}
});

//$(window).onbeforeunload=function(){

	//if(document.querySelector('#modifiedStatus').innerText	!= 'saved'){
	//	return ("There's unsaved work! Are you sure you want to leave?");
	//}
//};

// export blockly namespace locally
	window.addEventListener('blocklyReady', function(event){
		window.Blockly = event.detail.Blockly;
	})
	
	// autoload content if present
	window.addEventListener('blocklyReady', function(){
		Blockly.addChangeListener(function(){
			// NOTE - it seems to be called even if there it no change in the workspace
			$('#modifiedStatus').text('modified');
		})
		Blockly.mainWorkspace.traceOn(true);
	})
	window.addEventListener('blocklyReady', function(){
		if( location.hash ){
			loadWorkspace()
			setTimeout(function(){
				// should have the viewer iframe ready... 
				//runWorkspace()	
			}, 500);	// couch couch
		}
	})
	document.querySelector('#runButton').addEventListener('click', function(event){
		saveWorkspace()
		runWorkspace()
	})
	/*document.querySelector('#saveButton').addEventListener('click', function(event){
		saveWorkspace()
	})*/
	/*document.querySelector('#resetButton').addEventListener('click', function(event){
		resetWorkspace()
		
		var viewIframe	= document.querySelector('iframe.viewFrame')
		viewIframe.contentWindow.location.reload()
	})*/
	
	
	//////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////

	function saveWorkspace(){
		var xmlDom	= Blockly.Xml.workspaceToDom(Blockly.getMainWorkspace());
		var xmlText	= Blockly.Xml.domToText(xmlDom)
		var jsText = Blockly.JavaScript.workspaceToCode();
		//window.location.hash	= encodeURIComponent(xmlText)
		console.log("BIGOLPEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEENIS\n\n\n"+xmlText+"\n"+jsText);
		//Include codes in the form data 
		var input1 = $("<input>", { type: "hidden", name: "ecode", value: xmlText });
		$('#saveForm').append($(input1));
		var input2 = $("<input>", { type: "hidden", name: "jcode", value: jsText });
		$('#saveForm').append($(input2));
		// Submit complete form
		$('#saveForm').submit();
		// changed modifiedStatus
		$('#modifiedStatus').text('saved');
	}
    function closePopover(){
        $('#popover').popover('hide');
    }
	function loadWorkspace(){
	  	console.assert(location.hash)
		var xmlText	= decodeURIComponent(location.hash.substr(1))
		var xmlDoc	= Blockly.Xml.textToDom(xmlText);
		Blockly.Xml.domToWorkspace(Blockly.getMainWorkspace(), xmlDoc)	  	
	}
	function runWorkspace(){
		var generatedCode	= Blockly.JavaScript.workspaceToCode();
        console.log('generatedCode', generatedCode)
		var iframeView		= document.querySelector('iframe.viewFrame')
        //iframeView.contentWindow.init(); // TEMPORAL????
		iframeView.contentWindow.run(generatedCode);
	}
	function resetWorkspace(){
		Blockly.mainWorkspace.clear();
	}

$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
  var target = $(e.target).attr("href") // activated tab
  //if ($(target)=='#viewer') {
    saveWorkspace();
    runWorkspace();
 //}
})
/*
	$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
		  var target = $(e.target).attr("href") // activated tab
		  alert($(#navbar).height());
		  $(.tabset, .tab-content, .tab-pane, .tabbable).height('calc(100% -'+$(#navbar).height()*2+'px)');
		  if ($(target).is(':empty')) {
		    $.ajax({
		      type: "GET",
		      url: "/article/",
		      error: function(data){
		        alert("There was a problem");
		      },
		      success: function(data){
		        $(target).html(data);
		      }
		  })
		 }
		})
		
	$('a[data-toggle="tab"]').on('shown', function () {
    $('.selectpicker').selectpicker('setSize');
    
	});
*/
