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
function renderSubforumsList(data) {
	console.log('renderSubforumsList form file into page.');
	console.log(data);
	//	setActiveMenuItem("menu_homepage");

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
		var heading = $('<div class="media-heading">' + subforum.name + '</div>');
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
	//	setActiveMenuItem("menu_homepage");

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
		var heading = $('<div class="media-heading">' + topic.title + '</div>');


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

	//	setActiveMenuItem("menu_homepage");
	var topic = data.topic;
	var comment_ratings = data.comment_ratings;
	var topic_rating = data.topic_rating;

//	console.log("Whole object data! :" + data);

//	console.log("Whole object data stringify! :" + JSON.stringify(data));

	console.log("TOPIC: " + topic.title);
	//----------------------------------------

	var mediaRoot = $('<div class="media"></div>');
	var mediaLeft = $('<div id="t' + topic.topicId + '" class="media-left"></div>');
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
	//var rating = parseInt(comment.likes) - parseInt(comment.dislikes);
	//		var commentClass = "neutral"
	//		if (rating > 0) {
	//			commentClass = "likes";
	//		} else if (rating < 0) {
	//			commentClass = "dislikes";
	//		}
	//	var padding;
	//	if (userLogged()) {
	//		padding = " comment-rating-padding";
	//	}
	//var mediaRating = $('<div class="comment-rating comment-' + commentClass + ' ' + padding + '">' + rating + '</div>');

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
	//	mediaLeft.append(mediaRating);
	mediaLeft.append(mediaDislike);
	//	mediaBody.append(commentAuthor);
	//	mediaBody.append(commentText);
	//	mediaBody.append(reply);


	//-----------------------------------------
	var well = $('<div class="topic-root well well-sm"></div>');
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
		console.log("printAll : " + comment + "***********************" + comment_ratings + "***********************" + commentsContainer);

		printAll(comment, comment_ratings, commentsContainer);
	});


	$("#mediaContainer").append(mediaRoot);
	$("#mediaContainer").append(commentsContainer);

}

function printAll(comment, ratings, commentsContainer) {
	var rating;

	$.each(ratings, function(index, tempRating) {
		if (tempRating.commentId == comment.commentId) {
			rating = tempRating;
		console.log("Rating for comment id="+comment.commentId + " is-->" + JSON.stringify(rating));
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
		console.log("used rating: "+ rating);
		mediaDislike = $('<div class="comment-dislike comment-dislike-' + used + ' glyphicon glyphicon-menu-down" onclick="submitLike($(this),' + comment.commentId + ')"></div>');
	}
	var mediaBody = $('<div class="media-body"></div>');



	//	var well = $('<div class="comment well well-sm"></div>');
	var commentAuthor = $('<span class="comment-author">' + comment.author.username + '</span>' +
		'<span class="comment-date">' + comment.commentDate + '</span>');
	var commentText = $('<p class="comment-text">' + comment.text + '</p>');
//	console.log("Comment text: " + comment.text);
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
function renderManageUsersPage(data) {
	//data: list of users with attributes {id, role, username, firstname, lastname, email, telephone}

	console.log('renderUsersList start');
	console.log("users: " + JSON.stringify(data));

	console.log("Logged user" + JSON.parse(sessionStorage.getItem('user')).username);

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

			var item = $('<li class="list-group-item" onclick="goToProfile(\'' + user.userId + '\')"></li>');

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
			var heading = $('<div class="media-heading">' + user.firstName + ' ' + user.lastName + '</div>');
			var details = $('<span class="media-left media-top"> ' + user.username + '</span>');
			mediaBody.append(heading);
			mediaBody.append(details);
			media.append(mediaBody);

			//TODO: U href treba ubaciti ID za poruke
			var envelope = $('<div class="action-buttons media-right center-verticaly"><a href="http://www.google.com"><span class="glyphicon glyphicon-envelope"></span></a></div>');
			var trash = $('<div class="action-buttons media-right center-verticaly"><a href="http://www.google.com" class="trash"><span class="glyphicon glyphicon-trash"></span></a></div>');
			media.append(envelope);
			media.append(trash);

			item.append(media);
			container.append(item);
		});
	});
}
function renderProfilePage(user) {


	//	setActiveMenuItem("menu_profile");
	/*var menu = $('<div class="profile-menu menu" id="menu"></div>');
	var ul = $('<ul class="profile-list-inline list-inline"></ul>');
	var li1 = $('<li><a href="#">profile</a></li>');
	var li2 = $('<li><a href="#">messages</a></li>');
	var li3 = $('<li><a href="#">manage users</a></li>');
	var li4 = $('<li><a href="#">manage topics</a></li>');
	var li5 = $('<li><a href="#">manage subforums</a></li>');

	ul.append(li1);
	ul.append(li2);
	ul.append(li3);
	ul.append(li4);
	ul.append(li5);
	menu.append(ul);
	
	$("#mediaContainer").append(menu);
	
	
	<div class="col-md-2 hidden-xs">
	<img src="http://websamplenow.com/30/userprofile/images/avatar.jpg" class="img-responsive img-thumbnail ">
	  </div>*/
	if (user == undefined) {
		console.log("User is undefined!");
	}
	if (!userLogged(user.username)) { // Public profile data
		console.log("User not logged! Username:" + user.username);

		//showLoginButons();
		/* Show unlogged user profile page. */
		//return;

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
		//	
		//
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
	reply.hide();

	var container = $('<div class="row no-gutter col-xs-12 col-sm-8 col-md-6 col-lg-4" ></div>');
	var textArea = $('<textarea name="text" class="comment-reply-area form-control" rows="2" placeholder="Write your comment here" ></textarea>');
	var submitButton = $('<button class="submitComment pull-right" onclick="submitComment($(this).parent().parent().parent())">Comment</button>');
	container.append(textArea);
	container.append(submitButton);
//	console.log("reply hide ");
	reply.parent().append(container);
//	console.log("reply hide ");
}