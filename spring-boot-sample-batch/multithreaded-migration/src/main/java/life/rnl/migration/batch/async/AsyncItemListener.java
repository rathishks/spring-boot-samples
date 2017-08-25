package life.rnl.migration.batch.async;

import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.annotation.AfterProcess;
import org.springframework.batch.core.annotation.BeforeProcess;
import org.springframework.stereotype.Component;

import life.rnl.migration.destination.domain.Item;
import life.rnl.migration.source.domain.Asset;

@Component
public class AsyncItemListener {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	private int i = 0;

	@BeforeProcess
	public void beforeProcess(Asset item) {
		log.debug("Processing item {}.", i++);
	}

	@AfterProcess
	public void afterProcess(Asset item, Future<Item> result) {
		log.debug("End processing item {}.", i);
	}
}
