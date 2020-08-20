DO $$ 
--Test variables declaration:
DECLARE
	submitter_id_api_zomato INTEGER := 1;
	submitter_name_api_zomato varchar(20) := 'Zomato';
	submitter_id_api_here INTEGER := 2;
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
	(submitter_name_api_zomato, 'API'),
	(submitter_name_api_here, 'API'),
	('User', 'User');

	INSERT INTO Api(submitter_id, api_token) VALUES
	(submitter_id_api_zomato, '456'),
	(submitter_id_api_here, 'here:123');	
	
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
END $$;