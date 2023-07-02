package ua.foxminded.lezhenin.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import ua.foxminded.lezhenin.exeption.CategoryNotFoundException;
import ua.foxminded.lezhenin.model.Car;
import ua.foxminded.lezhenin.model.Category;
import ua.foxminded.lezhenin.service.CategoryService;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
	private final CategoryService categoryService;

	@Autowired
	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@Operation(summary = "Get all Categories")
	@GetMapping
	public List<Category> retrieveAllCategories() {
		return categoryService.getAll();
	}

	@Operation(summary = "Get a Category by its id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the Category", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Car.class)) }),
			@ApiResponse(responseCode = "404", description = "Category not found", content = @Content) })
	@GetMapping("/{id}")
	public Category retrieveCategory(@PathVariable int id) {
		Optional<Category> categoryOptional = categoryService.getById(id);
		if (categoryOptional.isEmpty()) {
			throw new CategoryNotFoundException("Not found id: " + id);
		}
		return categoryOptional.get();
	}

	@Operation(summary = "Delete a Category by its id")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Category is deleted", content = @Content),
			@ApiResponse(responseCode = "404", description = "Category not found", content = @Content) })
	@DeleteMapping("/{id}")
	public void deleteCategory(@PathVariable int id) {
		Optional<Category> categoryOptional = categoryService.getById(id);
		if (categoryOptional.isEmpty()) {
			throw new CategoryNotFoundException("Not found id: " + id);
		}
		Category category = categoryOptional.get();
		categoryService.deleteById(category.getId());
	}

	@Operation(summary = "Create a Category")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Category is created", content = @Content) })
	@PostMapping
	public ResponseEntity<Object> createCategory(@RequestBody Category category) {
		Category sevedCategory = categoryService.save(category);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(sevedCategory.getId()).toUri();
		return ResponseEntity.created(location).build();
	}

	@Operation(summary = "Update a Category")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Category is updated", content = @Content) })
	@PutMapping
	public ResponseEntity<Object> updateCategory(@RequestBody Category category) {
		Optional<Category> categoryToUpdateOptional = categoryService.getById(category.getId());
		if (categoryToUpdateOptional.isEmpty()) {
			throw new CategoryNotFoundException("Not found id: " + category.getId());
		}
		Category categoryToUpdate = categoryToUpdateOptional.get();
		categoryToUpdate.setName(category.getName());
		categoryService.save(categoryToUpdate);
		return ResponseEntity.ok().build();
	}

}
