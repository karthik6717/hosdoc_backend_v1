# Troubleshooting 401 Unauthorized Error

## Current Issue
Getting 401 when accessing `/api/v1/patients/me/members` with a PATIENT token.

## Configuration Summary

### Auth Service (hosdoc_auth)
- Secret Key: `404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970`
- Algorithm: HS512 (auto-detected by jjwt library)
- Token contains: `role`, `userId`, `sub`, `iat`, `exp`

### Backend Service (hosdoc_backend)
- Secret Key: `404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970` (should match)
- JWT Decoder: `NimbusJwtDecoder.withSecretKey()` - auto-detects HS512 from JWT header
- Role Converter: Maps `role` claim to `ROLE_{role}` (e.g., PATIENT → ROLE_PATIENT)

## Verification Steps

1. **Verify Secret Key Match**
   ```bash
   # Check auth service secret
   grep -r "SECRET_KEY" hosdoc_auth/src/main/java/com/kalex/hosdoc_auth/service/JwtService.java
   
   # Check backend secret
   grep -r "jwt.secret" hosdoc_backend/src/main/resources/application.properties
   ```
   Both should be: `404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970`

2. **Check Token Validity**
   - Decode token at https://jwt.io
   - Verify `exp` claim is in the future
   - Verify `role` is "PATIENT"
   - Verify `userId` is 5

3. **Check Backend Logs**
   With debug logging enabled, you should see:
   - JWT validation attempts
   - Role extraction
   - Authentication success/failure

4. **Test Token**
   Your token decodes to:
   ```json
   {
     "role": "PATIENT",
     "userId": 5,
     "sub": "ramu@gmail.com",
     "iat": 1762692633,
     "exp": 1762696233
   }
   ```

## Common Issues

1. **Secret Key Mismatch**: Most common cause of 401
   - Solution: Ensure both services use identical secret key

2. **Token Expired**: Check `exp` claim
   - Solution: Get a fresh token from auth service

3. **Algorithm Mismatch**: Token uses HS512 but decoder expects different
   - Solution: `NimbusJwtDecoder.withSecretKey()` should auto-detect, but verify

4. **Role Not Extracted**: Role converter not working
   - Solution: Check logs for role extraction, verify converter is called

## Next Steps

1. Restart backend service
2. Check backend logs for detailed error messages
3. Verify secret key matches exactly (no spaces, newlines)
4. Try with a fresh token
5. Test with a simple endpoint first to verify JWT validation works

