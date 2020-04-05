-- ############################## SubmissionRestaurant insertion function ##############################
CREATE FUNCTION restaurantSubmissionInsertion
(
	_submission_type varchar(5),
	_submitter_id integer,
	_restaurant_name varchar(100),
	_latitude real,
	_longitude real
) RETURNS VOID AS $$
	BEGIN
		SET TRANSACTION ISOLATION LEVEL serializable;	
		IF EXISTS (
			SELECT * FROM Restaurant WHERE restaurant_name = _restaurant_name AND
			latitude = _latitude AND longitude = _longitude
		) THEN			
			RAISE NOTICE 'The inserted restaurant already exists in the database.';
		END IF;
        INSERT INTO SubmissionSubmitter(submission_type, submitter_id) VALUES
		(_submission_type, _submitter_id);
		INSERT INTO Restaurant(restaurant_name, latitude, longitude) VALUES
		(_restaurant_name, _latitude, _longitude);		
	END; $$
	LANGUAGE PLPGSQL;
	
-- ############################## SubmissionRestaurant deletion function ############################## - Not complete
CREATE FUNCTION restaurantSubmissionDeletion
(	
	_submitter_id integer,
	_restaurant_id integer
) RETURNS VOID AS $$
	BEGIN
		SET TRANSACTION ISOLATION LEVEL serializable;	
		IF NOT EXISTS (
			SELECT * FROM Restaurant WHERE restaurant_name = _restaurant_name AND
			latitude = _latitude AND longitude = _longitude
		) THEN			
			RAISE NOTICE 'The inserted restaurant already exists in the database.';
		END IF;
        INSERT INTO SubmissionSubmitter(submission_type, submitter_id) VALUES
		(_submission_type, _submitter_id);
		INSERT INTO Restaurant(restaurant_name, latitude, longitude) VALUES
		(_restaurant_name, _latitude, _longitude);		
	END; $$
	LANGUAGE PLPGSQL;

-- ############################## SubmissionRestaurant update function ##############################
-- TODO