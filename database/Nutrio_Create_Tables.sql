CREATE TABLE Submitter(
	submitter_id serial PRIMARY KEY,
	submitter_name varchar(20) NOT NULL,
	submitter_type varchar(5) CHECK(submitter_type = 'User' OR submitter_type = 'API')
	-- TODO: Checks or boolean?
);

CREATE TABLE _User(
	submitter_id integer,
	email varchar(50),
	session_secret varchar(256) NOT NULL, -- TODO: Check maximum length
	PRIMARY KEY(submitter_id, email),
	FOREIGN KEY(submitter_id) REFERENCES Submitter(submitter_id)
);

CREATE TABLE API(
	submitter_id integer PRIMARY KEY,
	api_token varchar(256) NOT NULL,
	FOREIGN KEY(submitter_id) REFERENCES Submitter(submitter_id)
);

CREATE TABLE SubmissionSubmitter(
	submission_id serial PRIMARY KEY,
	submission_type varchar(5),
	submitter_id integer UNIQUE NOT NULL,
	FOREIGN KEY(submitter_id) REFERENCES Submitter(submitter_id)
);

CREATE TABLE Report(
	submission_id integer,
	submitter_id integer,
	description varchar(500) NOT NULL,
	PRIMARY KEY(submission_id, submitter_id),
	FOREIGN KEY(submission_id) REFERENCES SubmissionSubmitter(submission_id),
	FOREIGN KEY(submitter_id) REFERENCES Submitter(submitter_id)
);

CREATE TABLE Votable(
	submission_id integer,
	submitter_id integer,
	vote boolean NOT NULL,
	PRIMARY KEY(submission_id, submitter_id) ,
	FOREIGN KEY(submission_id) REFERENCES SubmissionSubmitter(submission_id),
	FOREIGN KEY(submitter_id) REFERENCES Submitter(submitter_id)
);

CREATE TABLE Restaurant(
	restaurant_id serial PRIMARY KEY,
	restaurant_name varchar(100) NOT NULL,
	latitude REAL,
	longitude REAL
);

CREATE TABLE SubmissionRestaurant(
	submission_id integer PRIMARY KEY,
	restaurant_id integer UNIQUE NOT NULL,
	FOREIGN KEY(submission_id) REFERENCES SubmissionSubmitter(submission_id)
);

CREATE TABLE Cuisine(
	cuisine_name varchar(20) PRIMARY KEY
);

CREATE TABLE Meal(
	meal_id serial PRIMARY KEY,
	meal_name varchar(20),
	cuisine_name varchar(20),
	FOREIGN KEY(cuisine_name) REFERENCES Cuisine(cuisine_name)
);

CREATE TABLE SubmissionMeal(
	submission_id integer PRIMARY KEY,
	meal_id integer NOT NULL,
	restaurant_id integer NOT NULL,
	FOREIGN KEY(submission_id) REFERENCES SubmissionSubmitter(submission_id),
	FOREIGN KEY(meal_id) REFERENCES Meal
);

CREATE TABLE SubmissionPortion(
	submission_id integer PRIMARY KEY,
	meal_id integer NOT NULL,
	quantity integer,
	FOREIGN KEY(submission_id) REFERENCES SubmissionSubmitter(submission_id),
	FOREIGN KEY(meal_id) REFERENCES SubmissionMeal
);

-- Corrigir ingredient
CREATE TABLE Ingredient(
	ingredient_id serial PRIMARY KEY,
	ingredient_name varchar(20) NOT NULL
);

CREATE TABLE SubmissionIngredient(
	submission_id integer PRIMARY KEY,
	meal_id integer NOT NULL,
	ingredient_id integer NOT NULL,	
	FOREIGN KEY(submission_id) REFERENCES SubmissionSubmitter(submission_id),
	FOREIGN KEY(ingredient_id) REFERENCES Ingredient(ingredient_id),
	FOREIGN KEY(meal_id) REFERENCES Meal(meal_id) -- ? Ou submission-meal
);

CREATE TABLE RestaurantCuisine(
	submission_id integer PRIMARY KEY,
	restaurant_id integer NOT NULL,
	cuisine_name varchar(20) NOT NULL,
	FOREIGN KEY(submission_id) REFERENCES SubmissionSubmitter(submission_id),
	FOREIGN KEY(restaurant_id) REFERENCES Restaurant(restaurant_id),
	FOREIGN KEY(cuisine_name) REFERENCES Cuisine(cuisine_name)
);

-- Importante: ver o caso da submission_id