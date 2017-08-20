package life.rnl.migration.source.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import life.rnl.migration.source.domain.Asset;
import life.rnl.migration.source.domain.ProcessedStatus;

public interface AssetRepository extends JpaRepository<Asset, Integer> {

	Long countByProcessed(ProcessedStatus status);
}
