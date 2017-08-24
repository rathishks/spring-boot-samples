package life.rnl.dto;

import java.util.Calendar;

public class VehicleView {
	private Long id;

	private Calendar dateManufactured;

	private Calendar dateSold;

	public Calendar getDateManufactured() {
		return dateManufactured;
	}

	public void setDateManufactured(Calendar dateManufactured) {
		this.dateManufactured = dateManufactured;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Calendar getDateSold() {
		return dateSold;
	}

	public void setDateSold(Calendar dateSold) {
		this.dateSold = dateSold;
	}
}
