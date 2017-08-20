package life.rnl.migration.batch;

import java.util.concurrent.FutureTask;

import org.springframework.batch.core.annotation.AfterProcess;
import org.springframework.batch.core.annotation.AfterRead;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import life.rnl.migration.destination.domain.Item;
import life.rnl.migration.source.domain.Asset;
import life.rnl.migration.source.domain.ProcessedStatus;
import life.rnl.migration.source.repository.AssetRepository;

@Component
public class ProcessedIndicatorStepListener {
	@Autowired
	private AssetRepository assetRepository;

	@AfterRead
	public void afterRead(Asset item) {
		item.setProcessed(ProcessedStatus.READ);
		assetRepository.save(item);
	}

	@AfterProcess
	public void afterProcess(Asset item, FutureTask<Item> result) {
		item.setProcessed(ProcessedStatus.PROCESSED);
		assetRepository.save(item);
	}
}