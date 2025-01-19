# Ecommerce-UserManagement-Service

User Management Service for Ecommerce Application

## API Documentation

### AddressController

#### Create Address
- **Endpoint:** `/api/address/{userId}`
- **Method:** `POST`
- **Description:** Creates an address for a user.
- **Path Variable:** `userId`
- **Request Body:** `AddressRequestDto`
- **Response:**
  - `201 Created`: Returns the created address details.
  - `400 Bad Request`: If the request is invalid.
  - `500 Internal Server Error`: If there is an error during creation.

### UserManagementController

#### Create User
- **Endpoint:** `/api/createUser`
- **Method:** `POST`
- **Description:** Creates a new user.
- **Request Body:** `UserRequestDto`
- **Response:**
  - `201 Created`: Returns the created user details.
  - `400 Bad Request`: If the request is invalid.
  - `500 Internal Server Error`: If there is an error during creation.

#### Update Password
- **Endpoint:** `/api/users/{email}/updatePassword`
- **Method:** `POST`
- **Description:** Updates the password of a user.
- **Path Variable:** `email`
- **Request Body:** `UpdatePasswordRequestDto`
- **Response:**
  - `200 OK`: Returns the update password response.
  - `400 Bad Request`: If the request is invalid.
  - `404 Not Found`: If the email is not found.
  - `500 Internal Server Error`: If there is an error during update.

#### Update User Details
- **Endpoint:** `/api/users/{email}/updateDetails`
- **Method:** `PATCH`
- **Description:** Updates user details.
- **Path Variable:** `email`
- **Request Body:** `Map<String, Object>`
- **Response:**
  - `200 OK`: Returns the updated user details.
  - `400 Bad Request`: If the request is invalid.
  - `404 Not Found`: If the email is not found.
  - `500 Internal Server Error`: If there is an error during update.

#### Get User by Email
- **Endpoint:** `/api/users/{email}/userDetails`
- **Method:** `GET`
- **Description:** Retrieves user details by email.
- **Path Variable:** `email`
- **Response:**
  - `200 OK`: Returns the user details.
  - `404 Not Found`: If the email is not found.
  - `500 Internal Server Error`: If there is an error during retrieval.

#### Get User by ID
- **Endpoint:** `/api/users/{userId}`
- **Method:** `GET`
- **Description:** Retrieves user details by user ID.
- **Path Variable:** `userId`
- **Response:**
  - `200 OK`: Returns the user details.
  - `404 Not Found`: If the user ID is not found.
  - `500 Internal Server Error`: If there is an error during retrieval.

#### Get All Users
- **Endpoint:** `/api/users`
- **Method:** `GET`
- **Description:** Retrieves all users.
- **Response:**
  - `200 OK`: Returns the list of all users.
  - `500 Internal Server Error`: If there is an error during retrieval.

#### Send Welcome Email
- **Endpoint:** `/api/send-welcome-email`
- **Method:** `POST`
- **Description:** Sends a welcome email to a user.
- **Request Body:** `String` (email)
- **Response:**
  - `200 OK`: If the email is sent successfully.
  - `500 Internal Server Error`: If there is an error during sending.

#### Delete User
- **Endpoint:** `/api/delete/user/{email}`
- **Method:** `DELETE`
- **Description:** Deletes a user by email.
- **Path Variable:** `email`
- **Response:**
  - `200 OK`: If the user is deleted successfully.
  - `404 Not Found`: If the email is not found.
  - `500 Internal Server Error`: If there is an error during deletion.
