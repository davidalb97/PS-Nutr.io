CREATE OR REPLACE FUNCTION AddFood(_type varchar,name varchar, carbs integer, quantity integer) 
RETURNS void
AS $$ 
	BEGIN 
		INSERT INTO Submission(submission_type) VALUES (_type);
		
		DECLARE 
			submissionId INTEGER := lastval();
		BEGIN
		
		
			IF _type = 'Ingredient' THEN 
				INSERT INTO Ingredient(submission_id, ingredient_name, carbs, quantity) 
				VALUES (submissionId, name, carbs, quantity);
			END IF;
			
			IF _type = 'Meal' THEN 
				INSERT INTO Meal(submission_id, meal_name, carbs, quantity) 
				VALUES (submissionId, name, carbs, quantity);
			END IF;	
			
			--Make Food favorable
			INSERT INTO SubmissionContract(submission_id, submission_contract) VALUES (submissionId, 'Favorable');
		END;
	END;
$$ LANGUAGE plpgsql;