package life.rnl.migration.destination.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.concurrent.ListenableFuture;

import life.rnl.migration.destination.domain.Part;

@Async
public interface PartRepository extends JpaRepository<Part, Integer> {
	
	ListenableFuture<Part> findOneById(Integer partId);
}
