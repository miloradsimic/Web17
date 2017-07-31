var DEFAULT_IMAGE = "resources/cvet.jpg"


function showLoginButons() {
	$("#loginButtons").show();
	$("#logoutButton").hide();
}

function showLogoutButons(data) {
	$("#loginButtons").hide();
	$("#welcomeUser").html('Welcome ' + data + '. Click here for ' +
		'<a id="logout" role="button" onclick=logout()>log out</a>');
	$("#logoutButton").show();

}

function showLoginModal() {
	$("#signupModal").modal("hide");
	$("#loginModal").modal();
	$("body").addClass("notScroll");
	$("#loginSubmit").on("click", login);
}

function showSignupModal() {
	$("#loginModal").modal("hide");
	$("#signupModal").modal();
	$("body").addClass("notScroll");
	$("#signUpSubmit").on("click", signup);
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
		console.log("TOPIC user: " + topic.author.username);

		var media = $('<div class="topic media" onclick="goToTopic(\'' + topic.topicId + '\')"></div>');

		var mediaFirstChild = $('<div class="media-left media-top"></div>');
		var slika = DEFAULT_IMAGE;

		if (topic.author.avatar !== "") {
			slika = topic.author.avatar;
		}
		var mediaThumbnail = $('<img class="media-object" src="' + slika + '"/>');

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
	var topic = data.topic;
	var ratings = data.ratings;
	//var list = data.comments == null ? [] : (data.comments instanceof Array ? data.comments : [ data.comments ]);

	//var list = data == null ? [] : (data instanceof Array ? data : [ data ]);

	console.log("TOPIC: " + topic.title);

	var well = $('<div id="' + topic.topicId + '" class="topic-root well well-sm"></div>');
	var media = $('<div class="media"></div>');
	var heading = $('<h4 class="media-heading media-top">' + topic.title + '</div>');


	var content = "";
	switch (topic.type) {
	case "TEXT": {
		content = $('<p>' + topic.content + '</p>');
		break;
	}
	case "LINK": {
		content = $('<a href="' + topic.content + '">' + topic.content + '</a>');
		break;
	}
	case "IMAGE": {
		content = $('<img src="' + topic.content + '" alt="Here should be image!"</img>');
		break;
	}
	}

	media.append(heading);
	media.append(content);
	well.append(media);

	var commentsContainer = $('<ul class="commentsContainer" id="commentsContainer"></ul>');

	$.each(topic.comments, function(index, comment) {
		//		console.log("Comment text: " + comment.text);

		printAll(comment, ratings, commentsContainer);
	});


	$("#mediaContainer").append(well);
	$("#mediaContainer").append(commentsContainer);

}

function printAll(comment, ratings, commentsContainer) {
	var rating;
	$.each(ratings, function(index, tempRating) {
		if (tempRating.commentId == comment.commentId) {
			rating = tempRating;
		}
	});

	console.log("Comment id: " + comment.commentId);
	var commentListItem = $('<li></li>');
	var media = $('<div class="media" id="c' + comment.commentId + '"></div>');
	var mediaLeft = $('<div class="media-left"></div>');
	var mediaLike;
	if (userLogged()) {
		var used = "unused";
		if (rating != undefined && rating.value == 1) {
			used = "used";
		}
		mediaLike = $('<div class="comment-like comment-like-' + used + ' glyphicon glyphicon-menu-up" onclick="submitLike($(this),' + comment.commentId + ')"></div>');
	}
	var rating = parseInt(comment.likes) - parseInt(comment.dislikes);
	var commentClass = "neutral"
	if (rating > 0) {
		commentClass = "likes";
	} else if (rating < 0) {
		commentClass = "dislikes";
	}
	var padding;
	if(userLogged()){
		padding = " comment-rating-padding";
	}
	var mediaRating = $('<div class="comment-rating comment-' + commentClass + ' ' +  padding + '">' + rating + '</div>');
	var mediaDislike;
	if (userLogged()) {
		var used = "unused";
		if (rating != undefined && rating.value == -1) {
			used = "used";
		}
		mediaDislike = $('<div class="comment-dislike comment-dislike-' + used + ' glyphicon glyphicon-menu-down" onclick="submitLike($(this),' + comment.commentId + ')"></div>');
	}
	var mediaBody = $('<div class="media-body"></div>');



	//	var well = $('<div class="comment well well-sm"></div>');
	var commentAuthor = $('<span class="comment-author">' + comment.author.username + '</span>' +
		'<span class="comment-date">' + comment.commentDate + '</span>');
	var commentText = $('<p class="comment-text">' + comment.text + '</p>');
	console.log("Comment text: " + comment.text);
	var reply;

	if (userLogged()) {
		reply = $('<a role="button" class="comment-replay" onclick="buildCommentReplyBox($(this))">Reply</a>');
	//		console.log("Currently logged as: " + sessionStorage.getItem("user").role + " with username: " + sessionStorage.getItem("user").username);
	}


	if (typeof comment.childComments != 'undefined') {
		var commentsContainerChild = $('<ul class="commentsContainer"></ul>');
		$.each(comment.childComments, function(index, commentChild) {
			printAll(commentChild, ratings, commentsContainerChild);
		});
		commentListItem.append(commentsContainerChild);
	}

	mediaLeft.append(mediaLike);
	mediaLeft.append(mediaRating);
	mediaLeft.append(mediaDislike);
	mediaBody.append(commentAuthor);
	mediaBody.append(commentText);
	mediaBody.append(reply);
	media.append(mediaLeft);
	media.append(mediaBody);
	commentListItem.prepend(media);

	//TODO: Srediti malo redosled komentara
	//	commentListItem.prepend(commentText);
	//	commentListItem.prepend(commentAuthor);
	//	well.append(commentListItem);
	commentsContainer.append(commentListItem);
}

function buildCommentReplyBox(reply) {
	console.log("reply hide " + reply.name);
	reply.hide();

	var container = $('<div class="row no-gutter col-xs-12 col-sm-8 col-md-6 col-lg-4" ></div>');
	var textArea = $('<textarea name="text" class="comment-reply-area form-control" rows="2" placeholder="Write your comment here" ></textarea>');
	var submitButton = $('<button class="submitComment pull-right" onclick="submitComment($(this).parent().parent().parent())">Comment</button>');
	container.append(textArea);
	container.append(submitButton);
	console.log("reply hide ");
	reply.parent().append(container);
	console.log("reply hide ");
}

/*function submitComment(button){
	console.log("Button " + button);
	console.log("Button parent (textarea) " + button.parent());
	console.log("TextArea parent (media body) " + button.parent().parent().find(".comment-author").text());
//	console.log();
	
}*/