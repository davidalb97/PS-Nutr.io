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
	
	INSERT INTO Submission(submission_type) VALUES
	('Meal'),--First user meal from ingredients
	('Ingredient'),--First user meal ingredients from food api
	('Ingredient'),--First user meal ingredients from food api
	('Ingredient'),--First user meal ingredients from food api
	('Meal'),--Second user meal from food api
	('Restaurant'),--First restaurant from user location
	('Restaurant'),--Second restaurant from restaurant api
	('Portion'),--First user meal from ingredients + First restaurant from user location
	('Portion'),--Second user meal from food api + First restaurant from user location
	('Cuisine'),--First hardcoded cuisine (submissionId = 10)
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
	('Cuisine'),--Last hardcoded cuisine (submissionId = 102)
	('ApiCuisine'),--First Here cuisine (submissionId = 103) Here cuisine_here_api_id_american
	('ApiCuisine'),--Here cuisine_here_api_id_asian
	('ApiCuisine'),--Here cuisine_here_api_id_belgian
	('ApiCuisine'),--Here cuisine_here_api_id_burger
	('ApiCuisine'),--Here cuisine_here_api_id_canadian
	('ApiCuisine'),--Here cuisine_here_api_id_chinese
	('ApiCuisine'),--Here cuisine_here_api_id_crepes
	('ApiCuisine'),--Here cuisine_here_api_id_cuban
	('ApiCuisine'),--Here cuisine_here_api_id_french
	('ApiCuisine'),--Here cuisine_here_api_id_greek
	('ApiCuisine'),--Here cuisine_here_api_id_grill
	('ApiCuisine'),--Here cuisine_here_api_id_ice_cream
	('ApiCuisine'),--Here cuisine_here_api_id_international
	('ApiCuisine'),--Here cuisine_here_api_id_italian
	('ApiCuisine'),--Here cuisine_here_api_id_japanese
	('ApiCuisine'),--Here cuisine_here_api_id_korean
	('ApiCuisine'),--Here cuisine_here_api_id_mediterranean
	('ApiCuisine'),--Here cuisine_here_api_id_mexican
	('ApiCuisine'),--Here cuisine_here_api_id_pakistani
	('ApiCuisine'),--Here cuisine_here_api_id_pizza
	('ApiCuisine'),--Here cuisine_here_api_id_portuguese
	('ApiCuisine'),--Here cuisine_here_api_id_sandwich
	('ApiCuisine'),--Here cuisine_here_api_id_seafood
	('ApiCuisine'),--Here cuisine_here_api_id_spanish
	('ApiCuisine'),--Here cuisine_here_api_id_steak
	('ApiCuisine'),--Here cuisine_here_api_id_street_food
	('ApiCuisine'),--Here cuisine_here_api_id_pakistani
	('ApiCuisine'),--Here cuisine_here_api_id_swiss
	('ApiCuisine'),--Here cuisine_here_api_id_tapas
	('ApiCuisine'),--Here cuisine_here_api_id_thai
	('ApiCuisine'),--Here cuisine_here_api_id_vegan
	('ApiCuisine'),--Here cuisine_here_api_id_vegetarian
	('ApiCuisine'),--Here cuisine_here_api_id_vietnamese
	('ApiCuisine'),--Last hardcoded cuisine (submissionId = 136) Here cuisine_here_api_id_vietnamese2
	('RestaurantMeal'),--First user meal from ingredients + First restaurant from user location
	('RestaurantMeal'),--First user meal from ingredients + Second restaurant from restaurant api
	('RestaurantMeal'),--Second user meal from food api + First restaurant from user location
	('RestaurantMeal');--Second user meal from food api + Second restaurant from restaurant api
	
	INSERT INTO Cuisine(submission_id, cuisine_name) VALUES
	(10, 'African'),--10
	(11, 'Alentejana'),
	(12, 'American'),--12 Here cuisine_here_api_id_american
	(13, 'Angolan'),
	(14, 'Arabian'),
	(15, 'Argentine'),
	(16, 'Asian'),--16 Here cuisine_here_api_id_asian
	(17, 'Austrian'),
	(18, 'Author'),
	(19, 'BBQ'),
	(20, 'Bakery'),
	(21, 'Bangladeshi'),
	(22, 'Bar Food'),
	(23, 'Belgian'),--23 Here cuisine_here_api_id_belgian
	(24, 'Beverages'),
	(25, 'Brazilian'),
	(26, 'British'),
	(27, 'Burger'),--27 Here cuisine_here_api_id_burger
	(28, 'Cafe'),
	(29, 'Cafe Food'),
	(30, 'Canadian'),--30 Here cuisine_here_api_id_canadian
	(31, 'Cape Verdean'),
	(32, 'Caribbean'),
	(33, 'Chinese'),--33 Here cuisine_here_api_id_chinese
	(34, 'Coffee'),
	(35, 'Coffee and Tea'),
	(36, 'Contemporary'),
	(37, 'Crepes'),--37 Here cuisine_here_api_id_crepes
	(38, 'Cuban'),--38 Here cuisine_here_api_id_cuban
	(39, 'Desserts'),
	(40, 'Eastern European'),
	(41, 'Filipino'),
	(42, 'Finger Food'),
	(43, 'French'),--43 Here cuisine_here_api_id_french
	(44, 'Fresh Fish'),
	(45, 'Fusion'),
	(46, 'Goan'),
	(47, 'Gourmet Fast Food'),
	(48, 'Greek'),--48 Here cuisine_here_api_id_greek
	(49, 'Grill'),--49 Here cuisine_here_api_id_grill
	(50, 'Healthy Food'),
	(51, 'Ice Cream'),--51 Here cuisine_here_api_id_ice_cream
	(52, 'International'),--52 Here cuisine_here_api_id_international
	(53, 'Iranian'),
	(54, 'Irish'),
	(55, 'Israeli'),
	(56, 'Italian'),--56 Here cuisine_here_api_id_italian
	(57, 'Japanese'),--57 Here cuisine_here_api_id_japanese
	(58, 'Juices'),
	(59, 'Kebab'),
	(60, 'Korean'),--60 Here cuisine_here_api_id_korean
	(61, 'Latin American'),
	(62, 'Lebanese'),
	(63, 'Madeiran'),
	(64, 'Malaysian'),
	(65, 'Mediterranean'),--65 Here cuisine_here_api_id_mexican
	(66, 'Mexican'),--66 Here cuisine_here_api_id_mexican
	(67, 'Middle Eastern'),
	(68, 'Mineira'),
	(69, 'Minhota'),
	(70, 'Moroccan'),
	(71, 'Mozambican'),
	(72, 'Nepalese'),
	(73, 'Oriental'),
	(74, 'Pakistani'),--74 Here cuisine_here_api_id_pakistani
	(75, 'Patisserie'),
	(76, 'Peruvian'),
	(77, 'Petiscos'),
	(78, 'Pizza'),--78 Here cuisine_here_api_id_pizza
	(79, 'Portuguese'),--79 Here cuisine_here_api_id_portuguese
	(80, 'Ramen'),
	(81, 'Russian'),
	(82, 'Salad'),
	(83, 'Sandwich'),--83 Here cuisine_here_api_id_sandwich
	(84, 'Santomean'),
	(85, 'Seafood'),--85 Here cuisine_here_api_id_seafood
	(86, 'Snack Bar'),
	(87, 'Snacks'),
	(88, 'Spanish'),--88 Here cuisine_here_api_id_spanish
	(89, 'Steak'),--89 Here cuisine_here_api_id_steak
	(90, 'Street Food'),--90 Here cuisine_here_api_id_street_food
	(91, 'Sushi'),--91 Here cuisine_here_api_id_pakistani
	(92, 'Swedish'),
	(93, 'Swiss'),--93 Here cuisine_here_api_id_swiss
	(94, 'Tapas'),--94 Here cuisine_here_api_id_tapas
	(95, 'Tea'),
	(96, 'Thai'),--96 Here cuisine_here_api_id_thai
	(97, 'Tibetan'),
	(98, 'Transmontana'),
	(99, 'Turkish'),
	(100, 'Vegan'),--100 Here cuisine_here_api_id_vegan
	(101, 'Vegetarian'),--101 Here cuisine_here_api_id_vegetarian
	(102, 'Vietnamese');--102 Here cuisine_here_api_id_vietnamese & cuisine_here_api_id_vietnamese2
	
	INSERT INTO ApiCuisine(submission_id, cuisine_submission_id) VALUES 
	(103, 12), --cuisine_here_api_id_american
	(104, 16), --cuisine_here_api_id_asian
	(105, 23), --cuisine_here_api_id_belgian
	(106, 27), --cuisine_here_api_id_burger
	(107, 30), --cuisine_here_api_id_canadian
	(108, 33), --cuisine_here_api_id_chinese
	(109, 37), --cuisine_here_api_id_crepes
	(110, 38), --cuisine_here_api_id_cuban
	(111, 43), --cuisine_here_api_id_french
	(112, 48), --cuisine_here_api_id_greek
	(113, 49), --cuisine_here_api_id_grill
	(114, 51), --cuisine_here_api_id_ice_cream
	(115, 52), --cuisine_here_api_id_international
	(116, 56), --cuisine_here_api_id_italian
	(117, 57), --cuisine_here_api_id_japanese
	(118, 60), --cuisine_here_api_id_korean
	(119, 65), --cuisine_here_api_id_mexican
	(120, 66), --cuisine_here_api_id_mexican
	(121, 74), --cuisine_here_api_id_pakistani
	(122, 78), --cuisine_here_api_id_pizza
	(123, 79), --cuisine_here_api_id_portuguese
	(124, 83), --cuisine_here_api_id_sandwich
	(125, 85), --cuisine_here_api_id_seafood
	(126, 88), --cuisine_here_api_id_spanish
	(127, 89), --cuisine_here_api_id_steak
	(128, 90), --cuisine_here_api_id_street_food
	(129, 91), --cuisine_here_api_id_pakistani
	(130, 93), --cuisine_here_api_id_swiss
	(131, 94), --cuisine_here_api_id_tapas
	(132, 96), --cuisine_here_api_id_thai
	(133, 100), --cuisine_here_api_id_vegan
	(134, 101), --cuisine_here_api_id_vegetarian
	(135, 102), --cuisine_here_api_id_vietnamese
	(136, 102); --cuisine_here_api_id_vietnamese2

	INSERT INTO ApiSubmission(submission_id, apiId) VALUES
	(2, '0'),--First user meal ingredients from food api
	(3, '1'),--First user meal ingredients from food api
	(4, '2'),--First user meal ingredients from food api
	(5, '3'),--Second user meal from food api
	(7, '4'),--Second restaurant from restaurant api
	(103, cuisine_here_api_id_american),
	(104, cuisine_here_api_id_asian),
	(105, cuisine_here_api_id_belgian),
	(106, cuisine_here_api_id_burger),
	(107, cuisine_here_api_id_canadian),
	(108, cuisine_here_api_id_chinese),
	(109, cuisine_here_api_id_crepes),
	(110, cuisine_here_api_id_cuban),
	(111, cuisine_here_api_id_french),
	(112, cuisine_here_api_id_greek),
	(113, cuisine_here_api_id_grill),
	(114, cuisine_here_api_id_ice_cream),
	(115, cuisine_here_api_id_international),
	(116, cuisine_here_api_id_italian),
	(117, cuisine_here_api_id_japanese),
	(118, cuisine_here_api_id_korean),
	(119, cuisine_here_api_id_mexican),
	(120, cuisine_here_api_id_mexican),
	(121, cuisine_here_api_id_pakistani),
	(122, cuisine_here_api_id_pizza),
	(123, cuisine_here_api_id_portuguese),
	(124, cuisine_here_api_id_sandwich),
	(125, cuisine_here_api_id_seafood),
	(126, cuisine_here_api_id_spanish),
	(127, cuisine_here_api_id_steak),
	(128, cuisine_here_api_id_street_food),
	(129, cuisine_here_api_id_pakistani),
	(130, cuisine_here_api_id_swiss),
	(131, cuisine_here_api_id_tapas),
	(132, cuisine_here_api_id_thai),
	(133, cuisine_here_api_id_vegan),
	(134, cuisine_here_api_id_vegetarian),
	(135, cuisine_here_api_id_vietnamese),
	(136, cuisine_here_api_id_vietnamese2);

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
	(103, submitter_id_api_here),
	(104, submitter_id_api_here),
	(105, submitter_id_api_here),
	(106, submitter_id_api_here),
	(107, submitter_id_api_here),
	(108, submitter_id_api_here),
	(109, submitter_id_api_here),
	(110, submitter_id_api_here),
	(111, submitter_id_api_here),
	(112, submitter_id_api_here),
	(113, submitter_id_api_here),
	(114, submitter_id_api_here),
	(115, submitter_id_api_here),
	(116, submitter_id_api_here),
	(117, submitter_id_api_here),
	(118, submitter_id_api_here),
	(119, submitter_id_api_here),
	(120, submitter_id_api_here),
	(121, submitter_id_api_here),
	(122, submitter_id_api_here),
	(123, submitter_id_api_here),
	(124, submitter_id_api_here),
	(125, submitter_id_api_here),
	(126, submitter_id_api_here),
	(127, submitter_id_api_here),
	(128, submitter_id_api_here),
	(129, submitter_id_api_here),
	(130, submitter_id_api_here),
	(131, submitter_id_api_here),
	(132, submitter_id_api_here),
	(133, submitter_id_api_here),
	(134, submitter_id_api_here),
	(135, submitter_id_api_here),
	(136, submitter_id_api_here);

	INSERT INTO SubmissionContract(submission_id, submission_contract) VALUES
	(1, 'Votable'),--First user meal from ingredients
	(1, 'Reportable'),--First user meal from ingredients
	(2, 'API'),--ingredient Açucar
	(3, 'API'),--ingredient sal
	(4, 'API'),--ingredient azeite
	(5, 'Votable'),--Second user meal from food api
	(5, 'Reportable'),--Second user meal from food api
	(5, 'API'),--Second user meal from food api
	(6, 'Votable'),--First restaurant from user location
	(6, 'Reportable'),--First restaurant from user location
	(7, 'Votable'),--Second restaurant from restaurant api
	(7, 'API'),--Second restaurant from restaurant api
	(103, 'API'), --cuisine_here_api_id_american
	(104, 'API'), --cuisine_here_api_id_asian
	(105, 'API'), --cuisine_here_api_id_belgian
	(106, 'API'), --cuisine_here_api_id_burger
	(107, 'API'), --cuisine_here_api_id_canadian
	(108, 'API'), --cuisine_here_api_id_chinese
	(109, 'API'), --cuisine_here_api_id_crepes
	(110, 'API'), --cuisine_here_api_id_cuban
	(111, 'API'), --cuisine_here_api_id_french
	(112, 'API'), --cuisine_here_api_id_greek
	(113, 'API'), --cuisine_here_api_id_grill
	(114, 'API'), --cuisine_here_api_id_ice_cream
	(115, 'API'), --cuisine_here_api_id_international
	(116, 'API'), --cuisine_here_api_id_italian
	(117, 'API'), --cuisine_here_api_id_japanese
	(118, 'API'), --cuisine_here_api_id_korean
	(119, 'API'), --cuisine_here_api_id_mexican
	(120, 'API'), --cuisine_here_api_id_mexican
	(121, 'API'), --cuisine_here_api_id_pakistani
	(122, 'API'), --cuisine_here_api_id_pizza
	(123, 'API'), --cuisine_here_api_id_portuguese
	(124, 'API'), --cuisine_here_api_id_sandwich
	(125, 'API'), --cuisine_here_api_id_seafood
	(126, 'API'), --cuisine_here_api_id_spanish
	(127, 'API'), --cuisine_here_api_id_steak
	(128, 'API'), --cuisine_here_api_id_street_food
	(129, 'API'), --cuisine_here_api_id_pakistani
	(130, 'API'), --cuisine_here_api_id_swiss
	(131, 'API'), --cuisine_here_api_id_tapas
	(132, 'API'), --cuisine_here_api_id_thai
	(133, 'API'), --cuisine_here_api_id_vegan
	(134, 'API'), --cuisine_here_api_id_vegetarian
	(135, 'API'), --cuisine_here_api_id_vietnamese
	(136, 'API'); --cuisine_here_api_id_vietnamese2
	
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

	INSERT INTO RestaurantMeal(submission_id, meal_submission_id, restaurant_submission_id) VALUES
	(137, 1, 6),--First user meal from ingredients + First restaurant from user location
	(138, 1, 7),--First user meal from ingredients + Second restaurant from restaurant api
	(139, 5, 6),--Second user meal from food api + First restaurant from user location
	(140, 5, 7);--Second user meal from food api + Second restaurant from restaurant api
	
	INSERT INTO Portion(submission_id, restaurant_meal_submission_id, quantity) VALUES
	(8, 137, 100),--First user meal from ingredients + First restaurant from user location
	(9, 139, 300);--Second user meal from food api + First restaurant from user location

	INSERT INTO RestaurantCuisine(restaurant_submission_id, cuisine_submission_id) VALUES
	(6, 14),-- First restaurant from user location (14 = Belgian)
	(7, 40); -- Second restaurant from restaurant api (30 = Deserts)

	INSERT INTO MealCuisine(meal_submission_id, cuisine_submission_id) VALUES
	(1, 14),--First user meal from ingredients, First restaurant from user location cuisine (14 = Belgian)
	(5, 40);--Second user meal from food api, Second restaurant from restaurant api cuisine
END $$;