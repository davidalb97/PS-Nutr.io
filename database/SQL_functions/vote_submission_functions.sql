-- ############################## Auxiliar function (compile just once)##############################

-- No need for transaction isolation, because 
-- this function will be always called inside
-- isolated transactions
CREATE FUNCTION verifySubmissionSubmitter(_submission_id integer)
RETURNS VOID AS $$	
	BEGIN
		IF NOT EXISTS (
			SELECT * FROM SubmissionSubmitter WHERE submission_id = _submission_id
		) THEN			
			RAISE NOTICE 'The submission cannot be voted because it does not exist.';
		END IF;
	END $$
	LANGUAGE PLPGSQL;

-- ############################## voteSubmission insertion function ##############################

CREATE FUNCTION voteSubmissionInsertion
(
	_submission_id integer,
	_submitter_id integer,
	_vote boolean
) RETURNS VOID AS $$
	BEGIN		
		SET TRANSACTION ISOLATION LEVEL serializable;	
		SELECT verifySubmissionSubmitter(_submission_id);		
        INSERT INTO Votable(submission_id, submitter_id, vote) VALUES
		(_submission_id, _submitter_id, _vote);		
	END; $$
	LANGUAGE PLPGSQL;
	
-- ############################## voteSubmission deletion function ##############################
CREATE FUNCTION voteSubmissionDeletion
(
	_submission_id integer,
	_submitter_id integer	
) RETURNS VOID AS $$
	BEGIN		
		SET TRANSACTION ISOLATION LEVEL serializable;	
		SELECT verifySubmissionSubmitter(_submission_id);
        DELETE FROM Votable WHERE submitter_id = _submitter_id;        
	END; $$
	LANGUAGE PLPGSQL;
	
-- ############################## voteSubmission update function ##############################
CREATE FUNCTION voteSubmissionUpdate
(
	_submission_id integer,
	_submitter_id integer,
	_desiredVote boolean
) RETURNS VOID AS $$
	BEGIN		
		SET TRANSACTION ISOLATION LEVEL serializable;	
		SELECT verifySubmissionSubmitter(_submission_id);
		UPDATE Votable SET vote = _desiredVote WHERE submitter_id = _submitter_id;  		
	END; $$
	LANGUAGE PLPGSQL;