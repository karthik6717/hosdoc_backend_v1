# HosDoc Backend - CURL Commands (Fixed)

## Important: Authorization Header Format

**Always include "Bearer " prefix before the token!**

❌ **Wrong:**
```bash
--header 'Authorization: eyJhbGciOiJIUzUxMiJ9...'
```

✅ **Correct:**
```bash
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...'
```

---

## Access Methods

### Method 1: Direct Access (Backend Service)

**Base URL:** `http://localhost:8082/api/v1`

**Example:**
```bash
curl --location --request GET 'http://localhost:8082/api/v1/patients/me/members' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiRE9DVE9SIiwidXNlcklkIjoxLCJzdWIiOiJna2FydGhpa0BnbWFpbC5jb20iLCJpYXQiOjE3NjI2OTA0MDksImV4cCI6MTc2MjY5NDAwOX0.mLAfK-O-2JgA7cgbKDylVoIBGVb1HoFlj1yrpR6Sx0h9oDCHSxA-r7AS4Fp6485ELf3K-Wy6cffUj47fzkC6Bg'
```

### Method 2: Through Gateway (Recommended)

**Base URL:** `http://localhost:8080/api/v1`

**Example:**
```bash
curl --location --request GET 'http://localhost:8080/api/v1/patients/me/members' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiRE9DVE9SIiwidXNlcklkIjoxLCJzdWIiOiJna2FydGhpa0BnbWFpbC5jb20iLCJpYXQiOjE3NjI2OTA0MDksImV4cCI6MTc2MjY5NDAwOX0.mLAfK-O-2JgA7cgbKDylVoIBGVb1HoFlj1yrpR6Sx0h9oDCHSxA-r7AS4Fp6485ELf3K-Wy6cffUj47fzkC6Bg'
```

---

## Fixed CURL Commands

### Get My Members (Fixed)

**Direct Access:**
```bash
curl --location --request GET 'http://localhost:8082/api/v1/patients/me/members' \
--header 'Authorization: Bearer YOUR_JWT_TOKEN'
```

**Through Gateway:**
```bash
curl --location --request GET 'http://localhost:8080/api/v1/patients/me/members' \
--header 'Authorization: Bearer YOUR_JWT_TOKEN'
```

### Create Member (Fixed)

**Direct Access:**
```bash
curl --location --request POST 'http://localhost:8082/api/v1/patients/me/members' \
--header 'Authorization: Bearer YOUR_JWT_TOKEN' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "Priya K",
    "gender": "FEMALE",
    "dob": "1992-05-20",
    "relation": "SPOUSE",
    "mobile": "9876543211",
    "ackDetails": true
}'
```

**Through Gateway:**
```bash
curl --location --request POST 'http://localhost:8080/api/v1/patients/me/members' \
--header 'Authorization: Bearer YOUR_JWT_TOKEN' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "Priya K",
    "gender": "FEMALE",
    "dob": "1992-05-20",
    "relation": "SPOUSE",
    "mobile": "9876543211",
    "ackDetails": true
}'
```

### Book Appointment (Fixed)

**Direct Access:**
```bash
curl --location --request POST 'http://localhost:8082/api/v1/appointments' \
--header 'Authorization: Bearer YOUR_JWT_TOKEN' \
--header 'Content-Type: application/json' \
--data-raw '{
    "doctorId": 1,
    "patientMemberId": 1,
    "date": "2025-01-15",
    "shift": "MORNING",
    "slot": "09:00:00",
    "paymentMode": "ONLINE",
    "notes": "Regular checkup"
}'
```

**Through Gateway:**
```bash
curl --location --request POST 'http://localhost:8080/api/v1/appointments' \
--header 'Authorization: Bearer YOUR_JWT_TOKEN' \
--header 'Content-Type: application/json' \
--data-raw '{
    "doctorId": 1,
    "patientMemberId": 1,
    "date": "2025-01-15",
    "shift": "MORNING",
    "slot": "09:00:00",
    "paymentMode": "ONLINE",
    "notes": "Regular checkup"
}'
```

---

## Quick Test Script

Save this as `test_members.sh`:

```bash
#!/bin/bash

# Get token first
TOKEN=$(curl -s --location --request POST 'http://localhost:8081/api/auth/login?username=raj@gmail.com&password=YOUR_PASSWORD' | tr -d '"')

echo "Token: $TOKEN"
echo ""

# Test direct access
echo "=== Testing Direct Access ==="
curl --location --request GET 'http://localhost:8082/api/v1/patients/me/members' \
--header "Authorization: Bearer $TOKEN"

echo ""
echo ""

# Test gateway access
echo "=== Testing Gateway Access ==="
curl --location --request GET 'http://localhost:8080/api/v1/patients/me/members' \
--header "Authorization: Bearer $TOKEN"
```

---

## Common Issues

### Issue 1: Missing "Bearer " Prefix

**Error:** `401 Unauthorized` or `403 Forbidden`

**Solution:** Always include "Bearer " before the token:
```bash
--header 'Authorization: Bearer YOUR_TOKEN'
```

### Issue 2: Token Expired

**Error:** `401 Unauthorized`

**Solution:** Login again to get a new token:
```bash
curl --location --request POST 'http://localhost:8081/api/auth/login?username=YOUR_USERNAME&password=YOUR_PASSWORD'
```

### Issue 3: Wrong User Role

**Error:** `403 Forbidden` when accessing patient endpoints with DOCTOR token

**Solution:** Use a token from a user with role "USER" or "PATIENT"

### Issue 4: Gateway Not Running

**Error:** Connection refused on port 8080

**Solution:** Start the gateway service or use direct access (port 8082)

---

## Port Summary

- **Gateway:** `8080` (routes to backend and auth)
- **Auth Service:** `8081` (direct access)
- **Backend Service:** `8082` (direct access)
- **Eureka:** `8761`

---

## Your Specific Command (Fixed)

**Original (Wrong):**
```bash
curl --location 'http://localhost:8082/api/v1/patients/me/members' \
--header 'Authorization: eyJhbGciOiJIUzUxMiJ9...'
```

**Fixed:**
```bash
curl --location --request GET 'http://localhost:8082/api/v1/patients/me/members' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiRE9DVE9SIiwidXNlcklkIjoxLCJzdWIiOiJna2FydGhpa0BnbWFpbC5jb20iLCJpYXQiOjE3NjI2OTA0MDksImV4cCI6MTc2MjY5NDAwOX0.mLAfK-O-2JgA7cgbKDylVoIBGVb1HoFlj1yrpR6Sx0h9oDCHSxA-r7AS4Fp6485ELf3K-Wy6cffUj47fzkC6Bg'
```

**Note:** Your token shows role "DOCTOR" but you're accessing a patient endpoint. You might need a token from a user with role "USER" or "PATIENT".

