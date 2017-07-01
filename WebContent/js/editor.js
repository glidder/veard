var models = [];

function addModels(model) {
    models.push(model);
}

function updateVariables() {
    
    var formatedVariables="";
    for(var model in models){
        formatedVariables+=model+"\n";
    }
    $('#assets').contents().find('#modellist').load("./rest/dao/models");
    $('#variablelist').html(formatedVariables);
    $('#modellist').load("./rest/dao/models");
}

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
			"	<input type='text' class='form-control' id='desc' name='desc'>"+
			"</div>"+
			"<button id='saveButton' type='button' class='btn btn-default' onclick='saveWorkspace();closePopover();'>Submit</button>"+
		"</form>"+
		"<script>"+
		"	$('#saveButton').on('click', function(event){"+
		"		saveWorkspace();"+
		"		$('#popover').popover('hide');"+
		"	});"+
		"</script>"
	});
}
	
$(document).ready(function(){
	$("#myTabContent").css('height', '100%').css('height', '-='+($("#myTab").height()*2)+'px');
    $.get( "./rest/dao/projects/"+$.urlParam('proc'), function( data ) {
		$("#theviewer").load("editor_view.html",function () {  
            setTimeout(function(){
				loadCodeandRun(data);	
			}, 500);// cough cough
			
	    });
	});
});

$(window).resize(function(){
	$("#myTabContent").css('height', '100%').css('height', '-='+($("#myTab").height()*2)+'px');
});

window.addEventListener("beforeunload", function (e) {
	console.log($('#modifiedStatus').text());
	console.log($('#contextButton').html());
	if($('#modifiedStatus').text() != 'saved' && Blockly.JavaScript.workspaceToCode() != ''){
		(e || window.event).returnValue = null;
		return ("There's unsaved work! Are you sure you want to leave?");
	}
});

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
        if (typeof($.urlParam('load')) !== 'undefined') {
            loadWorkspaceProject($.urlParam('load'));
        }
		if( location.hash ){
			loadWorkspace()
			setTimeout(function(){
				// should have the viewer iframe ready... 
				//runWorkspace()	
			}, 500);	// cough cough
		}
	})
	
	
	//////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////

	function saveWorkspace(){
		var xmlDom	= Blockly.Xml.workspaceToDom(Blockly.getMainWorkspace());
		var xmlText	= Blockly.Xml.domToText(xmlDom)
		var jsText = Blockly.JavaScript.workspaceToCode(Blockly.getMainWorkspace());

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
	  	console.assert(location.hash);
		var xmlText	= decodeURIComponent(location.hash.substr(1));
		var xmlDoc	= Blockly.Xml.textToDom(xmlText);
		Blockly.Xml.domToWorkspace(xmlDoc,Blockly.getMainWorkspace());	  	
	}
    function loadWorkspaceProject(project){
        $.get("./rest/dao/projects/edit/"+project, function(data){
            console.log("WOOOOOORRGHH "+data);
            var xmlDoc	= Blockly.Xml.textToDom(data);
            Blockly.Xml.domToWorkspace(xmlDoc,Blockly.getMainWorkspace);
        })
    }
	function runWorkspace(){
		var generatedCode	= Blockly.JavaScript.workspaceToCode();
        console.log('generatedCode', generatedCode)
		var iframeView		= document.querySelector('iframe.viewFrame');
		iframeView.contentWindow.run(generatedCode);
	}
	function resetWorkspace(){
		Blockly.mainWorkspace.clear();
	}

$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
  var target = $(e.target).attr("href") // activated tab
    saveWorkspace();
    runWorkspace();
})
