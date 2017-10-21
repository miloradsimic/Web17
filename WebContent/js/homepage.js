var DEFAULT_IMAGE = "resources/cvet.jpg"

function showLoginButons() {
	//	console.log("MENU PROFILE is visible: " + $("#menuContainer").is(':visible'));
	console.log("Show login buttons");
	$("#menuContainer").load("menu.html", function() {
		$(this).find("#menu_profile").hide();
		
		setMainMenuTabSelected();

	});
	$("#loginButtons").show();
	$("#logoutButton").hide();
}
function showLogoutButons() {
	var data = JSON.parse(sessionStorage.getItem("user"));
	console.log("Show logout buttons");
	console.log("userID=" + data.userId + " username=" + data.username);
	console.log("object: " + JSON.stringify(data));


	//	console.log("MENU PROFILE is visible: " + $("#menu_profile").is(':visible'));
	$("#menuContainer").load("menu.html", function() {
		$(this).find("#menu_profile").show();
		setMainMenuTabSelected();
	});
	$("#loginButtons").hide();
	$("#welcomeUser").html('Welcome <a role="button" onclick="goToProfile(' + data.userId + ')">' + data.username + '</a>. Click here for ' +
		'<a id="logout" role="button" onclick=logout()>log out</a>');
	$("#logoutButton").show();

	console.log("Logout button: " + $("#logoutButton"));
	console.log("Logout button html: " + $("#logoutButton").html());
	console.log("Logout button text: " + $("#logoutButton").text());


}
function setMainMenuTabSelected() {
	var page_name = window.location.pathname.split("/")[2];
	var page_name = page_name.substr(0, page_name.indexOf(".")).toLowerCase();
	console.log("Page name: " + page_name);

	var menu_item_id = "";
	switch (page_name) {
	case "homepage":
	case "subforum":
	case "topic": {
		menu_item_id = "menu_homepage";
		break;
	}
	case "profile": {
		menu_item_id = "menu_profile";
		break;
	}
	}
	console.log("Menu item ID: " + menu_item_id);
	$('.nav .navbar-nav li').removeClass('active');
	$("#" + menu_item_id).parent().addClass('active');
}
function setProfileMenuTabSelected(tab_name) {
}
function showLoginModal() {
	$("#signupModal").modal("hide");
	$("#loginModal").modal();
	$("#loginModal").find("#username").val("moderator");
	$("#loginModal").find("#password").val("moderator");
	$("body").addClass("notScroll");
	$("#loginSubmit").on("click", login);
}
function showSignupModal() {
	$("#loginModal").modal("hide");
	$("#signupModal").modal();
	$("body").addClass("notScroll");
	$("#signUpSubmit").on("click", signup);
}

function renderSearchResponse(data) {
	console.log(data);
	
	var subforums = data.subforums;
	var topics = data.topics;
	var users = data.users;
	
	var subforums = subforums == null ? [] : (subforums instanceof Array ? subforums : [ subforums ]);
	var topics = topics == null ? [] : (topics instanceof Array ? topics : [ topics ]);
	var users = users == null ? [] : (users instanceof Array ? users : [ users ]);

	console.log(JSON.stringify(subforums));
	$('#mediaContainer').html("");
	$('.subforums-header').html("");
	$('.topics-header').html("");
	
	if(subforums.length != 0) {
		$('#mediaContainer').append("<p class='subforums-header'>Subforums</p>");
	}
	$.each(subforums, function(index, subforum) {

		console.log("Subforum: " + subforum.name);

		var media = $('<div id="s' + subforum.subforumId + '" class="subforum media"></div>');
		var mediaFirstChild = $('<div class="media-left media-top"></div>');
		var mediaThumbnail = $('<img class="media-object" src="' + subforum.icon + '">');

		mediaFirstChild.append(mediaThumbnail);
		media.append(mediaFirstChild);

		var mediaBody = $('<div class="media-body"></div');
		var heading = $('<div class="media-heading"><a role="button" onclick="goToSubforum(\'' + subforum.subforumId + '\')">' + subforum.name + '</div>');
		var description = $('<p>' + subforum.description + '</p>')

		var edit;
		var deleteButton;
		var paragraph;
		if (userLogged() && (userAdmin() || userMainModerator(subforum.mainModerator.userId))) {
			
			paragraph = $('<p></p>');
			edit = $('<a role="button" class="subforum-edit" onclick="loadEditSubforumData(\'' + subforum.subforumId + '\');">Edit</a>');
			deleteButton = $('<a role="button" class="subforum-delete" onclick="deleteSubforum(\'' + subforum.subforumId + '\');">Delete</a>');
			paragraph.append(edit);
			paragraph.append(deleteButton);
		}

		mediaBody.append(heading);
		mediaBody.append(description);
		mediaBody.append(paragraph);
		media.append(mediaBody);

		$('#mediaContainer').append(media);
	});
	
	if(topics.length != 0) {
		$('#mediaContainer').append("<p class='topics-header'>Topics</p>");
	}
	$.each(topics, function(index, topic) {

		console.log("Topic: " + topic.title);

		var media = $('<div class="topic media" id="t' + topic.topicId + '"></div>');

		var mediaFirstChild = $('<div class="media-left media-top"></div>');
		var slika = DEFAULT_IMAGE;

		if (topic.author.avatar !== "") {
			slika = topic.author.avatar;
		}
		var mediaThumbnail = $('<img class="media-object" src="' + slika + '"/>');

		mediaFirstChild.append(mediaThumbnail);
		media.append(mediaFirstChild);

		var mediaBody = $('<div class="media-body"></div');
		var heading = $('<div class="media-heading"><a role="button" onclick="goToTopic(\'' + topic.topicId + '\')">' + topic.title + '</a></div>');


		var likes = $('<span class="media-left media-top topic-ratings-value">Likes: ' + topic.likes + '</span>');
		var dislikes = $('<span class="media-left media-bottom topic-ratings-value">Dislikes: ' + topic.dislikes + '</span>');
		var date = $('<span class="media-meta pull-right">' + topic.creationDate + '</span>');


		var edit;
		var deleteButton;
		var paragraph;
		if (userLogged() && (userAdmin() || userMainModerator(topic.mainModerator) || userLogged(topic.author.userId))) {
			paragraph = $('<p></p>');
			edit = $('<a role="button" class="topic-edit" onclick="loadEditTopicData(\'' + topic.topicId + '\');">Edit</a>');
			deleteButton = $('<a role="button" class="topic-delete" onclick="deleteTopic(\'' + topic.topicId + '\');">Delete</a>');
			paragraph.append(edit);
			paragraph.append(deleteButton);
		}

		mediaBody.append(heading);
		mediaBody.append(date);
		mediaBody.append(likes);
		mediaBody.append(dislikes);
		mediaBody.append(paragraph);
		media.append(mediaBody);

		$("#mediaContainer").append(media);
	});
	if(users.length != 0) {
		$('#mediaContainer').append("<p>Users</p>");
	}
	$.each(users, function(index, user) {
		console.log("username: " + user.username);
		
//		var item = $('<li class="list-group-item"></li>');

		var media = $('<div class="media" id="' + user.userId + '"></div>');
		var mediaLeft = $('<div class="media-left media-top"></div>');
		var slika = DEFAULT_IMAGE;
		if (user.avatar !== "") {
			slika = user.avatar;
		}
		var mediaThumbnail = $('<img class="media-object" src="' + slika + '"/>');
		mediaLeft.append(mediaThumbnail);
		media.append(mediaLeft);

		var mediaBody = $('<div class="media-body"></div>');
		var heading = $('<div class="media-heading"><a role="button" onclick="goToProfile(\'' + user.userId + '\')" >' + user.firstName + ' ' + user.lastName + '</a></div>');
		var details = $('<span class="media-left media-top"> ' + user.username + '</span>');
		mediaBody.append(heading);
		mediaBody.append(details);
		media.append(mediaBody);

		var role = $('<div class="action-buttons media-right center-verticaly"><span id="user_role">' + user.role + '</span></div>');
		var roleVote;
		//TODO: U href treba ubaciti ID za poruke
		if (userLogged() && userAdmin()) {
			roleVote = $('<div class="action-buttons media-right center-verticaly"></div>');
			var roleUp = $('<div class="action-buttons media-right center-horizontally" onclick="submitRoleChange(\'' + user.userId + '\', \'-1\');"><a role="button"><span class="glyphicon glyphicon-triangle-bottom"></span></a></div>');
			var roleDown = $('<div class="action-buttons media-right center-horizontally" onclick="submitRoleChange(\'' + user.userId + '\', \'1\');"><a role="button"><span class="glyphicon glyphicon-triangle-top"></span></a></div>');
			roleVote.append(roleUp);
			roleVote.append(roleDown);
		}
		var envelope = $('<div class="action-buttons media-right center-verticaly"><a href="http://www.google.com"><span class="glyphicon glyphicon-envelope"></span></a></div>');
		//var trash = $('<div class="action-buttons media-right center-verticaly"><a href="http://www.google.com" class="trash"><span class="glyphicon glyphicon-trash"></span></a></div>');
		media.append(role);

		media.append(roleVote);
		media.append(envelope);
		//media.append(trash);

//		item.append(media);
		$('#mediaContainer').append(media);
	});
}
function renderSubforumsList(data) {
	console.log('renderSubforumsList form file into page.');
	console.log(data);
	//	setActiveMenuItem("menu_homepage");

	// new subforum
	if (userLogged() && (userAdmin() || userModerator())) {
		$('.subforums-header').prepend('<a role="button" class="subforums-new-subforum" onclick="renderNewSubforum()">New</a>');
		$('.subforums-header').prepend('<div id="subforum_new_subforum_container"></div>');
	}

	// JAX-RS serializes an empty list as null, and a 'collection of one' as an object (not an 'array of one')
	var list = data == null ? [] : (data instanceof Array ? data : [ data ]);

	$.each(list, function(index, subforum) {

		console.log(subforum);

		var media = $('<div id="s' + subforum.subforumId + '" class="subforum media"></div>');
		var mediaFirstChild = $('<div class="media-left media-top"></div>');
		var mediaThumbnail = $('<img class="media-object" src="' + subforum.icon + '">');

		mediaFirstChild.append(mediaThumbnail);
		media.append(mediaFirstChild);

		var mediaBody = $('<div class="media-body"></div');
		var heading = $('<div class="media-heading"><a role="button" onclick="goToSubforum(\'' + subforum.subforumId + '\')">' + subforum.name + '</div>');
		var description = $('<p>' + subforum.description + '</p>')

		var edit;
		var deleteButton;
		var paragraph;
		if (userLogged() && (userAdmin() || userMainModerator(subforum.mainModerator.userId))) {
			
			paragraph = $('<p></p>');
			edit = $('<a role="button" class="subforum-edit" onclick="loadEditSubforumData(\'' + subforum.subforumId + '\');">Edit</a>');
			deleteButton = $('<a role="button" class="subforum-delete" onclick="deleteSubforum(\'' + subforum.subforumId + '\');">Delete</a>');
			paragraph.append(edit);
			paragraph.append(deleteButton);
		}

		mediaBody.append(heading);
		mediaBody.append(description);
		mediaBody.append(paragraph);
		media.append(mediaBody);

		$('#mediaContainer').append(media);
	});
}
function renderTopicsList(data) {
	console.log('renderTopicsList form file into page.');
	var topics = data.topics;
	var main_moderator = data.main_moderator;
	console.log(topics);
	//	setActiveMenuItem("menu_homepage");

	// new topic
	if (userLogged()) { //TODO: admins and administators
		$('.topics-header').prepend('<a role="button" class="topics-new-topic" onclick="renderNewTopic()">New</a>');
		$('.topics-header').prepend('<div id="topic_new_topic_container"></div>');
	}
	// JAX-RS serializes an empty list as null, and a 'collection of one' as an object (not an 'array of one')
	var list = topics == null ? [] : (topics instanceof Array ? topics : [ topics ]);

	$.each(list, function(index, topic) {

		console.log("TOPIC: " + topic.title);
		console.log("TOPIC user: " + topic.author.username);


		var media = $('<div class="topic media" id="t' + topic.topicId + '"></div>');

		var mediaFirstChild = $('<div class="media-left media-top"></div>');
		var slika = DEFAULT_IMAGE;

		if (topic.author.avatar !== "") {
			slika = topic.author.avatar;
		}
		var mediaThumbnail = $('<img class="media-object" src="' + slika + '"/>');

		mediaFirstChild.append(mediaThumbnail);
		media.append(mediaFirstChild);

		var mediaBody = $('<div class="media-body"></div');
		var heading = $('<div class="media-heading"><a role="button" onclick="goToTopic(\'' + topic.topicId + '\')">' + topic.title + '</a></div>');


		var likes = $('<span class="media-left media-top topic-ratings-value">Likes: ' + topic.likes + '</span>');
		var dislikes = $('<span class="media-left media-bottom topic-ratings-value">Dislikes: ' + topic.dislikes + '</span>');
		var date = $('<span class="media-meta pull-right">' + topic.creationDate + '</span>');


		var edit;
		var deleteButton;
		var paragraph;
		if (userLogged() && (userAdmin() || userMainModerator(main_moderator) || userLogged(topic.author.userId))) {
			paragraph = $('<p></p>');
			edit = $('<a role="button" class="topic-edit" onclick="loadEditTopicData(\'' + topic.topicId + '\');">Edit</a>');
			deleteButton = $('<a role="button" class="topic-delete" onclick="deleteTopic(\'' + topic.topicId + '\');">Delete</a>');
			paragraph.append(edit);
			paragraph.append(deleteButton);
		}

		mediaBody.append(heading);
		mediaBody.append(date);
		mediaBody.append(likes);
		mediaBody.append(dislikes);
		mediaBody.append(paragraph);
		media.append(mediaBody);

		$("#mediaContainer").append(media);
	});
}
function renderNewTopic() {
	if (!userLogged()) {
		console.log("User is not logged!");
	} else {
		$("#topic_new_topic_container").load("topic_new.html", function() {
			$(this).find("#text").hide();
			$(this).find("#link").hide();
			$(this).find("#image").hide();
			$(this).find("#new_topic_form").change(function() {
				$(this).find("#text").hide();
				$(this).find("#link").hide();
				$(this).find("#image").hide();
				if ($(this).find("#text_radio").is(":checked") == true) {
					$(this).find("#text").show();
				}
				if ($(this).find("#link_radio").is(":checked") == true) {
					$(this).find("#link").show();
				}
				if ($(this).find("#image_radio").is(":checked") == true) {
					$(this).find("#image").show();
					if ($(this).find("#topic_img_tag").attr('src') === "") {
						$(this).find("#topic_image").hide();
					} else {
						$(this).find("#topic_image").show();
					}
				}
			});

			// fixes bug of validator with placeholders, he parses placeholder as inputed text
			// removes placeholder before submit, and returns after is submited
			var placeholders = {};
			$(this).find("#new_topic_form").validate({
				submitHandler : function() {
					$(this).find("#new_topic_form").find(':input[placeholder]').each(function() {
						var placeholder = $(this).attr('placeholder');
						placeholders[placeholder] = this;
						$(this).removeAttr('placeholder');
					});
					$(this).find("#new_topic_form").submit();
				},
				invalidHandler : function() {
					$.each(placeholders, function(placeholder, element) {
						$(element).attr('placeholder', placeholder);
					});

				},
				// important rules
				rules : {
					topic_title : {
						minlength : 3,
						required : true
					},
					type : {
						required : true
					},
					link : {
						required : $("#link_radio :checked")
					},
					text : {
						required : $("#text_radio :checked")
					},
					image : {
						required : $("#image_radio :checked")
					}
				},
				messages : {
					topic_title : {
						minlength : "Minimal length is 3",
						required : "Topic title is required"
					}
				},
				// puts error under radio buttons
				errorPlacement : function(error, element) {
					if (element.attr("name") == "type") {
						error.insertAfter("#image_radio_label");
					} else {
						error.insertAfter(element);
					}
				}
			});

		});
	}
}
//#################################
function renderNewSubforum() {
	if (!userLogged()) {
		console.log("User is not logged!");
	} else {
		$("#subforum_new_subforum_container").load("subforum_new.html", function() {
			if ($(this).find("#subforum_img_tag").attr('src') === "") {
				$(this).find("#subforum_image").hide();
			} else {
				$(this).find("#subforum_image").show();
			}

			// fixes bug of validator with placeholders, he parses placeholder as inputed text
			// removes placeholder before submit, and returns after is submited
			var placeholders = {};
			$(this).find("#new_subforum_form").validate({
				submitHandler : function() {
					$(this).find("#new_subforum_form").find(':input[placeholder]').each(function() {
						var placeholder = $(this).attr('placeholder');
						placeholders[placeholder] = this;
						$(this).removeAttr('placeholder');
					});
					$(this).find("#new_subforum_form").submit();
				},
				invalidHandler : function() {
					$.each(placeholders, function(placeholder, element) {
						$(element).attr('placeholder', placeholder);
					});

				},
				// important rules
				rules : {
					subforum_title : {
						minlength : 3,
						required : true
					},
					description : {
						required : true
					},
					image : {
						required : true
					}
				},
				messages : {
					subforum_title : {
						minlength : "Minimal length is 3",
						required : "Subforum title is required"
					}
				}
			});

		});
	}
}
function renderTopicAndComments(data) {
	console.log('renderTopicAndComments form file into page.');
	console.log(data);

	var topic = data.topic;
	var comment_ratings = data.comment_ratings;
	var topic_rating = data.topic_rating;
	var moderator = data.main_moderator;

	//	console.log("Whole object data stringify! :" + JSON.stringify(data));

	console.log("TOPIC: " + topic.title);

	var mediaRoot = $('<div class="media"></div>');
	var mediaLeft = $('<div id="t' + topic.topicId + '" class="topic-root media-left"></div>');
	var mediaLike;
	if (userLogged()) {
		var used = "unused";
		if (topic_rating != undefined && topic_rating.value == 1) {
			used = "used";
		}
		console.log("renderTopicAndComments(): topic_rating=" + JSON.stringify(topic_rating));
		console.log("renderTopicAndComments(): like used=" + used);
		mediaLike = $('<div class="comment-like comment-like-' + used + ' glyphicon glyphicon-menu-up" onclick="submitTopicRating($(this),' + topic.topicId + ')"></div>');
	}
	var mediaDislike;
	if (userLogged()) {
		var used = "unused";
		if (topic_rating != undefined && topic_rating.value == -1) {
			used = "used";
		}
		console.log("renderTopicAndComments(): dislike used=" + used);
		mediaDislike = $('<div class="comment-dislike comment-dislike-' + used + ' glyphicon glyphicon-menu-down" onclick="submitTopicRating($(this),' + topic.topicId + ')"></div>');
	}
	var mediaBody = $('<div class="media-body"></div>');

	mediaLeft.append(mediaLike);
	mediaLeft.append(mediaDislike);

	var well = $('<div class="well well-sm"></div>');
	var media = $('<div class="media"></div>');
	var heading = $('<h4 class="media-heading media-top">' + topic.title + '</div>');
	var commentsContainer1;
	if (userLogged()) {
		// Comment topic directly
		var container = $('<div class="row no-gutter col-xs-12 col-sm-8 col-md-6 col-lg-4" ></div>');
		var textArea = $('<textarea name="text" class="comment-reply-area form-control" rows="2" placeholder="Write your comment here" ></textarea>');
		var submitButton = $('<button class="submitComment pull-right" onclick="submitComment($(this).parent().parent())">Comment</button>');
		var commentListItem = $('<li></li>');
		var media2 = $('<div class="media" id="c0"></div>');
		container.append(textArea);
		container.append(submitButton);
		media2.append(container);
		commentListItem.append(media2);

		commentsContainer1 = $('<ul class="commentsContainer" id="commentsContainerRoot"></ul>');
		commentsContainer1.append(commentListItem);
	}

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
	mediaBody.append(well);
	mediaRoot.append(mediaLeft);
	mediaRoot.append(mediaBody);

	var commentsContainer = $('<ul class="commentsContainer" id="commentsContainer"></ul>');
	commentsContainer.append(commentsContainer1);

	$.each(topic.comments, function(index, comment) {
		printAll(comment, moderator, comment_ratings, commentsContainer);
	});


	$("#mediaContainer").append(mediaRoot);
	$("#mediaContainer").append(commentsContainer);

}

function printAll(comment, moderator, ratings, commentsContainer) {
	var rating;

	$.each(ratings, function(index, tempRating) {
		if (tempRating.commentId == comment.commentId) {
			rating = tempRating;
			console.log("Rating for comment id=" + comment.commentId + " is-->" + JSON.stringify(rating));
		}
	});

	//	console.log("Comment id: " + comment.commentId);
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
	var ratingTotal = parseInt(comment.likes) - parseInt(comment.dislikes);
	var commentClass = "neutral"
	if (ratingTotal > 0) {
		commentClass = "likes";
	} else if (ratingTotal < 0) {
		commentClass = "dislikes";
	}
	var padding;
	if (userLogged()) {
		padding = " comment-rating-padding";
	}
	var mediaRating = $('<div class="comment-rating comment-' + commentClass + ' ' + padding + '">' + ratingTotal + '</div>');
	var mediaDislike;
	if (userLogged()) {
		var used = "unused";
		if (rating != undefined && rating.value == -1) {
			used = "used";
		}
		//		console.log("used rating: " + rating);
		mediaDislike = $('<div class="comment-dislike comment-dislike-' + used + ' glyphicon glyphicon-menu-down" onclick="submitLike($(this),' + comment.commentId + ')"></div>');
	}
	var mediaBody = $('<div class="media-body"></div>');



	//	var well = $('<div class="comment well well-sm"></div>');
	var edited = comment.edited == true ? ' edited' : '';
	var commentAuthor = $('<span class="comment-author">' + comment.author.username + '</span>' +
		'<span class="comment-date">' + comment.commentDate + '</span>' +
		'<span class="comment-edited">' + edited + '</span>');
	var commentText = $('<p class="comment-text">' + comment.text + '</p>');
	//	console.log("Comment text: " + comment.text);
	var reply;
	var edit;
	var deleteButton;
	var paragraph;

	if (userLogged()) {
		paragraph = $('<p></p>');
		reply = $('<a role="button" class="comment-reply" onclick="buildCommentReplyBox($(this))">Reply</a>');
		edit = $('<a role="button" class="comment-edit" onclick="buildCommentEditBox($(this))">Edit</a>');
		deleteButton = $('<a role="button" class="comment-delete" onclick="deleteComment($(this).parent().parent().parent())">Delete</a>');
		//		console.log("Currently logged as: " + sessionStorage.getItem("user").role + " with username: " + sessionStorage.getItem("user").username);
		paragraph.append(reply);
		//		paragraph.append(edit);
		if (userAdmin()) {
			console.log('User is admin');
			//			paragraph.append(edit);
			paragraph.append(deleteButton);
		}
		if (userMainModerator(moderator)) {
			console.log('User is moderator');
			paragraph.append(edit);
			paragraph.append(deleteButton);
		}
		if (userLogged(comment.author.username)) {
			console.log('User is author');
			paragraph.append(edit);
			paragraph.append(deleteButton);
		}
	}

	if (typeof comment.childComments != 'undefined') {
		var commentsContainerChild = $('<ul class="commentsContainer"></ul>');
		$.each(comment.childComments, function(index, commentChild) {
			printAll(commentChild, moderator, ratings, commentsContainerChild);
		});
		commentListItem.append(commentsContainerChild);
	}

	if (comment.deleted == true) {
		var media = $('<div class="media comment-deleted" id="c' + comment.commentId + '">Comment is deleted</div>');
	} else {
		mediaLeft.append(mediaLike);
		mediaLeft.append(mediaRating);
		mediaLeft.append(mediaDislike);
		mediaBody.append(commentAuthor);
		mediaBody.append(commentText);
		mediaBody.append(paragraph);
		media.append(mediaLeft);
		media.append(mediaBody);
	}
	commentListItem.prepend(media);

	//TODO: Srediti malo redosled komentara
	//	commentListItem.prepend(commentText);
	//	commentListItem.prepend(commentAuthor);
	//	well.append(commentListItem);
	commentsContainer.append(commentListItem);
}
function renderEditedComment(data) {
	var comment = data.comment;
	if (comment.edited == true) {
		$('#c' + comment.commentId).find('.comment-date').append(' edited');
	}
}
function renderDeletedComment(data, comment) {
	if (data == true) {
		comment.html('<div class="media" id="c' + comment.commentId + '">Comment is deleted</div>');
	}
}
function renderManageUsersPage(data) {
	//data: list of users with attributes {id, role, username, firstname, lastname, email, telephone}

	console.log("users: " + JSON.stringify(data));

	$("#menuContainer").load("menu.html", function() {
		$(this).find("#menu_profile").show();
		setMainMenuTabSelected();
	});

	$("#profileMenuContainer").load("profile_menu.html", function() {
		$('profile-list-inline li').removeClass('active');
		$("#users").parent().addClass('active');
	//		console.log(this.find("#first_name"));
	});

	$("#mediaContainer").load("profile_manage_users.html", function() {
		var list = data == null ? [] : (data instanceof Array ? data : [ data ]);
		var container = $(this).find("#profile_manage_users_list");

		$.each(list, function(index, user) {

			console.log("username: " + user.username);

			console.log("LIST username: " + user.username);

			var item = $('<li class="list-group-item"></li>');

			var media = $('<div class="media" id="' + user.userId + '"></div>');
			var mediaLeft = $('<div class="media-left media-top"></div>');
			var slika = DEFAULT_IMAGE;
			if (user.avatar !== "") {
				slika = user.avatar;
			}
			var mediaThumbnail = $('<img class="media-object" src="' + slika + '"/>');
			mediaLeft.append(mediaThumbnail);
			media.append(mediaLeft);

			var mediaBody = $('<div class="media-body"></div>');
			var heading = $('<div class="media-heading"><a role="button" onclick="goToProfile(\'' + user.userId + '\')" >' + user.firstName + ' ' + user.lastName + '</a></div>');
			var details = $('<span class="media-left media-top"> ' + user.username + '</span>');
			mediaBody.append(heading);
			mediaBody.append(details);
			media.append(mediaBody);

			var role = $('<div class="action-buttons media-right center-verticaly"><span id="user_role">' + user.role + '</span></div>');
			var roleVote;
			//TODO: U href treba ubaciti ID za poruke
			if (userLogged() && userAdmin()) {
				roleVote = $('<div class="action-buttons media-right center-verticaly"></div>');
				var roleUp = $('<div class="action-buttons media-right center-horizontally" onclick="submitRoleChange(\'' + user.userId + '\', \'-1\');"><a role="button"><span class="glyphicon glyphicon-triangle-bottom"></span></a></div>');
				var roleDown = $('<div class="action-buttons media-right center-horizontally" onclick="submitRoleChange(\'' + user.userId + '\', \'1\');"><a role="button"><span class="glyphicon glyphicon-triangle-top"></span></a></div>');
				roleVote.append(roleUp);
				roleVote.append(roleDown);
			}
			var envelope = $('<div class="action-buttons media-right center-verticaly"><a href="http://www.google.com"><span class="glyphicon glyphicon-envelope"></span></a></div>');
			//var trash = $('<div class="action-buttons media-right center-verticaly"><a href="http://www.google.com" class="trash"><span class="glyphicon glyphicon-trash"></span></a></div>');
			media.append(role);

			media.append(roleVote);
			media.append(envelope);
			//media.append(trash);

			item.append(media);
			container.append(item);
		});
	});
	
}
function renderProfilePage(user) {
	if (user == undefined) {
		console.log("User is undefined!");
	}
	if (!userLogged(user.username)) { // Public profile data
		console.log("User not logged! Username:" + user.username);

		if (userLogged()) { // Ako je bilo koji user logovan prikazi i profile button
			console.log("One of users is logged.");
			$("#menuContainer").load("menu.html", function() {
				$(this).find("#menu_profile").show();
				setMainMenuTabSelected();
			});
		}

		$("#mediaContainer").load("profile_not_logged_user.html", function() {
			$(this).find("#name").text(user.firstName + ' ' + user.lastName);
			$(this).find("#username").text(user.username);
			$(this).find("#role").text(user.role);
			$(this).find("#avatar").attr("src", user.avatar);
		});
	} else { // Logged user page
		console.log(user.firstName);
		console.log("Logged user" + JSON.parse(sessionStorage.getItem('user')).username);
		console.log("Provided user" + user.username);


		// Logged user profile
		if (JSON.parse(sessionStorage.getItem('user')).username === user.username) {

			console.log("TRUE!");
			$("#menuContainer").load("menu.html", function() {
				$(this).find("#menu_profile").show();
				setMainMenuTabSelected();
			});

			$("#profileMenuContainer").load("profile_menu.html", function() {
				$('profile-list-inline li').removeClass('active');
				$("#profile").parent().addClass('active');
			//				console.log(this.find("#first_name"));
			});

			$("#mediaContainer").load("profile_logged_user.html", function() {
				$(this).find("#first_name").val(user.firstName);
				$(this).find("#last_name").val(user.lastName);
				$(this).find("#email").val(user.email);
				$(this).find("#avatar").attr("src", user.avatar);
				//				$(this).find("#upload_avatar").on("")

				$(this).find("#edit_profile_form").validate({
					rules : {
						new_password : {
							minlength : 3
						},
						confirm_password : {
							equalTo : "#new_password"
						},
						email : {
							email : true
						}
					},
					messages : {
						new_password : {
							minlength : "Minimal length is 3"
						},
						confirm_password : {
							equalTo : "Passwords don't match"
						},
						email : {
							email : "Your email address must be in the format of name@domain.com"
						}
					}
				});
			});
		}
	}
}

function buildCommentReplyBox(reply) {
	//	console.log("reply hide " + reply.name);
	//reply.hide();

	if (reply.parent().parent().find("textarea").parent().html() == undefined) {
		//		console.dir(reply.parent().parent().find("textarea").parent().html());
		var container = $('<div class="comment-reply-container row no-gutter col-xs-12 col-sm-8 col-md-6 col-lg-4" ></div>');
		var textArea = $('<textarea name="text" class="comment-reply-area form-control" rows="2" placeholder="Write your comment here" ></textarea>');
		var submitButton = $('<button class="submitComment pull-right" onclick="submitComment($(this).parent().parent().parent())">Comment</button>');
		container.append(textArea);
		container.append(submitButton);
		//console.log("reply hide ");
		reply.parent().parent().append(container);
		//console.log("reply hide ");

	} else {
		reply.parent().parent().find("textarea").parent().remove();
	}


}
function buildCommentEditBox(editButton) {
	if (editButton.parent().parent().find("textarea").parent().html() == undefined) {
		//		console.dir(reply.parent().parent().find("textarea").parent().html());
		var container = $('<div class="comment-reply-container row no-gutter col-xs-12 col-sm-8 col-md-6 col-lg-4" ></div>');
		var textArea = $('<textarea name="text" class="comment-reply-area form-control" rows="2" placeholder="Write your comment here" ></textarea>');
		var submitButton = $('<button class="submitComment pull-right" onclick="submitEditComment($(this).parent().parent().parent())">Save</button>');
		textArea.val(editButton.parent().parent().children().eq(3).text());

		container.append(textArea);
		container.append(submitButton);
		//console.log("reply hide ");
		editButton.parent().parent().append(container);
		//console.log("reply hide ");

	} else {
		editButton.parent().parent().find("textarea").parent().remove();
	}
}
function renderEditTopicForm(data) {
	//	console.log('Topic type2: ' + JSON.stringify(topic).type);
	//	console.log('Topic type3: ' + JSON.parse(topic));
	var topic = data.topic;
	if (!userLogged()) {
		console.log("User is not logged!");
	} else {
		$("#topic_new_topic_container").load("topic_new.html", function() {

			$(this).find("#text").hide();
			$(this).find("#link").hide();
			$(this).find("#image").hide();

			// load data

			$(this).find(".id").attr('id', topic.topicId);
			console.log('Topic title: ' + topic.title);
			$(this).find("#topic_title").val(topic.title);
			console.log('Topic type: ' + topic.type);
			switch (topic.type) {
			case "TEXT": {
				$(this).find("#text_radio").attr('checked', true);
				$(this).find("#text textarea").val(topic.content);
				$(this).find("#text").show();
				break;
			}
			case "LINK": {
				$(this).find("#link_radio").attr('checked', true);
				$(this).find("#link input").val(topic.content);
				$(this).find("#link").show();
				break;
			}
			case "IMAGE": {
				$(this).find("#image_radio").attr('checked', true);
				$(this).find('#upload_image').attr('value', topic.content);
				$(this).find('#topic_img_tag').attr('src', topic.content);
				$(this).find("#image").show();
				$(this).find("#topic_image").show();
				break;
			}
			}

			$(this).find("#new_topic_form").change(function() {
				$(this).find("#text").hide();
				$(this).find("#link").hide();
				$(this).find("#image").hide();
				if ($(this).find("#text_radio").is(":checked") == true) {
					$(this).find("#text").show();
				}
				if ($(this).find("#link_radio").is(":checked") == true) {
					$(this).find("#link").show();
				}
				if ($(this).find("#image_radio").is(":checked") == true) {
					$(this).find("#image").show();
					if ($(this).find("#topic_img_tag").attr('src') === "") {
						$(this).find("#topic_image").hide();
					} else {
						$(this).find("#topic_image").show();
					}
				}
			});

			// fixes bug of validator with placeholders, he parses placeholder as inputed text
			// removes placeholder before submit, and returns after is submited
			var placeholders = {};
			$(this).find("#new_topic_form").validate({
				submitHandler : function() {
					$(this).find("#new_topic_form").find(':input[placeholder]').each(function() {
						var placeholder = $(this).attr('placeholder');
						placeholders[placeholder] = this;
						$(this).removeAttr('placeholder');
					});
					$(this).find("#new_topic_form").submit();
				},
				invalidHandler : function() {
					$.each(placeholders, function(placeholder, element) {
						$(element).attr('placeholder', placeholder);
					});

				},
				// important rules
				rules : {
					topic_title : {
						minlength : 3,
						required : true
					},
					type : {
						required : true
					},
					link : {
						required : $("#link_radio :checked")
					},
					text : {
						required : $("#text_radio :checked")
					},
					image : {
						required : $("#image_radio :checked")
					}
				},
				messages : {
					topic_title : {
						minlength : "Minimal length is 3",
						required : "Topic title is required"
					}
				},
				// puts error under radio buttons
				errorPlacement : function(error, element) {
					if (element.attr("name") == "type") {
						error.insertAfter("#image_radio_label");
					} else {
						error.insertAfter(element);
					}
				}
			});

		});
	}
}
function renderEditSubforumForm(data) {
	var subforum = data.subforum;
	if (!userLogged()) {
		console.log("User is not logged!");
	} else {
		$("#subforum_new_subforum_container").load("subforum_new.html", function() {
			 console.log(arguments.length);

			// load data
			$(this).find(".id").attr('id', subforum.subforumId);
			$(this).find("#subforum_title").val(subforum.name);
			$(this).find("#description textarea").val(subforum.description);

			console.log("Image" + subforum.icon);
			
			$(this).find('#upload_image').attr('value', subforum.icon);
			$(this).find('#subforum_img_tag').attr('src', subforum.icon);

			// fixes bug of validator with placeholders, he parses placeholder as inputed text
			// removes placeholder before submit, and returns after is submited
			var placeholders = {};
			$(this).find("#new_subforum_form").validate({
				submitHandler : function() {
					$(this).find("#new_subforum_form").find(':input[placeholder]').each(function() {
						var placeholder = $(this).attr('placeholder');
						placeholders[placeholder] = this;
						$(this).removeAttr('placeholder');
					});
					$(this).find("#new_subforum_form").submit();
				},
				invalidHandler : function() {
					$.each(placeholders, function(placeholder, element) {
						$(element).attr('placeholder', placeholder);
					});

				},
				
				// important rules
				rules : {
					subforum_title : {
						minlength : 3,
						required : true
					},
					description : {
						required : true
					},
					image : {
						required : true
					}
				},
				messages : {
					subforum_title : {
						minlength : "Minimal length is 3",
						required : "Subforum title is required"
					}
				}
			});

		});
	}
}

function renderMessages() {
//	console.log("users: " + JSON.stringify(data));

	$("#menuContainer").load("menu.html", function() {
		$(this).find("#menu_profile").show();
		setMainMenuTabSelected();
	});

	$("#profileMenuContainer").load("profile_menu.html", function() {
		$('profile-list-inline li').removeClass('active');
		$("#messages").parent().addClass('active');
	//		console.log(this.find("#first_name"));
	});

	$("#mediaContainer").load("profile_messages.html", function() {
//		var list = data == null ? [] : (data instanceof Array ? data : [ data ]);
//		var container = $(this).find("#profile_manage_users_list");
//
//		$.each(list, function(index, user) {
//
//			console.log("username: " + user.username);
//
//			console.log("LIST username: " + user.username);
//
//			var item = $('<li class="list-group-item"></li>');
//
//			var media = $('<div class="media" id="' + user.userId + '"></div>');
//			var mediaLeft = $('<div class="media-left media-top"></div>');
//			var slika = DEFAULT_IMAGE;
//			if (user.avatar !== "") {
//				slika = user.avatar;
//			}
//			var mediaThumbnail = $('<img class="media-object" src="' + slika + '"/>');
//			mediaLeft.append(mediaThumbnail);
//			media.append(mediaLeft);
//
//			var mediaBody = $('<div class="media-body"></div>');
//			var heading = $('<div class="media-heading"><a role="button" onclick="goToProfile(\'' + user.userId + '\')" >' + user.firstName + ' ' + user.lastName + '</a></div>');
//			var details = $('<span class="media-left media-top"> ' + user.username + '</span>');
//			mediaBody.append(heading);
//			mediaBody.append(details);
//			media.append(mediaBody);
//
//			var role = $('<div class="action-buttons media-right center-verticaly"><span id="user_role">' + user.role + '</span></div>');
//			var roleVote;
//			//TODO: U href treba ubaciti ID za poruke
//			if (userLogged() && userAdmin()) {
//				roleVote = $('<div class="action-buttons media-right center-verticaly"></div>');
//				var roleUp = $('<div class="action-buttons media-right center-horizontally" onclick="submitRoleChange(\'' + user.userId + '\', \'-1\');"><a role="button"><span class="glyphicon glyphicon-triangle-bottom"></span></a></div>');
//				var roleDown = $('<div class="action-buttons media-right center-horizontally" onclick="submitRoleChange(\'' + user.userId + '\', \'1\');"><a role="button"><span class="glyphicon glyphicon-triangle-top"></span></a></div>');
//				roleVote.append(roleUp);
//				roleVote.append(roleDown);
//			}
//			var envelope = $('<div class="action-buttons media-right center-verticaly"><a href="http://www.google.com"><span class="glyphicon glyphicon-envelope"></span></a></div>');
//			//var trash = $('<div class="action-buttons media-right center-verticaly"><a href="http://www.google.com" class="trash"><span class="glyphicon glyphicon-trash"></span></a></div>');
//			media.append(role);
//
//			media.append(roleVote);
//			media.append(envelope);
//			//media.append(trash);
//
//			item.append(media);
//			container.append(item);
//		});
	});

}




















