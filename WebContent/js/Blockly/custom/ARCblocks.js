//https://blockly-demo.appspot.com/static/demos/blockfactory/index.html#pnmsvs

Blockly.Blocks['marker'] = {
  init: function() {
    this.setHelpUrl('http://www.example.com/');
    this.setColour(210);
    this.appendDummyInput()
        .appendField("When marker");
    this.appendValueInput("id")
        .setCheck("Number");
    this.appendDummyInput()
        .appendField(new Blockly.FieldDropdown([["appears","APPEARS"], ["disappears","DISAPPEARS"], ["rotates","ROTATES"], ["moves","MOVES"], ["flips","FLIPS"]]), "DROPDOWN");
    this.appendStatementInput("consequence");
    this.setInputsInline(true);
    this.setTooltip('');
  }
};

Blockly.JavaScript['marker'] = function(block) {
  var value_id = Blockly.JavaScript.valueToCode(block, 'id', Blockly.JavaScript.ORDER_ATOMIC);
    var dropdown_name = block.getFieldValue('DROPDOWN',Blockly.JavaScript.ORDER_ATOMIC);
  var statements_consequence = Blockly.JavaScript.statementToCode(block, 'consequence');
  // TODO: Assemble JavaScript into code variable.
    var condition;
    console.log("JURRRP: "+dropdown_name);
    switch(dropdown_name){
        case 'APPEARS':
            condition = 'this.ARl.signalIsActive';
            break;
        case 'DISAPPEARS':
            condition =  'this.signalIsGone';
            break;
        case 'ROTATES':
            condition = 'this.signalIsRotated';
            break;
        case 'FLIPS':
            condition = 'this.signalIsTurnedOver';
            break;
        case 'MOVES':
            condition = 'this.signalHasMoved';
            break;
        default:
            condition  = 'this.ARl.signalIsActive';
            break;
    }
  var code = 'if('+condition+'('+value_id+')){'+statements_consequence+'}';
  return code;
};

//https://blockly-demo.appspot.com/static/demos/blockfactory/index.html#zwgzqv

Blockly.Blocks['load'] = {
  init: function() {
    this.setHelpUrl('http://www.example.com/');
    this.setColour(160);
    this.appendDummyInput()
        .appendField("Load object");
    this.appendValueInput("obj")
        .setCheck("String");
    this.appendDummyInput()
        .appendField("in marker");
    this.appendValueInput("id")
        .setCheck("Number");
    this.setInputsInline(true);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setTooltip('');
  }
};

Blockly.JavaScript['load'] = function(block) {
  var value_obj = Blockly.JavaScript.valueToCode(block, 'obj', Blockly.JavaScript.ORDER_ATOMIC);
  var value_id = Blockly.JavaScript.valueToCode(block, 'id', Blockly.JavaScript.ORDER_ATOMIC);
  // TODO: Assemble JavaScript into code variable.
  var code = 	'if(this.ARl.signalIsActive('+value_id+')){'+
                'this.hideObject('+value_obj+', false);'+
  				'this.setObjectMarker('+value_obj+','+value_id+');}';
  return code;
};

Blockly.Blocks['hide'] = {
    init: function(){
        this.setHelpUrl('');
        this.setColour(160);
        this.appendDummyInput()
        .appendField("Hide object");
    this.appendValueInput("obj")
        .setCheck("String");
    this.setInputsInline(true);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setTooltip('');
    }
};

Blockly.JavaScript['hide'] = function(block) {
  var value_obj = Blockly.JavaScript.valueToCode(block, 'obj', Blockly.JavaScript.ORDER_ATOMIC);
  // TODO: Assemble JavaScript into code variable.
  var code = 	'this.hideObject('+value_obj+', true);';
  return code;
};

//https://blockly-demo.appspot.com/static/demos/blockfactory/index.html#k3k5ez

Blockly.Blocks['animate'] = {
  init: function() {
    this.setHelpUrl('http://www.example.com/');
    this.setColour(330);
    this.appendDummyInput()
        .appendField("Apply transformation");
    this.appendValueInput("animation")
        .setCheck("String");
    this.appendDummyInput()
        .appendField("in object");
    this.appendValueInput("obj")
        .setCheck("String");
    this.setInputsInline(true);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setTooltip('');
  }
};

Blockly.JavaScript['animate'] = function(block) {
  var value_animation = Blockly.JavaScript.valueToCode(block, 'animation', Blockly.JavaScript.ORDER_ATOMIC);
  var value_obj = Blockly.JavaScript.valueToCode(block, 'obj', Blockly.JavaScript.ORDER_ATOMIC);
  // TODO: Assemble JavaScript into code variable.
  var code = 'this.runAnimation('+value_animation+','+value_obj+');';
  return code;
};

Blockly.Blocks['model'] = {
  init: function() {
      var options = [["none","NONE"]];
      for (i in parent.models){
          options.push([parent.models[i],parent.models[i]])
      }
    this.appendDummyInput()
        .appendField("Model");
    this.appendDummyInput()
        .appendField(new Blockly.FieldDropdown(options), "DROPDOWN");
    this.setOutput(true, null);
    this.setColour(160);
    this.setTooltip('');
    this.setHelpUrl('');
  }
};


Blockly.JavaScript['model'] = function(block) {
    var dropdown_name = block.getFieldValue('DROPDOWN',Blockly.JavaScript.ORDER_ATOMIC);
  return ['"'+dropdown_name+'"', Blockly.JavaScript.ORDER_NONE];
};

Blockly.Blocks['animation'] = {
  init: function() {
      var options=[["none","NONE"]];

      for (i in parent.animations){
          if(parent.animations[i]!="undefined")
            options.push([parent.animations[i],parent.animations[i]])
    }
    this.appendDummyInput().appendField('Animation');
     this.appendDummyInput().appendField(new Blockly.FieldDropdown(options), "DROPDOWN");
    this.setOutput(true, null);
    this.setColour(330);
    this.setTooltip('');
    this.setHelpUrl('');
  }
};

Blockly.JavaScript['animation'] = function(block) {
  var dropdown_name = block.getFieldValue('DROPDOWN',Blockly.JavaScript.ORDER_ATOMIC);
  return ['"'+dropdown_name+'"', Blockly.JavaScript.ORDER_NONE];
};