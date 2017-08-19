package life.rnl.migration.source.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import life.rnl.migration.source.domain.Asset;

public interface AssetRepository extends JpaRepository<Asset, Integer> {

	Long countByProcessed(Boolean processed);
}
