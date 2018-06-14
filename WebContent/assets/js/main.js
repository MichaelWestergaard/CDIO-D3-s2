	var notificationNum = 0;


	$('#homeBtn').click(function(e) {
		e.preventDefault();
		$(".content-container").load($(this).attr("href"));
		if($(this).attr("href") == "ReceptComponentList.html"){
			var formData = $('form').serializeArray();
 		   	console.log(formData);
			$.ajax({
				type: 'GET',
				url: 'http://localhost:8080/CDIO-D3-s2/rest/recept/getReceptComponentList',
				dataType: 'json',
				success: function(data){
					if(data.response_status == "success"){
						response = JSON.parse(data.response_message);
						
						var receptID = formData[1].value;
						var receptName = formData[0].value;
						
						for(var i = 0; i < response.length; i++){
							if(response[i].receptID == receptID){
								$(".user-table tbody").append('<tr id="' + receptID + response[i].ingredientID + '"><td>' + receptID + '</td><td>' + receptName + '</td><td>' + response[i].ingredientID + '</td><td>' + response[i].nomNetto + '</td><td>' + response[i].tolerance + '</td></tr>');
							}
						}
					}	
				}
			});
	   }
    });
	
	$('#createBtn').on('click', function(){
		$(".content-container").load("createUser.html");
	});
	$('#createRecept').on('click', function(){
		$(".content-container").load("createRecept.html");
	});
	$('#createReceptComponent').on('click', function(){
		var receptID = $("#receptID").val();
		var receptName = $("#receptName").val();
		$(".content-container").load("createReceptComponent.html", function(){
			$("input[name=receptID]").val(receptID);
			$("input[name=receptName]").val(receptName);
		});
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
        	   console.log(data);
        	   showStatusMessage(data.response_code + ": " + data.response_message, data.response_status);
        	   if(data.response_status == "success"){
        		   if($(form).attr('action') == "rest/Product/createProductBatch"){
            		   var productBatchID = $('input[name=productBatchID]').val();
            		   generateReport(productBatchID);
            	   }
        		   var formData = $('form').serializeArray();
        		   console.log(formData);
            	   $('.content-container').load($(form).attr('id'));
            	   if($(form).attr('action') == "rest/recept/createReceptComponent"){
            			$.ajax({
            				type: 'GET',
            				url: 'http://localhost:8080/CDIO-D3-s2/rest/recept/getReceptComponentList',
            				dataType: 'json',
            				success: function(data){
            					console.log(data);
            					if(data.response_status == "success"){
            						response = JSON.parse(data.response_message);
            						
            						var receptID = formData[1].value;
            						var receptName = formData[0].value;
            						
            						for(var i = 0; i < response.length; i++){
            							if(response[i].receptID == receptID){
            								$(".user-table tbody").append('<tr id="' + receptID + response[i].ingredientID + '"><td>' + receptID + '</td><td>' + receptName + '</td><td>' + response[i].ingredientID + '</td><td>' + response[i].nomNetto + '</td><td>' + response[i].tolerance + '</td></tr>');
            							}
            						}
            					}	
            				}
            			});
            	   }
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
	
	function generateReport(productBatchID){
		showStatusMessage("Vent venligst! Dette kan tage noget tid..", "info");
		$.ajax({
			type: 'GET',
			url: 'http://localhost:8080/CDIO-D3-s2/rest/Product/getProductBatch',
			dataType: 'json',
			data: {
				productBatchID: productBatchID
			},
			success: function(data){
				if(data.response_status == "success"){
					var response = JSON.parse(data.response_message);
					
					var mywindow = window.open('', 'PRINT', 'height=500,width=500');
					
					mywindow.document.write('<style type="text/css">.report-content {margin: 0 5px;}.report-content .top-info table td, .report-content .recept-components table td {padding-right: 15px;}.recept-components {margin-top: 20px;}.recept-components table {margin-bottom: 20px;}.ingredientTable td {padding-right: 45px !important;}.recept-components table.bordered {border-top: 1px dashed #898989;}.recept-components .info-table th, .recept-components .info-table td {text-align: left;width: 13%;}</style>');
				    
					var date = new Date();
				    var currentDate = date.getDate() + "/" + (date.getMonth()+1) + "/" + date.getFullYear();
					
					mywindow.document.write('<div class="report-content"><div class="top-info"><table><tr><td>Udskrevet d.</td><td id="date">'+currentDate+'</td></tr><tr><td>Produkt Batch Nr.</td><td id="productBatchID">'+ response.productBatchID +'</td></tr><tr><td>Recept Nr.</td><td id="receptID">'+response.receptID+'</td></tr></table></div><div class="recept-components">');
					//Lav om til et kald
					$.ajax({
						type: 'GET',
						url: 'http://localhost:8080/CDIO-D3-s2/rest/recept/getReceptComponentList',
						dataType: 'json',
						async: false,
						success: function(dataComponent){
							if(dataComponent.response_status == "success"){
								responseReceptComponent = JSON.parse(dataComponent.response_message);
								
								var sumTara = 0;
								var sumNetto = 0;
								
								for(var i = 0; i < responseReceptComponent.length; i++){
									if(responseReceptComponent[i].receptID == response.receptID){
										var availableProductBatches = "";
										var productBatchesCount = 0;
										
										$.ajax({
											type: 'GET',
											url: 'http://localhost:8080/CDIO-D3-s2/rest/ingredient/getIngredientBatchesByIngredient',
											data: {
												ingredientID: responseReceptComponent[i].ingredientID
											},
											dataType: 'json',
											async: false,
											success: function(productBatchResponse){
												if(productBatchResponse.response_status == "success"){
													productBatches = JSON.parse(productBatchResponse.response_message);
													for(var k = 0; k < productBatches.length; k++){
														availableProductBatches += productBatches[k].toString();
														if(k+1 != productBatches.length){
															availableProductBatches += ", ";
														}
													}
													productBatchesCount = productBatches.length;
												}
											}
										});
										
										if(productBatchesCount == 1){
											var ingredientBatchText = "Mulig Råvarebatch ID:";
										} else {
											var ingredientBatchText = "Mulige Råvarebatch ID'er:";
										}
										
										var firstElement = '<table class="ingredientTable"><tr><td>Råvare nr.</td><td>'+responseReceptComponent[i].ingredientID+'</td></tr><tr><td>Råvare Navn</td><td>'+responseReceptComponent[i].ingredientName+'</td><tr><td>' + ingredientBatchText + '</td><td>' + availableProductBatches + '</td></tr></table><table class="bordered info-table"><thead><tr><th>Del</th><th>Mængde</th><th>Tolerance</th><th>Tara</th><th>Netto(kg)</th><th>Batch</th><th>Opr.</th><th>Terminal</th></tr></thead><tbody><tr><td>1</td><td>'+responseReceptComponent[i].nomNetto+'</td><td>&plusmn;'+responseReceptComponent[i].tolerance+'%</td>';
										$.ajax({
											type: 'GET',
											url: 'http://localhost:8080/CDIO-D3-s2/rest/ingredient/getIngBatchList',
											async: false,
											dataType: 'json',
											success: function(dataRB){
												if(dataRB.response_status == "success"){
													responseRB = JSON.parse(dataRB.response_message);
													var inserted = false;
													for(var j = 0; j < responseRB.length; j++){
														$.ajax({
															type: 'GET',
															url: 'http://localhost:8080/CDIO-D3-s2/rest/Product/getProductBatchComponent',
															async: false,
															data: {
																productBatchID: productBatchID,
																raavareBatchID: responseRB[j].ingBatchID
															},
															dataType: 'json',
															success: function(dataPBC){
																if(dataPBC.response_status == "success"){
																	responsePBC = JSON.parse(dataPBC.response_message);
																	if(responsePBC != null && responsePBC.ingredientID == responseReceptComponent[i].ingredientID){
																		inserted = true;
																		mywindow.document.write(firstElement + '<td>'+responsePBC.tara+'</td><td>'+responsePBC.netto+'</td><td>'+responsePBC.ingredientBatchID+'</td><td>'+responsePBC.initials+'</td><td>1</td></tr></tbody></table>');
																		sumTara += responsePBC.tara;
																		sumNetto += responsePBC.netto;
																	} else {
																		if(!inserted && (j+1) == responseRB.length){
																			mywindow.document.write(firstElement + '<td></td><td></td><td></td><td></td><td></td></tr></tbody></table>');
																			inserted = true;
																		}
																	}
																}
															}
														});
													}
												}
											}
										});
									}
								}
								
								if(Math.round(sumTara) !== sumTara){
									sumTara = sumTara.toFixed(3);
								}
								
								if(Math.round(sumNetto) !== sumNetto){
									sumNetto = sumNetto.toFixed(3);
								}
								
								mywindow.document.write('<table><tr><td>Sum Tara:</td><td id="sumTara">'+sumTara+'</td></tr><tr><td>Sum Netto:</td><td id="sumNetto">'+sumNetto+'</td></tr></table></div>')
								
								switch(response.status){
									case 0:
										status = "Startet";
										break;
									case 1:
										status = "Under produktion";
										break;
									case 2:
										status = "Afsluttet";
										break;
								}
								
								mywindow.document.write('<div class="bottom-info"><table><tr><td>Produktion Status:</td><td id="productionStatus">'+status+'</td></tr><tr><td>Produktion Startet:</td><td id="productionStartDate">'+response.startdato+'</td></tr><tr><td>Produktion Slut:</td><td id="productionEndDate">'+response.slutdato+'</td></tr></table></div>');
								
								mywindow.document.close();
							    mywindow.focus();

							    mywindow.print();
							    mywindow.close();
							}	
						}
					});
				} else {
					showStatusMessage(data.response_code + ": " + data.response_message, data.response_status);
				}
			}
		});
	}
	
	function showStatusMessage(text, status){
		var num = notificationNum;
		$('.status-container').append('<div class="status-content ' + status + '" id="notification-'+num+'"><div class="status-message">'+ text +'</div></div>');
		notificationNum++;
		setTimeout(function(){
			console.log("Removing " + num);
			$('.status-content#notification-'+ num).fadeOut(400, function() { $(this).remove(); });
		}, 3000);
	}