-- ############################## SubmissionMeal insertion function ##############################

CREATE FUNCTION mealSubmissionInsertion
(
	_submission_type varchar(5),
	_submitter_id integer,
	_meal_name varchar(20),
	_cuisine_name varchar(20),
	_restaurant_id integer
) RETURNS VOID AS $$
	DECLARE 
		mealId integer = 0;
	BEGIN		
		IF EXISTS (
			SELECT * FROM Meal WHERE meal_name = _meal_name 
		) THEN			
			RAISE NOTICE 'The inserted meal already exists in the database.';
		END IF;
		
		IF NOT EXISTS (
			SELECT * FROM Restaurant WHERE restaurant_id = _restaurant_id 
		) THEN			
			RAISE NOTICE 'The restaurant where you are associating the meal does not exist.';
		END IF;
		
        INSERT INTO SubmissionSubmitter(submission_type, submitter_id) VALUES
		(_submission_type, _submitter_id);		
		
		INSERT INTO Meal(meal_name, cuisine_name) VALUES
		(_meal_name, _cuisine_name);
		
		SELECT meal_id INTO mealId FROM Meal WHERE meal_name = _meal_name;
		INSERT INTO SubmissionMeal(meal_id, restaurant_id) VALUES
		(mealId, _restaurant_id);
		
	END; $$
	LANGUAGE PLPGSQL;
	
-- ############################## SubmissionMeal deletion function ##############################
-- TODO

-- ############################## SubmissionMeal update function ##############################
-- TODO
