SET client_min_messages = error;
DROP TABLE IF EXISTS MealCuisine CASCADE;
DROP TABLE IF EXISTS RestaurantCuisine CASCADE;
DROP TABLE IF EXISTS RestaurantMealPortion CASCADE;
DROP TABLE IF EXISTS MealIngredient CASCADE;
DROP TABLE IF EXISTS Ingredient CASCADE;
DROP TABLE IF EXISTS Portion CASCADE;
DROP TABLE IF EXISTS Meal CASCADE;
DROP TABLE IF EXISTS Cuisine CASCADE;
DROP TABLE IF EXISTS Restaurant CASCADE;
DROP TABLE IF EXISTS Vote CASCADE;
DROP TABLE IF EXISTS Report CASCADE;
DROP TABLE IF EXISTS SubmissionContract CASCADE;
DROP TABLE IF EXISTS SubmissionSubmitter CASCADE;
DROP TABLE IF EXISTS ApiSubmission CASCADE;
DROP TABLE IF EXISTS Submission CASCADE;
DROP TABLE IF EXISTS Api CASCADE;
DROP TABLE IF EXISTS _User CASCADE;
DROP TABLE IF EXISTS Submitter CASCADE;

CREATE TABLE Submitter(
	submitter_id serial PRIMARY KEY,
	submitter_name varchar(20) NOT NULL,
	submitter_type varchar(5) CHECK(submitter_type = 'User' OR submitter_type = 'API')	
);

CREATE TABLE _User(
	submitter_id integer,
	email varchar(50),
	session_secret varchar(256) NOT NULL, -- TODO: Check maximum length
	creation_date timestamp with time zone default CURRENT_TIMESTAMP, -- Add to doc
	PRIMARY KEY(submitter_id, email),
	FOREIGN KEY(submitter_id) REFERENCES Submitter(submitter_id)
);

CREATE TABLE Api(
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

CREATE TABLE ApiSubmission(
	submission_id integer,
	apiId integer,	
	PRIMARY KEY(submission_id, apiId),
	FOREIGN KEY(submission_id) REFERENCES Submission(submission_id)
);

CREATE TABLE SubmissionSubmitter(	
	submission_id integer,
	submitter_id integer,
	submission_date timestamp with time zone default CURRENT_TIMESTAMP, -- Add to doc
	PRIMARY KEY(submission_id, submitter_id),
	FOREIGN KEY(submission_id) REFERENCES Submission(submission_id),
	FOREIGN KEY(submitter_id) REFERENCES Submitter(submitter_id)
);

CREATE TABLE SubmissionContract(
	submission_id integer,
	submission_contract varchar(10) CHECK(
		submission_contract = 'Votable' OR
		submission_contract = 'Reportable' OR
		submission_contract = 'API'
	)
);

CREATE TABLE Report(
	submitter_id integer,
	submission_id integer,
	description varchar(500) NOT NULL,
	PRIMARY KEY(submitter_id, submission_id),
	FOREIGN KEY(submission_id) REFERENCES Submission(submission_id),
	FOREIGN KEY(submitter_id) REFERENCES Submitter(submitter_id)
);

CREATE TABLE Vote(
	submission_id integer,
	vote_submitter_id integer,
	vote boolean NOT NULL,
	PRIMARY KEY(submission_id, vote_submitter_id) ,
	FOREIGN KEY(submission_id) REFERENCES Submission(submission_id),
	FOREIGN KEY(vote_submitter_id) REFERENCES Submitter(submitter_id)
);

CREATE TABLE Restaurant(
	submission_id integer PRIMARY KEY,
	restaurant_name varchar(30) NOT NULL,
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

CREATE TABLE MealIngredient(
	meal_submission_id integer,
	ingredient_submission_id integer,
	PRIMARY KEY(meal_submission_id, ingredient_submission_id),
	FOREIGN KEY(meal_submission_id) REFERENCES Meal(submission_id),
	FOREIGN KEY(ingredient_submission_id) REFERENCES Ingredient(submission_id)
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