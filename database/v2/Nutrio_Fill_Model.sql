DO $$ 
DECLARE
	submitter_id_user_admin INTEGER := 1;
	submitter_name_user_admin varchar(20) := 'Admin';
	submitter_id_api_spoonacular INTEGER := 2;
	submitter_name_api_spoonacular varchar(20) := 'Spoonacular';
	submitter_id_api_zomato INTEGER := 3;
	submitter_name_api_zomato varchar(20) := 'Zomato';
	submitter_id_api_here INTEGER := 4;
	submitter_name_api_here varchar(20) := 'Here';
	cuisine_here_api_id_american varchar(20) := '101-000';
	cuisine_here_api_id_mexican varchar(20) := '102-000';
	cuisine_here_api_id_canadian varchar(20) := '103-000';
	cuisine_here_api_id_cuban varchar(20) := '153-000';
	cuisine_here_api_id_asian varchar(20) := '200-000';
	cuisine_here_api_id_chinese varchar(20) := '201-000';
	cuisine_here_api_id_japanese varchar(20) := '203-000';
	cuisine_here_api_id_sushi varchar(20) := '203-026';
	cuisine_here_api_id_thai varchar(20) := '205-000';
	cuisine_here_api_id_vietnamese varchar(20) := '206-000';
	cuisine_here_api_id_korean varchar(20) := '207-000';
	cuisine_here_api_id_pakistani varchar(20) := '208-000';
	cuisine_here_api_id_french varchar(20) := '301-000';
	cuisine_here_api_id_greek varchar(20) := '303-000';
	cuisine_here_api_id_italian varchar(20) := '304-000';
	cuisine_here_api_id_belgian varchar(20) := '307-000';
	cuisine_here_api_id_swiss varchar(20) := '310-000';
	cuisine_here_api_id_spanish varchar(20) := '311-000';
	cuisine_here_api_id_tapas varchar(20) := '311-034';
	cuisine_here_api_id_portuguese varchar(20) := '313-000';
	cuisine_here_api_id_mediterranean varchar(20) := '372-000';
	cuisine_here_api_id_steak varchar(20) := '800-056';
	cuisine_here_api_id_pizza varchar(20) := '800-057';
	cuisine_here_api_id_street_food varchar(20) := '800-058';
	cuisine_here_api_id_sandwich varchar(20) := '800-060';
	cuisine_here_api_id_ice_cream varchar(20) := '800-063';
	cuisine_here_api_id_international varchar(20) := '800-064';
	cuisine_here_api_id_burger varchar(20) := '800-067';
	cuisine_here_api_id_crepes varchar(20) := '800-068';
	cuisine_here_api_id_seafood varchar(20) := '800-075';
	cuisine_here_api_id_vegan varchar(20) := '800-076';
	cuisine_here_api_id_vegetarian varchar(20) := '800-077';
	cuisine_here_api_id_grill varchar(20) := '800-078';
	cuisine_here_api_id_vietnamese2 varchar(20) := '800-085';
BEGIN 
	INSERT INTO Submitter(submitter_name, submitter_type) VALUES
	(submitter_name_user_admin, 'User'),
	(submitter_name_api_spoonacular, 'API'),
	(submitter_name_api_zomato, 'API'),
	(submitter_name_api_here, 'API');

	INSERT INTO _User(submitter_id, email, session_secret) VALUES
	(submitter_id_user_admin, 'admin@gmail.com', '123456789');

	INSERT INTO Api(submitter_id, api_token) VALUES
	(submitter_id_api_spoonacular, '123'),
	(submitter_id_api_zomato, '456');

	INSERT INTO Cuisine(cuisine_name) VALUES
	('African'),
	('Alentejana'),
	('American'),--Here cuisine_here_api_id_american
	('Angolan'),
	('Arabian'),
	('Argentine'),
	('Asian'),--Here cuisine_here_api_id_asian
	('Austrian'),
	('Author'),
	('BBQ'),
	('Bakery'),
	('Bangladeshi'),
	('Bar Food'),
	('Belgian'),--Here cuisine_here_api_id_belgian
	('Beverages'),
	('Brazilian'),
	('British'),	
	('Burger'),--Here cuisine_here_api_id_burger
	('Cafe'),
	('Cafe Food'),
	('Canadian'),--Here cuisine_here_api_id_canadian
	('Cape Verdean'),
	('Caribbean'),
	('Chinese'),--Here cuisine_here_api_id_chinese
	('Coffee'),
	('Coffee and Tea'),
	('Contemporary'),
	('Crepes'),--Here cuisine_here_api_id_crepes
	('Cuban'),--Here cuisine_here_api_id_cuban
	('Desserts'),
	('Eastern European'),
	('Filipino'),
	('Finger Food'),
	('French'),--Here cuisine_here_api_id_french
	('Fresh Fish'),
	('Fusion'),
	('Goan'),
	('Gourmet Fast Food'),
	('Greek'),--Here cuisine_here_api_id_greek
	('Grill'),--Here cuisine_here_api_id_grill
	('Healthy Food'),
	('Ice Cream'),--Here cuisine_here_api_id_ice_cream
	('International'),--Here cuisine_here_api_id_international
	('Iranian'),
	('Irish'),
	('Israeli'),
	('Italian'),--Here cuisine_here_api_id_italian
	('Japanese'),--Here cuisine_here_api_id_japanese
	('Juices'),
	('Kebab'),
	('Korean'),--Here cuisine_here_api_id_korean
	('Latin American'),
	('Lebanese'),
	('Madeiran'),
	('Malaysian'),
	('Mediterranean'),--Here cuisine_here_api_id_mexican
	('Mexican'),--Here cuisine_here_api_id_mexican
	('Middle Eastern'),
	('Mineira'),
	('Minhota'),
	('Moroccan'),
	('Mozambican'),
	('Nepalese'),
	('Oriental'),
	('Pakistani'),--Here cuisine_here_api_id_pakistani
	('Patisserie'),
	('Peruvian'),
	('Petiscos'),
	('Pizza'),--Here cuisine_here_api_id_pizza
	('Portuguese'),--Here cuisine_here_api_id_portuguese
	('Ramen'),
	('Russian'),
	('Salad'),
	('Sandwich'),--Here cuisine_here_api_id_sandwich
	('Santomean'),
	('Seafood'),--Here cuisine_here_api_id_seafood
	('Snack Bar'),
	('Snacks'),
	('Spanish'),--Here cuisine_here_api_id_spanish
	('Steak'),--Here cuisine_here_api_id_steak
	('Street Food'),--Here cuisine_here_api_id_street_food
	('Sushi'),--Here cuisine_here_api_id_pakistani
	('Swedish'),
	('Swiss'),--Here cuisine_here_api_id_swiss
	('Tapas'),--Here cuisine_here_api_id_tapas
	('Tea'),
	('Thai'),--Here cuisine_here_api_id_thai
	('Tibetan'),
	('Transmontana'),
	('Turkish'),
	('Vegan'),--Here cuisine_here_api_id_vegan
	('Vegetarian'),--Here cuisine_here_api_id_vegetarian
	('Vietnamese');--Here cuisine_here_api_id_vietnamese & cuisine_here_api_id_vietnamese2
	
	INSERT INTO Submission(submission_type) VALUES
	('Meal'),--First user meal from ingredients
	('Ingredient'),--First user meal ingredients from food api
	('Ingredient'),--First user meal ingredients from food api
	('Ingredient'),--First user meal ingredients from food api
	('Meal'),--Second user meal from food api
	('Restaurant'),--First restaurant from user location
	('Restaurant'),--Second restaurant from restaurant api
	('Portion'),--Portion from first user meal from ingredients
	('Portion'),
	('Cuisine'),--First here cuisine (10)
	('Cuisine'),
	('Cuisine'),
	('Cuisine'),
	('Cuisine'),
	('Cuisine'),
	('Cuisine'),
	('Cuisine'),
	('Cuisine'),
	('Cuisine'),
	('Cuisine'),
	('Cuisine'),
	('Cuisine'),
	('Cuisine'),
	('Cuisine'),
	('Cuisine'),
	('Cuisine'),
	('Cuisine'),
	('Cuisine'),
	('Cuisine'),
	('Cuisine'),
	('Cuisine'),
	('Cuisine'),
	('Cuisine'),
	('Cuisine'),
	('Cuisine'),
	('Cuisine'),
	('Cuisine'),
	('Cuisine'),
	('Cuisine'),
	('Cuisine'),
	('Cuisine'),
	('Cuisine'),
	('Cuisine');--Last here cuisine (43)
	
	INSERT INTO ApiCuisine(submission_id, cuisine_id) VALUES 
	(10, 3), --cuisine_here_api_id_american
	(11, 7), --cuisine_here_api_id_asian
	(12, 14), --cuisine_here_api_id_belgian
	(13, 18), --cuisine_here_api_id_burger
	(14, 21), --cuisine_here_api_id_canadian
	(15, 24), --cuisine_here_api_id_chinese
	(16, 28), --cuisine_here_api_id_crepes
	(17, 29), --cuisine_here_api_id_cuban
	(18, 34), --cuisine_here_api_id_french
	(19, 39), --cuisine_here_api_id_greek
	(20, 40), --cuisine_here_api_id_grill
	(21, 42), --cuisine_here_api_id_ice_cream
	(22, 43), --cuisine_here_api_id_international
	(23, 47), --cuisine_here_api_id_italian
	(24, 48), --cuisine_here_api_id_japanese
	(25, 51), --cuisine_here_api_id_korean
	(26, 56), --cuisine_here_api_id_mexican
	(27, 57), --cuisine_here_api_id_mexican
	(28, 65), --cuisine_here_api_id_pakistani
	(29, 69), --cuisine_here_api_id_pizza
	(30, 70), --cuisine_here_api_id_portuguese
	(31, 74), --cuisine_here_api_id_sandwich
	(32, 76), --cuisine_here_api_id_seafood
	(33, 79), --cuisine_here_api_id_spanish
	(34, 80), --cuisine_here_api_id_steak
	(35, 81), --cuisine_here_api_id_street_food
	(36, 82), --cuisine_here_api_id_pakistani
	(37, 84), --cuisine_here_api_id_swiss
	(38, 85), --cuisine_here_api_id_tapas
	(39, 86), --cuisine_here_api_id_thai
	(40, 87), --cuisine_here_api_id_vegan
	(41, 88), --cuisine_here_api_id_vegetarian
	(42, 89), --cuisine_here_api_id_vietnamese
	(43, 89); --cuisine_here_api_id_vietnamese2

	INSERT INTO ApiSubmission(submission_id, apiId) VALUES
	(2, '0'),--First user meal ingredients from food api
	(3, '1'),--First user meal ingredients from food api
	(4, '2'),--First user meal ingredients from food api
	(5, '3'),--Second user meal from food api
	(7, '4'),--Second restaurant from restaurant api
	(10, cuisine_here_api_id_american),
	(11, cuisine_here_api_id_asian),
	(12, cuisine_here_api_id_belgian),
	(13, cuisine_here_api_id_burger),
	(14, cuisine_here_api_id_canadian),
	(15, cuisine_here_api_id_chinese),
	(16, cuisine_here_api_id_crepes),
	(17, cuisine_here_api_id_cuban),
	(18, cuisine_here_api_id_french),
	(19, cuisine_here_api_id_greek),
	(20, cuisine_here_api_id_grill),
	(21, cuisine_here_api_id_ice_cream),
	(22, cuisine_here_api_id_international),
	(23, cuisine_here_api_id_italian),
	(24, cuisine_here_api_id_japanese),
	(25, cuisine_here_api_id_korean),
	(26, cuisine_here_api_id_mexican),
	(27, cuisine_here_api_id_mexican),
	(28, cuisine_here_api_id_pakistani),
	(29, cuisine_here_api_id_pizza),
	(30, cuisine_here_api_id_portuguese),
	(31, cuisine_here_api_id_sandwich),
	(32, cuisine_here_api_id_seafood),
	(33, cuisine_here_api_id_spanish),
	(34, cuisine_here_api_id_steak),
	(35, cuisine_here_api_id_street_food),
	(36, cuisine_here_api_id_pakistani),
	(37, cuisine_here_api_id_swiss),
	(38, cuisine_here_api_id_tapas),
	(39, cuisine_here_api_id_thai),
	(40, cuisine_here_api_id_vegan),
	(41, cuisine_here_api_id_vegetarian),
	(42, cuisine_here_api_id_vietnamese),
	(43, cuisine_here_api_id_vietnamese2);

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
	(9, submitter_id_user_admin), -- Portion from second user meal from food api, second restaurant from restaurant api
	(10, submitter_id_api_here),
	(11, submitter_id_api_here),
	(12, submitter_id_api_here),
	(13, submitter_id_api_here),
	(14, submitter_id_api_here),
	(15, submitter_id_api_here),
	(16, submitter_id_api_here),
	(17, submitter_id_api_here),
	(18, submitter_id_api_here),
	(19, submitter_id_api_here),
	(20, submitter_id_api_here),
	(21, submitter_id_api_here),
	(22, submitter_id_api_here),
	(23, submitter_id_api_here),
	(24, submitter_id_api_here),
	(25, submitter_id_api_here),
	(26, submitter_id_api_here),
	(27, submitter_id_api_here),
	(28, submitter_id_api_here),
	(29, submitter_id_api_here),
	(30, submitter_id_api_here),
	(31, submitter_id_api_here),
	(32, submitter_id_api_here),
	(33, submitter_id_api_here),
	(34, submitter_id_api_here),
	(35, submitter_id_api_here),
	(36, submitter_id_api_here),
	(37, submitter_id_api_here),
	(38, submitter_id_api_here),
	(39, submitter_id_api_here),
	(40, submitter_id_api_here),
	(41, submitter_id_api_here),
	(42, submitter_id_api_here),
	(43, submitter_id_api_here);

	INSERT INTO SubmissionContract(submission_id, submission_contract) VALUES
	(1, 'Votable'),--First user meal from ingredients
	(1, 'Reportable'),--First user meal from ingredients
	(5, 'Votable'),--Second user meal from food api
	(5, 'Reportable'),--Second user meal from food api
	(5, 'API'),--Second user meal from food api
	(6, 'Votable'),--First restaurant from user location
	(6, 'Reportable'),--First restaurant from user location
	(7, 'Votable'),--Second restaurant from restaurant api
	(7, 'API'),--Second restaurant from restaurant api
	(10, 'API'), --cuisine_here_api_id_american
	(11, 'API'), --cuisine_here_api_id_asian
	(12, 'API'), --cuisine_here_api_id_belgian
	(13, 'API'), --cuisine_here_api_id_burger
	(14, 'API'), --cuisine_here_api_id_canadian
	(15, 'API'), --cuisine_here_api_id_chinese
	(16, 'API'), --cuisine_here_api_id_crepes
	(17, 'API'), --cuisine_here_api_id_cuban
	(18, 'API'), --cuisine_here_api_id_french
	(19, 'API'), --cuisine_here_api_id_greek
	(20, 'API'), --cuisine_here_api_id_grill
	(21, 'API'), --cuisine_here_api_id_ice_cream
	(22, 'API'), --cuisine_here_api_id_international
	(23, 'API'), --cuisine_here_api_id_italian
	(24, 'API'), --cuisine_here_api_id_japanese
	(25, 'API'), --cuisine_here_api_id_korean
	(26, 'API'), --cuisine_here_api_id_mexican
	(27, 'API'), --cuisine_here_api_id_mexican
	(28, 'API'), --cuisine_here_api_id_pakistani
	(29, 'API'), --cuisine_here_api_id_pizza
	(30, 'API'), --cuisine_here_api_id_portuguese
	(31, 'API'), --cuisine_here_api_id_sandwich
	(32, 'API'), --cuisine_here_api_id_seafood
	(33, 'API'), --cuisine_here_api_id_spanish
	(34, 'API'), --cuisine_here_api_id_steak
	(35, 'API'), --cuisine_here_api_id_street_food
	(36, 'API'), --cuisine_here_api_id_pakistani
	(37, 'API'), --cuisine_here_api_id_swiss
	(38, 'API'), --cuisine_here_api_id_tapas
	(39, 'API'), --cuisine_here_api_id_thai
	(40, 'API'), --cuisine_here_api_id_vegan
	(41, 'API'), --cuisine_here_api_id_vegetarian
	(42, 'API'), --cuisine_here_api_id_vietnamese
	(43, 'API'); --cuisine_here_api_id_vietnamese2
	
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

	INSERT INTO RestaurantCuisine(restaurant_submission_id, cuisine_id) VALUES
	(6, 14),-- First restaurant from user location (14 = Belgian)
	(7, 40); -- Second restaurant from restaurant api (30 = Deserts)

	INSERT INTO MealCuisine(meal_submission_id, cuisine_id) VALUES
	(1, 14),--First user meal from ingredients, First restaurant from user location cuisine (14 = Belgian)
	(5, 40);--Second user meal from food api, Second restaurant from restaurant api cuisine
END $$;