# User Relations Data Storage - Explanation

## Overview

The system stores user relations (family members) in the `patient` table. Each authenticated user can have multiple patient records representing themselves and their family members.

## Database Structure

### Patient Table Schema

```sql
CREATE TABLE patient (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,              -- FK to hosdoc_auth_user.id
    full_name VARCHAR(255),
    gender ENUM('MALE','FEMALE','OTHER'),
    dob DATE,
    relation ENUM('SELF','SPOUSE','PARENT','CHILD','OTHER'),  -- Relationship to account holder
    phone VARCHAR(20),
    avatar_url VARCHAR(1024),
    ack_details BOOLEAN DEFAULT FALSE,
    created_at DATETIME,
    updated_at DATETIME
);
```

## How It Works

### 1. Relationship Model

```
hosdoc_auth_user (1) ──< (Many) patient
     │                          │
     │                          └─ relation: SELF, SPOUSE, PARENT, CHILD, OTHER
     └─ id (user_id in patient table)
```

**Key Points:**
- One user account can have **multiple patient records** (family members)
- Each patient record has a `relation` field indicating relationship to the account holder
- The `user_id` in patient table links all members to the same account holder

### 2. Example Data Structure

**User Account:**
```
hosdoc_auth_user:
  id: 5
  username: "raj@gmail.com"
  role: "PATIENT"
```

**Patient Records (Members) for this user:**
```
patient table:
  id: 1, user_id: 5, full_name: "Raj Kumar", relation: "SELF", dob: "1990-05-15"
  id: 2, user_id: 5, full_name: "Priya Kumar", relation: "SPOUSE", dob: "1992-03-20"
  id: 3, user_id: 5, full_name: "Arjun Kumar", relation: "CHILD", dob: "2015-07-10"
  id: 4, user_id: 5, full_name: "Ramesh Kumar", relation: "PARENT", dob: "1965-01-15"
```

### 3. Relation Enum Values

- **SELF**: The account holder themselves
- **SPOUSE**: Spouse/Partner
- **PARENT**: Parent of account holder
- **CHILD**: Child of account holder
- **OTHER**: Other family members or relations

## API Flow

### Creating a Member

**Request:**
```json
POST /api/v1/patients/me/members
{
    "name": "Priya Kumar",
    "gender": "FEMALE",
    "dob": "1992-03-20",
    "relation": "SPOUSE",
    "mobile": "9876543211",
    "ackDetails": true
}
```

**What Happens:**
1. System extracts `user_id` from JWT token (current logged-in user)
2. Creates new Patient record with:
   - `user_id` = current user's ID from auth service
   - `relation` = "SPOUSE" (from request)
   - Other details from request

**Database Insert:**
```sql
INSERT INTO patient (user_id, full_name, gender, dob, relation, phone, ack_details)
VALUES (5, 'Priya Kumar', 'FEMALE', '1992-03-20', 'SPOUSE', '9876543211', TRUE);
```

### Retrieving Members

**Request:**
```
GET /api/v1/patients/me/members
Authorization: Bearer {token}
```

**What Happens:**
1. System extracts `user_id` from JWT token
2. Queries: `SELECT * FROM patient WHERE user_id = {extracted_user_id}`
3. Returns all members (SELF, SPOUSE, PARENT, CHILD, etc.) for that user

**Response:**
```json
[
    {
        "memberId": 1,
        "name": "Raj Kumar",
        "relation": "SELF",
        "gender": "MALE",
        "dob": "1990-05-15",
        "age": 35
    },
    {
        "memberId": 2,
        "name": "Priya Kumar",
        "relation": "SPOUSE",
        "gender": "FEMALE",
        "dob": "1992-03-20",
        "age": 32
    }
]
```

## Data Relationships

### Visual Representation

```
┌─────────────────────────┐
│  hosdoc_auth_user       │
│  ─────────────────────  │
│  id: 5                   │
│  username: raj@gmail.com │
│  role: PATIENT           │
└──────────┬───────────────┘
           │
           │ user_id (FK)
           │
           ▼
┌──────────────────────────────────────────────────────────┐
│  patient (Multiple records for same user_id)             │
│  ──────────────────────────────────────────────────────  │
│  id: 1, user_id: 5, name: "Raj", relation: "SELF"       │
│  id: 2, user_id: 5, name: "Priya", relation: "SPOUSE"   │
│  id: 3, user_id: 5, name: "Arjun", relation: "CHILD"    │
│  id: 4, user_id: 5, name: "Ramesh", relation: "PARENT"  │
└──────────────────────────────────────────────────────────┘
```

## Key Design Decisions

### 1. Why `user_id` in Patient Table?

- **One-to-Many Relationship**: One user account can manage multiple family members
- **Security**: All members are scoped to the account holder
- **Simplified Queries**: Easy to fetch all members for a user: `WHERE user_id = ?`

### 2. Why `relation` Field?

- **Clarity**: Explicitly shows relationship to account holder
- **UI Display**: Frontend can show "Who is the consultation for?" with relation labels
- **Business Logic**: Can implement rules based on relation (e.g., children need parent consent)

### 3. Why Not Separate Tables?

**Current Design (Single Table):**
- ✅ Simple and efficient
- ✅ All members share same structure
- ✅ Easy to query all members together

**Alternative (Separate Tables):**
- ❌ More complex joins
- ❌ Duplicate schema across tables
- ❌ Harder to maintain

## Example Scenarios

### Scenario 1: User Registers and Adds Self

1. User registers in auth service → `hosdoc_auth_user` record created (id: 5)
2. User logs in and creates member:
   ```json
   {
       "name": "Raj Kumar",
       "relation": "SELF",
       ...
   }
   ```
3. Patient record created: `user_id=5, relation=SELF`

### Scenario 2: User Adds Family Members

User (id: 5) adds spouse:
```json
POST /api/v1/patients/me/members
{
    "name": "Priya Kumar",
    "relation": "SPOUSE",
    ...
}
```

Result: New patient record with `user_id=5, relation=SPOUSE`

### Scenario 3: Booking Appointment

When booking:
```json
POST /api/v1/appointments
{
    "doctorId": 1,
    "patientMemberId": 2,  // Priya (SPOUSE)
    ...
}
```

System:
1. Validates `patientMemberId=2` belongs to current user (user_id=5)
2. Creates appointment linking to patient record id=2
3. Appointment shows it's for "Priya Kumar (SPOUSE)"

## SQL Queries for Understanding

### Get All Members for a User
```sql
SELECT * FROM patient WHERE user_id = 5;
```

### Get Only Self Record
```sql
SELECT * FROM patient WHERE user_id = 5 AND relation = 'SELF';
```

### Get Family Members (excluding self)
```sql
SELECT * FROM patient WHERE user_id = 5 AND relation != 'SELF';
```

### Count Members by Relation
```sql
SELECT relation, COUNT(*) as count 
FROM patient 
WHERE user_id = 5 
GROUP BY relation;
```

## Summary

**Storage Model:**
- **Table**: `patient`
- **Key Field**: `user_id` (links to `hosdoc_auth_user.id`)
- **Relation Field**: `relation` enum (SELF, SPOUSE, PARENT, CHILD, OTHER)
- **Structure**: One user → Many patient records (one-to-many)

**Access Pattern:**
- All members for a user: `WHERE user_id = {current_user_id}`
- Specific member: `WHERE user_id = {current_user_id} AND id = {member_id}`
- Security: Users can only access their own members

**Benefits:**
- Simple and efficient
- Easy to query all family members
- Clear relationship indication
- Supports multiple members per account

