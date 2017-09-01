package life.rnl.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Season extends BaseEntity {
	@NotNull
	@Size(min = 1)
	@Column(nullable = false)
	private String name;

	@NotNull
	@Column(nullable = false)
	private Integer number;

	@OneToMany(mappedBy = "season", targetEntity = Episode.class)
	private Set<Episode> episodes = new HashSet<>();

	@ManyToOne(optional = false)
	@JoinColumn(nullable = false)
	private Series series;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Set<Episode> getEpisodes() {
		return episodes;
	}

	public void setEpisodes(Set<Episode> episodes) {
		this.episodes = episodes;
	}
}
