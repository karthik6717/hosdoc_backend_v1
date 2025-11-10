# Architecture Pattern - JWT Validation

## Pattern Analysis from employeewellness Services

### employeewellness Pattern:
1. **Gateway (employeewellness-gateway)**:
   - Has `JwtValidationFilter` that validates JWT by calling auth service `/validate` endpoint
   - Public endpoints (login, register) are excluded from validation
   - Protected endpoints require valid JWT token
   - Routes requests to backend services

2. **Backend (employeewellness-backend)**:
   - No Spring Security JWT validation
   - Trusts requests coming from gateway
   - No `@PreAuthorize` annotations

3. **Auth Service (employeewellness-Auth)**:
   - Provides `/validate` endpoint for JWT validation
   - Generates JWT tokens with HS512 algorithm

### hosdoc Pattern (Current Issue):
1. **Gateway (hosdoc_gateway)**:
   - ✅ Now has `JwtValidationFilter` (just created)
   - ✅ Validates JWT by calling auth service `/validate` endpoint
   - ✅ Public endpoints excluded

2. **Backend (hosdoc_backend)**:
   - ❌ Trying to validate JWT using Spring Security OAuth2 Resource Server
   - ❌ JWT decoder failing with "JWT was invalid"
   - ✅ Has `@PreAuthorize` annotations for role-based access

## Solution Options

### Option 1: Remove Backend JWT Validation (Like employeewellness)
**Pros:**
- Simpler architecture
- Gateway handles all JWT validation
- Matches employeewellness pattern

**Cons:**
- Less secure (backend trusts gateway)
- If gateway is bypassed, backend is unprotected

**Implementation:**
- Remove Spring Security OAuth2 Resource Server from backend
- Remove JWT decoder configuration
- Keep `@PreAuthorize` but they won't work (or remove them)

### Option 2: Fix Backend JWT Validation (Recommended)
**Pros:**
- Defense in depth (both gateway and backend validate)
- More secure
- `@PreAuthorize` annotations work properly

**Cons:**
- More complex
- Need to fix JWT decoder issue

**Implementation:**
- Fix JWT decoder to properly validate HS512 tokens
- Keep Spring Security OAuth2 Resource Server
- Keep `@PreAuthorize` annotations

### Option 3: Hybrid Approach
**Pros:**
- Gateway validates for routing
- Backend validates for authorization
- Best security

**Cons:**
- Most complex
- Need to fix JWT decoder

**Implementation:**
- Gateway validates JWT (already done)
- Backend validates JWT and extracts roles for `@PreAuthorize`
- Fix JWT decoder issue

## Recommended: Option 2 or 3

Since we already have `@PreAuthorize` annotations in the backend, we should fix the JWT decoder to make them work.

The issue is likely:
1. Secret key mismatch
2. Algorithm detection issue
3. Key format issue

Let's fix the JWT decoder configuration.

