
var subforumsURL = "rest/homepage/subforums";
var topicsURL = "rest/homepage/topics";
var topicURL = "rest/homepage/topic";

executeOnLoad();



function executeOnLoad() {
	assignListeners();
	loadLoggedUser();
//	var user = JSON.parse(sessionStorage.getItem('user'));
//	if (user != null) {
//		//		console.log("ExecuteOnLoad executed not null!" + sessionStorage.getItem('user'));
//		$(function() {
//			showLogoutButons(user.firstName);
//		});
//	} else {
//		showLoginButons();
//		//		console.log("ExecuteOnLoad executed is null!" + sessionStorage.getItem('user'));
//	}
//	console.log("ExecuteOnLoad executed!" + sessionStorage.getItem('user'));
}

function assignListeners() {
	$(function() {
		$("#loginSubmit").on( "click", login);
		$("#signUpSubmit").on( "click", signup);
		$("#logout").on( "click", logout);
		
	});
}

//Redirect functions

function goToSubforum(id) {
	window.location.href = "subforum.html" + "?id=" + id;
}

function goToTopic(id) {
	window.location.href = "topic.html" + "?id=" + id;
}

//AJAX calls



function loadTopicDetailsAndComments() {
//	console.log('LoadTopicDetailsAndComments from file into page.');
	var id = getUrlParameter("id");
//	console.log('Topic id: ' + id);
	$.ajax({
		type : 'GET',
		url : topicURL + "/" + id,
		dataType : "json", // data type of response
		success : renderTopicAndComments,
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR: " + errorThrown + "\nTextStatus: " + textStatus);
		}
	});
}

function loadSubforums() {
	console.log('LoadSubforums from file into page.');
	$.ajax({
		type : 'GET',
		url : subforumsURL,
		dataType : "json", // data type of response
		success : renderSubforumsList,
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR: " + errorThrown + "\nTextStatus: " + textStatus);
		}
	});
}


function loadTopics() {
	console.log('LoadTopics from file into page.');
	var id = getUrlParameter("id");
	console.log('Subforum id: ' + id);
	$.ajax({
		type : 'GET',
		url : topicsURL + "/" + id,
		dataType : "json", // data type of response
		success : renderTopicsList,
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR: " + errorThrown + "\nTextStatus: " + textStatus);
		}
	});
}

//Helper functions

//Helper function to serialize all the form fields into a JSON string
function formToJSON(id, count) {
	return JSON.stringify({
		"id" : id,
		"count" : count,
	});
}

function getUrlParameter(sParam) {
	var sPageURL = decodeURIComponent(window.location.search.substring(1)),
		sURLVariables = sPageURL.split('&'),
		sParameterName,
		i;

	for (i = 0; i < sURLVariables.length; i++) {
		sParameterName = sURLVariables[i].split('=');

		if (sParameterName[0] === sParam) {
			return sParameterName[1] === undefined ? true : sParameterName[1];
		}
	}
}