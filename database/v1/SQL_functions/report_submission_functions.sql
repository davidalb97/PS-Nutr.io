-- ############################## Auxiliar function (compile just once)##############################

-- No need for transaction isolation, because 
-- this function will be always called inside
-- isolated transactions
CREATE OR REPLACE FUNCTION verifySubmissionSubmitter(_submission_id integer)
RETURNS VOID AS $$
	BEGIN
		IF NOT EXISTS (
			SELECT * FROM SubmissionSubmitter WHERE submission_id = _submission_id
		) THEN			
			RAISE NOTICE 'The submission cannot be voted because it does not exist.';
		END IF;
	END $$
	LANGUAGE PLPGSQL;

-- ############################## reportSubmission insertion function ##############################

CREATE OR REPLACE FUNCTION voteSubmissionInsertion
(
	_submission_id integer,
	_submitter_id integer,
	_description varchar(500)
) RETURNS VOID AS $$	
	BEGIN
		SET TRANSACTION ISOLATION LEVEL serializable;	
		SELECT verifySubmissionSubmitter(_submission_id);
        INSERT INTO Report(submission_id, submitter_id, description) VALUES
		(_submission_id, _submitter_id, _description);		
	END; $$
	LANGUAGE PLPGSQL;