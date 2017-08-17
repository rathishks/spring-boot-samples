package life.rnl.migration.source.domain;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "ASSET")
public class Asset {
	@Id
	@GeneratedValue
	private Integer id;

	@Version
	private Integer version;

	@NotNull
	@Size(min = 4)
	@Pattern(regexp = "[a-zA-Z0-9]")
	@Column(name = "SERIAL_NUMBER")
	private String serialNumber;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_CREATED")
	private Calendar dateCreated;

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Calendar getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Calendar dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}