--Portuguese meals
SELECT AddCuisineToFood('Duck rice', ARRAY ['Portuguese']);
SELECT AddCuisineToFood('Codfish à brás', ARRAY ['Portuguese']);
SELECT AddCuisineToFood('Codfish with cream', ARRAY ['Portuguese']);
SELECT AddCuisineToFood('Codfish and chickpea salad', ARRAY ['Portuguese']);
SELECT AddCuisineToFood('Pastel bacalhau', ARRAY ['Portuguese']);
SELECT AddCuisineToFood('Caldeirada', ARRAY ['Portuguese']);

--Italian
SELECT AddCuisineToFood('Pizza, high crust', ARRAY ['Italian', 'Pizza']);
SELECT AddCuisineToFood('Pizza, thin crust', ARRAY ['Italian', 'Pizza']);
SELECT AddCuisineToFood('Lasagne', ARRAY ['Italian']);
SELECT AddCuisineToFood('Gnocchi', ARRAY ['Italian']);
SELECT AddCuisineToFood('Mushroom risotto', ARRAY ['Italian']);

--Hamburgers
SELECT AddCuisineToFood('Burger, cheese', ARRAY ['Burger']);
SELECT AddCuisineToFood('Burger, chicken', ARRAY ['Burger']);
SELECT AddCuisineToFood('Burger, vegetables', ARRAY ['Burger']);

--Sushi
SELECT AddCuisineToFood('Sashimi', ARRAY ['Sushi', 'Japanese']);
SELECT AddCuisineToFood('California roll', ARRAY ['Sushi', 'Japanese']);
