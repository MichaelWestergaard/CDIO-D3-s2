	$('#homeBtn').on('click', function(){
		console.log("home");
		$(".content-container").load("Brugerliste.html");
	});
	$('#createBtn').on('click', function(){
		console.log("create");
		$(".content-container").load("createUser.html");
	});
	$('#createRecept').on('click', function(){
		console.log("create");
		$(".content-container").load("createRecept.html");
	});
	
	
	function toggleNavbar(){
		//Fjern alle knapper
		$('.navbar .navigation').empty();

		//Få brugerens roller
		$.ajax({
        	type: "GET",
        	url: "http://localhost:8080/CDIO-D3-s2/rest/user/getUser",
        	dataType: 'json',
        	data: {
        		userID: sessionStorage.getItem("userID")
        	},
        	success: function(data){
        		console.log(data.role);
        	}
		});
		
		$('.navbar').toggle();
	}