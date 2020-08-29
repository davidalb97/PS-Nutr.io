CREATE OR REPLACE FUNCTION AddFood(name varchar, carbs integer, quantity integer, type varchar) 
RETURNS void
AS $$ 
	BEGIN 
		INSERT INTO Submission(submission_type) VALUES ('Meal');
		
		DECLARE 
			submissionId INTEGER := lastval();
		BEGIN
			INSERT INTO Meal(submission_id, meal_name, carbs, quantity, unit, meal_type) VALUES (submissionId, name, carbs, quantity, 'gr', type);
			--Make Food favorable
			INSERT INTO SubmissionContract(submission_id, submission_contract) VALUES (submissionId, 'Favorable');
		END;
	END;
$$ LANGUAGE plpgsql;