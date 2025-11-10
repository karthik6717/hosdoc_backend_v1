# HosDoc Backend - All CURL Commands

Base URL: `http://localhost:8082/api/v1`

**Note:** Replace `YOUR_JWT_TOKEN` with the actual JWT token obtained from the auth service login.

---

## Authentication

All authenticated endpoints require JWT token in the header:
```
Authorization: Bearer YOUR_JWT_TOKEN
```

**⚠️ IMPORTANT:** Always include "Bearer " (with space) before the token!

❌ **Wrong:** `Authorization: YOUR_TOKEN`
✅ **Correct:** `Authorization: Bearer YOUR_TOKEN`

Get token by logging in to auth service:
```bash
curl --location --request POST 'http://localhost:8081/api/auth/login?username=YOUR_USERNAME&password=YOUR_PASSWORD'
```

---

## Public Endpoints (No Auth Required)

### 1. Get All Specialties

**Endpoint:** `GET /api/v1/specialties`

```bash
curl --location --request GET 'http://localhost:8082/api/v1/specialties'
```

**Response:**
```json
[
    {
        "id": 1,
        "code": "CARD",
        "name": "Cardiology",
        "description": "Heart related care",
        "iconUrl": null,
        "createdAt": "2025-01-15T10:00:00"
    }
]
```

---

### 2. Get Specialty By ID

**Endpoint:** `GET /api/v1/specialties/{id}`

```bash
curl --location --request GET 'http://localhost:8082/api/v1/specialties/1'
```

---

### 3. Get Doctors By Specialty

**Endpoint:** `GET /api/v1/specialties/{id}/doctors`

```bash
curl --location --request GET 'http://localhost:8082/api/v1/specialties/1/doctors'
```

**Response:**
```json
[
    {
        "doctorId": 1,
        "name": "Dr. Rajesh Kumar",
        "specializations": ["Cardiology"],
        "profileImage": "https://example.com/doctors/dr-rajesh.jpg",
        "yearsOfExperience": 15,
        "languages": ["English", "Hindi"],
        "fee": 500.00,
        "qualificationSummary": "MBBS, MD Cardiology",
        "nextAvailable": null
    }
]
```

---

### 4. Search Doctors

**Endpoint:** `GET /api/v1/doctors`

**Query Parameters:**
- `specialtyId` (optional): Filter by specialty ID
- `name` (optional): Search by doctor name

```bash
# Get all doctors
curl --location --request GET 'http://localhost:8082/api/v1/doctors'

# Search by specialty
curl --location --request GET 'http://localhost:8082/api/v1/doctors?specialtyId=1'

# Search by name
curl --location --request GET 'http://localhost:8082/api/v1/doctors?name=Rajesh'

# Combined search
curl --location --request GET 'http://localhost:8082/api/v1/doctors?specialtyId=1&name=Rajesh'
```

---

### 5. Get Doctor Details

**Endpoint:** `GET /api/v1/doctors/{doctorId}`

```bash
curl --location --request GET 'http://localhost:8082/api/v1/doctors/1'
```

**Response:**
```json
{
    "doctorId": 1,
    "name": "Dr. Rajesh Kumar",
    "about": "Senior Cardiologist with 15 years of experience",
    "specializations": ["Cardiology"],
    "qualifications": [
        {
            "degree": "MBBS",
            "institution": "AIIMS Delhi",
            "year": 2008
        },
        {
            "degree": "MD Cardiology",
            "institution": "AIIMS Delhi",
            "year": 2012
        }
    ],
    "yearsOfExperience": 15,
    "languages": ["English", "Hindi"],
    "fee": 500.00,
    "profileImageUrl": "https://example.com/doctors/dr-rajesh.jpg",
    "rating": 4.8
}
```

---

### 6. Get Doctor Availability

**Endpoint:** `GET /api/v1/doctors/{doctorId}/availability`

**Query Parameters:**
- `from` (required): Start date (YYYY-MM-DD)
- `to` (optional): End date (YYYY-MM-DD), defaults to 7 days from `from`

```bash
curl --location --request GET 'http://localhost:8082/api/v1/doctors/1/availability?from=2025-01-15&to=2025-01-22'
```

**Response:**
```json
{
    "date": "2025-01-15",
    "shifts": [
        {
            "shift": "MORNING",
            "startTime": "09:00:00",
            "endTime": "12:00:00",
            "availableSlots": [
                {
                    "slotTime": "09:00:00",
                    "available": true
                },
                {
                    "slotTime": "09:30:00",
                    "available": true
                }
            ]
        }
    ]
}
```

---

## Authenticated Endpoints (JWT Required)

### 7. Get My Members

**Endpoint:** `GET /api/v1/patients/me/members`

**Direct Access (Backend):**
```bash
curl --location --request GET 'http://localhost:8082/api/v1/patients/me/members' \
--header 'Authorization: Bearer YOUR_JWT_TOKEN'
```

**Through Gateway:**
```bash
curl --location --request GET 'http://localhost:8080/api/v1/patients/me/members' \
--header 'Authorization: Bearer YOUR_JWT_TOKEN'
```

**Note:** Replace `YOUR_JWT_TOKEN` with actual token. Make sure to include "Bearer " prefix!

**Response:**
```json
[
    {
        "memberId": 1,
        "name": "Karthik G",
        "relation": "SELF",
        "gender": "MALE",
        "dob": "1990-01-15",
        "age": 35,
        "avatarUrl": null
    }
]
```

---

### 8. Create Member

**Endpoint:** `POST /api/v1/patients/me/members`

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

**Response (201):**
```json
{
    "memberId": 2,
    "name": "Priya K",
    "relation": "SPOUSE",
    "gender": "FEMALE",
    "dob": "1992-05-20",
    "age": 32,
    "avatarUrl": null
}
```

---

### 9. Update Member

**Endpoint:** `PUT /api/v1/patients/me/members/{memberId}`

```bash
curl --location --request PUT 'http://localhost:8082/api/v1/patients/me/members/1' \
--header 'Authorization: Bearer YOUR_JWT_TOKEN' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "Karthik G Updated",
    "gender": "MALE",
    "dob": "1990-01-15",
    "relation": "SELF",
    "mobile": "9876543210",
    "ackDetails": true
}'
```

---

### 10. Delete Member

**Endpoint:** `DELETE /api/v1/patients/me/members/{memberId}`

```bash
curl --location --request DELETE 'http://localhost:8082/api/v1/patients/me/members/2' \
--header 'Authorization: Bearer YOUR_JWT_TOKEN'
```

**Response:** `204 No Content`

---

### 11. Get Member By ID

**Endpoint:** `GET /api/v1/patients/me/members/{memberId}`

```bash
curl --location --request GET 'http://localhost:8082/api/v1/patients/me/members/1' \
--header 'Authorization: Bearer YOUR_JWT_TOKEN'
```

---

### 12. Book Appointment

**Endpoint:** `POST /api/v1/appointments`

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

**Response (201):**
```json
{
    "appointmentUid": "HOS-20250115-000001",
    "status": "CONFIRMED",
    "doctorId": 1,
    "patientId": 1,
    "date": "2025-01-15",
    "shift": "MORNING",
    "slot": "09:00:00",
    "fee": 500.00,
    "paymentStatus": "PENDING"
}
```

---

### 13. Get Appointment By UID

**Endpoint:** `GET /api/v1/appointments/{appointmentUid}`

```bash
curl --location --request GET 'http://localhost:8082/api/v1/appointments/HOS-20250115-000001' \
--header 'Authorization: Bearer YOUR_JWT_TOKEN'
```

**Response:**
```json
{
    "appointmentUid": "HOS-20250115-000001",
    "doctorId": 1,
    "doctorName": "Dr. Rajesh Kumar",
    "doctorImage": "https://example.com/doctors/dr-rajesh.jpg",
    "patientId": 1,
    "patientName": "Karthik G",
    "date": "2025-01-15",
    "shift": "MORNING",
    "slotTime": "09:00:00",
    "durationMinutes": 30,
    "fee": 500.00,
    "paymentMode": "ONLINE",
    "paymentStatus": "PENDING",
    "status": "CONFIRMED",
    "notes": "Regular checkup",
    "createdAt": "2025-01-15T10:30:00"
}
```

---

### 14. Get My Appointments

**Endpoint:** `GET /api/v1/patients/me/appointments`

**Query Parameters:**
- `status` (optional): Filter by status (CONFIRMED, CANCELLED, COMPLETED, NO_SHOW)

```bash
# Get all appointments
curl --location --request GET 'http://localhost:8082/api/v1/patients/me/appointments' \
--header 'Authorization: Bearer YOUR_JWT_TOKEN'

# Get only confirmed appointments
curl --location --request GET 'http://localhost:8082/api/v1/patients/me/appointments?status=CONFIRMED' \
--header 'Authorization: Bearer YOUR_JWT_TOKEN'
```

**Response:**
```json
[
    {
        "appointmentUid": "HOS-20250115-000001",
        "doctorId": 1,
        "doctorName": "Dr. Rajesh Kumar",
        "date": "2025-01-15",
        "shift": "MORNING",
        "slotTime": "09:00:00",
        "fee": 500.00,
        "status": "CONFIRMED"
    }
]
```

---

### 15. Cancel Appointment

**Endpoint:** `PUT /api/v1/appointments/{appointmentUid}/cancel`

```bash
curl --location --request PUT 'http://localhost:8082/api/v1/appointments/HOS-20250115-000001/cancel' \
--header 'Authorization: Bearer YOUR_JWT_TOKEN'
```

**Response:**
```json
{
    "appointmentUid": "HOS-20250115-000001",
    "status": "CANCELLED",
    ...
}
```

---

### 16. Reschedule Appointment

**Endpoint:** `PUT /api/v1/appointments/{appointmentUid}/reschedule`

```bash
curl --location --request PUT 'http://localhost:8082/api/v1/appointments/HOS-20250115-000001/reschedule' \
--header 'Authorization: Bearer YOUR_JWT_TOKEN' \
--header 'Content-Type: application/json' \
--data-raw '{
    "doctorId": 1,
    "patientMemberId": 1,
    "date": "2025-01-16",
    "shift": "AFTERNOON",
    "slot": "14:00:00",
    "paymentMode": "ONLINE",
    "notes": "Rescheduled appointment"
}'
```

---

### 17. Pay Appointment

**Endpoint:** `POST /api/v1/appointments/{appointmentUid}/pay`

```bash
curl --location --request POST 'http://localhost:8082/api/v1/appointments/HOS-20250115-000001/pay' \
--header 'Authorization: Bearer YOUR_JWT_TOKEN'
```

**Note:** This endpoint is a stub and needs payment gateway integration.

---

### 18. Upload Report

**Endpoint:** `POST /api/v1/reports`

**Note:** This requires multipart/form-data

```bash
curl --location --request POST 'http://localhost:8082/api/v1/reports' \
--header 'Authorization: Bearer YOUR_JWT_TOKEN' \
--form 'file=@"/path/to/report.pdf"' \
--form 'request="{\"name\":\"Blood Test Report\",\"description\":\"Complete blood count\",\"patientId\":1}"'
```

**Response (201):**
```json
{
    "reportId": 1,
    "name": "Blood Test Report",
    "description": "Complete blood count",
    "fileUrl": "./uploads/reports/uuid-filename.pdf",
    "uploadedAt": "2025-01-15T10:30:00",
    "patientId": 1,
    "patientName": "Karthik G"
}
```

---

### 19. Get My Reports

**Endpoint:** `GET /api/v1/patients/me/reports`

```bash
curl --location --request GET 'http://localhost:8082/api/v1/patients/me/reports' \
--header 'Authorization: Bearer YOUR_JWT_TOKEN'
```

**Response:**
```json
[
    {
        "reportId": 1,
        "name": "Blood Test Report",
        "description": "Complete blood count",
        "fileUrl": "./uploads/reports/uuid-filename.pdf",
        "uploadedAt": "2025-01-15T10:30:00",
        "patientId": 1,
        "patientName": "Karthik G"
    }
]
```

---

### 20. Get Report By ID

**Endpoint:** `GET /api/v1/reports/{reportId}`

```bash
curl --location --request GET 'http://localhost:8082/api/v1/reports/1' \
--header 'Authorization: Bearer YOUR_JWT_TOKEN'
```

---

## Complete Workflow Example

### Step 1: Login to Auth Service
```bash
TOKEN=$(curl -s --location --request POST 'http://localhost:8081/api/auth/login?username=testuser&password=test123' | tr -d '"')
echo "Token: $TOKEN"
```

### Step 2: Get All Specialties
```bash
curl --location --request GET 'http://localhost:8082/api/v1/specialties'
```

### Step 3: Get Doctors by Specialty
```bash
curl --location --request GET 'http://localhost:8082/api/v1/specialties/1/doctors'
```

### Step 4: Get Doctor Details
```bash
curl --location --request GET 'http://localhost:8082/api/v1/doctors/1'
```

### Step 5: Get Doctor Availability
```bash
curl --location --request GET 'http://localhost:8082/api/v1/doctors/1/availability?from=2025-01-15'
```

### Step 6: Create a Member
```bash
curl --location --request POST 'http://localhost:8082/api/v1/patients/me/members' \
--header "Authorization: Bearer $TOKEN" \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "Test User",
    "gender": "MALE",
    "dob": "1990-01-15",
    "relation": "SELF",
    "mobile": "9876543210",
    "ackDetails": true
}'
```

### Step 7: Book Appointment
```bash
curl --location --request POST 'http://localhost:8082/api/v1/appointments' \
--header "Authorization: Bearer $TOKEN" \
--header 'Content-Type: application/json' \
--data-raw '{
    "doctorId": 1,
    "patientMemberId": 1,
    "date": "2025-01-15",
    "shift": "MORNING",
    "slot": "09:00:00",
    "paymentMode": "ONLINE",
    "notes": "First consultation"
}'
```

### Step 8: Get My Appointments
```bash
curl --location --request GET 'http://localhost:8082/api/v1/patients/me/appointments' \
--header "Authorization: Bearer $TOKEN"
```

---

## Enums Reference

### Gender
- `MALE`
- `FEMALE`
- `OTHER`

### Relation
- `SELF`
- `SPOUSE`
- `PARENT`
- `CHILD`
- `OTHER`

### Shift
- `MORNING`
- `AFTERNOON`
- `EVENING`
- `NIGHT`

### PaymentMode
- `ONLINE`
- `PAY_AT_HOSPITAL`

### AppointmentStatus
- `CONFIRMED`
- `CANCELLED`
- `COMPLETED`
- `NO_SHOW`

---

## Error Responses

All errors follow this format:
```json
{
    "timestamp": "2025-01-15T10:30:00.000+00:00",
    "status": 400,
    "error": "Bad Request",
    "message": "Validation failed: member name required",
    "path": "/api/v1/patients/me/members"
}
```

Common HTTP Status Codes:
- `200` - Success
- `201` - Created
- `204` - No Content
- `400` - Bad Request
- `401` - Unauthorized
- `403` - Forbidden
- `404` - Not Found
- `409` - Conflict
- `500` - Internal Server Error

---

## Notes

- All dates should be in `YYYY-MM-DD` format
- All times should be in `HH:mm:ss` format (24-hour)
- JWT tokens expire after the time set in auth service (default: 1 hour)
- File uploads are limited to 10MB by default
- Appointment booking uses pessimistic locking to prevent double-booking

