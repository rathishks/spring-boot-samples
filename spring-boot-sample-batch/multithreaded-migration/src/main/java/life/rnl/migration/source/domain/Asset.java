package life.rnl.migration.source.domain;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
	@Column(name = "SERIAL_NUMBER", nullable = false)
	private String serialNumber;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_CREATED", nullable = false)
	private Calendar dateCreated;

	@Enumerated(EnumType.STRING)
	@Column(name = "PROCESSED_IND", nullable = false)
	private ProcessedStatus processed = ProcessedStatus.UNREAD;

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

	public ProcessedStatus getProcessed() {
		return processed;
	}

	public void setProcessed(ProcessedStatus processed) {
		this.processed = processed;
	}
}