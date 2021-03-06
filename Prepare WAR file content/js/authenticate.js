
var userURL = "rest/authenticate/get_logged_user";

function loadLoggedUser() {
	$.ajax({
		type : 'GET',
		url : userURL,
		dataType : "json", // data type of response
		success : function(user) {
			if (user != null && user != undefined) {
				sessionStorage.setItem("user", JSON.stringify(user));

				console.log('Load Logged User [User is logged]');
				
			} else {
				sessionStorage.removeItem("user");
				console.log('Load Logged User [User is NOT logged]');
				showLoginButons();
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR1: " + errorThrown + "\nTextStatus: " + textStatus);
		}
	});
}

function login() {
	var $form = $("#loginform");
	var data = getFormData($form);
	var s = JSON.stringify(data);
	
//		alert("Request object: " + s);
	console.log("Login triggered!");
	$.ajax({
		url : "rest/authenticate/login",
		type : "POST",
		data : s,
		contentType : "application/json",
		dataType : "json",
		success : function(user) {

			if (user == undefined) {
				alert("User doesn't exist!");
				return;
			} else {
				sessionStorage.setItem("user", JSON.stringify(user));
				showLogoutButons(user);
				console.log("Logged user saved in localStorage!");
			}
						alert("Response stringify: " + JSON.stringify(user));

			if (user.role.toLowerCase() === "ADMIN".toLowerCase()) {
								alert("role is admin");
				console.log("Successfully logged as ADMIN with username: " + user.username);
				enableUserPrivs();
			//				window.location.href = "admin.html";
			}
			if (user.role.toLowerCase() === "MODERATOR".toLowerCase()) {
								alert("role is moderator");
				console.log("Successfully logged as MODERATOR with username: " + user.username);
				enableModeratorPrivs();
			}
			if (user.role.toLowerCase() === "USER".toLowerCase()) {
								alert("role is user");
				console.log("Successfully logged as USER with username: " + user.username);
				enableAdminPrivs();
			}

			location.reload();

		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR2: " + errorThrown);
		}
	});
}

function logout() {
	console.log("Logout triggered!");
	$.ajax({
		url : "rest/authenticate/logout",
		type : "GET",
		dataType : "text",
		success : function(response) {

			//			alert(response);
			console.log(response);
			showLoginButons();
			//$("#welcomeUser").prepend('Welcome ' + user.firstName + '. Click here for ');

			sessionStorage.removeItem("user");
			if(window.location.href.includes("profile")) {
				console.log("true");
				window.location.href = "homepage.html";
			} else {
				location.reload();	
			}
			
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR3: " + errorThrown);
		}
	});
}

function enableUserPrivs() {
}

function enableModeratorPrivs() {
	enableUserPrivs();
}

function enableAdminPrivs() {
	enableUserPrivs();
	enableModeratorPrivs();
}

function signup() {
	console.log("Signup triggered!");
	var $form = $("#signupform");
	var data = getFormData($form);
	var s = JSON.stringify(data);
	alert(s);
	$.ajax({
		url : "rest/authenticate/signup",
		type : "POST",
		data : s,
		contentType : "application/json",
		dataType : "json",
		success : function(user) {

			if (user == undefined) {
				alert("User with that username already exist!");
				return;
			} else {
				sessionStorage.setItem("user", JSON.stringify(user));
				console.log("Logged user saved in localStorage!");
				location.reload();
			}

		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR4: " + errorThrown);
		}
	});
}


function getFormData($form) {
	var unindexed_array = $form.serializeArray();
	var indexed_array = {};

	$.map(unindexed_array, function(n, i) {
		indexed_array[n['name']] = n['value'];
	});

	return indexed_array;
}