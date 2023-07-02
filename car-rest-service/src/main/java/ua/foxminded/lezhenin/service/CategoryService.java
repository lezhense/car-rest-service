package ua.foxminded.lezhenin.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import ua.foxminded.lezhenin.model.Category;
import ua.foxminded.lezhenin.repository.CategoryRepository;

@Service
@Slf4j
public class CategoryService {
	private final CategoryRepository categoryRepository;

	@Autowired
	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	public List<Category> getAll() {
		List<Category> categories = categoryRepository.findAll();
		log.info("List of all categories was found");
		return categories;
	}

	public Optional<Category> getById(Integer id) {
		return categoryRepository.findById(id);
	}

	public Optional<Category> getByName(String name) {
		return categoryRepository.findByName(name);
	}

	public Category save(Category categoryEntity) {
		Category category = categoryRepository.save(categoryEntity);
		log.info("Category with id {} was saved", categoryEntity.getId());
		return category;
	}

	public void deleteById(Integer id) {
		categoryRepository.deleteById(id);
		log.info("Category with id {} was deleted", id);
	}

}
