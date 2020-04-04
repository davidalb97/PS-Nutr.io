-- Trigger example that will be used for the next situations

CREATE FUNCTION Restaurant_Submission_Insertion() RETURNS trigger AS $emp_stamp$
    BEGIN
        INSERT INTO SubmissionSubmitter(submission_id, submission_type, submitter_id) VALUES
		(0, 'User', 0);
        RETURN NEW;
    END;
$emp_stamp$ LANGUAGE plpgsql;

CREATE TRIGGER Restaurant_Submission_Trigger BEFORE INSERT
    ON SubmissionRestaurant
	FOR EACH ROW
    EXECUTE PROCEDURE Restaurant_Submission_Insertion();