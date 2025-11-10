package com.kalex.hosdoc_backend.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Base64;

/**
 * Utility class to extract user information from JWT token in Authorization header
 * Gateway handles JWT validation, backend extracts claims from token
 */
@Component
public class JwtUtil {
	
	@Autowired
	private ObjectMapper objectMapper;
	
	/**
	 * Extract JWT token from Authorization header
	 */
	private String extractToken() {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attributes != null) {
			HttpServletRequest request = attributes.getRequest();
			String authHeader = request.getHeader("Authorization");
			if (authHeader != null && authHeader.startsWith("Bearer ")) {
				return authHeader.substring(7);
			}
		}
		return null;
	}
	
	/**
	 * Decode JWT payload (without verification - gateway already validated)
	 */
	private JsonNode decodeJwtPayload(String token) {
		try {
			String[] parts = token.split("\\.");
			if (parts.length == 3) {
				String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
				return objectMapper.readTree(payload);
			}
		} catch (Exception e) {
			// Log error if needed
		}
		return null;
	}
	
	/**
	 * Get current user ID from JWT token
	 */
	public String getCurrentUserId() {
		String token = extractToken();
		if (token != null) {
			JsonNode payload = decodeJwtPayload(token);
			if (payload != null) {
				// Try userId claim first
				if (payload.has("userId")) {
					return payload.get("userId").asText();
				}
				// Fallback to sub (username)
				if (payload.has("sub")) {
					return payload.get("sub").asText();
				}
			}
		}
		return null;
	}
	
	/**
	 * Get current user ID as Integer from JWT token
	 */
	public Integer getCurrentUserIdAsInteger() {
		String token = extractToken();
		if (token != null) {
			JsonNode payload = decodeJwtPayload(token);
			if (payload != null && payload.has("userId")) {
				try {
					return payload.get("userId").asInt();
				} catch (Exception e) {
					// Try as text and parse
					try {
						return Integer.parseInt(payload.get("userId").asText());
					} catch (Exception ex) {
						// Ignore
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Get current user email from JWT token
	 */
	public String getCurrentUserEmail() {
		String token = extractToken();
		if (token != null) {
			JsonNode payload = decodeJwtPayload(token);
			if (payload != null && payload.has("email")) {
				return payload.get("email").asText();
			}
			// Fallback to sub (username) if email not present
			if (payload.has("sub")) {
				return payload.get("sub").asText();
			}
		}
		return null;
	}
	
	/**
	 * Check if user has a specific role
	 */
	public boolean hasRole(String role) {
		String token = extractToken();
		if (token != null) {
			JsonNode payload = decodeJwtPayload(token);
			if (payload != null && payload.has("role")) {
				String userRole = payload.get("role").asText();
				return role.equals(userRole) || role.equals("ROLE_" + userRole);
			}
		}
		return false;
	}
}

