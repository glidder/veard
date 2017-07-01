/**
 * Augmented Reality Coordinator Library (ARC)
 */

/**
 * Function that checks if an object implements the indicated functions
 * It is used to check the implementation of an ARC "interface".
 */
Object.defineProperty(Object.prototype, 'implements', {
    enumerable: false,
    value: function(methods) {
        for(methodName in methods){
        	if(typeof this[methods[methodName]] != 'function') {
            	return false;
        	}
    	}
    	return true;
    }
});
/**
 * Function that returns an array of those input names that do not correspond with that of a
 * function implemented in the object.
 */
Object.defineProperty(Object.prototype, 'listMissingFunctions', {
    enumerable: false,
    value: function(methods) {
    	var missing = [];
        for(methodName in methods){
        	if(typeof this[methods[methodName]] != 'function') {
            	missing.push(methods[methodName]);
        	}
    	}
    	return missing;
    }
});
/**
 * Custom Error function for indicating what functions are missing in the "offender" object
 * for this one to be considered valid by the "offended" object.
 */
function missingFunctionsError(offender,offended){
	this.name = "missingFunctionsError";
	if(typeof offender == "string"){ //Temporary check
		this.message = offender + " is missing the following necessary functions:\n"+ offended;
	} else {
		this.message = offended.constructor.name+" complains about "+offender.constructor.name+
					 " missing the following necessary functions:\n"+
					   offender.listMissingFunctions(offended.listInterface()).join("(),")+"()";
	}
}
missingFunctionsError.prototype = Object.create(Error.prototype);
missingFunctionsError.prototype.constructor = missingFunctionsError;

/**
 * Model class
 * It implements the necessary methods for ARC's interaction with 3D models
 * Inherits from THREE.Object3D
 */
function Model(name){
	THREE.Object3D.apply(this, arguments);
    // Prepare to use THREE.js in ZipLoader
    ZipLoader.use( { 'THREE': THREE } );
    console.log("FUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUURRRRKKKKK: "+window.location.host+"/files/models/"+name+".zip");
	var object = this,
		loader = new ZipLoader( "https://"+window.location.host+"/files/models/"+name+".zip" );//,//new THREE.JSONLoader(),
		//mesh;

    loader.on( 'progress', function ( e ) {
	   console.log( 'loading', e.loaded / e.total * 100 + '%' );
    } );
    loader.on( 'load', function ( e ) {

        // use loadThreeJson method to get geometry and materials
        // just like THREE.JSONLoader
        // You will get `result.geometry` and `result.materials`
        var result = loader.loadThreeJson( name+"/"+name+".json" );
        
        result.materials.forEach( function ( material ) {
            material.skinning = true;
            material.side = THREE.DoubleSide;
        } );
        var mesh = new THREE.SkinnedMesh(
		  result.geometry,
		  new THREE.MultiMaterial( result.materials )
	   );
        mesh.position.z = -0.2;
        
        object.add( mesh );

	   loader.clear();
        
    //loader.load("./models/"+name+"/"+name+".js", function (geometry, materials) {
	//		mesh = new THREE.Mesh(geometry, new THREE.MeshFaceMaterial( materials ));
	//		object.add(mesh);
	//	}, "models/"+name+"/");
    
	
//};
    });
    loader.load();
    this.base = { position : {x:0,y:0,z:0}, rotation : {x:0,y:0,z:0} };
	this.relative = { position : {x:0,y:0,z:0}, rotation : {x:0,y:0,z:0} };
};
    
Model.prototype = new THREE.Object3D();
Model.prototype.update = function(){
	for(var i in this.base.position){
		this.position[i] = this.base.position[i]+this.relative.position[i];
		this.rotation[i] = this.base.rotation[i]+this.relative.rotation[i];
		this.relative.position[i]=0;
		this.relative.rotation[i]=0;
	}
};
Model.prototype.setScale = function(x,y,z){
	this.scale.x = x;
	this.scale.y = y;
	this.scale.z = z;
};
Model.prototype.setBasePose = function(pos_x,pos_y,pos_z,rot_x,rot_y,rot_z){
	this.base.position.x = pos_x;
	this.base.position.y = pos_y;
	this.base.position.z = pos_z;
	this.base.rotation.x = rot_z;
	this.base.rotation.y = rot_y;
	this.base.rotation.z = rot_z;
};
Model.prototype.translate = function(pos_x,pos_y,pos_z){
	this.relative.position.x += pos_x;
	this.relative.position.y += pos_y;
	this.relative.position.z += pos_z;
};
Model.prototype.rotate = function(rot_x,rot_y,rot_z){
	this.relative.rotation.x += rot_z;
	this.relative.rotation.y += rot_y;
	this.relative.rotation.z += rot_z;
};
Model.prototype.rescale = function(x,y,z){
	this.scale.x*=x;
	this.scale.y*=y;
	this.scale.z*=z;
};
	
/**
 * ARC class (Augmented Reality Coordinator)
 * This is the main class that coordinates all other modules.
 */
function ARC(source,canvas,container,/*camera,*/ARlibrary){
	this.source = source;
	this.canvas = canvas;
	this.canvas.width = parseInt(this.canvas.style.width);
    this.canvas.height = parseInt(this.canvas.style.height);
	this.context= this.canvas.getContext("2d");
    //Checking that ARlibrary implements the "interface"
    if(ARlibrary.implements(this.listInterface()))
    	this.ARl = ARlibrary;
    else
    	throw new missingFunctionsError(ARlibrary,this);
    //Creating Renderer 
    this.renderer= new THREE.WebGLRenderer( { alpha: true } );
    this.renderer.setClearColor(0x000000, 0);
    this.renderer.setSize(this.canvas.width, this.canvas.height);
    container.appendChild(this.renderer.domElement);
    
    canvas.style.position = 'absolute';
    this.renderer.domElement.style.position = 'absolute';
    
    this.scene = new THREE.Scene();
    //Creating the screen for showing the camera snapshots
    /*var texture = new THREE.Texture(this.source),
    	geometry = new THREE.PlaneGeometry(1.0, 1.0, 0.0),
    	material = new THREE.MeshBasicMaterial( {map: texture, depthTest: false, depthWrite: false} ),
    	mesh = new THREE.Mesh(geometry, material);
    this.texture = new THREE.Object3D()
    this.texture.position.z = -1;
    this.texture.add(mesh);
    this.scene.add(this.texture);*/
    this.imageData;
    //Adding the camera to the scene
    this.camera = new THREE.PerspectiveCamera(40, this.canvas.width / this.canvas.height, 1, 1000);
    this.scene.add(this.camera);
    //Adding light to the scene
    var directionalLight = new THREE.DirectionalLight(0xffffff);
    directionalLight.position.set(1, 1, 1).normalize();
    this.scene.add(directionalLight);
    //Structure for storing all loaded 3D models.
    this.map = {};
    //Structure for storing all loaded external animations.
    this.amap = {};

    /**
     * Function that updates the Scene.
     * It's behaviour can be modified by the method 'METHODNAME'
     * thus it's defined as a variable and not as a prototype.
     */
    this.updateScene = function(){
  		console.log("Active signals: ",this.ARl.getActiveSignalList().toString());
    };
};
ARC.prototype = {
	/**
	 * Function that returns a list of the names of all necessary methods for ARlibrary
	 */
	listInterface: function(){//TBD
		return ["getActiveSignalList","detectSignals","getPose"];
	},

	/**
	 * Function that incorporates the code generated by blockly into the update function
	 */
	evaluate: function(generatedCode){
		generatedCode="this.updateScene = function() {"+generatedCode+"};";
		console.log('Generated code inside view iframe :', generatedCode);
		eval(generatedCode);
	},

	/**
	 * Function that updates the state of the scene and renderer
	 */
	update: function(){
		if (this.source.readyState === this.source.HAVE_ENOUGH_DATA){
			//snapshot
			this.context.drawImage(this.source, 0, 0, this.canvas.width, this.canvas.height);
			this.imageData = this.context.getImageData(0,0,this.canvas.width,this.canvas.height);

			this.ARl.detectSignals(this.imageData);
			this.updateScene();
			//updateObjects
			for (var i in this.map)
	    		this.map[i].update();

	    	//this.texture.children[0].material.map.needsUpdate = true;
			//render
			this.renderer.autoClear = false;
  			this.renderer.clear();
  			this.renderer.render(this.scene, this.camera);
		}
	},

	/*
	 * Function that allows the addition of external elements to the scene
	 */
	sceneAdd: function(object){
		this.scene.add(object);
	},

	/****************************************************************************************************
	 * BLOCKLY FUNCTIONS
	 ***************************************************************************************************/
	/**
	 * Basic Interaction Models
	 ********************************/
	/**
	 * Function that checks if a signal has just appeared in scene
	 */
	signalIsNew: function(signal_id){
		return (this.ARl.signalIsActive(signal_id) && !this.Arl.signalWasActive(signal_id));
	},

	/**
	 * Function that checks if a signal has just disappear from scene
	 */
	signalIsGone: function(signal_id){
		return (this.ARl.signalWasActive(signal_id) && !this.ARl.signalIsActive(signal_id));
	},

	/**
	 * Function that checks if a signal has rotated
	 */
	signalIsRotated: function(signal_id, direction){
		if (this.ARl.signalWasActive(signal_id)){
		var currentpose = this.ARl.getPose(signal_id),
			previouspose = this.ARl.getPreviousPose(signal_id);
		var rotationz = previouspose.rotation.z - currentpose.rotation.z;

		if(rotationz>0.1 || rotationz<-0.1){
			console.log("\nROTATIOOOOOON: "+previouspose.rotation.z+" - "+currentpose.rotation.z+" = "+rotationz+"\n");
			if(direction=='right' && rotationz>0){
				console.log("RIIIIGHT");
				return true;
			}else if(direction=='left' && rotationz<0){
				console.log("LEEEEEFT");
				return true;
			}
		}
		} else {return false;}
	},

	/**
	 * Function that checks if a signal has been turned over
	 */
	signalIsTurnedOver: function(signal_id){
		var currentpose = this.ARl.getPose(signal_id);
		return (Math.abs(currentpose.rotation.x)>0.75 || Math.abs(currentpose.rotation.y)>0.75);
	},

	/**
	 * Function that checks if two signals are touching
	 */
	signalIsTouching: function(signal_a, signal_b){
		var apose = this.ARl.getPose(signal_a),
			bpose = this.ARl.getPose(signal_b);
		return false;
	},

	/**
	 * Function that checks if a signal has moved
	 */
	signalHasMoved: function(signal_id){
		var currentpose = this.ARl.getPose(signal_id),
			previouspose = this.ARl.getPreviousPose(signal_id);

		return ((currentpose.translation.x - previouspose.translation.x) +
				(currentpose.translation.y - previouspose.translation.y) +
				(currentpose.translation.z - previouspose.translation.z)) > 4; //CREATE [GLOBAL?] THRESHOLD!!
	},

	setObjectMarker: function(object, marker_id){ //WITH THE NEW ONES THIS WILL BE DEPRECATED!!!!!!!!!!!!
		var pose = this.ARl.getPose(marker_id);
		var rotation = pose.rotation;
		var translation = pose.translation;
		if(!this.map[object]){
			console.log("Creating new model "+object);
			this.map[object]=new Model(object);
			this.scene.add(this.map[object]);
		}
		this.map[object].setScale(35.0,35.0,35.0);
	    /*this.map[object].setBasePose(translation[0],translation[1],-translation[2],
	    							-Math.asin(-rotation[1][2]),
	    							-Math.atan2(rotation[0][2], rotation[2][2]),
	    							Math.atan2(rotation[1][0], rotation[1][1]));*/
		this.map[object].setBasePose(translation.x,translation.y,translation.z,
	    							rotation.x,rotation.y,rotation.z);
	},

	runAnimation: function(animation, object){ 
		if(!this.amap[animation]){
			var ramap = this.amap;
			$.ajax({url: "./models/"+animation+".js", dataType: "script", async: false,
			success: function(){
				var tempanim=new Animation();
				//check for an animate function
				if(tempanim.implements("animate")){
    				ramap[animation]=tempanim;
    				console.log("Creating new animation "+animation);
				}else
    				throw new missingFunctionsError(animation,"animate"); //COULD BE BETTER
			}});
		}
		this.amap[animation].animate(this.map[object]);
	},
	/**
	 * DEBUG FUNCTIONS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	 */
	debug: function(){
		if (this.source.readyState === this.source.HAVE_ENOUGH_DATA){
			//snapshot
			this.context.drawImage(this.source, 0, 0, this.canvas.width, this.canvas.height);
			this.imageData = this.context.getImageData(0,0,this.canvas.width,this.canvas.height);

			this.ARl.detectSignals(this.imageData);
			this.drawCorners();
        	this.drawId();
		}
	},

	drawCorners: function(){
		var corners, corner, i, j;

		this.context.lineWidth = 3;

		for(i = 0; i !== this.ARl.currentMarkers.length; ++i){
			corners = this.ARl.currentMarkers[i].corners;
        
        	this.context.strokeStyle = "red";
        	this.context.beginPath(); 

        	for (j = 0; j !== corners.length; ++ j){
	          corner = corners[j];
	          this.context.moveTo(corner.x, corner.y);
	          corner = corners[(j + 1) % corners.length];
	          this.context.lineTo(corner.x, corner.y);
	        }

	        this.context.stroke();
	        this.context.closePath();
	        this.context.strokeStyle = "green";
        	this.context.strokeRect(corners[0].x - 2, corners[0].y - 2, 4, 4);
		}
	},

	drawId: function(){
		var corners, corner, x, y, i, j, pose, signal_id;
	    this.context.strokeStyle = "blue";
	    this.context.lineWidth = 1;
	      
	    for (i = 0; i !== this.ARl.currentMarkers.length; ++ i){
	      corners = this.ARl.currentMarkers[i].corners;
	      
	      x = Infinity;
	      y = Infinity;
	      
	      for (j = 0; j !== corners.length; ++ j){
	        corner = corners[j];
	        
	        x = Math.min(x, corner.x)+70;
	        y = Math.min(y, corner.y);
	      }
	      signal_id = this.ARl.currentMarkers[i].id;
	      pose = this.ARl.getPose(signal_id);
	      this.context.strokeText(signal_id, x, y);
	      this.context.strokeText("rotation x: "+pose.rotation.x.toFixed(2), x, y+10);
	      this.context.strokeText("rotation y: "+pose.rotation.y.toFixed(2), x, y+20);
	      this.context.strokeText("rotation z: "+pose.rotation.z.toFixed(2), x, y+30);
	    }	
	}
};

window.ARC = ARC;
