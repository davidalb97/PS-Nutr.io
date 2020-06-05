--List of hardcoded ingredients
SELECT AddFood('Ingredient', 'peas, green', 8, 100);
SELECT AddFood('Ingredient', 'beans, broad', 7, 100);
SELECT AddFood('Ingredient', 'chickpea', 10, 60);
SELECT AddFood('Ingredient', 'peas, black eyed', 12, 57);
SELECT AddFood('Ingredient', 'beans, kidney', 5, 30);
SELECT AddFood('Ingredient', 'lupine bean', 7, 100);

--Bread
SELECT AddFood('Ingredient', 'corn bread', 37, 100);
SELECT AddFood('Ingredient', 'rye bread', 56, 100);
SELECT AddFood('Ingredient', 'mixed bread', 54, 100);
SELECT AddFood('Ingredient', 'wheat bread', 57, 100);
SELECT AddFood('Ingredient', 'loaf of bread', 55, 100);

--potatos
SELECT AddFood('Ingredient', 'potato, roasted', 24, 100);
SELECT AddFood('Ingredient', 'potato, boiled', 19, 100);
SELECT AddFood('Ingredient', 'potato, mashed', 17, 100);
SELECT AddFood('Ingredient', 'sweet potato, roasted', 28, 100);
SELECT AddFood('Ingredient', 'sweet potato, boiled', 28, 100);
SELECT AddFood('Ingredient', 'french fries', 28, 100);

--Pastas
SELECT AddFood('Ingredient', 'ravioli', 10, 40);
SELECT AddFood('Ingredient', 'pasta, tagliatelle', 10, 30);
SELECT AddFood('Ingredient', 'pasta, penne', 10, 30);
SELECT AddFood('Ingredient', 'pasta, bow', 10, 30);
SELECT AddFood('Ingredient', 'pasta, fusilli', 10, 30);
SELECT AddFood('Ingredient', 'pasta, macaroni', 10, 30);
SELECT AddFood('Ingredient', 'pasta, shell', 10, 30);
SELECT AddFood('Ingredient', 'spaghetti', 20, 60);
SELECT AddFood('Ingredient', 'spaghetti, integral', 30, 105);

--Other cereals/grains
SELECT AddFood('Ingredient', 'couscous', 10, 45);
SELECT AddFood('Ingredient', 'lentils', 10, 60);

--Rices
SELECT AddFood('Ingredient', 'rice', 65, 100);
SELECT AddFood('Ingredient', 'rice, integral', 65, 100);

--Fruits
SELECT AddFood('Ingredient', 'plum', 10, 110);
SELECT AddFood('Ingredient', 'clementine', 10, 160);
SELECT AddFood('Ingredient', 'nectarine', 15, 165);
SELECT AddFood('Ingredient', 'blackberries', 5, 57);
SELECT AddFood('Ingredient', 'pineapple, canned', 10, 80);
SELECT AddFood('Ingredient', 'pineapple', 4, 40);
SELECT AddFood('Ingredient', 'banana, without peel', 20, 85);
SELECT AddFood('Ingredient', 'banana, with peel', 20, 130);
SELECT AddFood('Ingredient', 'cherries', 6, 50);
SELECT AddFood('Ingredient', 'figs', 5, 28);
SELECT AddFood('Ingredient', 'raspberries', 5, 110);
SELECT AddFood('Ingredient', 'kiwi', 5, 55);
SELECT AddFood('Ingredient', 'orange', 4, 70);
SELECT AddFood('Ingredient', 'apple', 10, 85);
SELECT AddFood('Ingredient', 'mango', 10, 70);
SELECT AddFood('Ingredient', 'watermelon', 10, 140);
SELECT AddFood('Ingredient', 'melon, honeydew', 10, 150);
SELECT AddFood('Ingredient', 'melon, cantaloupe', 6, 150);
SELECT AddFood('Ingredient', 'blueberries', 5, 40);
SELECT AddFood('Ingredient', 'strawberries', 5, 85);
SELECT AddFood('Ingredient', 'papaya', 5, 90);
SELECT AddFood('Ingredient', 'pear', 10, 105);
SELECT AddFood('Ingredient', 'peach, canned', 10, 100);
SELECT AddFood('Ingredient', 'peach', 10, 140);
SELECT AddFood('Ingredient', 'pomegranate', 5, 40);
SELECT AddFood('Ingredient', 'grapes', 10, 65);


--Delete function
DROP FUNCTION IF EXISTS AddFood(varchar, varchar, integer);

