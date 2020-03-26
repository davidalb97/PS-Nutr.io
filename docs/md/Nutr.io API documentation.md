

# Nutr.io API Documentation

## API endpoints

The following sections describe each API endpoint.

### Get restaurants

```http
GET /restaurant?count=:count&skip=:skip
```

- **Request**:
  - **Path parameters:** none
  - **Query parameters**:
    - :count - maximum item count per page
    - :skip - items skipped in the list
  - **Body parameters**:
    - :lat - latitude
    - :lon - longitude
    - :name - restaurant name
- **Response**:
  - **Success**:
    - Status code: 200
    - Content-Type: application/json
    - Body example:

```json
{
	
}
```



---

### Get restaurants by id

```http
GET /restaurant/:id
```

- **Request:**
  - **Path parameters:**
    - :id - restaurant id
  - **Query parameters:** none
  - **Body parameters:** none
- **Response**:
  - **Success**:
    - Status code: 200
    - Content-Type: application/json
    - Body example:

```json
{

}
```



---

### Get meals from a restaurant

```http
GET /restaurant/:id/meal?count=:count&skip=:skip
```

- **Request:**
  - **Path parameters:** 
    - :id - restaurant's id
  - **Query parameters:**
    - :count - maximum item count per page
    - :skip - items skipped in the list
  - **Body:** none    
- **Response:**
  - **Success:**
    - Status code: 200
    - Content-Type: application/json
    - Body example:

```json
{
	
}
```



---

### Get Cuisines

```http
GET /cuisines?count=:count&skip=:skip
```

- **Request:**
  - **Path parameters:** none
  - **Query parameters:**
    - :count - maximum item count per page
    - :skip - items skipped in the list
  - **Body:** none
- 
- Body: none    
- **Response:**
  - **Success:**
    - Status code: 200
    - Content-Type: application/json
    - Body example:

```json
{
	
}
```



---

### Get ingredients

```http
GET /ingredients?count=:count&skip=:skip
```

- **Request:**
  - **Path parameters:** none
  - **Query parameters:**
    - :count - maximum item count per page
    - :skip - items skipped in the list
  - **Body:** none
- **Response:**
  - **Success:**
    - Status code: 200
    - Content-Type: application/json
    - Body example

```json
{

}
```



---

### Get meals by id

```http
GET /meal/:id
```

- **Request:**
  - **Path parameters:** 
    - :id - meal's id
  - **Query parameters:** none
  - **Body:** none
- **Response:**
  - **Success:**
    - Status code: 200
    - Content-Type: application/json
    - Body example

```json
{

}
```



---

### Get user by id (?)

```http
GET /user?id=:id
```

- **Request:**
  - **Path parameters:** none
  - **Query parameters:**
    - :id - user id
  - **Body:** none
- **Response:**
  - **Success:**
    - Status code: 200
    - Content-Type: application/json
    - Body example

```json
{

}
```



---

### Post restaurant

```http
POST /restaurant
```



- **Request:**
  - **Path parameters:** none
  - **Query parameters:** none
  - **Body:**
    - name
    - lat
    - lon
    - submitterId
- **Response:**
  - **Success:**
    - Status code: 201
    - Content-Type: 
    - Body example:

```json
{

}
```



----

### Post a meal for a restaurant

```http
POST /restaurant/:restaurantId/meal
```



- **Request:**
  - **Path parameters:**
    - :restaurantId - restaurant's id
  - **Query parameters:** none
  - **Body:**
    - name
    - submittedId
    - defaultPortion
    - ingredients
- **Response:**
  - **Success:**
    - Status code: 201
    - Content-Type: 
    - Body example:

```json
{

}
```



---

### Post a portion to a specific meal in a restaurant

```http
POST restaurant/:restaurantId/meal/:mealId/portion
```

- **Request:**
  - **Path parameters:**
    - :restaurantId - restaurant's id
    - :mealId - meal's id
  - **Query parameters:** None
  - **Body parameters:**
    - :portion - meal's portion
- **Response:**
  - **Success:**
    - Status code: 201
    - Content-Type: 
    - Body example:

```json
{

}
```



---

### Update a portion from a specific meal in a restaurant

```http
POST restaurant/:restaurantId/meal/:mealId/portion
```

- **Request:**
  - **Path parameters:**
    - :restaurantId - restaurant's id
    - :mealId - meal's id
  - **Query parameters:** None
  - **Body parameters:**
    - :portion - meal's portion
- **Response:**
  - **Success:**
    - Status code: 200
    - Content-Type: 
    - Body example:

```json
{

}
```



---

### Post a report to a specific meal in a restaurant

```http
POST restaurant/:restaurantId/meal/:mealId/report
```

- **Request:**
  - **Path parameters:**
    - :restaurantId - restaurant's id
    - :mealId - meal's id
  - **Query parameters:** None
  - **Body parameters:**
    - :reason - report's reason
- **Response:**
  - **Success:**
    - Status code: 201
    - Content-Type: 
    - Body example:

```json
{

}
```



---

### Update a report to a specific meal in a restaurant

```http
POST restaurant/:restaurantId/meal/:mealId/report
```

- **Request:**
  - **Path parameters:**
    - :restaurantId - restaurant's id
    - :mealId - meal's id
  - **Query parameters:** None
  - **Body parameters:**
    - :reason - report's reason
- **Response:**
  - **Success:**
    - Status code: 200
    - Content-Type: 
    - Body example:

```json
{

}
```



---

### Post a vote to a specific meal in a restaurant

```http
POST restaurant/:restaurantId/meal/:mealId/vote
```

- **Request:**
  - **Path parameters:**
    - :restaurantId - restaurant's id
    - :mealId - meal's id
  - **Query parameters:** None
  - **Body parameters:**
    - :vote - upvote / downvote
- **Response:**
  - **Success:**
    - Status code: 201
    - Content-Type: 
    - Body example:

```json
{

}
```



---

### Update a vote to a specific meal in a restaurant

```http
POST restaurant/:restaurantId/meal/:mealId/vote
```

- **Request:**
  - **Path parameters:**
    - :restaurantId - restaurant's id
    - :mealId - meal's id
  - **Query parameters:** None
  - **Body parameters:**
    - :vote - upvote / downvote
- **Response:**
  - **Success:**
    - Status code: 200
    - Content-Type:
    - Body example:

```json
{

}
```



---

### Delete a restaurant

```http
DELETE restaurant/:restaurantId
```

- **Request:**
  - **Path parameters:**
    - :restaurantId - restaurant's id
  - **Query parameters:** None
  - **Body parameters:** None
- **Response:**
  - **Success:**
    - Status code: 200
    - Content-Type:
    - Body example:

```json
{

}
```



---

### Delete a meal from a restaurant

```http
DELETE restaurant/:restaurantId/meal/:mealId
```

- **Request:**
  - **Path parameters:**
    - :restaurantId - restaurant's id
    - :mealId - meal's id
  - **Query parameters:** None
  - **Body parameters:** None
- **Response:**
  - **Success:**
    - Status code: 200
    - Content-Type:
    - Body example:

```json
{

}
```



---

### Delete a vote from a specific meal in a restaurant

```http
DELETE restaurant/:restaurantId/meal/:mealId/vote
```

- **Request:**
  - **Path parameters:**
    - :restaurantId - restaurant's id
    - :mealId - meal's id
  - **Query parameters:** None
  - **Body parameters:** None
- **Response:**
  - **Success:**
    - Status code: 200
    - Content-Type:
    - Body example:

```json
{

}
```



---

### Delete a portion from a specific meal in a restaurant

```http
DELETE restaurant/:restaurantId/meal/:mealId/portion
```

- **Request:**
  - **Path parameters:**
    - :restaurantId - restaurant's id
    - :mealId - meal's id
  - **Query parameters:** None
  - **Body parameters:** None
- **Response:**
  - **Success:**
    - Status code: 200
    - Content-Type:
    - Body example:

```json
{

}
```



---





