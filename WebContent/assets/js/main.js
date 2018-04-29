	$('#homeBtn').on('click', function(){
		console.log("home");
		$(".content-container").load("Brugerliste.html");
	});
	$('#createBtn').on('click', function(){
		console.log("create");
		$(".content-container").load("createUser.html");
	});
	