package life.rnl.migration.destination.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import life.rnl.migration.destination.domain.Part;

public interface PartRepository extends JpaRepository<Part, Integer> {

}
