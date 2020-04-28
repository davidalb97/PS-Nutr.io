package pt.isel.ps.g06.httpserver.dataAccess.api.food

enum class SpoonacularCuisine {
    African,
    American,
    British,
    Cajun,
    Caribbean,
    Chinese,
    Eastern_European,
    European,
    French,
    German,
    Greek,
    Indian,
    Irish,
    Italian,
    Japanese,
    Jewish,
    Korean,
    Latin_American,
    Mediterranean,
    Mexican,
    Middle_Eastern,
    Nordic,
    Southern,
    Spanish,
    Thai,
    Vietnamese;

    override fun toString(): String {
        return super.toString().replace("_", " ")
    }
}

enum class SpoonacularDiet {
    Gluten_Free,
    Ketogenic,
    Vegetarian,
    Lacto__Vegetarian,
    Ovo__Vegetarian,
    Vegan,
    Pescetarian,
    Paleo,
    Primal,
    Whole30;

    override fun toString(): String {
        return super.toString()
                .replace("__", "-")
                .replace("_", " ")
    }
}

enum class SpoonacularAisle {
    Baking,
    Health_Foods,
    Spices_and_Seasonings,
    Pasta_and_Rice,
    Bakery__Bread,
    Refrigerated,
    Canned_and_Jarred,
    Frozen,
    Nut_butters___Jams___and_Honey,
    Oil___Vinegar___Salad_Dressing,
    Condiments,
    Savory_Snacks,
    Milk___Eggs___Other_Dairy,
    Ethnic_Foods,
    Tea_and_Coffee,
    Meat,
    Gourmet,
    Sweet_Snacks,
    Gluten_Free,
    Alcoholic_Beverages,
    Cereal,
    Nuts,
    Beverages,
    Produce,
    Not_in_Grocery_Store__Homemade,
    Seafood,
    Cheese,
    Dried_Fruits,
    Online,
    Grilling_Supplies,
    Bread;

    override fun toString(): String {
        return super.toString()
                .replace("___", ", ")
                .replace("__", "/")
                .replace("_", " ")
    }
}

enum class SpoonacularUnitTypes {
    grams,
    ounce;

    override fun toString(): String {
        return super.toString()
    }
}