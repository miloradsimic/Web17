
var subforumsURL = "rest/homepage/subforums";
var topicsURL = "rest/homepage/topics";
var topicURL = "rest/homepage/topic";
var profileURL = "rest/homepage/profile";
var users_listURL = "rest/homepage/users_list";
var upload_avatarURL = "rest/homepage/upload_avatar";
var submitProfileDataURL = "rest/homepage/submit_profile_data";
var sumbpitTopicRatingURL = "rest/homepage/rate_topic_toogle";

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
		$("#loginSubmit").on("click", login);
		$("#signUpSubmit").on("click", signup);
		$("#logout").on("click", logout);
		//$("#homeButton").on( "click", logout);

	});
}

//Redirect functions
function goToSubforum(id) {
	window.location.href = "subforum.html" + "?id=" + id;
}
function goToTopic(id) {
	window.location.href = "topic.html" + "?id=" + id;
}
// ------PROFILE-----
function goToProfile(id) {
	console.log('Go To Profile: id=' + id);
	var ID = id;
	if (id == -1 || id == JSON.parse(sessionStorage.getItem('user')).userId) { // Gets logged user.
		console.log("id = -1 !" + JSON.parse(sessionStorage.getItem('user')).username);
		ID = JSON.parse(sessionStorage.getItem('user')).userId;
	} else { //Gets public profile data
		console.log("Public profile data of user with ID: " + id);
	}
	window.location.href = "profile.html" + "?id=" + ID;

}
function setUpProfileAvatar(input) {
	if (input.files && input.files[0]) {


		var objFormData = new FormData();
		var objFile = input.files[0]

		//	    var name1 = objFile.fileName;
		//	    var name2 = objFile.name;
		//	    
		//	    console.log('file name1: ' + name1);
		//	    console.log('file name2: ' + name2);
		//	    console.log('file name type: ' + name1.constructor.name + " name 2 " + name2.constructor.name);

		objFormData.append('image', objFile);
		objFormData.append('name', objFile.name);


		$.ajax({
			url : upload_avatarURL,
			type : 'POST',
			contentType : false,
			data : objFormData,
			processData : false,
			success : function(isDone) {
				var reader = new FileReader();

				reader.onload = function(e) {
					$('#avatar')
						.attr('src', e.target.result);
				//                .width(150)
				//                .height(200);
				};

				reader.readAsDataURL(input.files[0]);
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				var OriginalString = XMLHttpRequest.responseText;
				var StrippedString = OriginalString.replace(/(<([^>]+)>)/ig, "");
				console.log(StrippedString);
				alert("AJAX ERROR71: " + errorThrown + "\nTextStatus: " + textStatus + "\nRequest" + XMLHttpRequest);
			}
		});
	/*

	//		var data = new FormData();
	var file = input.files[0];
	//		data.append('file', file);
	//		data.append('name', file.name);
	//		var formData = new FormData();
	//		formData.append('uploadfile', file);
	var reader = new FileReader();
	reader.readAsDataURL(file);
	reader.onload = function() {
		console.log(reader.result);

		var data = {
			image : reader
		};
		console.log("JSON upload data type:" + reader.result.constructor.name);
		console.log("TRUE");
		$.ajax({
			url : upload_avatarURL,
			type : "POST",
			contentType : "application/json",
			data : data,
			processData : false,
			success : function(isDone) {
				var reader = new FileReader();

				reader.onload = function(e) {
					$('#avatar')
						.attr('src', e.target.result);
				//                .width(150)
				//                .height(200);
				};

				reader.readAsDataURL(input.files[0]);
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				var OriginalString = XMLHttpRequest.responseText;
				var StrippedString = OriginalString.replace(/(<([^>]+)>)/ig, "");
				console.log(StrippedString);
				alert("AJAX ERROR71: " + errorThrown + "\nTextStatus: " + textStatus + "\nRequest" + XMLHttpRequest);
			}
		});

	};
	reader.onerror = function(error) {
		console.log('Error: ', error);
	};

	//var extension = file.fileName.substr('.', file.name.length);
	*/
	}
}
/** Loaded on click on Profile*/
function loadProfileData() {
	if (userLogged() == false) {
		showLoginButons();
	} else {
		showLogoutButons();
	}
	console.log('Load Profile Data');
	var id = getUrlParameter("id");
	console.log('User id: ' + id);
	$.ajax({
		type : 'GET',
		url : profileURL + "/" + id,
		dataType : "json", // data type of response
		success : renderProfilePage,
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			var OriginalString = XMLHttpRequest.responseText;
			var StrippedString = OriginalString.replace(/(<([^>]+)>)/ig, "");
			console.log(StrippedString);
			alert("AJAX ERROR7: " + errorThrown + "\nTextStatus: " + textStatus + "\nRequest" + XMLHttpRequest);
		}
	});
}
function submitProfileData(form) {
	console.log('Submiting form: ' + form.find("#upload_avatar").val());

	//	if (form.find("#upload_avatar").files) {
	//
	//		console.log("It works!");
	//	}


	var data = {
		firstName : form.find("#first_name").val(),
		lastName : form.find("#last_name").val(),
		email : form.find("#email").val(),
		password : form.find("#new_password").val()
	//		,
	//		avatar : form.find("#upload_avatar").files[0]
	};
	if (form.valid()) {
		console.log("Form is valid");
		console.log("Submiting data object" + JSON.stringify(data));

		var s = JSON.stringify(data);
		console.log(s);
		$.ajax({
			url : submitProfileDataURL,
			type : "POST",
			data : s,
			contentType : "application/json",
			dataType : "json",
			success : function(flag) {

				if (flag == undefined) {
					alert("Undefined! Not successful!");
					return;
				}
				if (flag == true) {
					alert("True! Successful!");
				} else {
					alert("False! Not successful!");
				}
				location.reload();

			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				alert("AJAX ERROR8 submitComment: " + errorThrown + "\nRequest" + XMLHttpRequest);
			}
		});
	}

//
//	
}
function loadUsersList() {
	saveProfileMenuTab(3); //not implemented
	console.log('Load Users List');
	var id = getUrlParameter("id");
	console.log('User with id: ' + id + ' requests all users list');
	$.ajax({
		type : 'GET',
		url : users_listURL + "/" + id,
		dataType : "json", // data type of response
		success : renderManageUsersPage,
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			var OriginalString = XMLHttpRequest.responseText;
			var StrippedString = OriginalString.replace(/(<([^>]+)>)/ig, "");
			console.log(StrippedString);
			alert("AJAX ERROR7: " + errorThrown + "\nTextStatus: " + textStatus + "\nRequest" + XMLHttpRequest);
		}
	});
}
//-------PROFILE END-----

//AJAX calls
function loadTopicDetailsAndComments() {
	if (userLogged() == false) {
		showLoginButons();
	} else {
		showLogoutButons();
	}
	//	console.log('LoadTopicDetailsAndComments from file into page.');
	var id = getUrlParameter("id");
	//	console.log('Topic id: ' + id);
	$.ajax({
		type : 'GET',
		url : topicURL + "/" + id,
		dataType : "json", // data type of response
		success : renderTopicAndComments,
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR5: " + errorThrown + "\nTextStatus: " + textStatus);
		}
	});
}
function loadSubforums() {
	if (userLogged() == false) {
		showLoginButons();
	} else {
		showLogoutButons();
	}
	console.log('LoadSubforums from file into page.');
	$.ajax({
		type : 'GET',
		url : subforumsURL,
		dataType : "json", // data type of response
		success : renderSubforumsList,
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR6: " + errorThrown + "\nTextStatus: " + textStatus);
		}
	});
}
function loadTopics() {
	if (userLogged() == false) {
		showLoginButons();
	} else {
		showLogoutButons();
	}
	console.log('LoadTopics from file into page.');
	var id = getUrlParameter("id");
	console.log('Subforum id: ' + id);
	$.ajax({
		type : 'GET',
		url : topicsURL + "/" + id,
		dataType : "json", // data type of response
		success : renderTopicsList,
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			var OriginalString = XMLHttpRequest.responseText;
			var StrippedString = OriginalString.replace(/(<([^>]+)>)/ig, "");
			console.log(StrippedString);
			alert("AJAX ERROR7: " + errorThrown + "\nTextStatus: " + textStatus + "\nRequest" + XMLHttpRequest);
		}
	});
}
function submitComment(comment) {
	console.log('Submiting comment.');
	var data = {
		text : comment.find(".comment-reply-area").val(),
		parentId : comment.attr("id").replace('c', ''),
		topicId : $('.topic-root').attr("id")
	};

	var s = JSON.stringify(data);
	console.log(s);
	$.ajax({
		url : "rest/homepage/add_comment",
		type : "POST",
		data : s,
		contentType : "application/json",
		dataType : "json",
		success : function(flag) {

			if (flag == undefined) {
				alert("Undefined!");
				return;
			}
			if (flag == true) {
				//				alert("true!");
			} else {
				alert("false!");
			}
			location.reload();

		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR8 submitComment: " + errorThrown + "\nRequest" + XMLHttpRequest);
		}
	});

}
function submitLike(element, commentId) {
	console.log('Submiting Like.');
	var rate = -1;
	if (element.hasClass("comment-like")) {
		rate = 1;
	}
	var data = {
		commentId : commentId,
		ratingValue : rate
	};

	var s = JSON.stringify(data);
	console.log(s);
	$.ajax({
		url : "rest/homepage/like_comment_toogle",
		type : "POST",
		data : s,
		contentType : "application/json",
		dataType : "json",
		success : function(rating) {

			console.log("Successful rating: " + rating.total);
			// Icon color
			if (rate == 1) {
				if (rating.rated == 1) {
					$('#c' + commentId).find(".comment-like-unused").removeClass("comment-like-unused").addClass("comment-like-used");
				} else {
					$('#c' + commentId).find(".comment-like-used").removeClass("comment-like-used").addClass("comment-like-unused");
				}
				$('#c' + commentId).find(".comment-dislike-used").removeClass("comment-dislike-used").addClass("comment-dislike-unused");

			} else if (rate == -1) {
				if (rating.rated == -1) {
					$('#c' + commentId).find(".comment-dislike-unused").removeClass("comment-dislike-unused").addClass("comment-dislike-used");
				} else {
					$('#c' + commentId).find(".comment-dislike-used").removeClass("comment-dislike-used").addClass("comment-dislike-unused");
				}
				$('#c' + commentId).find(".comment-like-used").removeClass("comment-like-used").addClass("comment-like-unused");
			}
			// Rating text color
			$('#c' + commentId).find(".comment-rating").removeClass("comment-likes").removeClass("comment-dislikes").removeClass("comment-neutral");
			if (rating.total > 0) {
				$('#c' + commentId).find(".comment-rating").addClass("comment-likes");
			}
			if (rating.total < 0) {
				$('#c' + commentId).find(".comment-rating").addClass("comment-dislikes");
			}
			if (rating.total == 0) {
				$('#c' + commentId).find(".comment-rating").addClass("comment-neutral");
			}

			// Rating text update
			$('#c' + commentId).find(".comment-rating").text(rating.total);


		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR8 submitComment: " + errorThrown + "\nRequest" + XMLHttpRequest);
		}
	});

}

function submitTopicRating(element, topicId) {
	console.log('submitTopicRating(): \ntopicId=' + topicId );
	var rate = -1;
	if (element.hasClass("comment-like")) {
		rate = 1;
	}
	var data = {
		topicId : topicId,
		ratingValue : rate
	};

	var s = JSON.stringify(data);
	console.log("REST sending: " + s);
	$.ajax({
		url : sumbpitTopicRatingURL,
		type : "POST",
		data : s,
		contentType : "application/json",
		dataType : "json",
		success : function(rating) {

			console.log("Successful rating: \nrated=" + rating.rated);
			// Icon color ~ farbanje strelica
			if (rate == 1) {
				if (rating.rated == 1) {
					$('#t' + topicId).find(".comment-like-unused").removeClass("comment-like-unused").addClass("comment-like-used");
				} else {
					$('#t' + topicId).find(".comment-like-used").removeClass("comment-like-used").addClass("comment-like-unused");
				}
				$('#t' + topicId).find(".comment-dislike-used").removeClass("comment-dislike-used").addClass("comment-dislike-unused");

			} else if (rate == -1) {
				if (rating.rated == -1) {
					$('#t' + topicId).find(".comment-dislike-unused").removeClass("comment-dislike-unused").addClass("comment-dislike-used");
				} else {
					$('#t' + topicId).find(".comment-dislike-used").removeClass("comment-dislike-used").addClass("comment-dislike-unused");
				}
				$('#t' + topicId).find(".comment-like-used").removeClass("comment-like-used").addClass("comment-like-unused");
			}

		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR8 submitComment: " + errorThrown + "\nRequest" + XMLHttpRequest);
		}
	});

}




//Helper functions
//Helper function to serialize all the form fields into a JSON string
/** Saves last selected tab, if you go F5 it selects same tab (optional) */
function saveProfileMenuTab(index) {
}
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
function userLogged(username) {
	if (arguments.length == 0) {
//		console.log('userLogged()');
		var isLogged = sessionStorage.getItem("user") != null && sessionStorage.getItem("user") != undefined;

//		console.log('return userLogged(): ' + isLogged);
		return isLogged;
	} else {
//		console.log('userLogged(username): ' + username);
		var isLogged = sessionStorage.getItem("user") != null && sessionStorage.getItem("user") != undefined && JSON.parse(sessionStorage.getItem('user')).username === username;
//		console.log('return userLogged(username): ' + isLogged);
		return isLogged;
	}
}