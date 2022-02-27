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

import java.util.Map;
import java.util.HashMap;
import org.springframework.http.HttpHeaders;
import org.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import org.springframework.http.HttpEntity;
import com.fasterxml.jackson.databind.JsonNode;
import org.json.JSONArray;
import com.example.accessingdatamysql.AccessingDataMysqlApplication;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


//@ExtendWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(AccessingDataMysqlApplication.class)
//@ExtendWith(SpringExtension.class)
//@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class MainControllerTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	public void testDemoAdd() throws Exception {
		//this.port = 8080;

        	HttpHeaders headers = new HttpHeaders();

		String createPersonUrl = "http://localhost:" + this.port + "/demo/add";

        	headers.set("X-COM-PERSIST", "true"); 
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		JSONObject personJsonObject = new JSONObject();

		personJsonObject.put("name", "Jonny B");
		personJsonObject.put("email", "jonnyb@isAlive.com");

		HttpEntity<String> request = new HttpEntity<String>(personJsonObject.toString(), headers);
    		ResponseEntity<String> responseEntityStr = restTemplate.postForEntity(createPersonUrl, request, String.class);

		Map<String,String> result = objectMapper.readValue(responseEntityStr.getBody(), HashMap.class);

                System.out.println("\nADD NEW USER:");
                System.out.println("============================");
                System.out.println("Body: " + responseEntityStr.getBody());
                System.out.println("Saved Path: " + result.get("Saved"));
                System.out.println("ID Path: " + result.get("ID"));
                System.out.println("============================");

                //test that the json response came in with expected values
                assertEquals(HttpStatus.OK, responseEntityStr.getStatusCode());
                assertNotNull(responseEntityStr.getBody());
                assertNotNull(result.get("Saved"));
                assertEquals(result.get("Saved"), "true");
                assertTrue(new Integer(result.get("ID")).intValue() > 0 );

    		System.out.println("testDemoAdd EndPoint: " + responseEntityStr);
		System.out.println("===============================\n");

		//Test updating the user and removing without creating extra Test/Startup runs
		this.testDemoUpdate(personJsonObject, "Jackson Cat");
		this.testDemoDelete(personJsonObject);
	}

	public void testDemoUpdate(JSONObject pJsonObject, String pNewName) throws Exception {
		//this.port = 8080;

        	HttpHeaders headers = new HttpHeaders();

		String createPersonUrl = "http://localhost:" + this.port + "/demo/update";

        	headers.set("X-COM-PERSIST", "true"); 
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		JSONObject updateJsonObject = new JSONObject();
		updateJsonObject.put("userName", pNewName);
		updateJsonObject.put("oldName", pJsonObject.get("name"));
		updateJsonObject.put("userEmail", pJsonObject.get("email"));

		HttpEntity<String> request = new HttpEntity<String>(updateJsonObject.toString(), headers);
    		ResponseEntity<String> responseEntityStr = restTemplate.postForEntity(createPersonUrl, request, String.class);

		Map<String,String> result = objectMapper.readValue(responseEntityStr.getBody(), HashMap.class);

                System.out.println("============================");
                System.out.println("Body: " + responseEntityStr.getBody());
                System.out.println("oldname Path: " + result.get("oldname"));
                System.out.println("newname Path: " + result.get("newname"));
                System.out.println("============================");

                //test that the json response came in with expected values
                assertEquals(HttpStatus.OK, responseEntityStr.getStatusCode());
                assertNotNull(responseEntityStr.getBody());
                assertNotNull(result.get("oldname"));
                assertEquals(result.get("oldname"), "Jonny B");
                assertEquals(result.get("newname"), "Jackson Cat");

    		System.out.println("testDemoUpdate EndPoint: " + responseEntityStr);
		System.out.println("===============================\n");

		//change json object name to new name
		pJsonObject.remove("name");
		pJsonObject.put("name", pNewName);
		pJsonObject.put("email", updateJsonObject.get("userEmail"));
	}


	public void testDemoDelete(JSONObject pUserJsonObject) throws Exception {
		//this.port = 8080;

        	HttpHeaders headers = new HttpHeaders();

		String deletePersonUrl = "http://localhost:" + this.port + "/demo/delete";

        	headers.set("X-COM-PERSIST", "true"); 
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> request = new HttpEntity<String>(pUserJsonObject.toString(), headers);
    		ResponseEntity<String> responseEntityStr = restTemplate.postForEntity(deletePersonUrl, request, String.class);

		Map<String,String> result = objectMapper.readValue(responseEntityStr.getBody(), HashMap.class);

                System.out.println("============================");
                System.out.println("Body: " + responseEntityStr.getBody());
                System.out.println("DeleteCount Path: " + new Integer(result.get("DeleteCount")).intValue());
                System.out.println("============================");

                //test that the json response came in with expected values
                assertEquals(HttpStatus.OK, responseEntityStr.getStatusCode());
                assertNotNull(responseEntityStr.getBody());
                assertNotNull(result.get("DeleteCount"));
                assertTrue(new Integer(result.get("DeleteCount")).intValue() > 0 );

    		System.out.println("testDemoDelete Endpoint: " + responseEntityStr);
		System.out.println("===============================\n");
	}

	@Test
	public void testDemoAll() throws Exception {
		//this.port = 8080;
		ResponseEntity<String> entity = restTemplate
				.getForEntity("http://localhost:" + this.port + "/demo/all", String.class);

                System.out.println("============================");
                System.out.println("Body: " + entity.getBody());
                System.out.println("============================");

		JSONArray jsonarray = new JSONArray(entity.getBody());
		for (int i = 0; i < jsonarray.length(); i++) {
			JSONObject jsonobject = jsonarray.getJSONObject(i);
			String name = jsonobject.getString("name");
			String email = jsonobject.getString("email");
		}

                //test that the json response came in with expected values
                assertEquals(HttpStatus.OK, entity.getStatusCode());
                assertNotNull(entity.getBody());
                assertTrue(jsonarray.length() > 0 );

    		System.out.println("<html><head>demo all endpoint</head><body>" + "JSONARRAY" + "</body></html>");
		System.out.println("===============================\n");
	}
}
