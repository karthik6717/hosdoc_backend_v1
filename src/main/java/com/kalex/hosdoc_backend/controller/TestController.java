package com.kalex.hosdoc_backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/test")
@CrossOrigin(origins = "*")
public class TestController {
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@GetMapping("/jwt")
	public ResponseEntity<Map<String, Object>> testJwt(@RequestHeader(value = "Authorization", required = false) String authHeader) {
		Map<String, Object> response = new HashMap<>();
		
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			response.put("error", "No Authorization header");
			return ResponseEntity.ok(response);
		}
		
		String token = authHeader.substring(7);
		response.put("tokenReceived", true);
		response.put("tokenLength", token.length());
		
		// Decode JWT payload to show claims
		try {
			String[] parts = token.split("\\.");
			if (parts.length == 3) {
				String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
				JsonNode claims = objectMapper.readTree(payload);
				response.put("authenticated", true);
				response.put("subject", claims.has("sub") ? claims.get("sub").asText() : null);
				response.put("role", claims.has("role") ? claims.get("role").asText() : null);
				response.put("userId", claims.has("userId") ? claims.get("userId").asText() : null);
				response.put("claims", claims);
			}
		} catch (Exception e) {
			response.put("error", "Failed to decode token: " + e.getMessage());
		}
		
		return ResponseEntity.ok(response);
	}
}

