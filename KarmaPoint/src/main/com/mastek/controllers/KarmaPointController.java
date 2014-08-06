package com.mastek.controllers;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.mastek.domain.KarmaPointCalc;

import net.sf.json.JSONObject;

@Path("/")
public class KarmaPointController {
	@Path("/CalculateKarmaPoints")
	@GET
	@Produces("text/json")
	public JSONObject CalculateKarmaPoints(
			@QueryParam("userid") int userid) {
		
		KarmaPointCalc kp = new KarmaPointCalc();
		Double  points =  kp.CalculateKP(userid);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("userid", userid);
		jsonObj.put("karmapoints", points);
		return jsonObj;

	}
}

// http://localhost:8080/YammerSync/rest/CalculateKarmaPoints?userid=8374244
//Ravi - 8374244
		// CD - 8374242
		// Vikrant - 1483241
		// Suyash - 8373994
		// Kedar R - 8374865
		//Hiren - 1497442141
		// Yogesh J - 8375042