DO $$ 
DECLARE
	submitter_id_user_admin INTEGER := 1;
	submitter_name_user_admin varchar(20) := 'Admin';
	submitter_id_api_spoonacular INTEGER := 2;
	submitter_name_api_spoonacular varchar(20) := 'Spoonacular';
	submitter_id_api_zomato INTEGER := 3;
	submitter_name_api_zomato varchar(20) := 'Zomato';
BEGIN 
	INSERT INTO Submitter(submitter_name, submitter_type) VALUES
	(submitter_name_user_admin, 'User'),
	(submitter_name_api_spoonacular, 'API'),
	(submitter_name_api_zomato, 'API');

	INSERT INTO _User(submitter_id, email, session_secret) VALUES
	(submitter_id_user_admin, 'admin@gmail.com', '123456789');

	INSERT INTO Api(submitter_id, api_token) VALUES
	(submitter_id_api_spoonacular, '123'),
	(submitter_id_api_zomato, '456');

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

	INSERT INTO Submission(submission_type) VALUES
	('Meal'),--First user meal from ingredients
	('Ingredient'),--First user meal ingredients from food api
	('Ingredient'),--First user meal ingredients from food api
	('Ingredient'),--First user meal ingredients from food api
	('Meal'),--Second user meal from food api
	('Restaurant'),--First restaurant from user location
	('Restaurant'),--Second restaurant from restaurant api
	('Portion'),--Portion from first user meal from ingredients
	('Portion');

	INSERT INTO ApiSubmission(submission_id, apiId) VALUES
	(1, '0'),--First user meal ingredients from food api
	(2, '1'),--First user meal ingredients from food api
	(3, '2'),--First user meal ingredients from food api
	(4, '3'),--Second user meal from food api
	(6, '4');--Second restaurant from restaurant api

	INSERT INTO SubmissionSubmitter(submission_id, submitter_id) VALUES
	(1, submitter_id_user_admin), -- First user meal from ingredients
	(2, submitter_id_api_spoonacular), -- First user meal ingredients from food api
	(3, submitter_id_api_spoonacular), -- First user meal ingredients from food api
	(4, submitter_id_api_spoonacular), -- First user meal ingredients from food api
	(5, submitter_id_user_admin), -- Second user meal from food api (user)
	(5, submitter_id_api_spoonacular), -- Second user meal from food api (api)
	(6, submitter_id_user_admin), -- First restaurant from user location
	(7, submitter_id_user_admin), -- Second restaurant from restaurant api (user)
	(7, submitter_id_api_zomato), -- Second restaurant from restaurant api (api)
	(8, submitter_id_user_admin), -- Portion from first user meal from ingredients, first restaurant from user location
	(9, submitter_id_user_admin); -- Portion from second user meal from food api, second restaurant from restaurant api

	INSERT INTO SubmissionContract(submission_id, submission_contract) VALUES
	(1, 'Votable'),--First user meal from ingredients
	(1, 'Reportable'),--First user meal from ingredients
	(5, 'Votable'),--Second user meal from food api
	(5, 'Reportable'),--Second user meal from food api
	(6, 'Votable'),--First restaurant from user location
	(6, 'Reportable'),--First restaurant from user location
	(7, 'Votable');--Second restaurant from restaurant api

	INSERT INTO Vote(submission_id, vote_submitter_id, vote) VALUES
	(1, submitter_id_user_admin, true), --First user meal from ingredients
	(5, submitter_id_user_admin, true), --Second user meal from food api
	(6, submitter_id_user_admin, false), --First restaurant from user location
	(7, submitter_id_user_admin, true); --Second restaurant from restaurant api

	INSERT INTO Report(submission_id, submitter_id, description) VALUES
	(1, submitter_id_user_admin, 'Debug description 1'), -- First user meal from ingredients
	(5, submitter_id_user_admin, 'Debug description 2'), -- Second user meal from food api
	(6, submitter_id_user_admin, 'Debug description 3'); -- First restaurant from user location

	INSERT INTO Restaurant(submission_id, restaurant_name, latitude, longitude) VALUES
	(6, 'First rest from user coords', 0.0, 0.0),
	(7, 'Second rest from api', 1.0, 1.0);

	INSERT INTO Meal(submission_id, meal_name) VALUES
	(1, 'First meal from ingr'),
	(5, 'Second meal from api');

	INSERT INTO Ingredient(submission_id, ingredient_name) VALUES
	(2, 'açucar'),
	(3, 'sal'),
	(4, 'azeite');

	INSERT INTO MealIngredient(meal_submission_id, ingredient_submission_id) VALUES
	(1, 2),--First user meal from ingredients
	(1, 3),--First user meal from ingredients
	(1, 4);--First user meal from ingredients

	INSERT INTO Portion(submission_id, quantity) VALUES
	(8, 100), -- Portion from first user meal from ingredients, first restaurant from user location
	(9, 300); -- Portion from second user meal from food api, second restaurant from restaurant api

	INSERT INTO RestaurantMealPortion(meal_submission_id, portion_submission_id, restaurant_submission_id) VALUES
	(1, 8, 6),--First user meal from ingredients, First restaurant from user location
	(5, 9, 7);--Second user meal from food api, Second restaurant from restaurant api

	INSERT INTO RestaurantCuisine(restaurant_submission_id, cuisine_name) VALUES
	(6, 'Belgian'),-- First restaurant from user location
	(7, 'Desserts'); -- Second restaurant from restaurant api

	INSERT INTO MealCuisine(meal_submission_id, cuisine_name) VALUES
	(1, 'Belgian'),--First user meal from ingredients, First restaurant from user location cuisine
	(5, 'Desserts');--Second user meal from food api, Second restaurant from restaurant api cuisine
END $$;