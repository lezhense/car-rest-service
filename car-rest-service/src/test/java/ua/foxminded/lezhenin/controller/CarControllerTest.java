package ua.foxminded.lezhenin.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;
import ua.foxminded.lezhenin.exeption.CarNotFoundException;
import ua.foxminded.lezhenin.model.Car;
import ua.foxminded.lezhenin.model.utils.PagingResponse;
import ua.foxminded.lezhenin.service.CarService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarControllerTest {
	@Mock
	private CarService carService;

	@InjectMocks
	private CarController carController;

	@Test
	void test_RetrieveCar_shouldReturnCar_whenFound() {
		int carId = 1;
		Car car = new Car();
		car.setId(carId);
		when(carService.getById(carId)).thenReturn(Optional.of(car));
		Car retrievedCar = carController.retrieveCar(carId);
		assertNotNull(retrievedCar);
		assertEquals(carId, retrievedCar.getId());
	}

	@Test
	void test_RetrieveCar_shouldThrowException_whenNotFound() {
		int carId = 1;
		when(carService.getById(carId)).thenReturn(Optional.empty());
		assertThrows(CarNotFoundException.class, () -> {
			carController.retrieveCar(carId);
		});
	}

	@Test
	void test_DeleteCar_shouldDeleteCar_whenFound() {
		int carId = 1;
		Car car = new Car();
		car.setId(carId);
		when(carService.getById(carId)).thenReturn(Optional.of(car));
		carController.deleteCar(carId);
		verify(carService).deleteById(carId);
	}

	@Test
	void test_DeleteCar_shouldThrowException_whenNotFound() {
		int carId = 1;
		when(carService.getById(carId)).thenReturn(Optional.empty());
		assertThrows(CarNotFoundException.class, () -> {
			carController.deleteCar(carId);
		});
	}

	@Test
	void createCar_shouldReturnCreatedResponse() {
		Car car = new Car();
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getRequestURI()).thenReturn("/api/cars");
		when(carService.save(car)).thenReturn(car);
		ResponseEntity<Object> response = carController.createCar(car, request);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

	@Test
	void test_UpdateCar_shouldUpdateCar_whenFound() {
		Car carToUpdate = new Car();
		carToUpdate.setId(1);
		when(carService.getById(carToUpdate.getId())).thenReturn(Optional.of(carToUpdate));
		ResponseEntity<Object> response = carController.updateCar(carToUpdate);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(carService).save(carToUpdate);
	}

	@Test
	void test_UpdateCar_shouldThrowException_whenNotFound() {
		Car carToUpdate = new Car();
		carToUpdate.setId(1);
		when(carService.getById(carToUpdate.getId())).thenReturn(Optional.empty());
		assertThrows(CarNotFoundException.class, () -> {
			carController.updateCar(carToUpdate);
		});
	}

	@Test
	void test_Get_shouldReturnListOfCars() {
		Specification<Car> spec = mock(Specification.class);
		Sort sort = Sort.by("make");
		HttpHeaders headers = new HttpHeaders();
		List<Car> cars = Arrays.asList(new Car(), new Car());
		PagingResponse pagingResponse = new PagingResponse((long) cars.size(), 0L, 0L, 0L, 0L, cars);
		pagingResponse.setElements(cars);
		when(carService.get(spec, headers, sort)).thenReturn(pagingResponse);
		ResponseEntity<List<Car>> response = carController.get(spec, sort, headers);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(cars, response.getBody());
	}
}
