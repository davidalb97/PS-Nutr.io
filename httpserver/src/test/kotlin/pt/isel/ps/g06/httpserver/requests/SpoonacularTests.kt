package pt.isel.ps.g06.httpserver.requests

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.event.annotation.BeforeTestExecution
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.servlet.mvc.Controller
import pt.isel.ps.g06.httpserver.controller.FoodController

@SpringBootTest
class SpoonacularTests {

    @Autowired
    lateinit var webApplicationContext: WebApplicationContext
    lateinit var mockMvc: MockMvc

    @BeforeEach
    fun beforeEach() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
    }

    @Test
    fun ingredientInfoTest() {
        mockMvc.get("http://localhost:8080/spoonacular/ingredientInfo?id=9266&amount=1&unit=grams") {
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