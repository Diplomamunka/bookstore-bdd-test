package com.szelestamas.bookstorebddtest.api.category;

import com.szelestamas.bookstorebddtest.api.book.BookResource;
import com.szelestamas.bookstorebddtest.core.RunContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RequiredArgsConstructor
public class CategorySteps {
    private final RunContext runContext;
    private final CategoryApiManagementClient apiManagementClient;

    @Then("{word} can read the following categories:")
    public void userReceivesTheCategories(String personaName, List<String> expectedCategories) {
        ResponseEntity<List<CategoryResource>> response = apiManagementClient.getCategories();
        assertEquals(200, response.getStatusCode().value());
        List<String> categoryNames = response.getBody().stream().map(category -> {
            if (runContext.isAFeatureTestIdentifier(category.name()))
                return runContext.identifierWithoutRunId(category.name());
            return category.name();
        }).toList();
        assertTrue(categoryNames.size() >= expectedCategories.size());
        expectedCategories.forEach(category -> assertTrue(categoryNames.contains(category)));
    }


    @Given("The default categories")
    public void theDefaultCategories() {

    }

    @When("{word} adds the category {string}")
    public void userAddsCategory(String personaName, String name) {
        CategoryDto category = new CategoryDto(runContext.identifierWithRunId(name));
        ResponseEntity<CategoryResource> response =  apiManagementClient.createCategory(category, personaName);
        assertEquals(201, response.getStatusCode().value());
        runContext.addCreatedResource(name, response.getBody());
        assertEquals(name, runContext.identifierWithoutRunId(response.getBody().name()));
        assertTrue(response.getHeaders().containsKey("Location"));
        String location = apiManagementClient.getBaseUrl() + "/categories/" + response.getBody().id();
        assertEquals(location, response.getHeaders().getLocation().toString());
    }

    @And("{word} can read the previously created category {string}")
    public void userCanReadThePreviouslyCreatedCategory(String personaName, String name) {
        CategoryResource expectedCategory = runContext.createdResource(CategoryResource.class, name);
        ResponseEntity<CategoryResource> response = apiManagementClient.getCategory(expectedCategory.id());
        assertEquals(200, response.getStatusCode().value());
        assertEquals(name, runContext.identifierWithoutRunId(response.getBody().name()));
    }

    @When("{word} cannot create the category {string}")
    public void userCannotCreateTheCategory(String personaName, String name) {
        CategoryDto category = new CategoryDto(runContext.identifierWithRunId(name));
        ResponseEntity<CategoryResource> response =  apiManagementClient.createCategory(category, personaName);
        assertEquals(409, response.getStatusCode().value());
    }

    @When("{word} deletes the created category {string}")
    public void userDeleteCategory(String personaName, String name) {
        CategoryResource categoryResource = runContext.createdResource(CategoryResource.class, name);
        ResponseEntity<Void> response = apiManagementClient.deleteCategory(categoryResource.id(), personaName);
        runContext.removeCreatedResource(name, CategoryResource.class);
        assertEquals(204, response.getStatusCode().value());
    }

    @Then("{word} cannot delete the category {string}")
    public void userCannotDeleteTheCategory(String personaName, String name) {
        CategoryResource categoryResource = runContext.createdResource(CategoryResource.class, name);
        ResponseEntity<Void> response = apiManagementClient.deleteCategory(categoryResource.id(), personaName);
        assertEquals(409, response.getStatusCode().value());
    }

    @When("{word} deletes all books in the category {string}")
    public void userDeletesAllBooksInTheCategory(String personaName, String name) {
        CategoryResource categoryResource = runContext.createdResource(CategoryResource.class, name);
        ResponseEntity<Void> response = apiManagementClient.deleteBooksByCategory(categoryResource.id(), personaName);
        assertEquals(204, response.getStatusCode().value());
    }

    @Then("All books deleted from the category {string}")
    public void allBooksDeletedFromTheCategory(String name) {
        CategoryResource categoryResource = runContext.createdResource(CategoryResource.class, name);
        ResponseEntity<List<BookResource>> response = apiManagementClient.getBooksByCategory(categoryResource.id());
        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().isEmpty());
    }

    @Then("{word} doesn't have the rights to add the category {string}")
    public void userDoesntHaveTheRightsToAddTheCategory(String personaName, String name) {
        CategoryDto category = new CategoryDto(runContext.identifierWithRunId(name));
        ResponseEntity<CategoryResource> response = apiManagementClient.createCategory(category, personaName);
        assertEquals(403, response.getStatusCode().value());
    }

    @Then("{word} doesn't have the rights to delete the category {string}")
    public void userDoesntHaveTheRightsToDeleteTheCategory(String personaName, String name) {
        CategoryResource categoryResource = runContext.createdResource(CategoryResource.class, name);
        ResponseEntity<Void> response = apiManagementClient.deleteCategory(categoryResource.id(), personaName);
        assertEquals(403, response.getStatusCode().value());
    }

    @When("{word} cannot add the category {string}")
    public void userCannotAddTheCategory(String personaName, String name) {
        CategoryDto category = new CategoryDto(runContext.identifierWithRunId(name));
        ResponseEntity<CategoryResource> response = apiManagementClient.createCategory(category, "");
        assertEquals(401, response.getStatusCode().value());
    }

    @Then("{word} is not able to delete the category {string}")
    public void userIsNotAbleToDeleteTheCategory(String personaName, String name) {
        CategoryResource categoryResource = runContext.createdResource(CategoryResource.class, name);
        ResponseEntity<Void> response = apiManagementClient.deleteCategory(categoryResource.id(), "");
        assertEquals(401, response.getStatusCode().value());
    }

    @When("{word} updates the previously added category's name from {string} to {string}")
    public void userUpdatesThePreviouslyAddedCategoryName(String personaName, String oldName, String newName) {
        CategoryResource categoryResource = runContext.createdResource(CategoryResource.class, oldName);
        runContext.removeCreatedResource(oldName, CategoryResource.class);
        CategoryDto category = new CategoryDto(runContext.identifierWithRunId(newName));
        ResponseEntity<CategoryResource> response = apiManagementClient.updateCategory(categoryResource.id(), category ,personaName);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(runContext.identifierWithRunId(newName), response.getBody().name());
        runContext.addCreatedResource(newName, response.getBody());
    }

    @Then("{word} can read the previously modified category {string}")
    public void userCanReadThePreviouslyModifiedCategory(String personaName, String name) {
        CategoryResource categoryResource = runContext.createdResource(CategoryResource.class, name);
        ResponseEntity<CategoryResource> response = apiManagementClient.getCategory(categoryResource.id());
        assertEquals(200, response.getStatusCode().value());
        assertEquals(name, runContext.identifierWithoutRunId(response.getBody().name()));
    }

    @Then("{word} doesn't have the rights to update the previously added category's name from {string} to {string}")
    public void userDoesntHaveTheRightsToUpdateThePreviouslyAddedCategory(String personaName, String oldName, String newName) {
        CategoryResource categoryResource = runContext.createdResource(CategoryResource.class, oldName);
        CategoryDto category = new CategoryDto(runContext.identifierWithRunId(newName));
        ResponseEntity<CategoryResource> response = apiManagementClient.updateCategory(categoryResource.id(), category ,personaName);
        assertEquals(403, response.getStatusCode().value());
    }

    @Then("{word} is not able to update the previously added category's name from {string} to {string}")
    public void userIsNotAbleToUpdateThePreviouslyAddedCategory(String personaName, String oldName, String newName) {
        CategoryResource categoryResource = runContext.createdResource(CategoryResource.class, oldName);
        CategoryDto category = new CategoryDto(runContext.identifierWithRunId(newName));
        ResponseEntity<CategoryResource> response = apiManagementClient.updateCategory(categoryResource.id(), category, "");
        assertEquals(401, response.getStatusCode().value());
    }

    @Then("{word} cannot update the category from {string} to {string}")
    public void userCannotUpdateTheCategory(String personaName, String oldName, String newName) {
        CategoryResource categoryResource = runContext.createdResource(CategoryResource.class, oldName);
        CategoryDto category = new CategoryDto(runContext.identifierWithRunId(newName));
        ResponseEntity<CategoryResource> response = apiManagementClient.updateCategory(categoryResource.id(), category, personaName);
        assertEquals(409, response.getStatusCode().value());
    }
}
