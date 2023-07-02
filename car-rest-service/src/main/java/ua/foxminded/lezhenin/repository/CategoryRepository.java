package ua.foxminded.lezhenin.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.foxminded.lezhenin.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

	public Optional<Category> findByName(String name);

}
