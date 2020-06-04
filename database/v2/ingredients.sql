--List of hardcoded ingredients

SELECT AddIngredient('green peas', 8);
SELECT AddIngredient('broad beans', 7);
SELECT AddIngredient('chickpea', 17);
SELECT AddIngredient('black eyed peas', 18);
SELECT AddIngredient('soy', 6);
SELECT AddIngredient('lupine bean', 7);

--Bread
SELECT AddIngredient('corn bread', 37);
SELECT AddIngredient('rye bread', 56);
SELECT AddIngredient('mixed bread', 54);
SELECT AddIngredient('wheat bread', 57);
SELECT AddIngredient('loaf of bread', 55);

--Flour
SELECT AddIngredient('corn starch', 90);
SELECT AddIngredient('whole wheat flour', 65);
SELECT AddIngredient('wheat flour (type-55)', 74);

--Delete function
DROP FUNCTION IF EXISTS AddIngredient(varchar, integer);

