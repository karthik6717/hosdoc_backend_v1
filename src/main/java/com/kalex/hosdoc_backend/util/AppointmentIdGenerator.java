package com.kalex.hosdoc_backend.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class AppointmentIdGenerator {
	private static final String PREFIX = "HOS-";
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
	
	public String generateAppointmentId(Long sequenceNumber) {
		String dateStr = LocalDate.now().format(DATE_FORMATTER);
		String sequence = String.format("%06d", sequenceNumber);
		return PREFIX + dateStr + "-" + sequence;
	}
}

