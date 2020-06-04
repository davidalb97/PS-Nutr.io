CREATE OR REPLACE FUNCTION AddIngredient(name varchar, carbs integer) 
RETURNS void
AS $$ 
	BEGIN 
		INSERT INTO Submission(submission_type) VALUES ('Ingredient');
		
		DECLARE 
			ingredientSubmissionId INTEGER := lastval();
		BEGIN
			INSERT INTO Ingredient(submission_id, ingredient_name, carbs) VALUES (ingredientSubmissionId, name, carbs);
			--Make Ingredient favorable
			INSERT INTO SubmissionContract(submission_id, submission_contract) VALUES (ingredientSubmissionId, 'Favorable');
		END;
	END;
$$ LANGUAGE plpgsql;