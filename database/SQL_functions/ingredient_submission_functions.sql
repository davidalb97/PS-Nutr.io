-- ############################## SubmissionIngredient insertion function ##############################
CREATE FUNCTION ingredientSubmissionInsertion
(
	_submission_type varchar(5),
	_submitter_id integer,
	_ingredient_name varchar(20),
	_meal_id integer
) RETURNS VOID AS $$
	DECLARE
		ingredientID integer;
	BEGIN
		INSERT INTO SubmissionSubmitter(submission_type, submitter_id) VALUES
		(_submission_type, _submitter_id);
	
		IF NOT EXISTS (
			SELECT * FROM SubmissionMeal WHERE meal_id = _meal_id -- TODO: INNER JOIN
		) THEN			
			RAISE NOTICE 'The meal where you are associating the ingredient does not exist.';
			ROLLBACK;
		END IF;
	
		IF EXISTS (
			SELECT ingredient_id INTO ingredientID FROM Ingredient WHERE ingredient_name = _ingredient_name
		) THEN						
			INSERT INTO SubmissionIngredient(meal_id, ingredient_id) VALUES
			(_meal_id, ingredientID);
			RETURN;
		END IF;
		
		INSERT INTO Ingredient(ingredient_name) VALUES (_ingredient_name);
		INSERT INTO SubmissionIngredient(meal_id, ingredient_id) VALUES 
			(_meal_id, ingredientID); 
		
	END; $$
	LANGUAGE PLPGSQL;
	
-- ############################## SubmissionIngredient deletion function ##############################
-- TODO

-- ############################## SubmissionIngredient update function ##############################
-- TODO
