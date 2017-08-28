package life.rnl.migration.destination.domain;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "ITEM", uniqueConstraints = {
		@UniqueConstraint(name = "UQ_PART_AND_SERIAL", columnNames = { "PART", "SERIAL_NUMBER" }) })
public class Item {
	@Id
	@GeneratedValue
	private Integer id;

	@Version
	private Integer version;

	@NotNull
	@Size(min = 4)
	@Pattern(regexp = "[a-zA-Z0-9]")
	@Column(name = "SERIAL_NUMBER", nullable = false, unique = true)
	private String serialNumber;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_CREATED", nullable = false)
	private Calendar dateCreated;

	@NotNull
	@Column(nullable = false, unique = true)
	private Integer assetId;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "PART", nullable = false)
	private Part part;

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

	public Integer getAssetId() {
		return assetId;
	}

	public void setAssetId(Integer assetId) {
		this.assetId = assetId;
	}

	public Part getPart() {
		return part;
	}

	public void setPart(Part part) {
		this.part = part;
	}
}