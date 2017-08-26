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
@Table(name = "PART_TYPE")
public class PartType {
	@Id
	@GeneratedValue
	private Integer id;

	@Version
	private Integer version;

	@NotNull
	@Size(min = 4)
	@Pattern(regexp = "[a-zA-Z0-9-]")
	@Column(name = "PART_NUMBER", nullable = false)
	private String partNumber;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_CREATED", nullable = false)
	private Calendar dateCreated;

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
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

	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}
}