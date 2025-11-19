# ITSM – Backend API (Spring Boot)

A backend service inspired by ServiceNow for managing IT service tickets.  
Provides RESTful APIs for authentication, ticket lifecycle management, user roles, and assignment workflows.

---

## Tech Stack
- Java 21+
- Spring Boot
- Spring Security (JWT)
- Spring Data JPA
- MySQL
- Maven
- Postman (for API testing)
---

## Authentication & Authorization

The system uses JWT authentication with role-based access control (RBAC).

### User Roles and Capabilities

| Role           | Capabilities |
|----------------|--------------|
| EMPLOYEE       | Create tickets, view own tickets |
| SUPPORT_STAFF  | View assigned tickets, update status |
| ADMIN          | Full access; assign staff, update priority, manage all tickets |

---

## Ticket Features (CRUD)

Implemented features:
- Create Ticket  
- View Ticket  
- Update Ticket  
- Delete Ticket  
- Filter tickets by status, priority, assigned staff, or requester

---

## API Endpoints

### Create Ticket
- **POST /api/v1/tickets**  
- **Roles:** EMPLOYEE, SUPPORT_STAFF, ADMIN

## Request

```json
{
  "title": "Cannot connect to VPN",
  "description": "VPN disconnects every 5 minutes.",
  "category": "Network",
  "priority": "HIGH"
}
```
## Response

```json
{
  "ticketNumber": "INC0000001"
  "title": "Cannot connect to VPN",
  "description": "VPN disconnects every 5 minutes.",
  "category": "Network",
  "priority": "HIGH",
  "status": "OPEN",
  "createdBy": {
      "fullname": "John Doe",
      "email": "john.doe@user.com",
      "role": "EMPLOYEE",
      "createdAt": "2025-10-28T17:09:26.257944"
   },
  "assignedTo": null,
  "createdAt": "2025-02-03T10:23:41",
  "updatedAt": "2025-02-03T10:23:41"
}
```
### Get All Tickets (Admin)
- **GET /api/v1/tickets/admin/get**  
- **Roles:** ADMIN → sees all tickets
  
## Response

```json
{
  "ticketNumber": "INC0000001",
  "title": "Software installation request",
  "description": "Need Excel 365 installed.",
  "category": "Software",
  "priority": "MODERATE",
  "status": "IN_PROGRESS",
  "createdBy": {
      "fullname": "John Doe",
      "email": "john.doe@user.com",
      "role": "EMPLOYEE",
      "createdAt": "2025-10-28T17:09:26.257944"
  },
  "assignedTo": {
      "fullname": "John Karl",
      "email": "john.karl@users.com",
      "role": "SUPPORT_STAFF",
      "createdAt": "2025-10-29T11:54:18.74449"
  },
  "createdAt": "2025-11-15T15:44:37.215696",
  "updatedAt": "2025-11-15T15:50:55.332148"
},
{
    "ticketNumber": "INC0000002",
    "title": "WiFi not working",
    "description": "Can’t connect to office WiFi since morning.",
    "category": "Network",
    "priority": "HIGH",
    "status": "OPEN",
    "createdBy": {
        "fullname": "Mike Reyes",
        "email": "mike.reyes@user.com",
        "role": "EMPLOYEE",
        "createdAt": "2025-11-12T07:42:33.281152"
    },
    "assignedTo": null,
    "createdAt": "2025-11-17T12:06:42.642545",
    "updatedAt": "2025-11-17T12:06:42.642545"
},
```
## Get Tickets (Support Staff / Employee)
- **GET /api/v1/tickets**  
  - **SUPPORT_STAFF** → sees assigned tickets  
  - **EMPLOYEE** → sees own tickets
    
```json
{
  "ticketNumber": "INC0000003",
  "title": "Laptop overheating",
  "description": "Fan running loud and device shuts down.",
  "category": "Hardware",
  "priority": "MODERATE",
  "status": "OPEN",
  "createdBy": "employee_user"
  "assignedTo": "support_staff_user",
  "createdAt": "2025-11-17T12:07:50.668957",
  "updatedAt": "2025-11-17T12:07:50.668957"
},
```
### Get Ticket by ID

- **GET /api/tickets/{id}**  
- **Roles:**  
  - **EMPLOYEE** → can see their own ticket  
  - **SUPPORT_STAFF** → can see assigned tickets  
  - **ADMIN** → can see any ticket
    
```json
{
  "ticketId": "INC0000124",
  "title": "Cannot connect to VPN",
  "description": "VPN disconnects every 5 minutes.",
  "category": "Network",
  "priority": "HIGH",
  "status": "IN_PROGRESS",
  "createdBy": "employee_user",
  "assignedTo": "support_staff_user",
  "createdAt": "2025-02-03T10:23:41",
  "updatedAt": "2025-02-03T12:15:30"
}
```
### Assign Ticket (Admin)

- **PATCH /api/v1/tickets/{ticketId}/assign/{staffId}**  
- **Roles:**  
  - **ADMIN** → can assign a ticket to a support staff  
  - **SUPPORT_STAFF** → cannot assign, only for reference

#### Request
```json
{
  "assignedTo": "support_staff_user"
}
```
### Response
```json
{
  "ticketId": "INC0000124",
  "title": "Cannot connect to VPN",
  "status": "IN_PROGRESS",
  "priority": "HIGH",
  "assignedTo": "support_staff_user",
  "updatedAt": "2025-02-03T13:00:00"
}
```
### Update Ticket Status

- **PATCH /api/v1/tickets/update/{id}**  
- **Roles:**  
  - **SUPPORT_STAFF** → can update status of assigned tickets  
  - **ADMIN** → can update status of any ticket

#### Request
```json
{
  "status": "RESOLVED"
}
```

### Response
```json
{
  "ticketId": "INC0000124",
  "title": "Cannot connect to VPN",
  "status": "RESOLVED",
  "priority": "HIGH",
  "assignedTo": "support_staff_user",
  "updatedAt": "2025-02-03T14:15:00"
}
```
### Delete Ticket

- **DELETE /api/tickets/{id}**  
- **Role:**  
  - **ADMIN** → can delete any ticket

#### Response
```json
{
  "message": "Ticket INC0000124 has been successfully deleted."
}
```
