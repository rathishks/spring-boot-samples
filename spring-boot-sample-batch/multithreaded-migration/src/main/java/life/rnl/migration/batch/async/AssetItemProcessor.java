package life.rnl.migration.batch.async;

import java.util.concurrent.atomic.AtomicInteger;

import javax.persistence.EntityManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import life.rnl.migration.destination.domain.Item;
import life.rnl.migration.destination.domain.Part;
import life.rnl.migration.destination.repository.PartRepository;
import life.rnl.migration.source.domain.Asset;

@Component
public class AssetItemProcessor implements ItemProcessor<Asset, Item> {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PartRepository partRepository;
	
	@Autowired
	private EntityManagerFactory entityManagerFactory;

	private AtomicInteger count = new AtomicInteger(0);

	@Override
	public Item process(final Asset item) throws Exception {
		Integer currentCount = count.getAndIncrement();
		log.info("[START] Processing record # {} - {}", currentCount, item.getId());

		final Item newItem = new Item();
		newItem.setAssetId(item.getId());
		newItem.setDateCreated(item.getDateCreated());
		newItem.setSerialNumber(item.getSerialNumber());

//		StopWatch stopWatch = new StopWatch();
//		stopWatch.start();
//		String uuid = UUID.randomUUID().toString();
		//log.info("processing finding {} - {} ", item.getPartType().getId(), uuid);
		//EntityManager entityManager = entityManagerFactory.createEntityManager();
		ListenableFuture<Part> partFuture = partRepository.findOneById(item.getPartType().getId());
		//Part part = entityManager.find(Part.class, item.getPartType().getId());
//		stopWatch.stop();
//		log.info("processing found {} - {} - time took: {}", item.getPartType().getId(), uuid,
//				stopWatch.getTotalTimeMillis());
		
		partFuture.addCallback(part -> {
			newItem.setPart(part);
		}, exception -> {
			
		});

		log.info("[END] Processed record # {} - {}", currentCount, item.getId());
		//entityManager.close();
		return newItem;
	}
}