package pt.isel.ps.g06.httpserver.requests

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@SpringBootTest
class SpoonacularTests {

    /** TODO
     * Estes testes sao maus. A API e volatil e podem deixar de dar o assert
     * de um momento para o outro. Isto serve so para ver se tudo esta a funcionar...
     */

    @Autowired
    lateinit var webApplicationContext: WebApplicationContext
    lateinit var mockMvc: MockMvc

    private val spoonacular = "http://localhost:8080/spoonacular"

    @BeforeEach
    fun beforeEach() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
    }

    @Test
    fun getProductTest() {
        mockMvc.get("$spoonacular/product?query=sushi") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk }
            content { contentType(MediaType.APPLICATION_JSON) }
            content {
                json(
                        "{\n" +
                                "\"products\": [\n" +
                                "{\n" +
                                "\"id\": 19717,\n" +
                                "\"title\": \"Baycliff Company Sushi Chef Sushi Vinegar\",\n" +
                                "\"image\": \"https://spoonacular.com/productImages/19717-312x231.jpg\",\n" +
                                "\"imageType\": \"jpg\"\n" +
                                "},\n" +
                                "{\n" +
                                "\"id\": 86818,\n" +
                                "\"title\": \"Sushi Chef Soy Sauce\",\n" +
                                "\"image\": \"https://spoonacular.com/productImages/86818-312x231.jpg\",\n" +
                                "\"imageType\": \"jpg\"\n" +
                                "},\n" +
                                "{\n" +
                                "\"id\": 112905,\n" +
                                "\"title\": \"Sushi Chef Japanese Sesame Oil\",\n" +
                                "\"image\": \"https://spoonacular.com/productImages/112905-312x231.jpg\",\n" +
                                "\"imageType\": \"jpg\"\n" +
                                "},\n" +
                                "{\n" +
                                "\"id\": 199717,\n" +
                                "\"title\": \"Sushi Chef Seasoned Rice Vinegar, 10 oz (Pack of 6)\",\n" +
                                "\"image\": \"https://spoonacular.com/productImages/199717-312x231.jpg\",\n" +
                                "\"imageType\": \"jpg\"\n" +
                                "},\n" +
                                "{\n" +
                                "\"id\": 407808,\n" +
                                "\"title\": \"Ace Sushi Trio\",\n" +
                                "\"image\": \"https://spoonacular.com/productImages/407808-312x231.jpg\",\n" +
                                "\"imageType\": \"jpg\"\n" +
                                "},\n" +
                                "{\n" +
                                "\"id\": 408414,\n" +
                                "\"title\": \"Ace Sushi Asiana Seafood Wrap\",\n" +
                                "\"image\": \"https://spoonacular.com/productImages/408414-312x231.jpg\",\n" +
                                "\"imageType\": \"jpg\"\n" +
                                "},\n" +
                                "{\n" +
                                "\"id\": 95298,\n" +
                                "\"title\": \"Nishiki Vinegar\",\n" +
                                "\"image\": \"https://spoonacular.com/productImages/95298-312x231.jpg\",\n" +
                                "\"imageType\": \"jpg\"\n" +
                                "},\n" +
                                "{\n" +
                                "\"id\": 193933,\n" +
                                "\"title\": \"Nishiki Sushi Vinegar, 10 fl oz, (Pack of 6)\",\n" +
                                "\"image\": \"https://spoonacular.com/productImages/193933-312x231.jpg\",\n" +
                                "\"imageType\": \"jpg\"\n" +
                                "},\n" +
                                "{\n" +
                                "\"id\": 202669,\n" +
                                "\"title\": \"Kikkoman Sushi & Sashimi Soy Sauce, 10 oz (Pack of 6)\",\n" +
                                "\"image\": \"https://spoonacular.com/productImages/202669-312x231.jpg\",\n" +
                                "\"imageType\": \"jpg\"\n" +
                                "},\n" +
                                "{\n" +
                                "\"id\": 414786,\n" +
                                "\"title\": \"Roland Sesame Sushi Wraps - 20 CT\",\n" +
                                "\"image\": \"https://spoonacular.com/productImages/414786-312x231.jpg\",\n" +
                                "\"imageType\": \"jpg\"\n" +
                                "}\n" +
                                "],\n" +
                                "\"totalProducts\": 68,\n" +
                                "\"type\": \"product\",\n" +
                                "\"offset\": 0,\n" +
                                "\"number\": 10\n" +
                                "}"
                )
            }
        }
    }

    @Test
    fun getRecipeTest() {
        mockMvc.get("$spoonacular/recipes?recipeName=pizza&cuisines=Italian") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk }
            content { contentType(MediaType.APPLICATION_JSON) }
            content {
                json(
                        "[\n" +
                                "{\n" +
                                "\"id\": 492560,\n" +
                                "\"image\": \"St--Louis-Style-Pizza-492560.jpg\",\n" +
                                "\"imageUrls\": null,\n" +
                                "\"readyInMinutes\": 27,\n" +
                                "\"servings\": 8,\n" +
                                "\"title\": \"Quick and Easy St. Louis-Style Pizza\"\n" +
                                "},\n" +
                                "{\n" +
                                "\"id\": 559251,\n" +
                                "\"image\": \"Breakfast-Pizza-559251.jpg\",\n" +
                                "\"imageUrls\": null,\n" +
                                "\"readyInMinutes\": 25,\n" +
                                "\"servings\": 6,\n" +
                                "\"title\": \"Breakfast Pizza\"\n" +
                                "},\n" +
                                "{\n" +
                                "\"id\": 630293,\n" +
                                "\"image\": \"Egg---rocket-pizzas-630293.jpg\",\n" +
                                "\"imageUrls\": null,\n" +
                                "\"readyInMinutes\": 30,\n" +
                                "\"servings\": 2,\n" +
                                "\"title\": \"Egg & rocket pizzas\"\n" +
                                "},\n" +
                                "{\n" +
                                "\"id\": 601651,\n" +
                                "\"image\": \"Fruit-Pizza-601651.jpg\",\n" +
                                "\"imageUrls\": null,\n" +
                                "\"readyInMinutes\": 90,\n" +
                                "\"servings\": 8,\n" +
                                "\"title\": \"Fruit Pizza\"\n" +
                                "},\n" +
                                "{\n" +
                                "\"id\": 496972,\n" +
                                "\"image\": \"Ricotta-Pizza-with-Prosciutto-and-Fresh-Pea-Salad-496972.jpg\",\n" +
                                "\"imageUrls\": null,\n" +
                                "\"readyInMinutes\": 30,\n" +
                                "\"servings\": 6,\n" +
                                "\"title\": \"Ricotta Pizza with Prosciutto and Fresh Pea Salad\"\n" +
                                "},\n" +
                                "{\n" +
                                "\"id\": 223993,\n" +
                                "\"image\": \"Easy-tomato-pizzas-223993.jpg\",\n" +
                                "\"imageUrls\": null,\n" +
                                "\"readyInMinutes\": 90,\n" +
                                "\"servings\": 8,\n" +
                                "\"title\": \"Easy tomato pizzas\"\n" +
                                "},\n" +
                                "{\n" +
                                "\"id\": 481601,\n" +
                                "\"image\": \"Neapolitan-Pizza-and-Honey-Whole-Wheat-Dough-481601.jpg\",\n" +
                                "\"imageUrls\": null,\n" +
                                "\"readyInMinutes\": 102,\n" +
                                "\"servings\": 8,\n" +
                                "\"title\": \"Neapolitan Pizza and Honey Whole Wheat Dough\"\n" +
                                "},\n" +
                                "{\n" +
                                "\"id\": 222869,\n" +
                                "\"image\": \"Pizza-puff-pinwheels-222869.jpg\",\n" +
                                "\"imageUrls\": null,\n" +
                                "\"readyInMinutes\": 35,\n" +
                                "\"servings\": 12,\n" +
                                "\"title\": \"Pizza puff pinwheels\"\n" +
                                "},\n" +
                                "{\n" +
                                "\"id\": 225711,\n" +
                                "\"image\": \"Tuna--olive---rocket-pizzas-225711.jpg\",\n" +
                                "\"imageUrls\": null,\n" +
                                "\"readyInMinutes\": 27,\n" +
                                "\"servings\": 2,\n" +
                                "\"title\": \"Tuna, olive & rocket pizzas\"\n" +
                                "},\n" +
                                "{\n" +
                                "\"id\": 209785,\n" +
                                "\"image\": \"No-oven-pizza-209785.jpg\",\n" +
                                "\"imageUrls\": null,\n" +
                                "\"readyInMinutes\": 40,\n" +
                                "\"servings\": 4,\n" +
                                "\"title\": \"No-oven pizza\"\n" +
                                "}\n" +
                                "]"
                )
            }
        }
    }

    @Test
    fun getIngredientInfoTest() {
        mockMvc.get("$spoonacular/ingredient?id=9266&amount=1&unit=grams") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk }
            content { contentType(MediaType.APPLICATION_JSON) }
            content {
                json(
                        "{\n" +
                                "\"id\": 9266,\n" +
                                "\"aisle\": \"Produce\",\n" +
                                "\"name\": \"pineapple\",\n" +
                                "\"image\": \"pineapple.jpg\",\n" +
                                "\"amount\": 1,\n" +
                                "\"consistency\": \"solid\",\n" +
                                "\"estimatedCost\": {\n" +
                                "\"unit\": \"US Cents\",\n" +
                                "\"value\": 0.33\n" +
                                "},\n" +
                                "\"shoppingListUnits\": [\n" +
                                "\"pieces\"\n" +
                                "],\n" +
                                "\"unit\": \"grams\",\n" +
                                "\"unitLong\": \"gram\",\n" +
                                "\"unitShort\": \"g\",\n" +
                                "\"possibleUnits\": [\n" +
                                "\"piece\",\n" +
                                "\"slice\",\n" +
                                "\"fruit\",\n" +
                                "\"g\",\n" +
                                "\"oz\",\n" +
                                "\"cup\",\n" +
                                "\"serving\"\n" +
                                "]\n" +
                                "}"
                )
            }
        }
    }

}