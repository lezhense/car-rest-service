package ua.foxminded.lezhenin.model.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ua.foxminded.lezhenin.model.Car;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PagingResponse {
	private Long count;
	private Long pageNumber;
	private Long pageSize;
	private Long pageOffset;
	private Long pageTotal;
	private List<Car> elements;
}
