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
	console.log("Request object: " + s);
	$.ajax({
		url : "rest/authenticate/login",
		type : "POST",
		data : s,
		contentType : "application/json",
		dataType : "json",
		success : function(user) {
			
			if(user == undefined) {
				console.log("User doesn't exist!");
				return;
			}
			console.log("Response stringify: " + JSON.stringify(user));
			
			if (user.role.toLowerCase() === "ADMIN".toLowerCase()) {
				console.log("role is admin");
				window.location.href = "admin.html";
			}
			if (user.role.toLowerCase() === "MODERATOR".toLowerCase()) {
				console.log("role is moderator");
			}
			if (user.role.toLowerCase() === "USER".toLowerCase()) {
				console.log("role is user");
			}

			//window.location.href = "main.html";

		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("AJAX ERROR: " + errorThrown);
		}
	});
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