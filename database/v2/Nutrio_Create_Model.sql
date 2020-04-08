DROP TABLE IF EXISTS MealCuisine;
DROP TABLE IF EXISTS RestaurantCuisine;
DROP TABLE IF EXISTS RestaurantMealPortion;
DROP TABLE IF EXISTS Ingredient;
DROP TABLE IF EXISTS Portion;
DROP TABLE IF EXISTS Meal;
DROP TABLE IF EXISTS Cuisine;
DROP TABLE IF EXISTS Restaurant;
DROP TABLE IF EXISTS Votable;
DROP TABLE IF EXISTS Report;
DROP TABLE IF EXISTS SubmissionSubmitter;
DROP TABLE IF EXISTS API_Submission;
DROP TABLE IF EXISTS Submission;
DROP TABLE IF EXISTS API;
DROP TABLE IF EXISTS _User;
DROP TABLE IF EXISTS Submitter;

CREATE TABLE Submitter(
	submitter_id serial PRIMARY KEY,
	submitter_name varchar(20) NOT NULL,
	submitter_type varchar(5) CHECK(submitter_type = 'User' OR submitter_type = 'API')	
);

CREATE TABLE _User(
	submitter_id integer,
	email varchar(50),
	session_secret varchar(256) NOT NULL, -- TODO: Check maximum length
	creation_date timestamp with time zone, -- Add to doc
	PRIMARY KEY(submitter_id, email),
	FOREIGN KEY(submitter_id) REFERENCES Submitter(submitter_id)
);

CREATE TABLE API(
	submitter_id integer PRIMARY KEY,
	api_token varchar(256) NOT NULL,
	FOREIGN KEY(submitter_id) REFERENCES Submitter(submitter_id)
);

CREATE TABLE Submission(
	submission_id serial PRIMARY KEY,
	submission_type varchar(10) CHECK(
		submission_type = 'Restaurant' OR
		submission_type = 'Portion' OR
		submission_type = 'Meal' OR
		submission_type = 'Ingredient'
	)
);

CREATE TABLE API_Submission(
	submission_id integer,
	apiId integer,
	submission_type varchar(10) CHECK(
		submission_type = 'Restaurant' OR
		submission_type = 'Portion' OR
		submission_type = 'Meal' OR
		submission_type = 'Ingredient'
	),	
	PRIMARY KEY(submission_id, apiId),
	FOREIGN KEY(submission_id) REFERENCES Submission(submission_id)
);

CREATE TABLE SubmissionSubmitter(	
	submission_id integer,
	submitter_id integer,
	submission_date timestamp with time zone, -- Add to doc
	PRIMARY KEY(submission_id, submitter_id),
	FOREIGN KEY(submission_id) REFERENCES Submission(submission_id),
	FOREIGN KEY(submitter_id) REFERENCES Submitter(submitter_id)
);

CREATE TABLE Report(
	report_submission_id integer,
	submission_id integer,
	description varchar(500) NOT NULL,
	PRIMARY KEY(report_submission_id, submission_id),
	FOREIGN KEY(submission_id) REFERENCES Submission(submission_id),
	FOREIGN KEY(report_submission_id) REFERENCES Submitter(submitter_id)
);

CREATE TABLE Votable(
	submission_id integer,
	vote_submitter_id integer,
	vote boolean NOT NULL,
	PRIMARY KEY(submission_id, vote_submitter_id) ,
	FOREIGN KEY(submission_id) REFERENCES Submission(submission_id),
	FOREIGN KEY(vote_submitter_id) REFERENCES Submitter(submitter_id)
);

CREATE TABLE Restaurant(
	submission_id integer PRIMARY KEY,
	restaurant_name varchar(20) NOT NULL,
	latitude REAL,
	longitude REAL,
	FOREIGN KEY(submission_id) REFERENCES Submission(submission_id)
);

CREATE TABLE Cuisine(
	cuisine_name varchar(20) PRIMARY KEY
);

CREATE TABLE Meal(
	submission_id integer PRIMARY KEY,
	meal_name varchar(20) NOT NULL,
	FOREIGN KEY(submission_id) REFERENCES Submission(submission_id)
);

CREATE TABLE Portion(
	submission_id integer PRIMARY KEY,	
	quantity integer,
	FOREIGN KEY(submission_id) REFERENCES Submission(submission_id)	
);

CREATE TABLE Ingredient(
	submission_id integer,
	ingredient_name varchar(20) NOT NULL,
	PRIMARY KEY(submission_id),
	FOREIGN KEY(submission_id) REFERENCES Submission(submission_id)
);

CREATE TABLE RestaurantMealPortion(
	meal_submission_id integer,
	portion_submission_id integer,
	restaurant_submission_id integer,	
	PRIMARY KEY(meal_submission_id, portion_submission_id),
	FOREIGN KEY(meal_submission_id) REFERENCES Meal(submission_id),
	FOREIGN KEY(portion_submission_id) REFERENCES Portion(submission_id),
	FOREIGN KEY(restaurant_submission_id) REFERENCES Restaurant(submission_id)
);

-- Add to doc
CREATE TABLE RestaurantCuisine(
	restaurant_submission_id integer,
	cuisine_name varchar(20),
	PRIMARY KEY(restaurant_submission_id, cuisine_name),
	FOREIGN KEY(restaurant_submission_id) REFERENCES Restaurant(submission_id),
	FOREIGN KEY(cuisine_name) REFERENCES Cuisine(cuisine_name)
);

-- Add to doc
CREATE TABLE MealCuisine(
	meal_submission_id integer,
	cuisine_name varchar(20),
	PRIMARY KEY(meal_submission_id, cuisine_name),
	FOREIGN KEY(meal_submission_id) REFERENCES Meal(submission_id),
	FOREIGN KEY(cuisine_name) REFERENCES Cuisine(cuisine_name)
);