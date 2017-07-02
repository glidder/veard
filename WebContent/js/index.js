/**
 * index.js script that loads the contents of the index from the REST API
 * 30/07/2017
 * Copyright (c) Luis J. Quintana B.
 */
$(document).ready(function(){
	//Load Viewer List function content into the main page list
	$("#thumbs").load("./rest/dao/projects/limit/8"); //the last number is the max number of projects
                                                      //shown when the page loads
});

/* Set the width of the side navigation to 250px and the left margin of the page content to 250px */
function openNav() {
    document.getElementById("mySidenav").style.width = "250px";
    document.getElementById("main").style.marginLeft = "250px";
}

/* Set the width of the side navigation to 0 and the left margin of the page content to 0 */
function closeNav() {
    document.getElementById("mySidenav").style.width = "0";
    document.getElementById("main").style.marginLeft = "0";
} 