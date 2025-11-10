package com.kalex.hosdoc_backend.dto;

import com.kalex.hosdoc_backend.enums.Shift;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AvailabilityDTO {
	private LocalDate date;
	private List<ShiftAvailabilityDTO> shifts;
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ShiftAvailabilityDTO {
		private Shift shift;
		private LocalTime startTime;
		private LocalTime endTime;
		private List<SlotDTO> availableSlots;
	}
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class SlotDTO {
		private LocalTime slotTime;
		private Boolean available;
	}
}

