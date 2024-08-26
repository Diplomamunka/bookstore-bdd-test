package com.szelestamas.bookstorebddtest.api.category;

import com.szelestamas.bookstorebddtest.core.RunContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RequiredArgsConstructor
public class CategorySteps {
    private final RunContext runContext;
    private final CategoryApiManagementClient apiManagementClient;

    @Then("User can read the following categories:")
    public void userReceivesTheCategories(List<String> expectedCategories) {
        ResponseEntity<List<CategoryResource>> response = apiManagementClient.getCategories();
        assertEquals(200, response.getStatusCode().value());
        assertThat(expectedCategories, containsInAnyOrder(response.getBody()
                .stream().map(category -> {
                    if (runContext.isAFeatureTestIdentifier(category.name()))
                        return runContext.identifierWithoutRunId(category.name());
                    return category.name();
                }).toArray()));
    }


    @Given("The default categories")
    public void theDefaultCategories() {

    }

    @When("User adds the category {string}")
    public void userAddsCategory(String name) {
        CategoryDto category = new CategoryDto(runContext.identifierWithRunId(name));
        ResponseEntity<CategoryResource> response =  apiManagementClient.createCategory(category);
        runContext.addCreatedResource(name, response.getBody());
        assertEquals(201, response.getStatusCode().value());
        assertEquals(name, runContext.identifierWithoutRunId(response.getBody().name()));
        assertTrue(response.getHeaders().containsKey("Location"));
        String location = apiManagementClient.getApplicationUrl() + "/categories/" + response.getBody().id();
        assertEquals(location, response.getHeaders().getLocation().toString());
    }

    @And("User can read the previously created category {string}")
    public void userCanReadThePreviouslyCreatedCategory(String name) {
        CategoryResource expectedCategory = runContext.createdResource(CategoryResource.class, name);
        ResponseEntity<CategoryResource> response = apiManagementClient.getCategory(expectedCategory.id());
        assertEquals(200, response.getStatusCode().value());
        assertEquals(name, runContext.identifierWithoutRunId(response.getBody().name()));
    }

    @When("User cannot create the category {string}")
    public void userCannotCreateTheCategory(String name) {
        CategoryDto category = new CategoryDto(runContext.identifierWithRunId(name));
        ResponseEntity<CategoryResource> response =  apiManagementClient.createCategory(category);
        assertEquals(409, response.getStatusCode().value());
    }

    @When("User deletes the created category {string}")
    public void userDeleteCategory(String name) {
        CategoryResource categoryResource = runContext.createdResource(CategoryResource.class, name);
        ResponseEntity<Void> response = apiManagementClient.deleteCategory(categoryResource.id());
        runContext.removeCreatedResource(name, CategoryResource.class);
        assertEquals(204, response.getStatusCode().value());
    }
}
