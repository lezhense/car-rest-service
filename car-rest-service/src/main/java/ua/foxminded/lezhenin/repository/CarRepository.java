package ua.foxminded.lezhenin.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import ua.foxminded.lezhenin.model.Car;

@Repository
public interface CarRepository extends 
		CrudRepository<Car, Integer>, 
		PagingAndSortingRepository<Car, Integer>, 
		JpaSpecificationExecutor<Car> {

}
