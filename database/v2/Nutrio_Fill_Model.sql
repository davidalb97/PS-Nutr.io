INSERT INTO Submitter(submitter_id, submitter_name, submitter_type) VALUES
(0, 'Admin', 'User'),
(1, 'Debug_Api_Name', 'API');

INSERT INTO _User(submitter_id, email, session_secret) VALUES
(0, 'admin@gmail.com', '123456789');

INSERT INTO Api(submitter_id, api_token) VALUES
(1, '123');

INSERT INTO Cuisine(cuisine_name) VALUES
('African'),
('Alentejana'),
('American'),
('Angolan'),
('Arabian'),
('Argentine'),
('Asian'),
('Austrian'),
('Author'),
('BBQ'),
('Bakery'),
('Bangladeshi'),
('Bar Food'),
('Belgian'),
('Beverages'),
('Brazilian'),
('British'),
('Burger'),
('Cafe'),
('Cafe Food'),
('Canadian'),
('Cape Verdean'),
('Caribbean'),
('Chinese'),
('Coffee'),
('Coffee and Tea'),
('Contemporary'),
('Crepes'),
('Cuban'),
('Desserts'),
('Eastern European'),
('Filipino'),
('Finger Food'),
('French'),
('Fresh Fish'),
('Fusion'),
('Goan'),
('Gourmet Fast Food'),
('Greek'),
('Grill'),
('Healthy Food'),
('Ice Cream'),
('International'),
('Iranian'),
('Irish'),
('Israeli'),
('Italian'),
('Japanese'),
('Juices'),
('Kebab'),
('Korean'),
('Latin American'),
('Lebanese'),
('Madeiran'),
('Malaysian'),
('Mediterranean'),
('Mexican'),
('Middle Eastern'),
('Mineira'),
('Minhota'),
('Moroccan'),
('Mozambican'),
('Nepalese'),
('Oriental'),
('Pakistani'),
('Patisserie'),
('Peruvian'),
('Petiscos'),
('Pizza'),
('Portuguese'),
('Ramen'),
('Russian'),
('Salad'),
('Sandwich'),
('Santomean'),
('Seafood'),
('Snack Bar'),
('Snacks'),
('Spanish'),
('Steak'),
('Street Food'),
('Sushi'),
('Swedish'),
('Swiss'),
('Tapas'),
('Tea'),
('Thai'),
('Tibetan'),
('Transmontana'),
('Turkish'),
('Vegan'),
('Vegetarian'),
('Vietnamese');

INSERT INTO Submission(submission_id, submission_type) VALUES
(0, 'Meal'),--First user meal from ingredients
(1, 'Ingredient'),--First user meal ingredients from food api
(2, 'Ingredient'),--First user meal ingredients from food api
(3, 'Ingredient'),--First user meal ingredients from food api
(4, 'Meal'),--Second user meal from food api
(5, 'Restaurant'),--First restaurant from user location
(6, 'Restaurant'),--Second restaurant from restaurant api
(7, 'Portion'),--Portion from first user meal from ingredients
(8, 'Portion');

INSERT INTO ApiSubmission(submission_id, apiId) VALUES
(1, 0),--First user meal ingredients from food api
(2, 1),--First user meal ingredients from food api
(3, 2),--First user meal ingredients from food api
(4, 3),--Second user meal from food api
(6, 4);--Second restaurant from restaurant api

INSERT INTO SubmissionSubmitter(submission_id, submitter_id) VALUES
(0, 0), -- First user meal from ingredients
(1, 1), -- First user meal ingredients from food api
(2, 1), -- First user meal ingredients from food api
(3, 1), -- First user meal ingredients from food api
(4, 0), -- Second user meal from food api (user)
(4, 1), -- Second user meal from food api (api)
(5, 0), -- First restaurant from user location
(6, 0), -- Second restaurant from restaurant api (user)
(6, 1), -- Second restaurant from restaurant api (api)
(7, 0), -- Portion from first user meal from ingredients, first restaurant from user location
(8, 0); -- Portion from second user meal from food api, second restaurant from restaurant api

INSERT INTO SubmissionContract(submission_id, submission_contract) VALUES
(0, 'Votable'),--First user meal from ingredients
(0, 'Reportable'),--First user meal from ingredients
(4, 'Votable'),--Second user meal from food api
(4, 'Reportable'),--Second user meal from food api
(5, 'Votable'),--First restaurant from user location
(5, 'Reportable'),--First restaurant from user location
(6, 'Votable');--Second restaurant from restaurant api

INSERT INTO Vote(submission_id, vote_submitter_id, vote) VALUES
(0, 0, true), --First user meal from ingredients
(4, 0, true), --Second user meal from food api
(5, 0, false), --First restaurant from user location
(6, 0, true); --Second restaurant from restaurant api

INSERT INTO Report(submission_id, submitter_id, description) VALUES
(0, 0, 'Debug description 1'), -- First user meal from ingredients
(4, 0, 'Debug description 2'), -- Second user meal from food api
(5, 0, 'Debug description 3'); -- First restaurant from user location

INSERT INTO Restaurant(submission_id, restaurant_name, latitude, longitude) VALUES
(5, 'First rest from user coords', 0.0, 0.0),
(6, 'Second rest from api', 1.0, 1.0);

INSERT INTO Meal(submission_id, meal_name) VALUES
(0, 'First meal from ingr'),
(4, 'Second meal from api');

INSERT INTO Ingredient(submission_id, ingredient_name) VALUES
(1, 'açucar'),
(2, 'sal'),
(3, 'azeite');

INSERT INTO MealIngredient(meal_submission_id, ingredient_submission_id) VALUES
(0, 1),--First user meal from ingredients
(0, 2),--First user meal from ingredients
(0, 3);--First user meal from ingredients

INSERT INTO Portion(submission_id, quantity) VALUES
(7, 100), -- Portion from first user meal from ingredients, first restaurant from user location
(8, 300); -- Portion from second user meal from food api, second restaurant from restaurant api

INSERT INTO RestaurantMealPortion(meal_submission_id, portion_submission_id, restaurant_submission_id) VALUES
(0, 7, 5),--First user meal from ingredients, First restaurant from user location
(4, 8, 6);--Second user meal from food api, Second restaurant from restaurant api

INSERT INTO RestaurantCuisine(restaurant_submission_id, cuisine_name) VALUES
(5, 'Belgian'),-- First restaurant from user location
(6, 'Desserts'); -- Second restaurant from restaurant api

INSERT INTO MealCuisine(meal_submission_id, cuisine_name) VALUES
(0, 'Belgian'),--First user meal from ingredients, First restaurant from user location cuisine
(4, 'Desserts');--Second user meal from food api, Second restaurant from restaurant api cuisine


