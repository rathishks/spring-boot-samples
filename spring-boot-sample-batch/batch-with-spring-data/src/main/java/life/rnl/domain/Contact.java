package life.rnl.domain;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import life.rnl.batch.excel.annotations.ExcelColumn;
import life.rnl.batch.excel.annotations.ExcelSheet;

@Entity
@ExcelSheet("Contacts")
public class Contact {
	@Id
	@GeneratedValue
	private String id;

	@Version
	private Integer version;

	@NotNull
	@Size(min = 1)
	@Pattern(regexp = "[a-zA-Z]")
	@ExcelColumn("First Name")
	private String firstName;

	@NotNull
	@Size(min = 1)
	@Pattern(regexp = "[a-zA-Z]")
	@ExcelColumn("Last Name")
	private String lastName;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@ExcelColumn("Date of Birth")
	private Calendar dateOfBirth;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Calendar getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Calendar dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
}