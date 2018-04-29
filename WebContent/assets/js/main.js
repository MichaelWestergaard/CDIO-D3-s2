$(document).ready(function(){	
	$('#homeBtn').on('click', function(){
		console.log("home");
		$(".content-container").load("Brugerliste.html");
	});
	$('#createBtn').on('click', function(){
		console.log("create");
		$(".content-container").load("createUser.html");
	});
	
	//Brugerliste
	$('#options').on('change', function() {
		
		if (this.value == "deleteUser") {
			if (confirm("Er du sikker på at du vil fjerne denne bruger?")) {
		        alert("bg. Brugeren blev ikke fjernet, da vi ikke har lavet det endnu..");
		    }
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

	//Generate userList

	$.ajax({
		type: 'GET',
		url: 'http://localhost:8080/CDIO-D3-s2/rest/user/getUserList',
		dataType: 'json',
		success: function(data){
			for (var i = 0; i < data.length; i++) {
			 console.log(data[i]);
				
				
			 var roles = "";
			 var status;
			 
			 for (var j = 0; j < data[i]['role'].length; j++) {
			 	roles += data[i].role[j] + ", ";
			 }
			 
			 if(data[i].active == 1){
				 status = "Ja";
			 } else {
				 status = "Nej";
			 }
				 
				$(".user-table tbody").append('<tr><td>' + data[i].userID + '</td><td>' + data[i].userName + '</td><td>' + data[i].name + '</td><td>Bar' + data[i].lastName + '</td><td>' + data[i].password + '</td><td>' + roles + '</td><td>' + status + '</td><td><select id="options"><option value="vælg">-Vælg-</option><option value="deleteUser">Slet bruger</option><option value="updateUser">Opdater bruger</option><option value="resetPass">Nulstil kode</option></select></td></tr>');
			}
		}
	});

});