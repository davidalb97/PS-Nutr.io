-- ############################## SubmissionPortion insertion function ##############################
CREATE FUNCTION portionSubmissionInsertion
(
	_submission_type varchar(5),
	_submitter_id integer,
	_quantity integer,
	_meal_id integer
) RETURNS VOID AS $$	
	BEGIN
		SET TRANSACTION ISOLATION LEVEL serializable;
		INSERT INTO SubmissionSubmitter(submission_type, submitter_id) VALUES
		(_submission_type, _submitter_id);
	
		IF NOT EXISTS (
			SELECT * FROM SubmissionMeal WHERE meal_id = _meal_id -- TODO: INNER JOIN
		) THEN			
			RAISE NOTICE 'The meal where you are associating the portion does not exist.';
			ROLLBACK;
		END IF;
	
		INSERT INTO SubmissionPortion(meal_id, quantity) VALUES 
			(_meal_id, _quantity); 
		
	END; $$
	LANGUAGE PLPGSQL;
	
	
-- ############################## SubmissionPortion deletion function ##############################
-- TODO

-- ############################## SubmissionPortion update function ##############################
-- TODO