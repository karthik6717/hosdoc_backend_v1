# JWT Debugging Guide

## Issue: 401 Unauthorized when accessing `/api/v1/patients/me/members`

### Possible Causes:

1. **JWT Secret Key Mismatch**
   - Verify that the secret key in `hosdoc_backend/src/main/resources/application.properties` matches exactly with `hosdoc_auth/src/main/java/com/kalex/hosdoc_auth/service/JwtService.java`
   - Both should use: `404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970`

2. **Token Expiration**
   - Check if the token has expired by decoding it at https://jwt.io
   - The token should have a valid `exp` claim

3. **Algorithm Mismatch**
   - The token uses HS512 algorithm
   - The backend should automatically detect this from the JWT header

4. **Role Extraction**
   - The token contains `"role":"PATIENT"`
   - The converter should map this to `ROLE_PATIENT`
   - The endpoint allows `hasAnyRole('USER', 'PATIENT', 'DOCTOR', 'ADMIN')`

### Debugging Steps:

1. **Enable Debug Logging** (already added to application.properties):
   ```
   logging.level.org.springframework.security=DEBUG
   logging.level.org.springframework.security.oauth2=DEBUG
   ```

2. **Check Backend Logs** when making the request:
   - Look for JWT validation errors
   - Check for role extraction issues
   - Verify authentication success/failure

3. **Verify Token Claims**:
   - Decode the token at https://jwt.io
   - Verify:
     - `role`: "PATIENT"
     - `userId`: 5
     - `sub`: "ramu@gmail.com"
     - `exp`: future timestamp

4. **Test with a Fresh Token**:
   - Get a new token from the auth service
   - Make sure it's not expired

5. **Verify Secret Key**:
   - Ensure both services use the exact same secret key
   - No extra spaces or newlines

### Test Token Decoding:

Your token header decodes to:
```json
{
  "alg": "HS512"
}
```

Your token payload should decode to:
```json
{
  "role": "PATIENT",
  "userId": 5,
  "sub": "ramu@gmail.com",
  "iat": 1762692633,
  "exp": 1762696233
}
```

### Next Steps:

1. Restart the backend service after configuration changes
2. Check the backend logs for detailed error messages
3. Verify the secret key matches exactly between services
4. Try with a fresh token from the auth service

