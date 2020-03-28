

# Nutr.io API Documentation

## API endpoints

### API endpoints' table

| Method | Path                                | Query string                                         | Body param                                        | Description                                                  |
| ------ | ----------------------------------- | ---------------------------------------------------- | ------------------------------------------------- | ------------------------------------------------------------ |
| GET    | restaurant                          | int lat,  int lon, int skip,  int count, String name |                                                   | Search for restaurants and their cuisines, based on location or  named search |
| GET    | restaurant\:id                      |                                                      |                                                   | Restaurant's name and cuisines                               |
| GET    | restaurant\:id\meal                 | int skip,  int count                                 |                                                   | Restaurant's meals with votes                                |
| GET    | cuisines                            | int skip,  int count                                 |                                                   | List possible cuisines                                       |
| GET    | ingredients                         | int skip,  int count                                 |                                                   | List all possible ingredients and it's ids                   |
| GET    | meal\:id                            |                                                      |                                                   | Meal's portions and ingredients                              |
|        |                                     |                                                      |                                                   |                                                              |
| POST   | restaurant                          |                                                      | String name,  int lat, int lon, String[] cuisines | Create new restaurant                                        |
| POST   | restaurant\:id\meal                 |                                                      | String name, Object portion, int[] ingredientIds  | Create a new restaurant meal, allowing for multiple ingredients (optional), first portion submission (optional) |
| POST   | restaurant\:id\meal\:mealId\portion |                                                      | Object portion                                    | Creates a restaurant's meal portion                          |
| POST   | restaurant\:id\meal\:mealId\report  |                                                      | String reason                                     | Create a report on a restaurant's meal                       |
| POST   | restaurant\:id\meal\:mealId\vote    |                                                      | boolean isPositive                                | Create a vote on a restaurant's meal                         |
| POST   | restaurant\:id\report               |                                                      | String reason                                     | Create a report on a restaurant                              |
| POST   | restaurant\:id\vote                 |                                                      | boolean isPositive                                | Create a vote on a restaurant                                |
|        |                                     |                                                      |                                                   |                                                              |
| PUT    | restaurant\:id\meal\:mealId\portion |                                                      | Object portion                                    | Updates a restaurant's meal portion                          |
| PUT    | restaurant\:id\meal\:mealId\vote    |                                                      | boolean isPositive                                | Update a vote on a restaurant's meal                         |
| PUT    | restaurant\:id\vote                 |                                                      | boolean isPositive                                | Update a vote on a restaurant                                |
|        |                                     |                                                      |                                                   |                                                              |
| DELETE | restaurant\:id                      |                                                      |                                                   | Delete user created restaurant,  if it wasn't used by the community yet |
| DELETE | restaurant\:id\meal\:id             |                                                      |                                                   | Delete user created restaurant meal,  if it wasn't used by the community yet |
| DELETE | restaurant\:id\meal\:mealId\portion |                                                      |                                                   | Delete user's restaurant's meal portion                      |
| DELETE | restaurant\:id\meal\:mealId\vote    |                                                      |                                                   | Delete user's restaurant meal vote                           |
| DELETE | restaurant\:id\vote                 |                                                      |                                                   | Delete user's restaurant vote                                |

The following sections describe each API endpoint. 

Bear in mind that this documentation is not complete yet and it's under development. It will only show the most common aspects of each endpoint.

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





