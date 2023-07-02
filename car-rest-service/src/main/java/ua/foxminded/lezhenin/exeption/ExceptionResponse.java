package ua.foxminded.lezhenin.exeption;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class ExceptionResponse {
	private Date timestamp;
	private String message;
	private String detail;

	public ExceptionResponse(Date timestamp, String message, String detail) {
		this.timestamp = timestamp;
		this.message = message;
		this.detail = detail;
	}
}
