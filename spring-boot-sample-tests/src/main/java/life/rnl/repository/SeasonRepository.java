package life.rnl.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import life.rnl.domain.Season;

public interface SeasonRepository extends JpaRepository<Season, String>{

}
