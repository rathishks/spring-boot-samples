package life.rnl.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Series extends BaseEntity {
	@NotNull
	@Size(min = 1)
	@Column(nullable = false)
	private String name;

	@OneToMany(mappedBy = "series", targetEntity = Season.class)
	private Set<Season> seasons = new HashSet<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Season> getSeasons() {
		return seasons;
	}

	public void setSeasons(Set<Season> seasons) {
		this.seasons = seasons;
	}
}
