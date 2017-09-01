package life.rnl.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import life.rnl.domain.Actor;

public interface ActorRepository extends JpaRepository<Actor, String>{

}
