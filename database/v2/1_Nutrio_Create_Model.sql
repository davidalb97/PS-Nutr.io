SET client_min_messages = error;
DROP TABLE IF EXISTS MealCuisine CASCADE;
DROP TABLE IF EXISTS RestaurantCuisine CASCADE;
DROP TABLE IF EXISTS MealIngredient CASCADE;
DROP TABLE IF EXISTS Portion CASCADE;
DROP TABLE IF EXISTS Favorite CASCADE;
DROP TABLE IF EXISTS RestaurantMeal CASCADE;
DROP TABLE IF EXISTS Meal CASCADE;
DROP TABLE IF EXISTS ApiCuisine CASCADE;
DROP TABLE IF EXISTS Cuisine CASCADE;
DROP TABLE IF EXISTS Restaurant CASCADE;
DROP TABLE IF EXISTS Votes CASCADE;
DROP TABLE IF EXISTS UserVote CASCADE;
DROP TABLE IF EXISTS Report CASCADE;
DROP TABLE IF EXISTS SubmissionContract CASCADE;
DROP TABLE IF EXISTS SubmissionSubmitter CASCADE;
DROP TABLE IF EXISTS ApiSubmission CASCADE;
DROP TABLE IF EXISTS Submission CASCADE;
--DROP SEQUENCE submission_submission_id_seq CASCADE;
DROP TABLE IF EXISTS Api CASCADE;
DROP TABLE IF EXISTS _User CASCADE;
DROP TABLE IF EXISTS InsulinProfile CASCADE;
DROP TABLE IF EXISTS Submitter CASCADE;
DROP FUNCTION IF EXISTS AddFood;
--DROP SEQUENCE submitter_submitter_id_seq CASCADE;
CREATE EXTENSION IF NOT EXISTS postgis;
ALTER EXTENSION postgis UPDATE;
SET TIMEZONE='Portugal';

CREATE TABLE Submitter(
	submitter_id serial PRIMARY KEY,
	submitter_name varchar(20) NOT NULL,
	creation_date timestamp with time zone default CURRENT_TIMESTAMP, -- Add to doc
	submitter_type varchar(5) CHECK(submitter_type = 'User' OR submitter_type = 'API')	
);

CREATE TABLE _User(
	submitter_id integer,	
	email varchar(50) NOT NULL,
	password varchar(60) NOT NULL, -- it will always be 60, because of the BCrypt password encoding
	role varchar(10) CHECK(role = 'normal' OR role = 'mod') NOT NULL,
	is_banned boolean NOT NULL,
	PRIMARY KEY(submitter_id),
	FOREIGN KEY(submitter_id) REFERENCES Submitter(submitter_id)
);

CREATE TABLE InsulinProfile(
	submitter_id integer,
	profile_name varchar(30),
	start_time varchar(30),
	end_time varchar(30),
	glucose_objective varchar(30),
	insulin_sensitivity_factor varchar(30),
	carbohydrate_ratio varchar(30),
	modification_date varchar(100),
	PRIMARY KEY(submitter_id, profile_name),
	FOREIGN KEY(submitter_id) REFERENCES _User(submitter_id)
);

CREATE TABLE Api(
	submitter_id integer PRIMARY KEY,
	api_token varchar(256) NOT NULL,
	FOREIGN KEY(submitter_id) REFERENCES Submitter(submitter_id)
);

CREATE TABLE Submission(
	submission_id serial PRIMARY KEY,
	submission_type varchar(15) CHECK(
		submission_type = 'Restaurant' OR
		submission_type = 'Portion' OR
		submission_type = 'Meal' OR
		submission_type = 'Ingredient' OR 
		submission_type = 'Cuisine' OR
		submission_type = 'ApiCuisine' OR
		submission_type = 'RestaurantMeal'
	),
	submission_date timestamp with time zone default CURRENT_TIMESTAMP
);

CREATE TABLE ApiSubmission(
	submission_id integer,
	apiId varchar(100),	
	PRIMARY KEY(submission_id, apiId),
	FOREIGN KEY(submission_id) REFERENCES Submission(submission_id) ON DELETE CASCADE
);

CREATE TABLE SubmissionSubmitter(	
	submission_id integer,
	submitter_id integer,
	PRIMARY KEY(submission_id, submitter_id),
	FOREIGN KEY(submission_id) REFERENCES Submission(submission_id) ON DELETE CASCADE,
	FOREIGN KEY(submitter_id) REFERENCES Submitter(submitter_id)
);

CREATE TABLE SubmissionContract(
	submission_id integer,
	submission_contract varchar(10) CHECK(
		submission_contract = 'Votable' OR
		submission_contract = 'Reportable' OR
		submission_contract = 'API' OR
		submission_contract = 'Favorable'
	),
	PRIMARY KEY(submission_id, submission_contract),
	FOREIGN KEY(submission_id) REFERENCES Submission(submission_id) ON DELETE CASCADE
);

CREATE TABLE Report(
	report_id serial,
	submitter_id integer,
	submission_id integer,
	description varchar(500) NOT NULL,
	UNIQUE(submitter_id, submission_id),
	PRIMARY KEY(report_id),
	FOREIGN KEY(submission_id) REFERENCES Submission(submission_id) ON DELETE CASCADE,
	FOREIGN KEY(submitter_id) REFERENCES Submitter(submitter_id)
);

CREATE TABLE Votes(
	submission_id integer PRIMARY KEY,
	positive_count integer,
	negative_count integer,
	FOREIGN KEY(submission_id) REFERENCES Submission(submission_id) ON DELETE CASCADE
);

CREATE TABLE UserVote(
	submission_id integer,
	vote_submitter_id integer,
	vote boolean NOT NULL,
	PRIMARY KEY(submission_id, vote_submitter_id),
	FOREIGN KEY(submission_id) REFERENCES Submission(submission_id) ON DELETE CASCADE,
	FOREIGN KEY(vote_submitter_id) REFERENCES Submitter(submitter_id)
);

CREATE TABLE Restaurant(
	submission_id integer PRIMARY KEY,
	restaurant_name varchar(30) NOT NULL,
	latitude REAL,
	longitude REAL,
	owner_id integer,
	FOREIGN KEY(submission_id) REFERENCES Submission(submission_id) ON DELETE CASCADE,
	FOREIGN KEY(owner_id) REFERENCES Submitter(submitter_id) ON DELETE CASCADE
);

CREATE TABLE Cuisine(
	submission_id integer PRIMARY KEY,
	cuisine_name varchar(20) UNIQUE
);

CREATE TABLE ApiCuisine(
	submission_id integer PRIMARY KEY,
	cuisine_submission_id integer,
	FOREIGN KEY(submission_id) REFERENCES Submission(submission_id) ON DELETE CASCADE,
	FOREIGN KEY(cuisine_submission_id) REFERENCES Cuisine(submission_id)
);

CREATE TABLE Meal(
	submission_id integer PRIMARY KEY,
	meal_name varchar(50) NOT NULL,
	carbs integer NOT NULL,
	quantity integer NOT NULL,
	unit varchar(10) CHECK(unit = 'gr'),
	meal_type varchar(10) CHECK(
		meal_type = 'Suggested' OR
		meal_type = 'Custom'
	),
	FOREIGN KEY(submission_id) REFERENCES Submission(submission_id) ON DELETE CASCADE
);

CREATE TABLE RestaurantMeal(
	submission_id integer PRIMARY KEY,
	restaurant_submission_id integer,
	meal_submission_id integer,
	verified boolean NOT NULL,
	UNIQUE(meal_submission_id, restaurant_submission_id),
	FOREIGN KEY(submission_id) REFERENCES Submission(submission_id) ON DELETE CASCADE,
	FOREIGN KEY(restaurant_submission_id) REFERENCES Restaurant(submission_id) ON DELETE CASCADE,
	FOREIGN KEY(meal_submission_id) REFERENCES Meal(submission_id) ON DELETE CASCADE
);

CREATE TABLE Favorite(
	submission_id integer,
	submitter_id integer,
	PRIMARY KEY(submission_id, submitter_id),
	FOREIGN KEY(submission_id) REFERENCES Submission(submission_id) ON DELETE CASCADE,
	FOREIGN KEY(submitter_id) REFERENCES Submitter(submitter_id)
);

CREATE TABLE Portion(
	submission_id integer PRIMARY KEY,
	restaurant_meal_submission_id integer,
	quantity integer,
	FOREIGN KEY(submission_id) REFERENCES Submission(submission_id) ON DELETE CASCADE,
	FOREIGN KEY(restaurant_meal_submission_id) REFERENCES RestaurantMeal(submission_id) ON DELETE CASCADE
);

CREATE TABLE MealIngredient(
	meal_submission_id integer,
	ingredient_submission_id integer,
	quantity integer NOT NULL,
	PRIMARY KEY(meal_submission_id, ingredient_submission_id),
	FOREIGN KEY(meal_submission_id) REFERENCES Meal(submission_id) ON DELETE CASCADE,
	FOREIGN KEY(ingredient_submission_id) REFERENCES Meal(submission_id)
);

CREATE TABLE RestaurantCuisine(
	restaurant_submission_id integer,
	cuisine_submission_id integer,
	PRIMARY KEY(restaurant_submission_id, cuisine_submission_id),
	FOREIGN KEY(restaurant_submission_id) REFERENCES Restaurant(submission_id) ON DELETE CASCADE,
	FOREIGN KEY(cuisine_submission_id) REFERENCES Cuisine(submission_id)
);

CREATE TABLE MealCuisine(
	meal_submission_id integer,
	cuisine_submission_id integer,
	PRIMARY KEY(meal_submission_id, cuisine_submission_id),
	FOREIGN KEY(meal_submission_id) REFERENCES Meal(submission_id) ON DELETE CASCADE,
	FOREIGN KEY(cuisine_submission_id) REFERENCES Cuisine(submission_id)
);

---------
CREATE OR REPLACE FUNCTION AddFood(_type varchar,name varchar, carbs integer, quantity integer) 
RETURNS void
AS $$ 
	BEGIN 
		INSERT INTO Submission(submission_type) VALUES (_type);
		
		DECLARE 
			submissionId INTEGER := lastval();
		BEGIN
			INSERT INTO Meal(submission_id, meal_name, carbs, quantity, unit) VALUES (submissionId, name, carbs, quantity, 'gr');
			--Make Food favorable
			INSERT INTO SubmissionContract(submission_id, submission_contract) VALUES (submissionId, 'Favorable');
		END;
	END;
$$ LANGUAGE plpgsql;