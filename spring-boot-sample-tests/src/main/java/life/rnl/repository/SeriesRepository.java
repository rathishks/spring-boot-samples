package life.rnl.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import life.rnl.domain.Series;

public interface SeriesRepository extends JpaRepository<Series, String>{

}
