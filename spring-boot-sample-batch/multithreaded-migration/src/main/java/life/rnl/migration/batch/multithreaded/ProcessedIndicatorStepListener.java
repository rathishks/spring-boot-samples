package life.rnl.migration.batch.multithreaded;

import java.util.List;

import org.springframework.batch.core.annotation.AfterWrite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import life.rnl.migration.destination.domain.Item;
import life.rnl.migration.source.domain.Asset;
import life.rnl.migration.source.domain.ProcessedStatus;
import life.rnl.migration.source.repository.AssetRepository;

@Component
public class ProcessedIndicatorStepListener {
	@Autowired
	private AssetRepository assetRepository;

	@AfterWrite
	@Transactional("sourceTransactionManager")
	public void afterWrite(List<Item> items) {
		items.forEach((item) -> {
			Asset asset = assetRepository.findOne(item.getAssetId());
			asset.setProcessed(ProcessedStatus.WRITTEN);
			assetRepository.save(asset);
		});
	}
}