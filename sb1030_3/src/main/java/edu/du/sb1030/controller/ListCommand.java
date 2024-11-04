package edu.du.sb1030.controller;

import java.time.LocalDateTime;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class ListCommand {
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime from;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime to;

	
	
}
