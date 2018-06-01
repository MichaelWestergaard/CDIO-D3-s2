	$('#homeBtn').on('click', function(){
		$(".content-container").load("startpage.html");
	});
	$('#createBtn').on('click', function(){
		$(".content-container").load("createUser.html");
	});
	$('#createRecept').on('click', function(){
		$(".content-container").load("createRecept.html");
	});
	
	$('input[name=login]').on('click', function(){
		login();
	});
		
	function login(){
		$.ajax({
			type: "POST",
			url: "http://localhost:8080/CDIO-D3-s2/rest/user/login",
			datatype: "json",
			data: {
				username: $('input[name=username]').val()
			},
			success: function(data){
				if(data != null){
					sessionStorage.setItem("userID", data);
					toggleNavbar();
					$(".content-container").empty();
					$(".content-container").load("startpage.html");
				}
			},
			error: function(xhr, ajaxOptions, thrownError) {
	        	alert(thrownError + "\r\n" + xhr.statusText + "\r\n" + xhr.responseText);
	    	}
		});
	}

	function toggleNavbar(){
		//Fjern alle knapper
		$('.navbar .navigation').empty();
		var userID = sessionStorage.getItem("userID");
		//Få brugerens roller
		$.ajax({
        	type: "GET",
        	url: "http://localhost:8080/CDIO-D3-s2/rest/user/getUser",
        	dataType: 'json',
        	data: {
        		userID: userID
        	},
        	success: function(data){
        		if(data != null){
	        		$('.navbar .navigation').append('<li><a href="#" id="startpage">Forside</a></li>');
	        		for (var i = 0; i < data.role.length; i++) {
	        			switch(data.role[i]){        			
	        				case "Admin":
	        					$('.navbar .navigation').append('<li><a href="#" id="userList">Brugerliste</a></li>');
	        					break;
	        				case "Pharmacist":
	        					$('.navbar .navigation').append('<li><a href="#" id="receptliste">Receptliste</a></li>');
	        					break;
	        				case "Produktionsleder":
	        					$('.navbar .navigation').append('<li><a href="#" id="userList">Produktionsleder</a></li>');
	        					break;
	        				case "Laborant":
	        					$('.navbar .navigation').append('<li><a href="#" id="userList">Laborant</a></li>');
	        					break;
	        			}
	        		}
	        		$('.navbar .navigation').append('<li><a href="#" id="logout">Log Ud</a></li>');
	        		$('.navbar').show();
        		}
        	}
		});
		
		
	}