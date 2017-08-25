package life.rnl.migration.batch.multithreaded;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.annotation.AfterWrite;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;

import life.rnl.migration.destination.domain.Item;
import life.rnl.migration.source.domain.Asset;
import life.rnl.migration.source.domain.ProcessedStatus;

public class ProcessedIndicatorStepListener {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private EntityManagerFactory entityManagerFactory;

	@AfterWrite
	public void afterWrite(List<Item> items) {
		EntityManager entityManager = EntityManagerFactoryUtils.getTransactionalEntityManager(entityManagerFactory);
		if (entityManager == null) {
			throw new DataAccessResourceFailureException("Unable to obtain a transactional EntityManager");
		}
		entityManager.getTransaction().begin();

		updateFlags(entityManager, items);

		entityManager.flush();

		entityManager.getTransaction().commit();
	}

	private void updateFlags(EntityManager entityManager, List<Item> items) {
		if (!items.isEmpty()) {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();

			CriteriaUpdate<Asset> criteriaUpdate = builder.createCriteriaUpdate(Asset.class);

			Root<Asset> root = criteriaUpdate.from(Asset.class);

			criteriaUpdate.set(root.get("processed"), ProcessedStatus.WRITTEN);

			List<Integer> assetIds = items.parallelStream().map((item) -> item.getAssetId())
					.collect(Collectors.toList());

			criteriaUpdate.where(root.get("id").in(assetIds));

			Query query = entityManager.createQuery(criteriaUpdate);

			int updated = query.executeUpdate();

			logger.debug(updated + " entities marked as written.");
		}
	}

	public EntityManagerFactory getEntityManagerFactory() {
		return entityManagerFactory;
	}

	public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}
}