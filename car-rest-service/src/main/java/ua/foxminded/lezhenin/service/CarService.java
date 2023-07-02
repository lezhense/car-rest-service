package ua.foxminded.lezhenin.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import ua.foxminded.lezhenin.model.Car;
import ua.foxminded.lezhenin.model.utils.PagingHeaders;
import ua.foxminded.lezhenin.model.utils.PagingResponse;
import ua.foxminded.lezhenin.repository.CarRepository;

@Service
@Slf4j
public class CarService {
	private final CarRepository carRepository;

	@Autowired
	public CarService(CarRepository carRepository) {
		this.carRepository = carRepository;
	}

	public PagingResponse get(Specification<Car> spec, HttpHeaders headers, Sort sort) {
		if (isRequestPaged(headers)) {
			return get(spec, buildPageRequest(headers, sort));
		} else {
			final List<Car> entities = get(spec, sort);
			return new PagingResponse((long) entities.size(), 0L, 0L, 0L, 0L, entities);
		}
	}

	public PagingResponse get(Specification<Car> spec, Pageable pageable) {
		Page<Car> page = carRepository.findAll(spec, pageable);
		List<Car> content = page.getContent();
		return new PagingResponse(page.getTotalElements(), (long) page.getNumber(), (long) page.getNumberOfElements(),
				pageable.getOffset(), (long) page.getTotalPages(), content);
	}

	public List<Car> get(Specification<Car> spec, Sort sort) {
		return carRepository.findAll(spec, sort);
	}

	private boolean isRequestPaged(HttpHeaders headers) {
		return headers.containsKey(PagingHeaders.PAGE_NUMBER.getName())
				&& headers.containsKey(PagingHeaders.PAGE_SIZE.getName());
	}

	private Pageable buildPageRequest(HttpHeaders headers, Sort sort) {
		int page = Integer.parseInt(Objects.requireNonNull(headers.get(PagingHeaders.PAGE_NUMBER.getName())).get(0));
		int size = Integer.parseInt(Objects.requireNonNull(headers.get(PagingHeaders.PAGE_SIZE.getName())).get(0));
		return PageRequest.of(page, size, sort);
	}

	public Optional<Car> getById(Integer id) {
		return carRepository.findById(id);
	}

	public Car save(Car carEntity) {
		Car car = carRepository.save(carEntity);
		log.info("Car with id {} was saved", carEntity.getId());
		return car;
	}

	public void deleteById(Integer id) {
		carRepository.deleteById(id);
		log.info("Car with id {} was deleted", id);
	}

}
