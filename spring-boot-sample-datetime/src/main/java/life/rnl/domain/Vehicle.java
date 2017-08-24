package life.rnl.domain;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
public class Vehicle {
	@Id
	@GeneratedValue
	private Long id;
	
	@Version
	private Long version;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar dateManufactured;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar dateSold;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public Calendar getDateManufactured() {
		return dateManufactured;
	}

	public void setDateManufactured(Calendar dateManufactured) {
		this.dateManufactured = dateManufactured;
	}

	public Calendar getDateSold() {
		return dateSold;
	}

	public void setDateSold(Calendar dateSold) {
		this.dateSold = dateSold;
	}
}