DO $$ 
--Test variables declaration:
DECLARE
	submitter_id_api_spoonacular INTEGER := 1;
	submitter_name_api_spoonacular varchar(20) := 'Spoonacular';
	submitter_id_api_zomato INTEGER := 2;
	submitter_name_api_zomato varchar(20) := 'Zomato';
	submitter_id_api_here INTEGER := 3;
	submitter_name_api_here varchar(20) := 'Here';
	cuisineNames varchar[] := ARRAY[
		'African',--10
		'Alentejana',
		'American',--12 Here cuisine_here_api_id_american
		'Angolan',
		'Arabian',
		'Argentine',
		'Asian',--16 Here cuisine_here_api_id_asian
		'Austrian',
		'Author',
		'BBQ',
		'Bakery',
		'Bangladeshi',
		'Bar Food',
		'Belgian',--23 Here cuisine_here_api_id_belgian
		'Beverages',
		'Brazilian',
		'British',
		'Burger',--27 Here cuisine_here_api_id_burger
		'Cafe',
		'Cafe Food',
		'Canadian',--30 Here cuisine_here_api_id_canadian
		'Cape Verdean',
		'Caribbean',
		'Chinese',--33 Here cuisine_here_api_id_chinese
		'Coffee',
		'Coffee and Tea',
		'Contemporary',
		'Crepes',--37 Here cuisine_here_api_id_crepes
		'Cuban',--38 Here cuisine_here_api_id_cuban
		'Desserts',
		'Eastern European',
		'Filipino',
		'Finger Food',
		'French',--43 Here cuisine_here_api_id_french
		'Fresh Fish',
		'Fusion',
		'Goan',
		'Gourmet Fast Food',
		'Greek',--48 Here cuisine_here_api_id_greek
		'Grill',--49 Here cuisine_here_api_id_grill
		'Healthy Food',
		'Ice Cream',--51 Here cuisine_here_api_id_ice_cream
		'International',--52 Here cuisine_here_api_id_international
		'Iranian',
		'Irish',
		'Israeli',
		'Italian',--56 Here cuisine_here_api_id_italian
		'Japanese',--57 Here cuisine_here_api_id_japanese
		'Juices',
		'Kebab',
		'Korean',--60 Here cuisine_here_api_id_korean
		'Latin American',
		'Lebanese',
		'Madeiran',
		'Malaysian',
		'Mediterranean',--65 Here cuisine_here_api_id_mexican
		'Mexican',--66 Here cuisine_here_api_id_mexican
		'Middle Eastern',
		'Mineira',
		'Minhota',
		'Moroccan',
		'Mozambican',
		'Nepalese',
		'Oriental',
		'Pakistani',--74 Here cuisine_here_api_id_pakistani
		'Patisserie',
		'Peruvian',
		'Petiscos',
		'Pizza',--78 Here cuisine_here_api_id_pizza
		'Portuguese',--79 Here cuisine_here_api_id_portuguese
		'Ramen',
		'Russian',
		'Salad',
		'Sandwich',--83 Here cuisine_here_api_id_sandwich
		'Santomean',
		'Seafood',--85 Here cuisine_here_api_id_seafood
		'Snack Bar',
		'Snacks',
		'Spanish',--88 Here cuisine_here_api_id_spanish
		'Steak',--89 Here cuisine_here_api_id_steak
		'Street Food',--90 Here cuisine_here_api_id_street_food
		'Sushi',--91 Here cuisine_here_api_id_pakistani
		'Swedish',
		'Swiss',--93 Here cuisine_here_api_id_swiss
		'Tapas',--94 Here cuisine_here_api_id_tapas
		'Tea',
		'Thai',--96 Here cuisine_here_api_id_thai
		'Tibetan',
		'Transmontana',
		'Turkish',
		'Vegan',--100 Here cuisine_here_api_id_vegan
		'Vegetarian',--101 Here cuisine_here_api_id_vegetarian
		'Vietnamese'--102 Here cuisine_here_api_id_vietnamese & cuisine_here_api_id_vietnamese2
	];
	cuisine_here_api_id_american varchar(20) := '101-000';
	cuisine_here_api_id_asian varchar(20) := '200-000';
	cuisine_here_api_id_belgian varchar(20) := '307-000';
	cuisine_here_api_id_burger varchar(20) := '800-067';
	cuisine_here_api_id_canadian varchar(20) := '103-000';
	cuisine_here_api_id_chinese varchar(20) := '201-000';
	cuisine_here_api_id_crepes varchar(20) := '800-068';
	cuisine_here_api_id_cuban varchar(20) := '153-000';
	cuisine_here_api_id_french varchar(20) := '301-000';
	cuisine_here_api_id_greek varchar(20) := '303-000';
	cuisine_here_api_id_grill varchar(20) := '800-078';
	cuisine_here_api_id_ice_cream varchar(20) := '800-063';
	cuisine_here_api_id_international varchar(20) := '800-064';
	cuisine_here_api_id_italian varchar(20) := '304-000';
	cuisine_here_api_id_japanese varchar(20) := '203-000';
	cuisine_here_api_id_korean varchar(20) := '207-000';
	cuisine_here_api_id_mediterranean varchar(20) := '372-000';
	cuisine_here_api_id_mexican varchar(20) := '102-000';
	cuisine_here_api_id_pakistani varchar(20) := '208-000';
	cuisine_here_api_id_pizza varchar(20) := '800-057';
	cuisine_here_api_id_portuguese varchar(20) := '313-000';
	cuisine_here_api_id_sandwich varchar(20) := '800-060';
	cuisine_here_api_id_seafood varchar(20) := '800-075';
	cuisine_here_api_id_spanish varchar(20) := '311-000';
	cuisine_here_api_id_steak varchar(20) := '800-056';
	cuisine_here_api_id_street_food varchar(20) := '800-058';
	cuisine_here_api_id_sushi varchar(20) := '203-026';
	cuisine_here_api_id_swiss varchar(20) := '310-000';
	cuisine_here_api_id_tapas varchar(20) := '311-034';
	cuisine_here_api_id_thai varchar(20) := '205-000';
	cuisine_here_api_id_vegan varchar(20) := '800-076';
	cuisine_here_api_id_vegetarian varchar(20) := '800-077';
	cuisine_here_api_id_vietnamese varchar(20) := '206-000';
	cuisine_here_api_id_vietnamese2 varchar(20) := '800-085';
	cuisineHereApiIds varchar[] := ARRAY[
		cuisine_here_api_id_american,
		cuisine_here_api_id_asian,
		cuisine_here_api_id_belgian,
		cuisine_here_api_id_burger,
		cuisine_here_api_id_canadian,
		cuisine_here_api_id_chinese,
		cuisine_here_api_id_crepes,
		cuisine_here_api_id_cuban,
		cuisine_here_api_id_french,
		cuisine_here_api_id_greek,
		cuisine_here_api_id_grill,
		cuisine_here_api_id_ice_cream,
		cuisine_here_api_id_international,
		cuisine_here_api_id_italian,
		cuisine_here_api_id_japanese,
		cuisine_here_api_id_korean,
		cuisine_here_api_id_mediterranean,
		cuisine_here_api_id_mexican,
		cuisine_here_api_id_pakistani,
		cuisine_here_api_id_pizza,
		cuisine_here_api_id_portuguese,
		cuisine_here_api_id_sandwich,
		cuisine_here_api_id_seafood,
		cuisine_here_api_id_spanish,
		cuisine_here_api_id_steak,
		cuisine_here_api_id_street_food,
		cuisine_here_api_id_sushi,
		cuisine_here_api_id_swiss,
		cuisine_here_api_id_tapas,
		cuisine_here_api_id_thai,
		cuisine_here_api_id_vegan,
		cuisine_here_api_id_vegetarian,
		cuisine_here_api_id_vietnamese,
		cuisine_here_api_id_vietnamese2
	];
	--Associates ApiCuisine apiIds with Cuisine name array
	cuisineHereCuisineSubmissionIdx integer[] := ARRAY[
		3, --cuisine_here_api_id_american
		7, --cuisine_here_api_id_asian
		14, --cuisine_here_api_id_belgian
		18, --cuisine_here_api_id_burger
		21, --cuisine_here_api_id_canadian
		24, --cuisine_here_api_id_chinese
		28, --cuisine_here_api_id_crepes
		29, --cuisine_here_api_id_cuban
		34, --cuisine_here_api_id_french
		39, --cuisine_here_api_id_greek
		40, --cuisine_here_api_id_grill
		42, --cuisine_here_api_id_ice_cream
		43, --cuisine_here_api_id_international
		47, --cuisine_here_api_id_italian
		48, --cuisine_here_api_id_japanese
		51, --cuisine_here_api_id_korean
		56, --cuisine_here_api_id_mexican
		57, --cuisine_here_api_id_mexican
		65, --cuisine_here_api_id_pakistani
		69, --cuisine_here_api_id_pizza
		70, --cuisine_here_api_id_portuguese
		74, --cuisine_here_api_id_sandwich
		76, --cuisine_here_api_id_seafood
		79, --cuisine_here_api_id_spanish
		80, --cuisine_here_api_id_steak
		81, --cuisine_here_api_id_street_food
		82, --cuisine_here_api_id_pakistani
		84, --cuisine_here_api_id_swiss
		85, --cuisine_here_api_id_tapas
		87, --cuisine_here_api_id_thai
		91, --cuisine_here_api_id_vegan
		92, --cuisine_here_api_id_vegetarian
		93, --cuisine_here_api_id_vietnamese
		93 --cuisine_here_api_id_vietnamese2
	];
BEGIN 
	
	INSERT INTO Submitter(submitter_name, submitter_type) VALUES
	(submitter_name_api_spoonacular, 'API'),
	(submitter_name_api_zomato, 'API'),
	(submitter_name_api_here, 'API');

	INSERT INTO Api(submitter_id, api_token) VALUES
	(submitter_id_api_spoonacular, '123'),
	(submitter_id_api_zomato, '456');
	
	--Insert hardcoded cuisines
	FOR cuisineIdx in 1 .. array_length(cuisineNames, 1)
	LOOP
		INSERT INTO Submission(submission_type) VALUES ('Cuisine');
		DECLARE 
			last_cuisine_submission_id INTEGER := lastval();
			apiIdx INTEGER;
		BEGIN
			INSERT INTO SubmissionContract(submission_id, submission_contract) VALUES (last_cuisine_submission_id, 'Favorable');
			INSERT INTO Cuisine(submission_id, cuisine_name) VALUES (last_cuisine_submission_id, cuisineNames[cuisineIdx]);
			
			--If this cuisine has here api ids
			FOREACH apiIdx IN ARRAY array_positions(cuisineHereCuisineSubmissionIdx, cuisineIdx)
			LOOP
				INSERT INTO Submission(submission_type) VALUES ('ApiCuisine');
				INSERT INTO SubmissionSubmitter(submission_id, submitter_id) VALUES (lastval(), submitter_id_api_here);
				INSERT INTO SubmissionContract(submission_id, submission_contract) VALUES (lastval(), 'API');
				INSERT INTO ApiSubmission(submission_id, apiId) VALUES (lastval(), cuisineHereApiIds[apiIdx]);
				INSERT INTO ApiCuisine(submission_id, cuisine_submission_id) VALUES (lastval(), last_cuisine_submission_id);
			END LOOP;
		END;
	END LOOP;
	
	--Test variables declaration:
	DECLARE
		submitter_id_user_admin INTEGER := currval(pg_get_serial_sequence('Submitter', 'submitter_id')) + 1;
		submitter_name_user_admin varchar(20) := 'Admin';
		firstTestSubmissionId INTEGER := lastval() + 1;
		mealFromIngredientsId INTEGER := firstTestSubmissionId;
		mealIngredient1Id INTEGER := firstTestSubmissionId + 1;
		mealIngredient2Id INTEGER := firstTestSubmissionId + 2;
		mealIngredient3Id INTEGER := firstTestSubmissionId + 3;
		mealFromApiId INTEGER := firstTestSubmissionId + 4;
		restaurantFromLocationId INTEGER := firstTestSubmissionId + 5;
		restaurantFromApiId INTEGER := firstTestSubmissionId + 6;
		restaurantMealWithLocationWithIngredients INTEGER := firstTestSubmissionId + 7;
		restaurantMealWithLocationWithoutIngredients INTEGER := firstTestSubmissionId + 8;
		restaurantMealWithApiWithIngredients INTEGER := firstTestSubmissionId + 9;
		restaurantMealWithApiWithoutIngredients INTEGER := firstTestSubmissionId + 10;
		portionRestaurantMealWithLocationWithIngredients INTEGER := firstTestSubmissionId + 11;
		portionRestaurantMealWithLocationWithoutIngredients INTEGER := firstTestSubmissionId + 12;
		portionRestaurantMealWithApiWithIngredients INTEGER := firstTestSubmissionId + 13;
		portionRestaurantMealWithApiWithoutIngredients INTEGER := firstTestSubmissionId + 14;
	BEGIN
		--Test values insertion:
		INSERT INTO Submitter(submitter_name, submitter_type) VALUES
		(submitter_name_user_admin, 'User');
		
		INSERT INTO _User(submitter_id, email, session_secret) VALUES
		(submitter_id_user_admin, 'admin@gmail.com', '123456789');
		
		INSERT INTO Submission(submission_type) VALUES
		('Meal'),--First user meal from ingredients
		('Ingredient'),--First user meal ingredients from food api
		('Ingredient'),--First user meal ingredients from food api
		('Ingredient'),--First user meal ingredients from food api
		('Meal'),--Second user meal from food api
		('Restaurant'),--First restaurant from user location
		('Restaurant'),--Second restaurant from restaurant api
		('RestaurantMeal'),--Restaurant from user location + Meal from ingredients
		('RestaurantMeal'),--Rrestaurant from user location + Meal from api
		('RestaurantMeal'),--Restaurant from restaurant api + Meal from ingredients
		('RestaurantMeal'),--Restaurant from restaurant api + Meal from api
		('Portion'),--Restaurant from user location + Meal from ingredients
		('Portion'),--Rrestaurant from user location + Meal from api
		('Portion'),--Restaurant from restaurant api + Meal from ingredients
		('Portion');--Restaurant from restaurant api + Meal from api

		INSERT INTO ApiSubmission(submission_id, apiId) VALUES
		(mealIngredient1Id, 	'0'),--First user meal ingredients from food api
		(mealIngredient2Id, 	'1'),--First user meal ingredients from food api
		(mealIngredient3Id, 	'2'),--First user meal ingredients from food api
		(mealFromApiId,			'3'),--Second user meal from food api
		(restaurantFromApiId,	'4');--Second restaurant from restaurant api

		INSERT INTO SubmissionSubmitter(submission_id, submitter_id) VALUES
		(mealFromIngredientsId,									submitter_id_user_admin),
		(mealFromIngredientsId,									submitter_id_api_spoonacular),
		(mealIngredient1Id,										submitter_id_api_spoonacular),
		(mealIngredient2Id,										submitter_id_api_spoonacular),
		(mealIngredient3Id,										submitter_id_api_spoonacular),
		(mealFromApiId,											submitter_id_user_admin),--(user)
		(mealFromApiId,											submitter_id_api_spoonacular),--(api)
		(restaurantFromLocationId,								submitter_id_user_admin),
		(restaurantFromApiId,									submitter_id_user_admin),--(user)
		(restaurantFromApiId,									submitter_id_api_zomato),--(api)
		(restaurantMealWithLocationWithIngredients,				submitter_id_user_admin),
		(restaurantMealWithLocationWithoutIngredients,			submitter_id_user_admin),
		(restaurantMealWithApiWithIngredients,					submitter_id_user_admin),
		(restaurantMealWithApiWithoutIngredients,				submitter_id_user_admin),
		(portionRestaurantMealWithLocationWithIngredients,		submitter_id_user_admin),
		(portionRestaurantMealWithLocationWithoutIngredients,	submitter_id_user_admin),
		(portionRestaurantMealWithApiWithIngredients,			submitter_id_user_admin),
		(portionRestaurantMealWithApiWithoutIngredients,		submitter_id_user_admin);
		
		INSERT INTO Votes(submission_id, positive_count, negative_count) VALUES
		(restaurantFromLocationId, 20, 20),
		(restaurantFromApiId, 45, 36),
		(restaurantMealWithLocationWithIngredients, 10, 23),
		(restaurantMealWithLocationWithoutIngredients, 443, 1276),
		(restaurantMealWithApiWithIngredients, 1235, 643),
		(restaurantMealWithApiWithoutIngredients, 13423, 12563);
		
		INSERT INTO UserVote(submission_id, vote_submitter_id, vote) VALUES
		(restaurantMealWithLocationWithIngredients,		submitter_id_user_admin, false),
		(restaurantMealWithLocationWithoutIngredients,	submitter_id_user_admin, true),
		(restaurantMealWithApiWithIngredients,			submitter_id_user_admin, true),
		(restaurantMealWithApiWithoutIngredients,		submitter_id_user_admin, false),
		(restaurantFromLocationId,						submitter_id_user_admin, false),
		(restaurantFromApiId,							submitter_id_user_admin, true);

		INSERT INTO Report(submission_id, submitter_id, description) VALUES
		(restaurantMealWithLocationWithIngredients,		submitter_id_user_admin, 'Debug description 1'),
		(restaurantMealWithLocationWithoutIngredients,	submitter_id_user_admin, 'Debug description 2'),
		(restaurantMealWithApiWithIngredients,			submitter_id_user_admin, 'Debug description 3'),
		(restaurantMealWithApiWithoutIngredients,		submitter_id_user_admin, 'Debug description 4');

		INSERT INTO Restaurant(submission_id, restaurant_name, latitude, longitude) VALUES
		(restaurantFromLocationId,	'First rest from user coords', 0.0, 0.0),
		(restaurantFromApiId,		'Second rest from api', 1.0, 1.0);

		INSERT INTO Meal(submission_id, meal_name) VALUES
		(mealFromIngredientsId,		'First meal from ingr'),
		(mealFromApiId,				'Second meal from api');

		INSERT INTO Ingredient(submission_id, ingredient_name) VALUES
		(mealIngredient1Id, 'açucar'),
		(mealIngredient2Id, 'sal'),
		(mealIngredient3Id, 'azeite');

		INSERT INTO MealIngredient(meal_submission_id, ingredient_submission_id) VALUES
		(mealFromIngredientsId, mealIngredient1Id),
		(mealFromIngredientsId, mealIngredient2Id),
		(mealFromIngredientsId, mealIngredient3Id);

		INSERT INTO RestaurantMeal(submission_id, restaurant_submission_id, meal_submission_id) VALUES
		(restaurantMealWithLocationWithIngredients, 	restaurantFromLocationId,	mealFromIngredientsId),
		(restaurantMealWithLocationWithoutIngredients,	restaurantFromLocationId,	mealFromApiId),
		(restaurantMealWithApiWithIngredients,			restaurantFromApiId,		mealFromIngredientsId),
		(restaurantMealWithApiWithoutIngredients,		restaurantFromApiId,		mealFromApiId);
		
		INSERT INTO Favorite(submission_id, submitter_id) VALUES
		(restaurantMealWithLocationWithIngredients,		submitter_id_user_admin),
		(restaurantMealWithLocationWithoutIngredients,	submitter_id_user_admin),
		(restaurantMealWithApiWithIngredients,			submitter_id_user_admin),
		(restaurantMealWithApiWithoutIngredients,		submitter_id_user_admin);
		
		INSERT INTO Portion(submission_id, restaurant_meal_submission_id, quantity) VALUES
		(portionRestaurantMealWithLocationWithIngredients,		restaurantMealWithLocationWithIngredients, 100),
		(portionRestaurantMealWithLocationWithoutIngredients,	restaurantMealWithLocationWithoutIngredients, 200),
		(portionRestaurantMealWithApiWithIngredients,			restaurantMealWithApiWithIngredients, 300),
		(portionRestaurantMealWithApiWithoutIngredients,		restaurantMealWithApiWithoutIngredients, 400);

		INSERT INTO SubmissionContract(submission_id, submission_contract) VALUES
		(mealFromIngredientsId,							'API'),
		(mealFromIngredientsId,							'Favorable'),
		(mealIngredient1Id,								'API'),
		(mealIngredient2Id, 							'API'),
		(mealIngredient3Id,								'API'),
		(mealFromApiId,									'API'),
		(mealFromApiId,									'Favorable'),
		(restaurantFromLocationId,						'Votable'),
		(restaurantFromLocationId,						'Reportable'),
		(restaurantFromLocationId,						'Favorable'),
		(restaurantFromApiId,							'Votable'),
		(restaurantFromApiId,							'API'),
		(restaurantFromApiId,							'Favorable'),
		(restaurantMealWithLocationWithIngredients, 	'Votable'),
		(restaurantMealWithLocationWithIngredients, 	'Reportable'),
		(restaurantMealWithLocationWithIngredients, 	'Favorable'),
		(restaurantMealWithLocationWithoutIngredients, 	'Votable'),
		(restaurantMealWithLocationWithoutIngredients, 	'Reportable'),
		(restaurantMealWithLocationWithoutIngredients, 	'Favorable'),
		(restaurantMealWithApiWithIngredients,			'Votable'),
		(restaurantMealWithApiWithIngredients,			'Reportable'),
		(restaurantMealWithApiWithIngredients,			'Favorable'),
		(restaurantMealWithApiWithoutIngredients,		'Votable'),
		(restaurantMealWithApiWithoutIngredients,		'Reportable'),
		(restaurantMealWithApiWithoutIngredients,		'Favorable');

		INSERT INTO RestaurantCuisine(restaurant_submission_id, cuisine_submission_id) VALUES
		(restaurantFromLocationId,	1),--(1 = African)
		(restaurantFromApiId,		2);--(2 = Alentejana)

		INSERT INTO MealCuisine(meal_submission_id, cuisine_submission_id) VALUES
		(mealFromIngredientsId,	1),--(1 = African)
		(mealFromApiId,			2);--(2 = Alentejana)
	END;
END $$;