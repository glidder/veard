Blockly.Blocks['marker'] = {
  init: function() {
    this.appendDummyInput()
        .appendField(new Blockly.FieldNumber(446, 0), "ID");
    this.setInputsInline(true);
    this.setOutput(true, null);
    this.setColour(160);
    this.setTooltip('');
    this.setHelpUrl('');
  }
};
Blockly.JavaScript['marker'] = function(block) {
  var number_id = block.getFieldValue('ID');
  // TODO: Assemble JavaScript into code variable.
  var code = '...';
  // TODO: Change ORDER_NONE to the correct strength.
  return [code, Blockly.JavaScript.ORDER_NONE];
};

Blockly.Blocks['object'] = {
  init: function() {
    this.appendDummyInput()
        .appendField(new Blockly.FieldDropdown([["none","NONE"]]), "DROPDOWN");
    this.setInputsInline(true);
    this.setOutput(true, null);
    this.setColour(210);
    this.setTooltip('');
    this.setHelpUrl('');
  }
};
Blockly.JavaScript['object'] = function(block) {
  var dropdown_dropdown = block.getFieldValue('DROPDOWN');
  // TODO: Assemble JavaScript into code variable.
  var code = '...';
  // TODO: Change ORDER_NONE to the correct strength.
  return [code, Blockly.JavaScript.ORDER_NONE];
};

Blockly.Blocks['apply'] = {
  init: function() {
    this.appendDummyInput()
        .appendField("Apply transformation");
    this.appendValueInput("TRANSFORMATION")
        .setCheck("String");
    this.appendDummyInput()
        .appendField("to object");
    this.appendValueInput("OBJECT")
        .setCheck("String");
    this.setInputsInline(true);
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
    this.setColour(330);
    this.setTooltip('');
    this.setHelpUrl('');
  }
};
Blockly.JavaScript['apply'] = function(block) {
  var value_transformation = Blockly.JavaScript.valueToCode(block, 'TRANSFORMATION', Blockly.JavaScript.ORDER_ATOMIC);
  var value_object = Blockly.JavaScript.valueToCode(block, 'OBJECT', Blockly.JavaScript.ORDER_ATOMIC);
  // TODO: Assemble JavaScript into code variable.
  var code = '...;\n';
  return code;
};

Blockly.Blocks['load'] = {
  init: function() {
    this.appendDummyInput()
        .appendField("Load object");
    this.appendValueInput("OBJECT")
        .setCheck("String");
    this.appendDummyInput()
        .appendField("in marker");
    this.appendValueInput("MARKER")
        .setCheck("Number");
    this.setInputsInline(true);
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
    this.setColour(210);
    this.setTooltip('');
    this.setHelpUrl('');
  }
};
Blockly.JavaScript['load'] = function(block) {
  var value_object = Blockly.JavaScript.valueToCode(block, 'OBJECT', Blockly.JavaScript.ORDER_ATOMIC);
  var value_marker = Blockly.JavaScript.valueToCode(block, 'MARKER', Blockly.JavaScript.ORDER_ATOMIC);
  // TODO: Assemble JavaScript into code variable.
  var code = '...;\n';
  return code;
};

Blockly.Blocks['condition'] = {
  init: function() {
    this.appendDummyInput()
        .appendField("When marker");
    this.appendValueInput("MARKER")
        .setCheck("Number");
    this.appendDummyInput()
        .appendField(new Blockly.FieldDropdown([["appears","APPEARS"], ["disappears","DISAPPEARS"], ["rotates","ROTATES"], ["moves","MOVES"], ["flips","FLIPS"]]), "DROPDOWN");
    this.appendStatementInput("consequence");
    this.setInputsInline(true);
    this.setColour(160);
    this.setTooltip('');
    this.setHelpUrl('');
  }
};
Blockly.JavaScript['condition'] = function(block) {
  var value_marker = Blockly.JavaScript.valueToCode(block, 'MARKER', Blockly.JavaScript.ORDER_ATOMIC);
  var dropdown_dropdown = block.getFieldValue('DROPDOWN');
  var statements_consequence = Blockly.JavaScript.statementToCode(block, 'consequence');
  // TODO: Assemble JavaScript into code variable.
  var code = '...;\n';
  return code;
};

Blockly.Blocks['transformation'] = {
  init: function() {
    this.appendDummyInput()
        .appendField(new Blockly.FieldDropdown([["none","NONE"]]), "DROPDOWN");
    this.setInputsInline(true);
    this.setOutput(true, null);
    this.setColour(330);
    this.setTooltip('');
    this.setHelpUrl('');
  }
};
Blockly.JavaScript['transformation'] = function(block) {
  var dropdown_dropdown = block.getFieldValue('DROPDOWN');
  // TODO: Assemble JavaScript into code variable.
  var code = '...';
  // TODO: Change ORDER_NONE to the correct strength.
  return [code, Blockly.JavaScript.ORDER_NONE];
};