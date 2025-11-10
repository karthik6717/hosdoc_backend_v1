# JWT Validation Fix - 401 Error

## Problem
Getting "Failed to authenticate since the JWT was invalid" error when accessing protected endpoints.

## Root Cause Analysis

The JWT decoder (`NimbusJwtDecoder`) is rejecting the token. Possible causes:

1. **Secret Key Mismatch**: The secret key in backend doesn't match auth service
2. **Algorithm Detection Issue**: NimbusJwtDecoder might not be detecting HS512 correctly
3. **Key Format Issue**: The way the key is created might not match what the auth service uses

## Current Configuration

### Auth Service (hosdoc_auth)
- Secret Key: `404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970`
- Key Creation: `Keys.hmacShaKeyFor(keyBytes)` - creates a key compatible with any HMAC algorithm
- Algorithm: HS512 (auto-detected by jjwt library)

### Backend Service (hosdoc_backend)
- Secret Key: `404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970` (should match)
- Key Creation: `new SecretKeySpec(keyBytes, "HmacSHA512")`
- Decoder: `NimbusJwtDecoder.withSecretKey(secretKey).build()`

## Verification Steps

1. **Verify Secret Key Match**:
   - Check `hosdoc_auth/src/main/java/com/kalex/hosdoc_auth/service/JwtService.java` line 20
   - Check `hosdoc_backend/src/main/resources/application.properties` line 31
   - Both should be exactly: `404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970`

2. **Check Token Validity**:
   - Decode token at https://jwt.io
   - Verify algorithm is HS512
   - Verify secret key matches
   - Check expiration time

3. **Enable Debug Logging**:
   - Already enabled in `application.properties`:
     ```
     logging.level.org.springframework.security=DEBUG
     logging.level.org.springframework.security.oauth2=DEBUG
     ```

4. **Check Backend Logs**:
   - Look for detailed JWT validation errors
   - Check for algorithm mismatch errors
   - Verify secret key is being read correctly

## Solution

The configuration should work as-is. If still getting 401:

1. **Restart backend service** after configuration changes
2. **Verify secret key** matches exactly (no spaces, newlines, or encoding issues)
3. **Try with a fresh token** from auth service
4. **Check backend logs** for specific error messages

## Test Endpoint

Use the test endpoint to verify JWT validation:
```bash
curl --location 'http://localhost:8082/api/v1/test/jwt' \
--header 'Authorization: Bearer YOUR_TOKEN'
```

This will show if JWT validation is working and what claims are extracted.

