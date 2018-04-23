$(document).ready(function(){	
	$('#homeBtn').on('click', function(){
		console.log("home");
		$(".content-container").load("Brugerliste.html");
	});
	$('#createBtn').on('click', function(){
		console.log("create");
		$(".content-container").load("opretBruger.html");
	});
	
	//Brugerliste
	$('#options').on('change', function() {
		
		if (this.value == "deleteUser") {
			console.log("test");
		}
		
		if (this.value == "updateUser") {

			console.log("test1");
		$(".content-container").load('updateUser.html');
		}
		
		if (this.value == "resetPass") {

			console.log("test3");
		$(".content-container").load('resetPassword.html');
		}
		
	});
});

