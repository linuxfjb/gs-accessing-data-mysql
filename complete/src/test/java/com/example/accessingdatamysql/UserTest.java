/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	  https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.accessingdatamysql;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import com.example.accessingdatamysql.User;
import org.springframework.http.HttpHeaders;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.http.HttpEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.accessingdatamysql.AccessingDataMysqlApplication;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
import java.util.HashMap;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class UserTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	private final ObjectMapper objectMapper = new ObjectMapper();

	//test for generate id from User table
	@Test
	public void testGenerateUserIDJPA() throws Exception {
		User testUser = new User();
		testUser.setName("Charlie Tuna");
		testUser.setEmail("sunkist@thesea.com");

		//this.port = 8080;
        	HttpHeaders headers = new HttpHeaders();
		String createPersonUrl = "http://localhost:" + this.port + "/demo/add";
        	headers.set("X-COM-PERSIST", "true"); 
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		JSONObject personJsonObject = new JSONObject();
		personJsonObject.put("name", testUser.getName());
		personJsonObject.put("email", testUser.getEmail());

		HttpEntity<String> request = new HttpEntity<String>(personJsonObject.toString(), headers);
    		ResponseEntity<String> responseEntityStr = restTemplate.postForEntity(createPersonUrl, request, String.class);

		Map<String,String> result = objectMapper.readValue(responseEntityStr.getBody(), HashMap.class);

		System.out.println("New USER ID CHECK:");
		System.out.println("============================");
		System.out.println("Body: " + responseEntityStr.getBody());
		System.out.println("Saved Path: " + result.get("Saved"));
		System.out.println("ID Path: " + result.get("ID"));
		System.out.println("============================");

		testUser.setId(new Integer(result.get("ID")).intValue());

		System.out.println("AFTER DB ADD: JPA TEST: testUser ID: " + testUser.getId() + " Name: " + testUser.getName());
    		System.out.println("testDemoAdd EndPoint: " + responseEntityStr);
		System.out.println("============================\n");

		//test that the json response came in with expected values
		assertEquals(HttpStatus.OK, responseEntityStr.getStatusCode());
		assertNotNull(responseEntityStr.getBody());
		assertNotNull(result.get("Saved"));
		assertEquals(result.get("Saved"), "true");
		assertTrue(testUser.getId() > 0 );
	}

}
