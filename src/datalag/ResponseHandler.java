package datalag;

import com.google.gson.JsonObject;

public class ResponseHandler {

	public String createResponse(String responseStatus, int responseCode, String responseMessage) {
		JsonObject response = new JsonObject();
		response.addProperty("response_status", responseStatus);
		response.addProperty("response_code", responseCode);
		response.addProperty("response_message", responseMessage);
		return response.toString();
	}
	
}
