package ua.foxminded.lezhenin.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CarNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -8109071526561892042L;

	public CarNotFoundException(String message) {
		super(message);
	}

}
