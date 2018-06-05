	
	$('#homeBtn').click(function(e) {
		e.preventDefault();
		$(".content-container").load($(this).attr("href"));
    });
	
	$('#createBtn').on('click', function(){
		$(".content-container").load("createUser.html");
	});
	$('#createRecept').on('click', function(){
		$(".content-container").load("createRecept.html");
	});
	$('#createReceptComponent').on('click', function(){
		$(".content-container").load("createReceptComponent.html");
	});
	
	$('#createIngredient').on('click', function(){
		$(".content-container").load("createIngredient.html");
	});

	$('#createIngBatch').on('click', function(){
		$(".content-container").load("createRaavareBatch.html");
	});
	
	$('#createProductBatch').on('click', function(){
		$(".content-container").load("createProductBatch.html");
	});
	
	$('input[name=login]').on('click', function(){
		login();
	});
	
	$('form').submit(function(e){
		e.preventDefault();
		
		var form = $(this).closest("form");
		
		$.ajax({
           type: $(form).attr('method'),
           url: $(form).attr('action'),
           data: $('form').serialize(),
           success: function(data){
        	   data = JSON.parse(data);
        	   showStatusMessage(data.response_code + ": " + data.response_message, data.response_status);
        	   if(data.response_status == "success"){
            	   $('.content-container').load($(form).attr('id'));
        	   }
           },
			error: function(response, ajaxOptions, thrownError) {
				//alert(response.responseText);
				showStatusMessage("Fejl ved indsendelse af formularen, prøv igen!", "error");
	    	}
         });
	});

	function login(){
		$.ajax({
			type: "POST",
			url: "http://localhost:8080/CDIO-D3-s2/rest/user/login",
			datatype: "application/json",
			data: {
				username: $('input[name=username]').val()
			},
			success: function(data){
				data = JSON.parse(data);
				if(data.response_status == "success"){
					sessionStorage.setItem("userID", data.response_message);
					toggleNavbar();
					$(".content-container").load("startpage.html");
				} else {
					showStatusMessage("Fejl " + data.response_code + ": " + data.response_message, "error");
				}
			},
			error: function(xhr, ajaxOptions, thrownError) {
	        	alert(thrownError + "\r\n" + xhr.statusText + "\r\n" + xhr.responseText);
	    	}
		});
	}
	
	//Komma til punktum i decimal felter
	$(document).on('change', '.decimal', function() {
		$(this).val($(this).val().replace(/,/g, '.'));
	});
	
	//Login med enter
	$(document).keyup(function (e) {
	    if (e.keyCode === 13 && $('.content').is("#loginpage")) {
	    	login();
	    }
	 });

	function toggleNavbar(){
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
        		var response = JSON.parse(data.response_message);
        		if(data != null){

        			$('.navbar .navigation').empty();
	        		$('.navbar .navigation').append('<li><a href="#" id="startpage"><span>Forside</span></a></li>');
	        		for (var i = 0; i < response.role.length; i++) {
	        			switch(response.role[i]){        			
	        				case "Admin":
	        					$('.navbar .navigation').append('<li><a href="#" id="userList"><span>Brugerliste</span></a></li>');
	        					break;
	        				case "Pharmacist":
	        					$('.navbar .navigation').append('<li><a href="#" id="receptliste"><span>Receptliste</span></a></li>');
	        					$('.navbar .navigation').append('<li><a href="#" id="ingredientliste"><span>Råvareliste</span></a></li>');
	        					break;
	        				case "Produktionsleder":
	        					$('.navbar .navigation').append('<li><a href="#" id="ingbatchliste"><span>Råvarebatches</span></a></li>');
	        					$('.navbar .navigation').append('<li><a href="#" id="productBatch"><span>Produktbatches</span></a></li>');
	        					break;
	        			}
	        		}
	        		$('.navbar .navigation').append('<li><a href="#" id="logout"><span>Log Ud</span></a></li>');
	        		$('.navbar').show();
        		}
        	}
		});

	}

	function showStatusMessage(text, status){
		$('.status-container .status-content .status-message').html(text);
		$('.status-container .status-content').addClass(status);
		$('.status-container').toggleClass('shown');
		setTimeout(function(){
			$('.status-container').toggleClass('shown');
			$('.status-container .status-content').removeClass(status);
		}, 3000);
	}