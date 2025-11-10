# JWT Decoder Fix - Final Solution

## Current Issue
JWT decoder is rejecting tokens with "Failed to authenticate since the JWT was invalid"

## Root Cause
The `NimbusJwtDecoder.withSecretKey()` should auto-detect HS512 from JWT header, but there might be an issue with:
1. Secret key format/encoding
2. Algorithm detection
3. Key length requirements

## Solution: Use Same Key Creation Method as Auth Service

The auth service uses `Keys.hmacShaKeyFor(keyBytes)` which creates a key compatible with any HMAC algorithm. Let's ensure the backend uses the same approach.

### Current Configuration (Backend):
```java
byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
SecretKey secretKey = new SecretKeySpec(keyBytes, "HmacSHA512");
return NimbusJwtDecoder.withSecretKey(secretKey).build();
```

### Auth Service Configuration:
```java
byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
return Keys.hmacShaKeyFor(keyBytes);  // Creates key for any HMAC algorithm
```

## Verification Steps

1. **Verify Secret Key Match**:
   - Auth: `hosdoc_auth/src/main/java/com/kalex/hosdoc_auth/service/JwtService.java` line 20
   - Backend: `hosdoc_backend/src/main/resources/application.properties` line 31
   - Both must be exactly: `404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970`

2. **Test Token Validation**:
   - Use test endpoint: `GET /api/v1/test/jwt` with Bearer token
   - Check if JWT is validated correctly

3. **Check Logs**:
   - Look for detailed error messages
   - Verify secret key is being read correctly

## Alternative: Use Gateway-Only Validation

If JWT decoder continues to fail, we can:
1. Remove Spring Security OAuth2 Resource Server from backend
2. Trust gateway for JWT validation
3. Remove `@PreAuthorize` annotations (or make them optional)

This matches the employeewellness pattern but is less secure.

