package life.rnl.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import life.rnl.domain.Episode;

public interface EpisodeRepository extends JpaRepository<Episode, String>{

}
