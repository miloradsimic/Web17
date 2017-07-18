
var subforumsURL = "rest/homepage/subforums";
var topicsURL = "rest/homepage/topics";
var topicURL = "rest/homepage/topic";

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

function loadTopicDetailsAndComments() {
	console.log('LoadTopicDetailsAndComments from file into page.');
	var id = getUrlParameter("id");
	console.log('Topic id: ' + id);
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

function showLoginModal() {
	$("#signupModal").modal("hide");
	$("#loginModal").modal();
	$("body").addClass("notScroll");
}

function showSignupModal() {
	$("#loginModal").modal("hide");
	$("#signupModal").modal();
	$("body").addClass("notScroll");
}

function goToSubforum(id) {
	window.location.href = "subforum.html" + "?id=" + id;
}

function goToTopic(id) {
	window.location.href = "topic.html" + "?id=" + id;
}

function renderSubforumsList(data) {
	console.log('renderSubforumsList form file into page.');
	console.log(data);

	// JAX-RS serializes an empty list as null, and a 'collection of one' as an object (not an 'array of one')
	var list = data == null ? [] : (data instanceof Array ? data : [ data ]);

	$.each(list, function(index, subforum) {

		console.log(subforum);

		var media = $('<div class="subforum media" onclick="goToSubforum(\'' + subforum.subforumId + '\')"></div>');
		var mediaFirstChild = $('<div class="media-left media-top"></div>');
		var mediaThumbnail = $('<img class="media-object" src="' + subforum.icon + '">');

		mediaFirstChild.append(mediaThumbnail);
		media.append(mediaFirstChild);

		var mediaBody = $('<div class="media-body"></div');
		var heading = $('<h4 class="media-heading">' + subforum.name + '</div>');
		var description = $('<p>' + subforum.description + '</p>')


		mediaBody.append(heading);
		mediaBody.append(description);
		media.append(mediaBody);

		$('#mediaContainer').append(media);
	});
}

function renderTopicsList(data) {
	console.log('renderTopicsList form file into page.');
	console.log(data);

	// JAX-RS serializes an empty list as null, and a 'collection of one' as an object (not an 'array of one')
	var list = data == null ? [] : (data instanceof Array ? data : [ data ]);
	var container = $("#topicContainer");

	$.each(list, function(index, topic) {

		console.log("TOPIC: " + topic.title);

		var media = $('<div class="topic media" onclick="goToTopic(\'' + topic.topicId + '\')"></div>');

		var mediaFirstChild = $('<div class="media-left media-top"></div>');
		var mediaThumbnail = $('<img class="media-object" src="' + topic.author.avatar + '">');

		mediaFirstChild.append(mediaThumbnail);
		media.append(mediaFirstChild);

		var mediaBody = $('<div class="media-body"></div');
		var heading = $('<h4 class="media-heading">' + topic.title + '</div>');


		var likes = $('<span class="media-left media-top">Likes: ' + topic.likes + '</span>');
		var dislikes = $('<span class="media-left media-bottom">Dislikes: ' + topic.dislikes + '</span>');
		var date = $('<span class="media-meta pull-right">' + topic.creationDate + '</span>');


		mediaBody.append(heading);
		mediaBody.append(date);
		mediaBody.append(likes);
		mediaBody.append(dislikes);
		media.append(mediaBody);

		$("#mediaContainer").append(media);
	});
}

function renderTopicAndComments(data) {
	console.log('renderTopicAndComments form file into page.');
	console.log(data);
	//var list = data.comments == null ? [] : (data.comments instanceof Array ? data.comments : [ data.comments ]);

	//var list = data == null ? [] : (data instanceof Array ? data : [ data ]);

	console.log("TOPIC: " + data.title);

	var well = $('<div class="well well-sm"></div>');
	var media = $('<div class="media"></div>');
	var heading = $('<h4 class="media-heading media-top">' + data.title + '</div>');


	var content = "";
	switch (data.type) {
	case "TEXT": {
		content = $('<p>' + data.content + '</p>');
		break;
	}
	case "LINK": {
		content = $('<a href="' + data.content + '">' + data.content + '</a>');
		break;
	}
	case "IMAGE": {
		content = $('<img src="' + data.content + '" alt="Here should be image!"</img>');
		break;
	}
	}

	media.append(heading);
	media.append(content);
	well.append(media);

	var commentsContainer = $('<ul class="commentsContainer" id="commentsContainer"></ul>');

	$.each(data.comments, function(index, comment) {
		//		console.log("Comment text: " + comment.text);

		printAll(comment, commentsContainer);
	});


	$("#mediaContainer").append(well);
	$("#mediaContainer").append(commentsContainer);

}

function printAll(comment, commentsContainer) {
	var commentListItem = $('<li></li>');
	var media = $('<div class="media"></div>');
	var mediaLeft = $('<div class="media-left"></div>');
	var mediaLike = $('<div class="glyphicon glyphicon-menu-up"></div>');
	//var mediaRating = $('<div class="glyphicon glyphicon-menu-up"></div>');
	var rating = parseInt(comment.likes) - parseInt(comment.dislikes);
	var mediaRating = $('<div>' + rating +'</div>');
	var mediaDisike = $('<div class="glyphicon glyphicon-menu-down"></div>');
	var mediaBody = $('<div class="media-body"></div>');

	//	var well = $('<div class="comment well well-sm"></div>');
	var commentAuthor = $('<span class="comment-author">' + comment.author.username + '</span>' +
		'<span class="comment-date">' + comment.commentDate + '</span>');
	var commentText = $('<p class="comment-text">' + comment.text + '</p>');
	console.log("Comment text: " + comment.text);



	if (typeof comment.childComments != 'undefined') {
		var commentsContainerChild = $('<ul class="commentsContainer"></ul>');
		$.each(comment.childComments, function(index, commentChild) {
			printAll(commentChild, commentsContainerChild);
		});
		commentListItem.append(commentsContainerChild);
	}

	mediaLeft.append(mediaLike);
	mediaLeft.append(mediaRating);
	mediaLeft.append(mediaDisike);
	mediaBody.append(commentAuthor);
	mediaBody.append(commentText);
	media.append(mediaLeft);
	media.append(mediaBody);
	commentListItem.prepend(media);

	//TODO: Srediti malo redosled komentara
	//	commentListItem.prepend(commentText);
	//	commentListItem.prepend(commentAuthor);
	//	well.append(commentListItem);
	commentsContainer.append(commentListItem);
}

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
;