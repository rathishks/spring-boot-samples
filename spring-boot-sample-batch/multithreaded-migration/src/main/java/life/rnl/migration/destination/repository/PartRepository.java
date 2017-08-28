package life.rnl.migration.destination.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import life.rnl.migration.destination.domain.Part;

public interface PartRepository extends JpaRepository<Part, Integer> {
	
	@Query("SELECT p FROM Part p WHERE id = ?1")
	List<Part> getByPartId(Integer partId);
}
