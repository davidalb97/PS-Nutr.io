CREATE OR REPLACE FUNCTION AddCuisineToFood(foodName varchar, cuisines varchar[]) 
RETURNS void
AS $$ 
	BEGIN 
		DECLARE 
			food_id integer;
			cuisineName varchar;
		BEGIN
			
			SELECT Meal.submission_id INTO food_id FROM Meal WHERE Meal.meal_name = foodName;
		
			FOREACH cuisineName IN ARRAY cuisines 
			LOOP
				DECLARE
					cuisine_id integer;
				BEGIN 
					SELECT Cuisine.submission_id INTO cuisine_id FROM Cuisine WHERE Cuisine.cuisine_name = cuisineName;
					INSERT INTO MealCuisine(meal_submission_id, cuisine_submission_id) VALUES (food_id, cuisine_id);
				END;
			END LOOP;
		END;
	
	END;
$$ LANGUAGE plpgsql;