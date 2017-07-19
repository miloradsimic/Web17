function getFormData($form) {
	var unindexed_array = $form.serializeArray();
	var indexed_array = {};

	$.map(unindexed_array, function(n, i) {
		indexed_array[n['name']] = n['value'];
	});

	return indexed_array;
}


function login() {
	var $form = $("#loginform");
	var data = getFormData($form);
	var s = JSON.stringify(data);
	alert("Request object: " + s);
	$.ajax({
		url : "rest/authenticate/login",
		type : "POST",
		data : s,
		contentType : "application/json",
		dataType : "json",
		success : function(user) {
			
			if(user == undefined) {
				alert("User doesn't exist!");
				return;
			} else {
				$("#loginButtons").hide();
				$("#welcomeUser").html('Welcome ' + user.firstName + '. Click here for ' + 
						'<a id="logout" role="button" onclick="logout()">log out</a>');
				$("#logoutButton").show();
			}
//			alert("Response stringify: " + JSON.stringify(user));
			
			if (user.role.toLowerCase() === "ADMIN".toLowerCase()) {
//				alert("role is admin");
				console.log("Successfully logged as ADMIN with username: " + user.username);
				enableUserPrivs();
//				window.location.href = "admin.html";
			}
			if (user.role.toLowerCase() === "MODERATOR".toLowerCase()) {
//				alert("role is moderator");
				console.log("Successfully logged as MODERATOR with username: " + user.username);
				enableModeratorPrivs();
			}
			if (user.role.toLowerCase() === "USER".toLowerCase()) {
//				alert("role is user");
				console.log("Successfully logged as USER with username: " + user.username);
				enableAdminPrivs();
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR: " + errorThrown);
		}
	});
}

function logout() {
	$.ajax({
		url : "rest/authenticate/logout",
		type : "GET",
		dataType : "text",
		success : function(response) {
		
//			alert(response);
			console.log(response);
			$("#loginButtons").show();
			//$("#welcomeUser").prepend('Welcome ' + user.firstName + '. Click here for ');
			$("#logoutButton").hide();
			
			
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR: " + errorThrown);
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
		success : function(data2) {
			console.log("Response Object: " + data2);
			console.log("Response Text: " + data2.responseText);
//			window.location.href = "profile.html";
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR: " + errorThrown);
		}
	});
}