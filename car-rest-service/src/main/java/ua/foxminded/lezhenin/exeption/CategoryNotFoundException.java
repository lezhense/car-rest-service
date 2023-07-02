package ua.foxminded.lezhenin.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CategoryNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -3928080825183991012L;

	public CategoryNotFoundException(String message) {
		super(message);
	}

}
