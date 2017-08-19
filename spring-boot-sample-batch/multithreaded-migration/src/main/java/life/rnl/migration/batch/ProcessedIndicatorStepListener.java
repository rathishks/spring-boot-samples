package life.rnl.migration.batch;

import org.springframework.batch.core.annotation.AfterRead;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import life.rnl.migration.source.domain.Asset;
import life.rnl.migration.source.repository.AssetRepository;

@Component
public class ProcessedIndicatorStepListener{
	@Autowired
	private AssetRepository assetRepository;

	@AfterRead
	@Transactional("sourceTransactionManager")
	public void afterRead(Asset item) {
		item.setProcessed(true);
		assetRepository.save(item);
	}
}
