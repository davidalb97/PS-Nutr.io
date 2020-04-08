-- ############################## SubmissionMeal insertion function ##############################

CREATE OR REPLACE FUNCTION mealSubmissionInsertion
(
	_submission_type varchar(5),
	_submitter_id integer,
	_meal_name varchar(20),
	_cuisine_name varchar(20),
	_restaurant_id integer,
	ingredient_ids integer[]
) RETURNS VOID AS $$
	DECLARE 
	mealId integer = 0;
	ingredient_id integer = 0;
	BEGIN

		SET TRANSACTION ISOLATION LEVEL serializable;
		
		--Restaurant existance check
		IF NOT EXISTS (
			SELECT * FROM Restaurant WHERE restaurant_id = _restaurant_id 
		) THEN			
			RAISE NOTICE 'The restaurant where you are associating the meal does not exist.';
			ROLLBACK;
		END IF;
		
		--A new meal must be created for:
		-- * A meal with ingredients
		-- * A new meal
		IF (array_length(ingredient_ids, 1) > 0 OR NOT EXISTS (SELECT * FROM Meal WHERE meal_name = _meal_name )) THEN
			
			INSERT INTO Meal(meal_name, cuisine_name)
			VALUES(_meal_name, _cuisine_name);
			
			--Fetch the inserted meal_id
			SELECT * INTO mealId FROM currval(pg_get_serial_sequence('Meal', 'meal_id'));

		--If the default meal already exists
		ELSE
			SELECT meal_id INTO mealId FROM Meal WHERE meal_name = _meal_name;
		END IF;
		
        INSERT INTO SubmissionSubmitter(submission_type, submitter_id)
		VALUES(_submission_type, _submitter_id);
		
		INSERT INTO SubmissionMeal(meal_id, restaurant_id)
		VALUES(mealId, _restaurant_id);
		
		--Insert each ingredient
		IF(array_length(ingredient_ids, 1) > 0) THEN
			
			FOREACH ingredient_id IN ARRAY ingredient_ids
			LOOP 
				INSERT INTO SubmissionIngredient(meal_id, ingredient_id)
				VALUES (_meal_id, ingredientID); 
			END LOOP;
			
			INSERT INTO SubmissionIngredient(meal_id, ingredient_id)
			VALUES (_meal_id, ingredient_ids);
		END IF;
		
		COMMIT;
	END; $$
	LANGUAGE PLPGSQL;
	
-- ############################## SubmissionMeal deletion function ##############################
-- TODO


-- ############################## SubmissionMeal update function ##############################
-- TODO
