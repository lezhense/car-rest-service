package ua.foxminded.lezhenin.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import net.kaczmarzyk.spring.data.jpa.domain.Between;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.GreaterThanOrEqual;
import net.kaczmarzyk.spring.data.jpa.domain.LessThanOrEqual;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Join;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Schema;
import ua.foxminded.lezhenin.exeption.CarNotFoundException;
import ua.foxminded.lezhenin.model.Car;
import ua.foxminded.lezhenin.model.utils.PagingHeaders;
import ua.foxminded.lezhenin.model.utils.PagingResponse;
import ua.foxminded.lezhenin.service.CarService;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cars")
public class CarController {
	private final CarService carService;

	@Autowired
	public CarController(CarService carService) {
		this.carService = carService;
	}

	@Operation(summary = "Get a car by its id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the car", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Car.class)) }),
			@ApiResponse(responseCode = "404", description = "Car not found", content = @Content) })
	@GetMapping("/{id}")
	public Car retrieveCar(@Parameter(description = "id of car to be searched") @PathVariable int id) {
		Optional<Car> carOptional = carService.getById(id);
		if (carOptional.isEmpty()) {
			throw new CarNotFoundException("Not found id: " + id);
		}
		return carOptional.get();
	}

	@Operation(summary = "Delete a car by its id")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Car is deleted", content = @Content),
			@ApiResponse(responseCode = "404", description = "Car not found", content = @Content),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content) })
	@DeleteMapping("/{id}")
	public void deleteCar(@Parameter(description = "id of car to be deleted") @PathVariable int id) {
		Optional<Car> carOptional = carService.getById(id);
		if (carOptional.isEmpty()) {
			throw new CarNotFoundException("Not found id: " + id);
		}
		Car car = carOptional.get();
		carService.deleteById(car.getId());
	}

	@Operation(summary = "Create a car")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Car is created", content = @Content),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content) })
	@PostMapping
	public ResponseEntity<Object> createCar(@RequestBody Car car, HttpServletRequest request) {
		Car savedCar = carService.save(car);
		URI location = ServletUriComponentsBuilder.fromRequestUri(request).path("/{id}")
				.buildAndExpand(savedCar.getId()).toUri();
		return ResponseEntity.created(location).build();
	}

	@Operation(summary = "Update a car")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Car is updated", content = @Content),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content) })
	@PutMapping
	public ResponseEntity<Object> updateCar(@RequestBody Car car) {
		Optional<Car> carToUpdateOptional = carService.getById(car.getId());
		if (carToUpdateOptional.isEmpty()) {
			throw new CarNotFoundException("Not found id: " + car.getId());
		}
		Car carToUpdate = carToUpdateOptional.get();
		carToUpdate.setObjectId(car.getObjectId());
		carToUpdate.setMake(car.getMake());
		carToUpdate.setYear(car.getYear());
		carToUpdate.setModel(car.getModel());
		carToUpdate.setCategory(car.getCategory());
		carService.save(carToUpdate);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "Get a list of cars by filters if any")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Cars are founded", content = @Content) })
	@Transactional
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<List<Car>> get(
			@Join(path = "category", alias = "cat") @And({ @Spec(path = "make", params = "make", spec = Like.class),
					@Spec(path = "model", params = "model", spec = Like.class),
					@Spec(path = "year", params = "year", spec = Equal.class),
					@Spec(path = "cat.name", params = "category", spec = Like.class),
					@Spec(path = "year", params = "minYear", spec = GreaterThanOrEqual.class),
					@Spec(path = "year", params = "maxYear", spec = LessThanOrEqual.class),
					@Spec(path = "year", params = { "startYear",
							"endYear" }, spec = Between.class) }) Specification<Car> spec,
			Sort sort, @RequestHeader HttpHeaders headers) {
		final PagingResponse response = carService.get(spec, headers, sort);
		return new ResponseEntity<>(response.getElements(), returnHttpHeaders(response), HttpStatus.OK);
	}

	public HttpHeaders returnHttpHeaders(PagingResponse response) {
		HttpHeaders headers = new HttpHeaders();
		headers.set(PagingHeaders.COUNT.getName(), String.valueOf(response.getCount()));
		headers.set(PagingHeaders.PAGE_SIZE.getName(), String.valueOf(response.getPageSize()));
		headers.set(PagingHeaders.PAGE_OFFSET.getName(), String.valueOf(response.getPageOffset()));
		headers.set(PagingHeaders.PAGE_NUMBER.getName(), String.valueOf(response.getPageNumber()));
		headers.set(PagingHeaders.PAGE_TOTAL.getName(), String.valueOf(response.getPageTotal()));
		return headers;
	}

}
