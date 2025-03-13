package de.unistuttgart.iste.ese.api.cats;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.unistuttgart.iste.ese.api.Models.Assignee;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalTime;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AssigneeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Expect a bad request response code")
    void testCreatingNewCat_badRequest() throws Exception {
        // performs a post request without body content
        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/assignees"));

        // asserts result
        result.andExpect(status().isBadRequest());
    }

    //@Test
    @DisplayName("Expect a created as response code")
    void testCreatingNewCat_successful() throws Exception {
        String testCatJsonString = "{\"name\" : \"TestCat\", \"ageInYears\" : 2, \"picUrl\" : \"https://avatars1.githubusercontent.com/u/583231\"}";

        // performs a post request
        ResultActions result = this.mockMvc
                .perform(MockMvcRequestBuilders.post("/api/v1/cats").contentType(MediaType.APPLICATION_JSON)
                        .content(testCatJsonString));

        // asserts result
        result.andExpect(status().isCreated());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber());

        // accesses response body directly
        String newCatString = result.andReturn().getResponse().getContentAsString();
        System.out.println("Content: " + newCatString);

        ObjectMapper objectMapper = new ObjectMapper();
        Assignee newAssignee = objectMapper.readValue(newCatString, Assignee.class);
        long newCatId = newAssignee.getId();

        // performs a get request with the ID and asserts for returned object
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/assignees/" + newCatId)).andExpect(status().isOk());
    }

    @Test
    @DisplayName("Expect sample data created in the PostConstruct in the CatController class")
    void testReturningTheListOfCats_sampleData() throws Exception {

        // performs a get request
        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cats"));

        // Assertions for result object:
        // expected status code
        result.andExpect(status().isOk());
        // expected string in the content
        result.andExpect(content().string(Matchers.containsString("Octocat")));
        // expected array size using jsonPath expression:
        // $ <- indicates the json root element
        result.andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.greaterThanOrEqualTo(2)));
        // expected values
        // $[x] <- the x element in the array
        result.andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Octocat"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$[0].ageInYears").value(42));
        result.andExpect(
                MockMvcResultMatchers.jsonPath("$[0].picUrl").value("https://avatars1.githubusercontent.com/u/583231"));
    }

    //@Test
    @DisplayName("Created an new Cat and Expect it in the list returned by getting all cats")
    void testReturningTheListOfCats_successful() throws Exception {

        String testCatName = "TestCat-" + LocalTime.now().toString();
        String testCatJsonString = "{\"name\" : \"" + testCatName
                + "\", \"ageInYears\" : 3, \"picUrl\" : \"https://avatars1.githubusercontent.com/u/583231\"}";

        // performs a post request
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/cats").contentType(MediaType.APPLICATION_JSON)
                .content(testCatJsonString));

        // performs a get request
        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cats"));

        // Assertions for result object:
        // expected status code
        result.andExpect(status().isOk());

        // $ <- indicates the json root element
        result.andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.greaterThanOrEqualTo(1)));

        // expected name in the content
        result.andExpect(content().string(Matchers.containsString(testCatName)));
    }

    @Test
    @DisplayName("Expect a not found as response code")
    void testGettingtCatById_notFound() throws Exception {
        // performs a get request
        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/assignees"));

        // Assert result
        result.andExpect(status().isNotFound());
    }
}
