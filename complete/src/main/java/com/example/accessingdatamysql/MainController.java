package com.example.accessingdatamysql;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.json.JSONObject;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Map;
import java.util.List;


@RestController
public class MainController {
	@Autowired // This means to get the bean called userRepository
		   // Which is auto-generated by Spring, we will use it to handle the data
	private UserRepository userRepository;

	@PostMapping(path="/demo/add", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public ResponseEntity<String> addNewUser (@RequestBody User user) {
		// -ResponseBody means the returned String is the response, not a view name

		User n = new User();
		n.setName(user.getName());
		n.setEmail(user.getEmail());
		userRepository.save(n);

		System.out.println("\nADD QUERY: insert into user cols(name, email) values ('" + n.getName() + "', '" + n.getEmail() + "');");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		//returns JSON: saved = true and the new user id
		String metricsStr = "{\"Saved\":\"true\", \"ID\":\"" + n.getId() + "\"}";
		ResponseEntity<String> jsonResponse = new ResponseEntity<String>(metricsStr, headers, HttpStatus.OK);

		return jsonResponse;
	}

	//Note: this function is capable of updating more than one row.
	@PostMapping(path="/demo/update", consumes = "application/json", produces = "application/json")
	@ResponseBody
	public ResponseEntity<String> updateUser (@RequestBody ObjectNode pPostParams) {
		// -ResponseBody means the returned String is the response, not a view name

		String oldName = pPostParams.get("oldName").asText();
		String userName = pPostParams.get("userName").asText();
		String userEmail = pPostParams.get("userEmail").asText();
		
		List<User> users = userRepository.findByName(oldName);
		User user = users.get(0);
		user.setName(userName);
		user.setEmail(userEmail);
		userRepository.save(user);

		System.out.println("UPDATE QUERY: UPDATE user set name='" + userName + "', email='" + userEmail + "' where name='" + oldName + "';");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<>("{\"oldname\":\"" + oldName + "\", \"newname\":\"" + userRepository.findByName(userName).get(0).getName() +"\"}", headers, HttpStatus.OK);
	}



	@PostMapping(path="/demo/delete", consumes = "application/json", produces = "application/json")
	@Transactional
	@ResponseBody
	public ResponseEntity<String> deleteUser (@RequestBody User user) {
		// -ResponseBody means the returned String is the response, not a view name

		User n = new User();
		n.setName(user.getName());
		n.setEmail(user.getEmail());
		long rowsDeleted = userRepository.deleteByName(n.getName());

		System.out.println("DELETE QUERY: delete from user where name = '" + n.getName() + "';");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<>("{\"DeleteCount\":\"" + rowsDeleted + "\"}", headers, HttpStatus.OK);
	}

	@GetMapping(path="/demo/all")
	public @ResponseBody Iterable<User> getAllUsers() {
		// This returns a JSON or XML with the users
		return userRepository.findAll();
	}
}
