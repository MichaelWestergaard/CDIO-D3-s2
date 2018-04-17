$(document).ready(function(){	
	$('#homeBtn').on('click', function(){
		$(".content-container").load("Brugerliste.html",function(){}).hide().fadeIn();
	});
});